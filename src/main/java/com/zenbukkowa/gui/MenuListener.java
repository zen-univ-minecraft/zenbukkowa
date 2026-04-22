package com.zenbukkowa.gui;

import com.zenbukkowa.domain.EffectService;
import com.zenbukkowa.domain.PointService;
import com.zenbukkowa.domain.SkillService;
import com.zenbukkowa.domain.SkillType;
import com.zenbukkowa.scoreboard.ScoreboardService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {
    private final MenuService menuService;
    private final SkillService skillService;
    private final PointService pointService;
    private final ScoreboardService scoreboardService;
    private final EffectService effectService;

    public MenuListener(MenuService menuService, SkillService skillService,
                        PointService pointService, ScoreboardService scoreboardService,
                        EffectService effectService) {
        this.menuService = menuService;
        this.skillService = skillService;
        this.pointService = pointService;
        this.scoreboardService = scoreboardService;
        this.effectService = effectService;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        String open = menuService.getOpen(player);
        if (open == null) {
            return;
        }
        event.setCancelled(true);
        if (event.getClickedInventory() != event.getInventory()) {
            return;
        }
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            return;
        }
        int slot = event.getSlot();

        switch (open) {
            case "root" -> handleRoot(player, slot);
            case "skills" -> handleSkills(player, slot);
            case "stats" -> handleStats(player, slot);
            case "leaderboard" -> handleLeaderboard(player, slot);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            menuService.clear(player);
        }
    }

    private void handleRoot(Player player, int slot) {
        switch (slot) {
            case 10 -> SkillsMenu.open(player, menuService, skillService, pointService);
            case 12 -> StatsMenu.openPersonal(player, menuService, pointService);
            case 14 -> StatsMenu.openLeaderboard(player, menuService, pointService);
            case 16 -> {
                boolean next = !scoreboardService.isEnabled(player.getUniqueId());
                scoreboardService.setEnabled(player.getUniqueId(), next);
                player.sendMessage(ChatColor.YELLOW + "Scoreboard " + (next ? "enabled" : "disabled"));
                player.closeInventory();
            }
        }
    }

    private void handleSkills(Player player, int slot) {
        if (slot == 49) {
            RootMenu.open(player, menuService);
            return;
        }
        SkillType[] skills = SkillType.values();
        if (slot < 0 || slot >= skills.length) {
            return;
        }
        SkillType skill = skills[slot];
        int current = skillService.getSkills(player.getUniqueId()).tier(skill);
        if (current >= skill.maxTier()) {
            player.sendMessage(ChatColor.RED + "Already maxed");
            return;
        }
        int cost = skill.cost(current + 1);
        try {
            pointService.spendPoints(player.getUniqueId(), skill.category(), cost);
            skillService.purchase(player.getUniqueId(), skill, current + 1);
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage(ChatColor.GREEN + "Purchased " + skill.name() + " tier " + (current + 1));
            effectService.applyAll(player);
            scoreboardService.updateAll();
            SkillsMenu.open(player, menuService, skillService, pointService);
        } catch (IllegalStateException e) {
            player.sendMessage(ChatColor.RED + e.getMessage());
        }
    }

    private void handleStats(Player player, int slot) {
        if (slot == 18) {
            RootMenu.open(player, menuService);
        }
    }

    private void handleLeaderboard(Player player, int slot) {
        if (slot == 18) {
            StatsMenu.openPersonal(player, menuService, pointService);
        }
    }
}
