package com.zenbukkowa.domain;

import com.zenbukkowa.plugin.SchedulerBridge;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EffectListener implements Listener {
    private final EffectService effectService;

    public EffectListener(EffectService effectService, SchedulerBridge scheduler, JavaPlugin plugin) {
        this.effectService = effectService;
        scheduler.runTimer(plugin, () -> {
            for (var player : Bukkit.getOnlinePlayers()) {
                effectService.applyAll(player);
            }
        }, 0, 200);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        effectService.applyAll(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        effectService.applyAll(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockY() != event.getTo().getBlockY()
                || !event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            effectService.applyTideBreaker(event.getPlayer());
        }
    }
}
