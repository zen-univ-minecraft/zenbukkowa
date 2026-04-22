package com.zenbukkowa.domain;

import com.zenbukkowa.breaker.AreaCalculator;
import com.zenbukkowa.breaker.BlockCategoryMapper;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
        int maxBreaks = calculateMaxBreaks(tool, blocks.size());

        int fortuneTier = skillService.getSkills(player.getUniqueId()).tier(SkillType.FORTUNE_TOUCH);
        double fortuneChance = fortuneTier * 0.10;
        int voidTier = skillService.getSkills(player.getUniqueId()).tier(SkillType.VOID_SIPHON);
        double voidBonus = voidTier * 0.25;
        boolean leafConsume = skillService.getSkills(player.getUniqueId()).hasSkill(SkillType.LEAF_CONSUME);
        int salvageTier = skillService.getSkills(player.getUniqueId()).tier(SkillType.SALVAGE);
        double salvageChance = salvageTier * 0.15;

        int broken = 0;
        Location dropLoc = centerBlock.getLocation().add(0.5, 0.5, 0.5);

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
            if (isContainer(mat)) {
                continue;
            }

            BlockBreakEvent testEvent = new BlockBreakEvent(block, player);
            if (testEvent.isCancelled()) {
                continue;
            }

            int points = calculatePoints(block, category, fortuneChance, voidBonus, player.getWorld());
            if (points > 0) {
                pointService.addPoints(player.getUniqueId(), category, points, 1);
            }

            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                block.breakNaturally(tool, true);
            } else {
                block.setType(Material.AIR);
            }

            if (!shouldSalvage(salvageChance)) {
                damageTool(tool);
            }
            broken++;
        }

        if (broken > 1) {
            player.incrementStatistic(org.bukkit.Statistic.MINE_BLOCK, centerBlock.getType(), broken - 1);
        }
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

    private boolean isContainer(Material mat) {
        return mat == Material.CHEST || mat == Material.TRAPPED_CHEST
                || mat == Material.BARREL || mat == Material.SHULKER_BOX
                || mat.name().endsWith("_SHULKER_BOX")
                || mat == Material.HOPPER || mat == Material.DISPENSER
                || mat == Material.DROPPER;
    }

    private int calculateMaxBreaks(ItemStack tool, int targetCount) {
        if (tool == null || tool.getType().isAir() || !tool.getType().isItem()) {
            return targetCount;
        }
        var meta = tool.getItemMeta();
        if (!(meta instanceof org.bukkit.inventory.meta.Damageable d)) {
            return targetCount;
        }
        int maxDurability = tool.getType().getMaxDurability();
        if (maxDurability <= 0) {
            return targetCount;
        }
        int remaining = maxDurability - d.getDamage();
        return Math.min(targetCount, remaining);
    }

    private void damageTool(ItemStack tool) {
        if (tool == null || tool.getType().isAir()) {
            return;
        }
        var meta = tool.getItemMeta();
        if (!(meta instanceof org.bukkit.inventory.meta.Damageable d)) {
            return;
        }
        int maxDurability = tool.getType().getMaxDurability();
        if (maxDurability <= 0) {
            return;
        }
        d.setDamage(d.getDamage() + 1);
        tool.setItemMeta(d);
        if (d.getDamage() >= maxDurability) {
            tool.setAmount(0);
        }
    }

    private boolean shouldSalvage(double chance) {
        return random.nextDouble() < chance;
    }
}
