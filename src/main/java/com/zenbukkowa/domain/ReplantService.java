package com.zenbukkowa.domain;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.plugin.java.JavaPlugin;

public class ReplantService {
    private final JavaPlugin plugin;

    public ReplantService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleReplant(Block block, Material original) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Block current = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ());
            if (current.getType() != Material.AIR) return;
            tryReplant(current, original);
        });
    }

    public void tryReplant(Block block, Material original) {
        Material below = block.getRelative(0, -1, 0).getType();
        if (below != Material.FARMLAND && below != Material.SOUL_SAND) return;
        Material seed = switch (original) {
            case WHEAT -> Material.WHEAT;
            case CARROTS -> Material.CARROTS;
            case POTATOES -> Material.POTATOES;
            case BEETROOTS -> Material.BEETROOTS;
            case NETHER_WART -> Material.NETHER_WART;
            default -> null;
        };
        if (seed == null) return;
        block.setType(seed);
        if (block.getBlockData() instanceof Ageable ageable) {
            ageable.setAge(0);
            block.setBlockData(ageable);
        }
    }
}
