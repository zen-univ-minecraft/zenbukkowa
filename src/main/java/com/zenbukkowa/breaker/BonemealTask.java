package com.zenbukkowa.breaker;

import com.zenbukkowa.domain.SkillService;
import com.zenbukkowa.domain.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BonemealTask implements Runnable {
    private final SkillService skillService;
    private final JavaPlugin plugin;

    public BonemealTask(SkillService skillService, JavaPlugin plugin) {
        this.skillService = skillService;
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 100, 100);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            var skills = skillService.getSkills(player.getUniqueId());
            int organicRadius = halved(skills.tier(SkillType.BONEMEAL_AURA));
            int cropRadius = halved(skills.tier(SkillType.COMPOST_MASTER));
            if (organicRadius <= 0 && cropRadius <= 0) continue;
            int radius = Math.max(organicRadius, cropRadius);
            Block center = player.getLocation().getBlock();
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        Block b = center.getRelative(dx, dy, dz);
                        if (isCrop(b.getType())) {
                            b.applyBoneMeal(BlockFace.UP);
                        }
                    }
                }
            }
        }
    }

    private int halved(int tier) {
        return (tier + 1) / 2;
    }

    private boolean isCrop(Material mat) {
        return mat == Material.WHEAT || mat == Material.CARROTS || mat == Material.POTATOES
                || mat == Material.BEETROOTS || mat == Material.NETHER_WART
                || mat == Material.COCOA || mat == Material.MELON_STEM || mat == Material.PUMPKIN_STEM
                || mat == Material.TORCHFLOWER_CROP || mat == Material.PITCHER_CROP;
    }
}
