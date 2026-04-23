package com.zenbukkowa.gui;

import com.zenbukkowa.domain.*;
import com.zenbukkowa.scoreboard.ScoreboardService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {
    private final MenuService menuService;
    private final SkillService skillService;
    private final PointService pointService;
    private final ScoreboardService scoreboardService;
    private final EffectService effectService;
    private final EventService eventService;
    private final LocaleService locale;

    public MenuListener(MenuService menuService, SkillService skillService,
                        PointService pointService, ScoreboardService scoreboardService,
                        EffectService effectService, EventService eventService,
                        LocaleService locale) {
        this.menuService = menuService;
        this.skillService = skillService;
        this.pointService = pointService;
        this.scoreboardService = scoreboardService;
        this.effectService = effectService;
        this.eventService = eventService;
        this.locale = locale;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String open = menuService.getOpen(player);
        if (open == null) return;
        event.setCancelled(true);
        if (event.getClickedInventory() != event.getInventory()) return;
        ItemStack clicked = event.getCurrentItem();
        if (clicked != null && clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
        int slot = event.getSlot();

        if (open.startsWith("help_")) {
            if (slot == 49) HelpMenu.open(player, menuService, locale);
            else HelpTopicMenu.onClick(player, menuService, locale, open.substring(5), slot);
            return;
        }
        switch (open) {
            case "root" -> handleRoot(player, slot);
            case "skills" -> handleSkills(player, slot);
            case "stats" -> handleStats(player, slot);
            case "leaderboard" -> handleLeaderboard(player, slot);
            case "settings" -> handleSettings(player, slot);
            case "help" -> handleHelp(player, slot);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (menuService.getOpen(player) == null) return;
        int topSize = event.getView().getTopInventory().getSize();
        if (topSize == 0) return;
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot < topSize) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) menuService.clear(player);
    }

    private void handleRoot(Player player, int slot) {
        switch (slot) {
            case 11 -> SkillsMenu.open(player, menuService, skillService, pointService, locale);
            case 12 -> StatsMenu.openPersonal(player, menuService, pointService, locale);
            case 13 -> StatsMenu.openLeaderboard(player, menuService, pointService, locale);
            case 14 -> SettingsMenu.open(player, menuService, locale, scoreboardService, eventService);
            case 15 -> HelpMenu.open(player, menuService, locale);
            case 49 -> player.closeInventory();
        }
    }

    private void handleSkills(Player player, int slot) {
        if (slot == 49) { RootMenu.open(player, menuService, locale); return; }
        if (slot == 45) {
            int off = menuService.getScrollOffset(player);
            if (off > 0) {
                menuService.setScrollOffset(player, off - 1);
                SkillsMenu.open(player, menuService, skillService, pointService, locale);
            }
            return;
        }
        if (slot == 53) {
            int off = menuService.getScrollOffset(player);
            if (off < SkillTreeLayout.MAX_SCROLL) {
                menuService.setScrollOffset(player, off + 1);
                SkillsMenu.open(player, menuService, skillService, pointService, locale);
            }
            return;
        }
        int offset = menuService.getScrollOffset(player);
        SkillType skill = SkillTreeViewport.skillAtSlot(slot, offset);
        if (skill == null) return;
        int current = skillService.getSkills(player.getUniqueId()).tier(skill);
        if (current >= skill.maxTier()) {
            player.sendMessage(ChatColor.RED + locale.get(player.getUniqueId(), "menu.purchase_maxed"));
            return;
        }
        int cost = skill.cost(current + 1);
        try {
            pointService.spendPoints(player.getUniqueId(), skill.category(), cost);
            skillService.purchase(player.getUniqueId(), skill, current + 1);
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage(ChatColor.GREEN + locale.get(player.getUniqueId(), "menu.purchase_success")
                    .replace("{skill}", skill.name().replace('_', ' '))
                    .replace("{tier}", String.valueOf(current + 1)));
            effectService.applyAll(player);
            scoreboardService.updateAll();
            SkillsMenu.open(player, menuService, skillService, pointService, locale);
        } catch (IllegalStateException e) {
            player.sendMessage(ChatColor.RED + e.getMessage());
        }
    }

    private void handleStats(Player player, int slot) {
        if (slot == 49) RootMenu.open(player, menuService, locale);
    }

    private void handleLeaderboard(Player player, int slot) {
        if (slot == 49) RootMenu.open(player, menuService, locale);
    }

    private void handleSettings(Player player, int slot) {
        switch (slot) {
            case 10 -> {
                String next = locale.toggleLocale(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "Language: " + next.toUpperCase());
                SettingsMenu.open(player, menuService, locale, scoreboardService, eventService);
            }
            case 12 -> {
                boolean next = !scoreboardService.isEnabled(player.getUniqueId());
                scoreboardService.setEnabled(player.getUniqueId(), next);
                player.sendMessage(ChatColor.YELLOW + (next ? "Scoreboard ON" : "Scoreboard OFF"));
                SettingsMenu.open(player, menuService, locale, scoreboardService, eventService);
            }
            case 14 -> {
                if (player.isOp()) {
                    eventService.start();
                    player.sendMessage(ChatColor.GREEN + locale.get(player.getUniqueId(), "menu.event_started"));
                }
            }
            case 16 -> {
                if (player.isOp()) {
                    eventService.end();
                    player.sendMessage(ChatColor.RED + locale.get(player.getUniqueId(), "menu.event_ended"));
                }
            }
            case 31 -> {
                if (player.isOp()) {
                    pointService.resetAll();
                    skillService.resetAll();
                    eventService.reset();
                    player.sendMessage(ChatColor.RED + locale.get(player.getUniqueId(), "menu.reset_done"));
                    for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
                        effectService.applyAll(p);
                    }
                    scoreboardService.updateAll();
                    SettingsMenu.open(player, menuService, locale, scoreboardService, eventService);
                }
            }
            case 49 -> RootMenu.open(player, menuService, locale);
        }
    }

    private void handleHelp(Player player, int slot) {
        switch (slot) {
            case 11 -> HelpTopicMenu.open(player, menuService, locale, "rules");
            case 12 -> HelpTopicMenu.open(player, menuService, locale, "points");
            case 13 -> HelpTopicMenu.open(player, menuService, locale, "skills");
            case 14 -> HelpTopicMenu.open(player, menuService, locale, "breaking");
            case 15 -> HelpTopicMenu.open(player, menuService, locale, "structures");
            case 16 -> HelpTopicMenu.open(player, menuService, locale, "commands");
            case 49 -> RootMenu.open(player, menuService, locale);
        }
    }
}
