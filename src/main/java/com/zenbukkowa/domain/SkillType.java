package com.zenbukkowa.domain;

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
    LEAF_CONSUME(PointCategory.ORGANIC, 1, new int[]{1}),
    ROOT_RAZE(PointCategory.ORGANIC, 1, new int[]{1}),
    SAPLING_REPLANT(PointCategory.ORGANIC, 1, new int[]{1}),
    BONEMEAL_AURA(PointCategory.ORGANIC, 3, new int[]{1, 2, 3}),
    NATURE_TOUCH(PointCategory.ORGANIC, 3, new int[]{1, 2, 3}),
    SALVAGE(PointCategory.AQUATIC, 3, new int[]{1, 2, 3}),
    TIDE_BREAKER(PointCategory.AQUATIC, 1, new int[]{1}),
    FROST_WALKER(PointCategory.AQUATIC, 1, new int[]{1}),
    CONDUIT_AURA(PointCategory.AQUATIC, 3, new int[]{1, 2, 3}),
    DEEP_DIVE(PointCategory.AQUATIC, 3, new int[]{1, 2, 3}),
    VOID_SIPHON(PointCategory.VOID, 3, new int[]{1, 2, 3}),
    STRUCTURE_SENSE(PointCategory.VOID, 1, new int[]{1}),
    NIGHT_VISION(PointCategory.VOID, 1, new int[]{1}),
    FIRE_RESISTANCE(PointCategory.VOID, 1, new int[]{1}),
    VOID_WALK(PointCategory.VOID, 3, new int[]{1, 2, 3}),
    GREEN_THUMB(PointCategory.CROP, 5, new int[]{1, 2, 3, 4, 5}),
    HARVEST_AURA(PointCategory.CROP, 3, new int[]{1, 2, 3}),
    COMPOST_MASTER(PointCategory.CROP, 3, new int[]{1, 2, 3}),
    SEED_SATCHEL(PointCategory.CROP, 1, new int[]{1}),
    FARMERS_FORTUNE(PointCategory.CROP, 3, new int[]{1, 2, 3});

    private final PointCategory category;
    private final int maxTier;
    private final int[] tierValues;

    SkillType(PointCategory category, int maxTier, int[] tierValues) {
        this.category = category;
        this.maxTier = maxTier;
        this.tierValues = tierValues;
    }

    public PointCategory category() { return category; }
    public int maxTier() { return maxTier; }

    public int tierValue(int tier) {
        if (tier < 1 || tier > maxTier) return 0;
        return tierValues[tier - 1];
    }

    public int cost(int targetTier) {
        return 50 * targetTier * targetTier;
    }
}
