package com.zenbukkowa.structure;

import com.zenbukkowa.domain.PointCategory;
import com.zenbukkowa.domain.PointService;
import com.zenbukkowa.persistence.StructureDao;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.sql.SQLException;
import java.util.UUID;

public class StructureService {
    private final StructureDao structureDao;
    private final PointService pointService;

    public StructureService(StructureDao structureDao, PointService pointService) {
        this.structureDao = structureDao;
        this.pointService = pointService;
    }

    public void checkOceanMonument(PlayerContext ctx, Block block) {
        if (!isEligible(block, Material.PRISMARINE, Material.PRISMARINE_BRICKS, Material.DARK_PRISMARINE,
                Material.SEA_LANTERN, Material.SPONGE, Material.WET_SPONGE, Material.GOLD_BLOCK)) {
            return;
        }
        String id = "monument_" + block.getWorld().getName() + "_" + block.getX() / 100 + "_" + block.getZ() / 100;
        if (isClaimed(id)) {
            return;
        }
        if (scanMonument(block, id, ctx.uuid())) {
            grantBonus(ctx);
            broadcast(ctx, "Ocean Monument");
        }
    }

    public void checkWoodlandMansion(PlayerContext ctx, Block block) {
        if (!isEligible(block, Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.DARK_OAK_STAIRS,
                Material.COBBLESTONE, Material.COBBLED_DEEPSLATE)) {
            return;
        }
        String id = "mansion_" + block.getWorld().getName() + "_" + block.getX() / 200 + "_" + block.getZ() / 200;
        if (isClaimed(id)) {
            return;
        }
        if (scanMansion(block, id, ctx.uuid())) {
            grantBonus(ctx);
            broadcast(ctx, "Woodland Mansion");
        }
    }

    private boolean isEligible(Block block, Material... materials) {
        for (Material m : materials) {
            if (block.getType() == m) return true;
        }
        return false;
    }

    private boolean isClaimed(String id) {
        try {
            return structureDao.isClaimed(id);
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean scanMonument(Block origin, String id, UUID playerUuid) {
        World world = origin.getWorld();
        int cx = origin.getX();
        int cz = origin.getZ();
        int cy = origin.getY();
        if (!sample(world, cx, cy, cz, 50, 20, Material.PRISMARINE, Material.PRISMARINE_BRICKS,
                Material.DARK_PRISMARINE, Material.SEA_LANTERN, Material.SPONGE, Material.WET_SPONGE, Material.GOLD_BLOCK)) {
            return false;
        }
        int eligible = 0;
        int destroyed = 0;
        for (int dx = -50; dx <= 50; dx++) {
            for (int dz = -50; dz <= 50; dz++) {
                for (int dy = -20; dy <= 20; dy++) {
                    Block b = world.getBlockAt(cx + dx, cy + dy, cz + dz);
                    if (isEligible(b, Material.PRISMARINE, Material.PRISMARINE_BRICKS, Material.DARK_PRISMARINE,
                            Material.SEA_LANTERN, Material.SPONGE, Material.WET_SPONGE, Material.GOLD_BLOCK)) {
                        eligible++;
                        if (b.getType() == Material.AIR) destroyed++;
                    }
                }
            }
        }
        if (eligible > 0 && destroyed >= eligible * 0.8) {
            try {
                structureDao.claim(id, "OCEAN_MONUMENT", world.getName(), cx, cy, cz, playerUuid.toString());
            } catch (SQLException ignored) {}
            return true;
        }
        return false;
    }

    private boolean scanMansion(Block origin, String id, UUID playerUuid) {
        World world = origin.getWorld();
        int cx = origin.getX();
        int cz = origin.getZ();
        int cy = origin.getY();
        if (!sample(world, cx, cy, cz, 60, 25, Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS,
                Material.DARK_OAK_STAIRS, Material.COBBLESTONE, Material.COBBLED_DEEPSLATE)) {
            return false;
        }
        int eligible = 0;
        int destroyed = 0;
        for (int dx = -60; dx <= 60; dx++) {
            for (int dz = -60; dz <= 60; dz++) {
                for (int dy = -20; dy <= 30; dy++) {
                    Block b = world.getBlockAt(cx + dx, cy + dy, cz + dz);
                    if (isEligible(b, Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS, Material.DARK_OAK_STAIRS,
                            Material.COBBLESTONE, Material.COBBLED_DEEPSLATE)) {
                        eligible++;
                        if (b.getType() == Material.AIR) destroyed++;
                    }
                }
            }
        }
        if (eligible > 100 && destroyed >= eligible * 0.8) {
            try {
                structureDao.claim(id, "WOODLAND_MANSION", world.getName(), cx, cy, cz, playerUuid.toString());
            } catch (SQLException ignored) {}
            return true;
        }
        return false;
    }

    private boolean sample(World world, int cx, int cy, int cz, int radius, int height, Material... materials) {
        int sampleSize = 100;
        int destroyed = 0;
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < sampleSize; i++) {
            int x = cx + rand.nextInt(radius * 2 + 1) - radius;
            int z = cz + rand.nextInt(radius * 2 + 1) - radius;
            int y = cy + rand.nextInt(height * 2 + 1) - height;
            Block b = world.getBlockAt(x, y, z);
            if (isEligible(b, materials)) {
                if (b.getType() == Material.AIR) destroyed++;
            }
        }
        return destroyed >= sampleSize * 0.35;
    }

    private void grantBonus(PlayerContext ctx) {
        for (PointCategory cat : PointCategory.values()) {
            pointService.addPoints(ctx.uuid(), cat, 1000, 0);
        }
    }

    private void broadcast(PlayerContext ctx, String name) {
        ctx.player().sendTitle("Structure Destroyed!", "+5000 Points", 10, 70, 20);
        ctx.player().getWorld().spawnParticle(org.bukkit.Particle.TOTEM_OF_UNDYING, ctx.player().getLocation(), 100);
        org.bukkit.Bukkit.broadcastMessage(org.bukkit.ChatColor.GOLD + ctx.player().getName()
                + " destroyed a " + name + "! +5000 Points");
    }

    public record PlayerContext(UUID uuid, org.bukkit.entity.Player player) {
    }
}
