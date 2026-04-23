package com.zenbukkowa.breaker;

import com.zenbukkowa.domain.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Random;

public class BreakPointCalculator {
    private final Random random = new Random();
    private final int basePoints;
    private final int oreMultiplier;
    private final int ancientDebrisMultiplier;

    public BreakPointCalculator(int basePoints, int oreMultiplier, int ancientDebrisMultiplier) {
        this.basePoints = basePoints;
        this.oreMultiplier = oreMultiplier;
        this.ancientDebrisMultiplier = ancientDebrisMultiplier;
    }

    public int calculate(Block block, PointCategory category, Player player, PlayerSkills skills) {
        int value = BlockCategoryMapper.pointValue(block.getType());
        if (value == 0) value = basePoints;
        if (category == PointCategory.MINERAL) value = Math.max(value, oreMultiplier);
        if (block.getType() == Material.ANCIENT_DEBRIS) value = ancientDebrisMultiplier;

        int fortuneTier = skills.tier(SkillType.FORTUNE_TOUCH);
        if (random.nextDouble() < fortuneTier * 0.10) value *= 2;

        int voidTier = skills.tier(SkillType.VOID_SIPHON);
        if (voidTier > 0 && (block.getY() < 0 || isVoidWorld(player.getWorld()))) {
            value = (int) (value * (1 + voidTier * 0.25));
        }

        if (category == PointCategory.TERRA) {
            value = (int) (value * (1 + skills.tier(SkillType.TERRA_BLESSING) * 0.10));
        }
        if (category == PointCategory.ORGANIC) {
            value = (int) (value * (1 + skills.tier(SkillType.NATURE_TOUCH) * 0.10));
        }
        if (category == PointCategory.AQUATIC && player.isInWater()) {
            value = (int) (value * (1 + skills.tier(SkillType.DEEP_DIVE) * 0.10));
        }
        if (category == PointCategory.CROP) {
            value += skills.tier(SkillType.GREEN_THUMB);
            value = (int) (value * (1 + skills.tier(SkillType.FARMERS_FORTUNE) * 0.10));
        }

        int veinTier = skills.tier(SkillType.VEIN_MINER);
        if (veinTier > 0 && category == PointCategory.MINERAL && value > basePoints) {
            value += veinTier;
        }
        return value;
    }

    private boolean isVoidWorld(World world) {
        return world.getEnvironment() == World.Environment.NETHER
                || world.getEnvironment() == World.Environment.THE_END;
    }
}
