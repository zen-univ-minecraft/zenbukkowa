package com.zenbukkowa.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SettingsDaoTest {
    private SettingsDao dao;

    @BeforeEach
    void setUp() throws Exception {
        Path tmp = Files.createTempFile("test", ".db");
        SqliteDatabase db = new SqliteDatabase(tmp);
        db.initialize();
        dao = new SettingsDao(db);
        dao.initialize();
    }

    @Test
    void saveAndLoadLocale() {
        UUID uuid = UUID.randomUUID();
        dao.saveLocale(uuid, "ja");
        var all = dao.loadAllLocales();
        assertEquals("ja", all.get(uuid));
    }

    @Test
    void defaultLocaleIsEn() {
        UUID uuid = UUID.randomUUID();
        var all = dao.loadAllLocales();
        assertNull(all.get(uuid));
    }
}
