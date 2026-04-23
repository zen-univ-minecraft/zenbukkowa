package com.zenbukkowa.domain;

import com.zenbukkowa.breaker.AreaCalculator;
import com.zenbukkowa.breaker.BlockCategoryMapper;
import com.zenbukkowa.breaker.BreakHelper;
import com.zenbukkowa.breaker.BreakPointCalculator;
import com.zenbukkowa.breaker.PlacedBlockCache;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BreakService {
    private final PointService pointService;
    private final SkillService skillService;
    private final AreaCalculator areaCalculator;
    private final BreakPointCalculator pointCalculator;
    private final PlacedBlockCache placedBlockCache;
    private final BlockDiscoveryService blockDiscoveryService;
    private final ReplantService replantService;
    private final JavaPlugin plugin;
    private final Random random = new Random();

    public BreakService(PointService pointService, SkillService skillService,
                        AreaCalculator areaCalculator, BreakPointCalculator pointCalculator,
                        PlacedBlockCache placedBlockCache,
                        BlockDiscoveryService blockDiscoveryService,
                        JavaPlugin plugin) {
        this.pointService = pointService;
        this.skillService = skillService;
        this.areaCalculator = areaCalculator;
        this.pointCalculator = pointCalculator;
        this.placedBlockCache = placedBlockCache;
        this.blockDiscoveryService = blockDiscoveryService;
        this.replantService = new ReplantService(plugin);
        this.plugin = plugin;
    }

    public void onPlayerBreak(Player player, Block centerBlock) {
        if (player.getGameMode() == GameMode.CREATIVE) return;

        PlayerSkills skills = skillService.getSkills(player.getUniqueId());
        int radiusValue = SkillType.AREA_RADIUS.tierValue(skills.tier(SkillType.AREA_RADIUS));
        int depthValue = SkillType.AREA_DEPTH.tierValue(skills.tier(SkillType.AREA_DEPTH));

        PointCategory centerCategory = BlockCategoryMapper.categorize(centerBlock.getType());
        int bonusRadius = domainBonusRadius(skills, centerCategory);
        int bonusDepth = skillService.titanStrike(player.getUniqueId());
        int titanRadius = bonusDepth;

        List<Block> blocks = areaCalculator.calculate(centerBlock, radiusValue, depthValue, bonusRadius + titanRadius, bonusDepth + titanRadius);
        blocks.addAll(getPillarBlocks(player, centerBlock));
        blocks = areaCalculator.deduplicate(blocks);

        ItemStack tool = player.getInventory().getItemInMainHand();
        int maxBreaks = BreakHelper.remainingBreaks(tool, blocks.size());

        boolean leafConsume = skills.hasSkill(SkillType.LEAF_CONSUME);
        double salvageChance = skills.tier(SkillType.SALVAGE) * 0.15;
        boolean gravityWell = skills.hasSkill(SkillType.GRAVITY_WELL);
        boolean seedSatchel = skills.hasSkill(SkillType.SEED_SATCHEL);
        boolean magnet = skills.hasSkill(SkillType.MAGNET);

        Map<PointCategory, Long> batchPoints = new EnumMap<>(PointCategory.class);
        long batchBlocks = 0;

        int broken = 0;
        for (int i = 0; i < Math.min(blocks.size(), maxBreaks); i++) {
            Block block = blocks.get(i);
            Material mat = block.getType();
            PointCategory category = BlockCategoryMapper.categorize(mat);
            if (category == null) continue;
            if (!leafConsume && mat.name().endsWith("_LEAVES")) continue;
            if (BreakHelper.isProtectedBlock(mat)) continue;

            boolean isCenter = block.getX() == centerBlock.getX()
                    && block.getY() == centerBlock.getY()
                    && block.getZ() == centerBlock.getZ();

            boolean playerPlaced = placedBlockCache.isPlayerPlaced(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());

            if (!isCenter) {
                BlockBreakEvent testEvent = new BlockBreakEvent(block, player);
                player.getServer().getPluginManager().callEvent(testEvent);
                if (testEvent.isCancelled()) continue;
                if (gravityWell && (mat == Material.SAND || mat == Material.GRAVEL)) {
                    block.setType(Material.AIR);
                } else {
                    block.breakNaturally(tool, true);
                }
                if (!shouldSalvage(salvageChance)) BreakHelper.damageTool(tool);
            }

            placedBlockCache.delete(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());

            if (!playerPlaced) {
                int points = pointCalculator.calculate(block, category, player, skills);
                if (points > 0) {
                    batchPoints.merge(category, (long) points, Long::sum);
                }
                batchBlocks++;
                blockDiscoveryService.checkDiscovery(player, mat, skills);
            }
            broken++;

            if (seedSatchel && category == PointCategory.CROP) {
                replantService.scheduleReplant(block, mat);
            }
            if (magnet && !isCenter) {
                collectDrops(player, block.getLocation());
            }
        }

        if (!batchPoints.isEmpty() || batchBlocks > 0) {
            pointService.addPointsBatch(player.getUniqueId(), batchPoints, batchBlocks);
        }

        if (broken > 1) {
            player.incrementStatistic(org.bukkit.Statistic.MINE_BLOCK, centerBlock.getType(), broken - 1);
        }
    }

    private int domainBonusRadius(PlayerSkills skills, PointCategory category) {
        return switch (category) {
            case MINERAL -> skills.tier(SkillType.BLAST_MINING);
            case ORGANIC -> skills.tier(SkillType.WILD_GROWTH);
            case AQUATIC -> skills.tier(SkillType.TSUNAMI);
            case VOID -> skills.tier(SkillType.VOID_RIFT);
            case CROP -> skills.tier(SkillType.HARVEST_WAVE);
            default -> 0;
        };
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

    private void collectDrops(Player player, Location loc) {
        for (Item item : loc.getWorld().getNearbyEntitiesByType(Item.class, loc, 5, 5, 5)) {
            item.teleport(player);
        }
    }

    private boolean shouldSalvage(double chance) {
        return random.nextDouble() < chance;
    }
}
