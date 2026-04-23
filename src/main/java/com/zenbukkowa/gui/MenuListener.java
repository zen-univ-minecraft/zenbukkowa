package com.zenbukkowa.gui;

import com.zenbukkowa.domain.*;
import com.zenbukkowa.persistence.SettingsDao;
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
    private final HotbarMenuService hotbarMenuService;
    private final SkillsPurchaseHandler skillsHandler;
    private final ScoreboardService scoreboardService;
    private final EffectService effectService;
    private final EventService eventService;
    private final LocaleService locale;
    private final SettingsDao settingsDao;

    public MenuListener(MenuService menuService, HotbarMenuService hotbarMenuService,
                        SkillsPurchaseHandler skillsHandler, ScoreboardService scoreboardService,
                        EffectService effectService, EventService eventService,
                        LocaleService locale, SettingsDao settingsDao) {
        this.menuService = menuService;
        this.hotbarMenuService = hotbarMenuService;
        this.skillsHandler = skillsHandler;
        this.scoreboardService = scoreboardService;
        this.effectService = effectService;
        this.eventService = eventService;
        this.locale = locale;
        this.settingsDao = settingsDao;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String open = menuService.getOpen(player);
        if (open == null) return;
        event.setCancelled(true);
        if (event.getClickedInventory() != event.getInventory()) { syncCursor(player); return; }
        ItemStack clicked = event.getCurrentItem();
        if (clicked != null && clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) { syncCursor(player); return; }
        int slot = event.getSlot();
        if (open.startsWith("help_")) {
            if (slot == 49) HelpMenu.open(player, menuService, locale);
            else HelpTopicMenu.onClick(player, menuService, locale, open.substring(5), slot);
        } else {
            switch (open) {
                case "root" -> handleRoot(player, slot);
                case "skills" -> skillsHandler.handleSkills(player, slot);
                case "stats" -> handleStats(player, slot);
                case "leaderboard" -> handleLeaderboard(player, slot);
                case "settings" -> handleSettings(player, slot);
                case "help" -> handleHelp(player, slot);
            }
        }
        syncCursor(player);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (menuService.getOpen(player) == null) return;
        int topSize = event.getView().getTopInventory().getSize();
        if (topSize == 0) return;
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot < topSize) { event.setCancelled(true); return; }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            menuService.clearOpen(player);
            hotbarMenuService.resyncAfterMenuClose(player);
        }
    }

    private void syncCursor(Player player) {
        hotbarMenuService.clearGhostCursor(player);
        player.updateInventory();
    }

    private void handleRoot(Player player, int slot) {
        switch (slot) {
            case 11 -> { menuService.resetScroll(player); SkillsMenu.open(player, menuService, skillsHandler.skillService(), skillsHandler.pointService(), locale); }
            case 12 -> StatsMenu.openPersonal(player, menuService, skillsHandler.pointService(), locale);
            case 13 -> StatsMenu.openLeaderboard(player, menuService, skillsHandler.pointService(), locale);
            case 14 -> SettingsMenu.open(player, menuService, locale, scoreboardService, eventService, settingsDao);
            case 15 -> HelpMenu.open(player, menuService, locale);
            case 49 -> player.closeInventory();
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
                SettingsMenu.open(player, menuService, locale, scoreboardService, eventService, settingsDao);
            }
            case 12 -> {
                boolean next = !scoreboardService.isEnabled(player.getUniqueId());
                scoreboardService.setEnabled(player.getUniqueId(), next);
                player.sendMessage(ChatColor.YELLOW + (next ? "Scoreboard ON" : "Scoreboard OFF"));
                SettingsMenu.open(player, menuService, locale, scoreboardService, eventService, settingsDao);
            }
            case 14 -> { if (player.isOp()) { eventService.start(); player.sendMessage(ChatColor.GREEN + locale.get(player.getUniqueId(), "menu.event_started")); } }
            case 16 -> { if (player.isOp()) { eventService.end(); player.sendMessage(ChatColor.RED + locale.get(player.getUniqueId(), "menu.event_ended")); } }
            case 20 -> {
                if (player.isOp()) {
                    double current = Double.parseDouble(settingsDao.loadSetting("point_multiplier", "1.0"));
                    double next = switch ((int) (current * 10)) {
                        case 5 -> 1.0;
                        case 10 -> 2.0;
                        case 20 -> 5.0;
                        default -> 0.5;
                    };
                    settingsDao.saveSetting("point_multiplier", String.valueOf(next));
                    skillsHandler.pointService().setMultiplier(next);
                    player.sendMessage(ChatColor.GREEN + "Point multiplier: " + next + "x");
                    SettingsMenu.open(player, menuService, locale, scoreboardService, eventService, settingsDao);
                }
            }
            case 31 -> {
                if (player.isOp()) {
                    skillsHandler.pointService().resetAll(); skillsHandler.skillService().resetAll(); eventService.reset();
                    player.sendMessage(ChatColor.RED + locale.get(player.getUniqueId(), "menu.reset_done"));
                    for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) effectService.applyAll(p);
                    scoreboardService.updateAll();
                    SettingsMenu.open(player, menuService, locale, scoreboardService, eventService, settingsDao);
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
