package com.zenbukkowa.domain;

public enum SkillType {
    AREA_RADIUS(PointCategory.TERRA, 5, new int[]{1, 3, 5, 7, 9}),
    AREA_DEPTH(PointCategory.TERRA, 5, new int[]{1, 3, 5, 7, 9}),
    HASTE_AURA(PointCategory.MINERAL, 5, new int[]{1, 2, 3, 4, 5}),
    FORTUNE_TOUCH(PointCategory.MINERAL, 3, new int[]{1, 2, 3}),
    LEAF_CONSUME(PointCategory.ORGANIC, 1, new int[]{1}),
    ROOT_RAZE(PointCategory.ORGANIC, 1, new int[]{1}),
    SALVAGE(PointCategory.AQUATIC, 3, new int[]{1, 2, 3}),
    TIDE_BREAKER(PointCategory.AQUATIC, 1, new int[]{1}),
    VOID_SIPHON(PointCategory.VOID, 3, new int[]{1, 2, 3}),
    STRUCTURE_SENSE(PointCategory.VOID, 1, new int[]{1});

    private final PointCategory category;
    private final int maxTier;
    private final int[] tierValues;

    SkillType(PointCategory category, int maxTier, int[] tierValues) {
        this.category = category;
        this.maxTier = maxTier;
        this.tierValues = tierValues;
    }

    public PointCategory category() {
        return category;
    }

    public int maxTier() {
        return maxTier;
    }

    public int tierValue(int tier) {
        if (tier < 1 || tier > maxTier) {
            return 0;
        }
        return tierValues[tier - 1];
    }

    public int cost(int targetTier) {
        return 100 * targetTier;
    }
}
