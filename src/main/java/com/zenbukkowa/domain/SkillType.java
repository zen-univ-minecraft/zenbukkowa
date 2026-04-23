package com.zenbukkowa.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public enum SkillType {
    AREA_RADIUS(PointCategory.TERRA, 5, new int[]{1, 3, 5, 7, 9}),
    AREA_DEPTH(PointCategory.TERRA, 5, new int[]{1, 3, 5, 7, 9}),
    PILLAR_BREAK(PointCategory.TERRA, 3, new int[]{1, 2, 3}),
    EFFICIENCY(PointCategory.TERRA, 5, new int[]{1, 2, 3, 4, 5}),
    GRAVITY_WELL(PointCategory.TERRA, 1, new int[]{1}),
    TERRA_BLESSING(PointCategory.TERRA, 3, new int[]{1, 2, 3}),
    HASTE_AURA(PointCategory.MINERAL, 5, new int[]{1, 2, 3, 4, 5}),
    FORTUNE_TOUCH(PointCategory.MINERAL, 3, new int[]{1, 2, 3}),
    VEIN_MINER(PointCategory.MINERAL, 3, new int[]{1, 2, 3}),
    MAGNET(PointCategory.MINERAL, 1, new int[]{1}),
    CRYSTAL_VISION(PointCategory.MINERAL, 3, new int[]{1, 2, 3}),
    BLAST_MINING(PointCategory.MINERAL, 3, new int[]{1, 2, 3}),
    LEAF_CONSUME(PointCategory.ORGANIC, 1, new int[]{1}),
    ROOT_RAZE(PointCategory.ORGANIC, 1, new int[]{1}),
    SAPLING_REPLANT(PointCategory.ORGANIC, 1, new int[]{1}),
    BONEMEAL_AURA(PointCategory.ORGANIC, 3, new int[]{1, 2, 3}),
    NATURE_TOUCH(PointCategory.ORGANIC, 3, new int[]{1, 2, 3}),
    WILD_GROWTH(PointCategory.ORGANIC, 3, new int[]{1, 2, 3}),
    SALVAGE(PointCategory.AQUATIC, 3, new int[]{1, 2, 3}),
    TIDE_BREAKER(PointCategory.AQUATIC, 1, new int[]{1}),
    FROST_WALKER(PointCategory.AQUATIC, 1, new int[]{1}),
    CONDUIT_AURA(PointCategory.AQUATIC, 3, new int[]{1, 2, 3}),
    DEEP_DIVE(PointCategory.AQUATIC, 3, new int[]{1, 2, 3}),
    TSUNAMI(PointCategory.AQUATIC, 3, new int[]{1, 2, 3}),
    VOID_SIPHON(PointCategory.VOID, 3, new int[]{1, 2, 3}),
    STRUCTURE_SENSE(PointCategory.VOID, 1, new int[]{1}),
    NIGHT_VISION(PointCategory.VOID, 1, new int[]{1}),
    FIRE_RESISTANCE(PointCategory.VOID, 1, new int[]{1}),
    VOID_WALK(PointCategory.VOID, 3, new int[]{1, 2, 3}),
    VOID_RIFT(PointCategory.VOID, 3, new int[]{1, 2, 3}),
    GREEN_THUMB(PointCategory.CROP, 5, new int[]{1, 2, 3, 4, 5}),
    HARVEST_AURA(PointCategory.CROP, 3, new int[]{1, 2, 3}),
    COMPOST_MASTER(PointCategory.CROP, 3, new int[]{1, 2, 3}),
    SEED_SATCHEL(PointCategory.CROP, 1, new int[]{1}),
    FARMERS_FORTUNE(PointCategory.CROP, 3, new int[]{1, 2, 3}),
    HARVEST_WAVE(PointCategory.CROP, 3, new int[]{1, 2, 3}),
    ANGEL_WINGS(PointCategory.TERRA, 3, new int[]{1, 2, 3}, true, List.of(
            Map.of(PointCategory.TERRA, 500, PointCategory.MINERAL, 500, PointCategory.VOID, 500),
            Map.of(PointCategory.TERRA, 1000, PointCategory.MINERAL, 1000, PointCategory.VOID, 1000),
            Map.of(PointCategory.TERRA, 2000, PointCategory.MINERAL, 2000, PointCategory.VOID, 2000))),
    TITAN_STRIKE(PointCategory.TERRA, 3, new int[]{1, 2, 3}, true, List.of(
            Map.of(PointCategory.TERRA, 500, PointCategory.ORGANIC, 500, PointCategory.CROP, 500),
            Map.of(PointCategory.TERRA, 1000, PointCategory.ORGANIC, 1000, PointCategory.CROP, 1000),
            Map.of(PointCategory.TERRA, 2000, PointCategory.ORGANIC, 2000, PointCategory.CROP, 2000)));

    private final PointCategory category;
    private final int maxTier;
    private final int[] tierValues;
    private final boolean mythic;
    private final List<Map<PointCategory, Integer>> mythicCosts;

    SkillType(PointCategory category, int maxTier, int[] tierValues) {
        this(category, maxTier, tierValues, false, Collections.emptyList());
    }

    SkillType(PointCategory category, int maxTier, int[] tierValues, boolean mythic, List<Map<PointCategory, Integer>> mythicCosts) {
        this.category = category;
        this.maxTier = maxTier;
        this.tierValues = tierValues;
        this.mythic = mythic;
        this.mythicCosts = mythicCosts;
    }

    public PointCategory category() { return category; }
    public int maxTier() { return maxTier; }
    public boolean isMythic() { return mythic; }

    public int tierValue(int tier) {
        if (tier < 1 || tier > maxTier) return 0;
        return tierValues[tier - 1];
    }

    public int cost(int targetTier) {
        return 50 * targetTier * targetTier;
    }

    public Map<PointCategory, Integer> mythicCost(int targetTier) {
        if (!mythic || targetTier < 1 || targetTier > maxTier) return Collections.emptyMap();
        return mythicCosts.get(targetTier - 1);
    }
}
