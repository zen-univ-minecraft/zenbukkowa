package com.zenbukkowa.domain;

import com.zenbukkowa.persistence.BlockDiscoveryDao;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BlockDiscoveryService {
    private final BlockDiscoveryDao blockDiscoveryDao;
    private final PointService pointService;
    private final LocaleService localeService;

    public BlockDiscoveryService(BlockDiscoveryDao blockDiscoveryDao, PointService pointService, LocaleService localeService) {
        this.blockDiscoveryDao = blockDiscoveryDao;
        this.pointService = pointService;
        this.localeService = localeService;
    }

    public int checkDiscovery(Player player, Material material, PlayerSkills skills) {
        if (material == null || !material.isBlock()) return 0;
        UUID uuid = player.getUniqueId();
        if (blockDiscoveryDao.isDiscovered(uuid, material)) return 0;
        int bonus = discoveryBonus(material, skills);
        if (bonus > 0) {
            blockDiscoveryDao.recordDiscovery(uuid, material);
            pointService.addPoints(uuid, PointCategory.DISCOVERY, bonus, 0);
            String msg = localeService.get(uuid, "discovery.new_block")
                    .replace("{block}", formatName(material))
                    .replace("{points}", String.valueOf(bonus));
            player.sendMessage(msg);
        }
        return bonus;
    }

    private String formatName(Material material) {
        String name = material.name().toLowerCase().replace('_', ' ');
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private int discoveryBonus(Material material, PlayerSkills skills) {
        int base = baseBonus(material);
        int geologist = skills.tier(SkillType.GEOLOGIST);
        int surveyor = skills.tier(SkillType.SURVEYOR);
        int cartographer = skills.tier(SkillType.CARTOGRAPHER);
        int pathfinder = skills.tier(SkillType.PATHFINDER);
        int worldWalker = skills.tier(SkillType.WORLD_WALKER);

        switch (rarity(material)) {
            case COMMON -> base += geologist * 5;
            case UNCOMMON -> base += surveyor * 10;
            case RARE -> base += cartographer * 15;
            case EPIC -> base += pathfinder * 50;
        }

        if (worldWalker > 0) {
            base = (int) (base * (1 + worldWalker * 0.1));
        }
        return base;
    }

    private int baseBonus(Material material) {
        return switch (rarity(material)) {
            case COMMON -> 10;
            case UNCOMMON -> 25;
            case RARE -> 50;
            case EPIC -> 200;
        };
    }

    private Rarity rarity(Material material) {
        String name = material.name();
        if (name.contains("ANCIENT_DEBRIS") || name.contains("SPAWNER") || name.contains("DRAGON_EGG")) {
            return Rarity.EPIC;
        }
        if (name.contains("_ORE") || name.contains("AMETHYST") || name.contains("DIAMOND") || name.contains("EMERALD")) {
            return Rarity.RARE;
        }
        if (name.contains("LOG") || name.contains("WOOD") || name.contains("PLANKS") || name.contains("LEAVES")
                || name.contains("NYLIUM") || name.contains("WART_BLOCK") || name.contains("SHROOMLIGHT")
                || name.contains("HAY") || name.contains("MELON") || name.contains("PUMPKIN")
                || name.contains("BAMBOO") || name.contains("SUGAR_CANE") || name.contains("CACTUS")
                || name.contains("VINE") || name.contains("MOSS") || name.contains("GRASS_BLOCK")) {
            return Rarity.UNCOMMON;
        }
        return Rarity.COMMON;
    }

    private enum Rarity { COMMON, UNCOMMON, RARE, EPIC }
}
