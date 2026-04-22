package com.zenbukkowa.gui;

import com.zenbukkowa.domain.EffectService;
import com.zenbukkowa.domain.PointCategory;
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
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            return;
        }

        switch (open) {
            case "root" -> handleRoot(player, clicked);
            case "skills" -> handleSkills(player, clicked);
            case "stats" -> handleStats(player, clicked);
            case "leaderboard" -> handleLeaderboard(player, clicked);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            menuService.clear(player);
        }
    }

    private void handleRoot(Player player, ItemStack clicked) {
        switch (clicked.getType()) {
            case DIAMOND_PICKAXE -> SkillsMenu.open(player, menuService, skillService, pointService);
            case BOOK -> StatsMenu.openPersonal(player, menuService, pointService);
            case GOLD_INGOT -> StatsMenu.openLeaderboard(player, menuService, pointService);
            case REDSTONE -> {
                boolean next = !scoreboardService.isEnabled(player.getUniqueId());
                scoreboardService.setEnabled(player.getUniqueId(), next);
                player.sendMessage(ChatColor.YELLOW + "Scoreboard " + (next ? "enabled" : "disabled"));
                player.closeInventory();
            }
        }
    }

    private void handleSkills(Player player, ItemStack clicked) {
        if (clicked.getType() == Material.ARROW) {
            RootMenu.open(player, menuService);
            return;
        }
        SkillType[] skills = SkillType.values();
        int slot = eventToSlot(player, clicked);
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

    private void handleStats(Player player, ItemStack clicked) {
        if (clicked.getType() == Material.ARROW) {
            RootMenu.open(player, menuService);
        }
    }

    private void handleLeaderboard(Player player, ItemStack clicked) {
        if (clicked.getType() == Material.ARROW) {
            StatsMenu.openPersonal(player, menuService, pointService);
        }
    }

    private int eventToSlot(Player player, ItemStack clicked) {
        var inv = player.getOpenInventory().getTopInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            if (clicked.equals(inv.getItem(i))) {
                return i;
            }
        }
        return -1;
    }
}
