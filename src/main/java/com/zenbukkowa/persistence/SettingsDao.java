package com.zenbukkowa.persistence;

import java.sql.*;
import java.util.*;

public class SettingsDao {
    private final SqliteDatabase db;

    public SettingsDao(SqliteDatabase db) {
        this.db = db;
    }

    public void initialize() throws SQLException {
        try (Statement stmt = db.connection().createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player_settings (
                    uuid TEXT PRIMARY KEY,
                    locale TEXT NOT NULL DEFAULT 'en'
                )
                """);
        }
    }

    public void saveLocale(UUID uuid, String locale) {
        String sql = "INSERT INTO player_settings (uuid, locale) VALUES (?, ?) ON CONFLICT(uuid) DO UPDATE SET locale=excluded.locale";
        try (PreparedStatement ps = db.connection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, locale);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<UUID, String> loadAllLocales() {
        Map<UUID, String> map = new HashMap<>();
        try (Statement stmt = db.connection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT uuid, locale FROM player_settings")) {
            while (rs.next()) {
                map.put(UUID.fromString(rs.getString("uuid")), rs.getString("locale"));
            }
        } catch (SQLException ignored) {}
        return map;
    }
}
