package com.zenbukkowa.domain;

import com.zenbukkowa.persistence.PlayerDao;
import com.zenbukkowa.persistence.SqliteDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SkillServiceTest {
    private SkillService service;

    @BeforeEach
    void setUp() throws Exception {
        Path tmp = Files.createTempFile("test", ".db");
        SqliteDatabase db = new SqliteDatabase(tmp);
        db.initialize();
        PlayerDao dao = new PlayerDao(db);
        service = new SkillService(dao);
    }

    @Test
    void purchaseIncreasesTier() {
        UUID uuid = UUID.randomUUID();
        service.purchase(uuid, SkillType.AREA_RADIUS, 1);
        assertEquals(1, service.getSkills(uuid).tier(SkillType.AREA_RADIUS));
    }

    @Test
    void cannotSkipTiers() {
        UUID uuid = UUID.randomUUID();
        assertFalse(service.canPurchase(uuid, SkillType.AREA_RADIUS, 3));
    }

    @Test
    void cannotExceedMaxTier() {
        UUID uuid = UUID.randomUUID();
        service.purchase(uuid, SkillType.AREA_RADIUS, 1);
        service.purchase(uuid, SkillType.HASTE_AURA, 1);
        service.purchase(uuid, SkillType.HASTE_AURA, 2);
        service.purchase(uuid, SkillType.HASTE_AURA, 3);
        service.purchase(uuid, SkillType.HASTE_AURA, 4);
        service.purchase(uuid, SkillType.HASTE_AURA, 5);
        assertFalse(service.canPurchase(uuid, SkillType.HASTE_AURA, 6));
    }

    @Test
    void areaDepthRequiresRadius() {
        UUID uuid = UUID.randomUUID();
        assertTrue(service.canPurchase(uuid, SkillType.AREA_DEPTH, 1));
        service.purchase(uuid, SkillType.AREA_DEPTH, 1);
        assertTrue(service.canPurchase(uuid, SkillType.AREA_DEPTH, 2));
        service.purchase(uuid, SkillType.AREA_DEPTH, 2);
        assertFalse(service.canPurchase(uuid, SkillType.AREA_DEPTH, 3));
        service.purchase(uuid, SkillType.AREA_RADIUS, 1);
        service.purchase(uuid, SkillType.AREA_RADIUS, 2);
        service.purchase(uuid, SkillType.AREA_RADIUS, 3);
        assertTrue(service.canPurchase(uuid, SkillType.AREA_DEPTH, 3));
    }

    @Test
    void voidSiphonRequiresStructureSense() {
        UUID uuid = UUID.randomUUID();
        service.purchase(uuid, SkillType.AREA_RADIUS, 1);
        service.purchase(uuid, SkillType.VOID_SIPHON, 1);
        assertFalse(service.canPurchase(uuid, SkillType.VOID_SIPHON, 2));
        service.purchase(uuid, SkillType.STRUCTURE_SENSE, 1);
        assertTrue(service.canPurchase(uuid, SkillType.VOID_SIPHON, 2));
    }

    @Test
    void crossCategoryRootsRequireAreaRadius() {
        UUID uuid = UUID.randomUUID();
        assertFalse(service.canPurchase(uuid, SkillType.HASTE_AURA, 1));
        assertFalse(service.canPurchase(uuid, SkillType.LEAF_CONSUME, 1));
        assertFalse(service.canPurchase(uuid, SkillType.TIDE_BREAKER, 1));
        assertFalse(service.canPurchase(uuid, SkillType.SALVAGE, 1));
        assertFalse(service.canPurchase(uuid, SkillType.VOID_SIPHON, 1));
        assertFalse(service.canPurchase(uuid, SkillType.STRUCTURE_SENSE, 1));
        assertFalse(service.canPurchase(uuid, SkillType.GREEN_THUMB, 1));
        service.purchase(uuid, SkillType.AREA_RADIUS, 1);
        assertTrue(service.canPurchase(uuid, SkillType.HASTE_AURA, 1));
        assertTrue(service.canPurchase(uuid, SkillType.LEAF_CONSUME, 1));
        assertTrue(service.canPurchase(uuid, SkillType.TIDE_BREAKER, 1));
        assertTrue(service.canPurchase(uuid, SkillType.SALVAGE, 1));
        assertTrue(service.canPurchase(uuid, SkillType.VOID_SIPHON, 1));
        assertTrue(service.canPurchase(uuid, SkillType.STRUCTURE_SENSE, 1));
        assertTrue(service.canPurchase(uuid, SkillType.GREEN_THUMB, 1));
    }

    @Test
    void cropBranchRequiresPrerequisites() {
        UUID uuid = UUID.randomUUID();
        service.purchase(uuid, SkillType.AREA_RADIUS, 1);
        assertTrue(service.canPurchase(uuid, SkillType.GREEN_THUMB, 1));
        service.purchase(uuid, SkillType.GREEN_THUMB, 1);
        assertTrue(service.canPurchase(uuid, SkillType.HARVEST_AURA, 1));
        service.purchase(uuid, SkillType.HARVEST_AURA, 1);
        assertTrue(service.canPurchase(uuid, SkillType.COMPOST_MASTER, 1));
        service.purchase(uuid, SkillType.COMPOST_MASTER, 1);
        assertTrue(service.canPurchase(uuid, SkillType.SEED_SATCHEL, 1));
        service.purchase(uuid, SkillType.SEED_SATCHEL, 1);
        assertTrue(service.canPurchase(uuid, SkillType.FARMERS_FORTUNE, 1));
    }

    @Test
    void tierOneCostsFifty() {
        assertEquals(50, SkillType.AREA_RADIUS.cost(1));
        assertEquals(50, SkillType.HASTE_AURA.cost(1));
        assertEquals(50, SkillType.GREEN_THUMB.cost(1));
    }

    @Test
    void costFollowsQuadraticCurve() {
        assertEquals(200, SkillType.AREA_RADIUS.cost(2));
        assertEquals(450, SkillType.AREA_RADIUS.cost(3));
        assertEquals(800, SkillType.AREA_RADIUS.cost(4));
        assertEquals(1250, SkillType.AREA_RADIUS.cost(5));
    }
}
