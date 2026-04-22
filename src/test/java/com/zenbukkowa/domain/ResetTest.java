package com.zenbukkowa.domain;

import com.zenbukkowa.persistence.PlayerDao;
import com.zenbukkowa.persistence.SqliteDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ResetTest {
    private PointService pointService;
    private SkillService skillService;

    @BeforeEach
    void setUp() throws Exception {
        Path tmp = Files.createTempFile("test", ".db");
        SqliteDatabase db = new SqliteDatabase(tmp);
        db.initialize();
        PlayerDao dao = new PlayerDao(db);
        pointService = new PointService(dao);
        skillService = new SkillService(dao);
    }

    @Test
    void resetPlayerClearsPointsAndSkills() {
        UUID uuid = UUID.randomUUID();
        pointService.addPoints(uuid, PointCategory.TERRA, 100, 5);
        skillService.purchase(uuid, SkillType.AREA_RADIUS, 1);

        assertEquals(100, pointService.getProgress(uuid).points(PointCategory.TERRA));
        assertEquals(1, skillService.getSkills(uuid).tier(SkillType.AREA_RADIUS));

        pointService.resetPlayer(uuid);
        skillService.resetPlayer(uuid);

        assertEquals(0, pointService.getProgress(uuid).points(PointCategory.TERRA));
        assertEquals(0, skillService.getSkills(uuid).tier(SkillType.AREA_RADIUS));
    }

    @Test
    void resetAllClearsEverything() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        pointService.addPoints(a, PointCategory.TERRA, 100, 1);
        pointService.addPoints(b, PointCategory.MINERAL, 200, 1);
        skillService.purchase(a, SkillType.AREA_RADIUS, 1);
        skillService.purchase(b, SkillType.HASTE_AURA, 1);

        pointService.resetAll();
        skillService.resetAll();

        assertEquals(0, pointService.getProgress(a).totalPoints());
        assertEquals(0, pointService.getProgress(b).totalPoints());
        assertEquals(0, skillService.getSkills(a).tier(SkillType.AREA_RADIUS));
        assertEquals(0, skillService.getSkills(b).tier(SkillType.HASTE_AURA));
    }
}
