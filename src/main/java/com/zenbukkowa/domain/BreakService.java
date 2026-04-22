package com.zenbukkowa.domain;

import com.zenbukkowa.breaker.AreaCalculator;
import com.zenbukkowa.breaker.BlockCategoryMapper;
import com.zenbukkowa.breaker.BreakHelper;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class BreakService {
    private final PointService pointService;
    private final SkillService skillService;
    private final AreaCalculator areaCalculator;
    private final Random random = new Random();
    private final int basePoints;
    private final int oreMultiplier;
    private final int ancientDebrisMultiplier;

    public BreakService(PointService pointService, SkillService skillService,
                        AreaCalculator areaCalculator, int basePoints,
                        int oreMultiplier, int ancientDebrisMultiplier) {
        this.pointService = pointService;
        this.skillService = skillService;
        this.areaCalculator = areaCalculator;
        this.basePoints = basePoints;
        this.oreMultiplier = oreMultiplier;
        this.ancientDebrisMultiplier = ancientDebrisMultiplier;
    }

    public void onPlayerBreak(Player player, Block centerBlock) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        int radiusTier = skillService.radius(player.getUniqueId());
        int depthTier = skillService.depth(player.getUniqueId());
        if (radiusTier <= 0) radiusTier = 1;
        if (depthTier <= 0) depthTier = 1;

        List<Block> blocks = areaCalculator.calculate(centerBlock, radiusTier, depthTier);
        ItemStack tool = player.getInventory().getItemInMainHand();
        int maxBreaks = BreakHelper.remainingBreaks(tool, blocks.size());

        int fortuneTier = skillService.getSkills(player.getUniqueId()).tier(SkillType.FORTUNE_TOUCH);
        double fortuneChance = fortuneTier * 0.10;
        int voidTier = skillService.getSkills(player.getUniqueId()).tier(SkillType.VOID_SIPHON);
        double voidBonus = voidTier * 0.25;
        boolean leafConsume = skillService.getSkills(player.getUniqueId()).hasSkill(SkillType.LEAF_CONSUME);
        int salvageTier = skillService.getSkills(player.getUniqueId()).tier(SkillType.SALVAGE);
        double salvageChance = salvageTier * 0.15;
        boolean rootRaze = skillService.getSkills(player.getUniqueId()).hasSkill(SkillType.ROOT_RAZE);

        int broken = processArea(player, centerBlock, blocks, tool, maxBreaks, leafConsume, fortuneChance, voidBonus, salvageChance);

        if (rootRaze && BreakHelper.isLog(centerBlock.getType())) {
            broken += breakColumn(player, centerBlock, tool, fortuneChance, voidBonus, salvageChance, maxBreaks - broken);
        }

        if (broken > 1) {
            player.incrementStatistic(org.bukkit.Statistic.MINE_BLOCK, centerBlock.getType(), broken - 1);
        }
    }

    private int processArea(Player player, Block centerBlock, List<Block> blocks, ItemStack tool,
                            int maxBreaks, boolean leafConsume, double fortuneChance,
                            double voidBonus, double salvageChance) {
        int broken = 0;
        for (int i = 0; i < Math.min(blocks.size(), maxBreaks); i++) {
            Block block = blocks.get(i);
            Material mat = block.getType();
            PointCategory category = BlockCategoryMapper.categorize(mat);
            if (category == null) {
                continue;
            }
            if (!leafConsume && mat.name().endsWith("_LEAVES")) {
                continue;
            }
            if (BreakHelper.isContainer(mat)) {
                continue;
            }
            int points = calculatePoints(block, category, fortuneChance, voidBonus, player.getWorld());
            if (points > 0) {
                pointService.addPoints(player.getUniqueId(), category, points, 1);
            }
            boolean isCenter = block.getX() == centerBlock.getX()
                    && block.getY() == centerBlock.getY()
                    && block.getZ() == centerBlock.getZ();
            if (!isCenter) {
                BlockBreakEvent testEvent = new BlockBreakEvent(block, player);
                if (testEvent.isCancelled()) {
                    continue;
                }
                if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                    block.breakNaturally(tool, true);
                } else {
                    block.setType(org.bukkit.Material.AIR);
                }
                if (!shouldSalvage(salvageChance)) {
                    BreakHelper.damageTool(tool);
                }
                broken++;
            } else {
                broken++;
            }
        }
        return broken;
    }

    private int breakColumn(Player player, Block centerBlock, ItemStack tool,
                            double fortuneChance, double voidBonus, double salvageChance, int max) {
        int count = 0;
        int y = centerBlock.getY() - 1;
        while (y >= centerBlock.getWorld().getMinHeight() && count < max) {
            Block below = centerBlock.getWorld().getBlockAt(centerBlock.getX(), y, centerBlock.getZ());
            if (!BreakHelper.isLog(below.getType())) {
                break;
            }
            PointCategory cat = BlockCategoryMapper.categorize(below.getType());
            if (cat != null) {
                int points = calculatePoints(below, cat, fortuneChance, voidBonus, player.getWorld());
                if (points > 0) {
                    pointService.addPoints(player.getUniqueId(), cat, points, 1);
                }
            }
            BlockBreakEvent testEvent = new BlockBreakEvent(below, player);
            if (testEvent.isCancelled()) {
                y--;
                continue;
            }
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                below.breakNaturally(tool, true);
            } else {
                below.setType(org.bukkit.Material.AIR);
            }
            if (!shouldSalvage(salvageChance)) {
                BreakHelper.damageTool(tool);
            }
            count++;
            y--;
        }
        return count;
    }

    private int calculatePoints(Block block, PointCategory category, double fortuneChance, double voidBonus, World world) {
        int value = BlockCategoryMapper.pointValue(block.getType());
        if (value == 0) {
            value = basePoints;
        }
        if (category == PointCategory.MINERAL) {
            value = Math.max(value, oreMultiplier);
        }
        if (block.getType() == Material.ANCIENT_DEBRIS) {
            value = ancientDebrisMultiplier;
        }
        if (random.nextDouble() < fortuneChance) {
            value *= 2;
        }
        if (voidBonus > 0 && (block.getY() < 0 || isVoidWorld(world))) {
            value = (int) (value * (1 + voidBonus));
        }
        return value;
    }

    private boolean isVoidWorld(World world) {
        return world.getEnvironment() == World.Environment.NETHER
                || world.getEnvironment() == World.Environment.THE_END;
    }

    private boolean shouldSalvage(double chance) {
        return random.nextDouble() < chance;
    }
}
