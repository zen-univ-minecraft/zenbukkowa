package com.zenbukkowa.domain;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectService {
    private final SkillService skillService;

    public EffectService(SkillService skillService) {
        this.skillService = skillService;
    }

    public void applyAll(Player player) {
        int haste = Math.max(skillService.haste(player.getUniqueId()), skillService.efficiency(player.getUniqueId()));
        applyOrRemove(player, PotionEffectType.HASTE, haste);
        applyTideBreaker(player);
        applyNightVision(player);
        applyFireResistance(player);
        applyFrostWalker(player);
        applyConduitAura(player);
        applyVoidWalk(player);
    }

    public void applyTideBreaker(Player player) {
        boolean has = skillService.getSkills(player.getUniqueId()).hasSkill(SkillType.TIDE_BREAKER);
        if (has && player.isInWater()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 400, 0, true, false, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 400, 0, true, false, true));
        }
    }

    public void applyNightVision(Player player) {
        boolean has = skillService.getSkills(player.getUniqueId()).hasSkill(SkillType.NIGHT_VISION);
        applyOrRemove(player, PotionEffectType.NIGHT_VISION, has ? 1 : 0);
    }

    public void applyFireResistance(Player player) {
        boolean has = skillService.getSkills(player.getUniqueId()).hasSkill(SkillType.FIRE_RESISTANCE);
        applyOrRemove(player, PotionEffectType.FIRE_RESISTANCE, has ? 1 : 0);
    }

    public void applyFrostWalker(Player player) {
        boolean has = skillService.getSkills(player.getUniqueId()).hasSkill(SkillType.FROST_WALKER);
        ItemStack boots = player.getInventory().getBoots();
        if (boots == null || boots.getType().isAir()) return;
        ItemMeta meta = boots.getItemMeta();
        if (meta == null) return;
        if (has) {
            meta.addEnchant(Enchantment.FROST_WALKER, 1, true);
        } else {
            meta.removeEnchant(Enchantment.FROST_WALKER);
        }
        boots.setItemMeta(meta);
    }

    public void applyConduitAura(Player player) {
        int tier = skillService.getSkills(player.getUniqueId()).tier(SkillType.CONDUIT_AURA);
        if (tier > 0 && player.isInWater()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 400, tier - 1, true, false, true));
        }
    }

    public void applyVoidWalk(Player player) {
        int tier = skillService.getSkills(player.getUniqueId()).tier(SkillType.VOID_WALK);
        if (tier > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 400, Math.min(tier - 1, 2), true, false, true));
        }
    }

    private void applyOrRemove(Player player, PotionEffectType type, int level) {
        if (level > 0) {
            player.addPotionEffect(new PotionEffect(type, 400, level - 1, true, false, true));
        } else {
            player.removePotionEffect(type);
        }
    }
}
