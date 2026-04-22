package com.zenbukkowa.domain;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectService {
    private final SkillService skillService;

    public EffectService(SkillService skillService) {
        this.skillService = skillService;
    }

    public void applyAll(Player player) {
        applyHaste(player);
        applyTideBreaker(player);
    }

    public void applyHaste(Player player) {
        int haste = skillService.haste(player.getUniqueId());
        if (haste > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 400, haste - 1, true, false, true));
        } else {
            player.removePotionEffect(PotionEffectType.HASTE);
        }
    }

    public void applyTideBreaker(Player player) {
        boolean hasTide = skillService.getSkills(player.getUniqueId()).hasSkill(SkillType.TIDE_BREAKER);
        if (hasTide && player.isInWater()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 400, 0, true, false, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 400, 0, true, false, true));
        }
    }
}
