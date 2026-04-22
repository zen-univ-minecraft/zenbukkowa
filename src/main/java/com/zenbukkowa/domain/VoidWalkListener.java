package com.zenbukkowa.domain;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class VoidWalkListener implements Listener {
    private final SkillService skillService;

    public VoidWalkListener(SkillService skillService) {
        this.skillService = skillService;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof org.bukkit.entity.Player player)) return;
        int tier = skillService.getSkills(player.getUniqueId()).tier(SkillType.VOID_WALK);
        if (tier <= 0) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
        if (tier >= 3 && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        int tier = skillService.getSkills(player.getUniqueId()).tier(SkillType.VOID_WALK);
        if (tier >= 3 && player.getLocation().getY() < -60) {
            org.bukkit.Location safe = player.getLocation().clone();
            safe.setY(player.getWorld().getMinHeight() + 5);
            player.teleport(safe);
        }
    }
}
