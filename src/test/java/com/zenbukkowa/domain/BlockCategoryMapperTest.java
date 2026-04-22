package com.zenbukkowa.domain;

import com.zenbukkowa.breaker.BlockCategoryMapper;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockCategoryMapperTest {

    @Test
    void stoneIsTerra() {
        assertEquals(PointCategory.TERRA, BlockCategoryMapper.categorize(Material.STONE));
        assertEquals(1, BlockCategoryMapper.pointValue(Material.STONE));
    }

    @Test
    void coalOreIsMineral() {
        assertEquals(PointCategory.MINERAL, BlockCategoryMapper.categorize(Material.COAL_ORE));
        assertEquals(5, BlockCategoryMapper.pointValue(Material.COAL_ORE));
    }

    @Test
    void ancientDebrisIsMineral20() {
        assertEquals(PointCategory.MINERAL, BlockCategoryMapper.categorize(Material.ANCIENT_DEBRIS));
        assertEquals(20, BlockCategoryMapper.pointValue(Material.ANCIENT_DEBRIS));
    }

    @Test
    void oakLogIsOrganic() {
        assertEquals(PointCategory.ORGANIC, BlockCategoryMapper.categorize(Material.OAK_LOG));
        assertEquals(1, BlockCategoryMapper.pointValue(Material.OAK_LOG));
    }

    @Test
    void prismarineIsAquatic() {
        assertEquals(PointCategory.AQUATIC, BlockCategoryMapper.categorize(Material.PRISMARINE));
        assertEquals(1, BlockCategoryMapper.pointValue(Material.PRISMARINE));
    }

    @Test
    void obsidianIsVoid() {
        assertEquals(PointCategory.VOID, BlockCategoryMapper.categorize(Material.OBSIDIAN));
        assertEquals(1, BlockCategoryMapper.pointValue(Material.OBSIDIAN));
    }

    @Test
    void unmappedReturnsNull() {
        assertNull(BlockCategoryMapper.categorize(Material.BEDROCK));
        assertEquals(0, BlockCategoryMapper.pointValue(Material.BEDROCK));
    }
}
