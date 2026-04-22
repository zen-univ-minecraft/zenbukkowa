package com.zenbukkowa.breaker;

import com.zenbukkowa.domain.PointCategory;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class BlockCategoryMapper {
    private static final Map<Material, PointCategory> MAP = new EnumMap<>(Material.class);
    private static final Map<Material, Integer> VALUE = new EnumMap<>(Material.class);

    static {
        putTerra(Material.STONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE,
                Material.DEEPSLATE, Material.COBBLED_DEEPSLATE, Material.TUFF,
                Material.CALCITE, Material.DRIPSTONE_BLOCK, Material.POINTED_DRIPSTONE,
                Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT, Material.MUD,
                Material.SAND, Material.RED_SAND, Material.GRAVEL, Material.CLAY,
                Material.GRANITE, Material.DIORITE, Material.ANDESITE);
        for (Material m : Material.values()) {
            if (m.name().endsWith("_TERRACOTTA")) {
                putTerra(m);
            }
        }

        putMineral(Material.COAL_ORE, Material.IRON_ORE, Material.COPPER_ORE,
                Material.GOLD_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE,
                Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.NETHER_QUARTZ_ORE,
                Material.NETHER_GOLD_ORE, Material.ANCIENT_DEBRIS,
                Material.AMETHYST_BLOCK, Material.BUDDING_AMETHYST, Material.AMETHYST_CLUSTER,
                Material.SMALL_AMETHYST_BUD, Material.MEDIUM_AMETHYST_BUD,
                Material.LARGE_AMETHYST_BUD, Material.RAW_IRON_BLOCK,
                Material.RAW_COPPER_BLOCK, Material.RAW_GOLD_BLOCK);
        putMineral(Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_IRON_ORE,
                Material.DEEPSLATE_COPPER_ORE, Material.DEEPSLATE_GOLD_ORE,
                Material.DEEPSLATE_REDSTONE_ORE, Material.DEEPSLATE_LAPIS_ORE,
                Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE);

        putOrganic(Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG,
                Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG,
                Material.MANGROVE_LOG, Material.CHERRY_LOG, Material.PALE_OAK_LOG,
                Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG,
                Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.BIRCH_WOOD,
                Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.BIRCH_PLANKS,
                Material.ACACIA_PLANKS, Material.DARK_OAK_PLANKS,
                Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.BIRCH_LEAVES,
                Material.GRASS_BLOCK, Material.MOSS_BLOCK, Material.MOSS_CARPET,
                Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM,
                Material.NETHER_WART_BLOCK, Material.WARPED_WART_BLOCK, Material.SHROOMLIGHT,
                Material.HAY_BLOCK, Material.MELON, Material.PUMPKIN, Material.CARVED_PUMPKIN,
                Material.BAMBOO, Material.SUGAR_CANE, Material.CACTUS, Material.VINE);

        putAquatic(Material.PRISMARINE, Material.PRISMARINE_BRICKS, Material.DARK_PRISMARINE,
                Material.SEA_LANTERN, Material.SPONGE, Material.WET_SPONGE,
                Material.KELP, Material.SEAGRASS, Material.SEA_PICKLE);
        for (Material m : Material.values()) {
            if (m.name().contains("CORAL") || m.name().contains("CORAL_FAN")) {
                putAquatic(m);
            }
        }

        putVoid(Material.OBSIDIAN, Material.CRYING_OBSIDIAN,
                Material.NETHERRACK, Material.SOUL_SAND, Material.SOUL_SOIL,
                Material.BLACKSTONE, Material.BASALT, Material.SMOOTH_BASALT,
                Material.END_STONE, Material.END_STONE_BRICKS,
                Material.SCULK, Material.SCULK_VEIN, Material.SCULK_SENSOR,
                Material.SCULK_SHRIEKER, Material.SCULK_CATALYST,
                Material.MAGMA_BLOCK, Material.GLOWSTONE);

        for (Material m : Material.values()) {
            if (m.name().startsWith("POTTED_")) {
                MAP.remove(m);
                VALUE.remove(m);
            }
        }
    }

    private static void putTerra(Material... mats) {
        for (Material m : mats) put(m, PointCategory.TERRA, 1);
    }

    private static void putMineral(Material... mats) {
        for (Material m : mats) {
            int v = (m == Material.ANCIENT_DEBRIS) ? 20 : 5;
            put(m, PointCategory.MINERAL, v);
        }
    }

    private static void putOrganic(Material... mats) {
        for (Material m : mats) put(m, PointCategory.ORGANIC, 1);
    }

    private static void putAquatic(Material... mats) {
        for (Material m : mats) put(m, PointCategory.AQUATIC, 1);
    }

    private static void putVoid(Material... mats) {
        for (Material m : mats) put(m, PointCategory.VOID, 1);
    }

    private static void put(Material m, PointCategory c, int v) {
        MAP.put(m, c);
        VALUE.put(m, v);
    }

    public static PointCategory categorize(Material material) {
        return MAP.get(material);
    }

    public static int pointValue(Material material) {
        return VALUE.getOrDefault(material, 0);
    }

    public static boolean isMapped(Material material) {
        return MAP.containsKey(material);
    }
}
