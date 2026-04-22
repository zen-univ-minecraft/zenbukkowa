package com.zenbukkowa.domain;

import com.zenbukkowa.persistence.PlayerDao;
import com.zenbukkowa.persistence.SqliteDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PointServiceTest {
    private PointService service;

    @BeforeEach
    void setUp() throws Exception {
        Path tmp = Files.createTempFile("test", ".db");
        SqliteDatabase db = new SqliteDatabase(tmp);
        db.initialize();
        PlayerDao dao = new PlayerDao(db);
        service = new PointService(dao);
    }

    @Test
    void addPointsIncreasesTotal() {
        UUID uuid = UUID.randomUUID();
        service.addPoints(uuid, PointCategory.TERRA, 100, 5);
        assertEquals(100, service.getProgress(uuid).points(PointCategory.TERRA));
        assertEquals(100, service.getProgress(uuid).totalPoints());
        assertEquals(5, service.getProgress(uuid).blocksBroken());
    }

    @Test
    void spendPointsReducesBalance() {
        UUID uuid = UUID.randomUUID();
        service.addPoints(uuid, PointCategory.MINERAL, 500, 1);
        service.spendPoints(uuid, PointCategory.MINERAL, 200);
        assertEquals(300, service.getProgress(uuid).points(PointCategory.MINERAL));
    }

    @Test
    void spendPointsThrowsWhenInsufficient() {
        UUID uuid = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> service.spendPoints(uuid, PointCategory.TERRA, 1));
    }

    @Test
    void leaderboardOrdersByTotal() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        service.addPoints(a, PointCategory.TERRA, 100, 1);
        service.addPoints(b, PointCategory.TERRA, 200, 1);
        var board = service.getLeaderboard(10);
        assertEquals(b, board.get(0).getKey());
        assertEquals(a, board.get(1).getKey());
    }
}
