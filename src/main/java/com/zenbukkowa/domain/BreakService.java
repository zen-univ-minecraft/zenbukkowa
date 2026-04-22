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

import java.util.ArrayList;
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
        if (player.getGameMode() == GameMode.CREATIVE) return;

        int radiusTier = skillService.radius(player.getUniqueId());
        int depthTier = skillService.depth(player.getUniqueId());
        if (radiusTier <= 0) radiusTier = 1;
        if (depthTier <= 0) depthTier = 1;

        List<Block> blocks = areaCalculator.calculate(centerBlock, radiusTier, depthTier);
        blocks.addAll(getPillarBlocks(player, centerBlock));

        ItemStack tool = player.getInventory().getItemInMainHand();
        int maxBreaks = calculateMaxBreaks(tool, blocks.size());

        PlayerSkills skills = skillService.getSkills(player.getUniqueId());
        int fortuneTier = skills.tier(SkillType.FORTUNE_TOUCH);
        double fortuneChance = fortuneTier * 0.10;
        int voidTier = skills.tier(SkillType.VOID_SIPHON);
        double voidBonus = voidTier * 0.25;
        boolean leafConsume = skills.hasSkill(SkillType.LEAF_CONSUME);
        int salvageTier = skills.tier(SkillType.SALVAGE);
        double salvageChance = salvageTier * 0.15;
        double terraBonus = 1 + (skills.tier(SkillType.TERRA_BLESSING) * 0.10);
        double natureBonus = 1 + (skills.tier(SkillType.NATURE_TOUCH) * 0.10);
        double deepBonus = 1 + (skills.tier(SkillType.DEEP_DIVE) * 0.10);
        boolean gravityWell = skills.hasSkill(SkillType.GRAVITY_WELL);

        int broken = 0;
        Location dropLoc = centerBlock.getLocation().add(0.5, 0.5, 0.5);

        for (int i = 0; i < Math.min(blocks.size(), maxBreaks); i++) {
            Block block = blocks.get(i);
            Material mat = block.getType();
            PointCategory category = BlockCategoryMapper.categorize(mat);
            if (category == null) continue;
            if (!leafConsume && mat.name().endsWith("_LEAVES")) continue;
            if (isContainer(mat)) continue;

            int points = calculatePoints(block, category, fortuneChance, voidBonus, player.getWorld(),
                    terraBonus, natureBonus, deepBonus, player);
            if (points > 0) {
                pointService.addPoints(player.getUniqueId(), category, points, 1);
            }

            boolean isCenter = block.getX() == centerBlock.getX()
                    && block.getY() == centerBlock.getY()
                    && block.getZ() == centerBlock.getZ();
            if (!isCenter) {
                BlockBreakEvent testEvent = new BlockBreakEvent(block, player);
                player.getServer().getPluginManager().callEvent(testEvent);
                if (testEvent.isCancelled()) continue;
                if (gravityWell && (mat == Material.SAND || mat == Material.GRAVEL)) {
                    block.setType(Material.AIR);
                } else if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                    block.breakNaturally(tool, true);
                } else {
                    block.setType(Material.AIR);
                }
                if (!shouldSalvage(salvageChance)) damageTool(tool);
                broken++;
            } else {
                broken++;
            }
        }

        if (broken > 1) {
            player.incrementStatistic(org.bukkit.Statistic.MINE_BLOCK, centerBlock.getType(), broken - 1);
        }
    }

    private List<Block> getPillarBlocks(Player player, Block center) {
        List<Block> extra = new ArrayList<>();
        int pillarTier = skillService.getSkills(player.getUniqueId()).tier(SkillType.PILLAR_BREAK);
        for (int i = 1; i <= pillarTier; i++) {
            Block above = center.getWorld().getBlockAt(center.getX(), center.getY() + i, center.getZ());
            if (above.getType() != Material.AIR) extra.add(above);
        }
        boolean rootRaze = skillService.getSkills(player.getUniqueId()).hasSkill(SkillType.ROOT_RAZE);
        if (rootRaze && center.getType().name().endsWith("_LOG")) {
            for (int i = 1; i <= 20; i++) {
                Block below = center.getWorld().getBlockAt(center.getX(), center.getY() - i, center.getZ());
                if (!below.getType().name().endsWith("_LOG")) break;
                extra.add(below);
            }
        }
        return extra;
    }

    private int calculatePoints(Block block, PointCategory category, double fortuneChance,
                                double voidBonus, World world,
                                double terraBonus, double natureBonus, double deepBonus,
                                Player player) {
        int value = BlockCategoryMapper.pointValue(block.getType());
        if (value == 0) value = basePoints;
        if (category == PointCategory.MINERAL) value = Math.max(value, oreMultiplier);
        if (block.getType() == Material.ANCIENT_DEBRIS) value = ancientDebrisMultiplier;
        if (random.nextDouble() < fortuneChance) value *= 2;
        if (voidBonus > 0 && (block.getY() < 0 || isVoidWorld(world))) {
            value = (int) (value * (1 + voidBonus));
        }
        if (category == PointCategory.TERRA) value = (int) (value * terraBonus);
        if (category == PointCategory.ORGANIC) value = (int) (value * natureBonus);
        if (category == PointCategory.AQUATIC && player.isInWater()) {
            value = (int) (value * deepBonus);
        }
        int veinTier = skillService.getSkills(player.getUniqueId()).tier(SkillType.VEIN_MINER);
        if (veinTier > 0 && category == PointCategory.MINERAL && value > basePoints) {
            value += veinTier;
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
        if (tool == null || tool.getType().isAir() || !tool.getType().isItem()) return targetCount;
        var meta = tool.getItemMeta();
        if (!(meta instanceof org.bukkit.inventory.meta.Damageable d)) return targetCount;
        int maxDurability = tool.getType().getMaxDurability();
        if (maxDurability <= 0) return targetCount;
        int remaining = maxDurability - d.getDamage();
        return Math.min(targetCount, remaining);
    }

    private void damageTool(ItemStack tool) {
        if (tool == null || tool.getType().isAir()) return;
        var meta = tool.getItemMeta();
        if (!(meta instanceof org.bukkit.inventory.meta.Damageable d)) return;
        int maxDurability = tool.getType().getMaxDurability();
        if (maxDurability <= 0) return;
        d.setDamage(d.getDamage() + 1);
        tool.setItemMeta(d);
        if (d.getDamage() >= maxDurability) tool.setAmount(0);
    }

    private boolean shouldSalvage(double chance) {
        return random.nextDouble() < chance;
    }
}
