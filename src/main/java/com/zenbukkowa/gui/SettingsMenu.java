package com.zenbukkowa.gui;

import com.zenbukkowa.domain.EventService;
import com.zenbukkowa.domain.LocaleService;
import com.zenbukkowa.persistence.SettingsDao;
import com.zenbukkowa.scoreboard.ScoreboardService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsMenu {
    public static void open(Player player, MenuService menuService,
                            LocaleService locale, ScoreboardService scoreboardService,
                            EventService eventService, SettingsDao settingsDao) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + locale.get(player.getUniqueId(), "menu.settings_title"));
        String lang = locale.getLocale(player.getUniqueId());
        inv.setItem(10, MenuItems.create(Material.BOOK, ChatColor.GREEN + locale.get(player.getUniqueId(), "menu.language"),
                ChatColor.GRAY + locale.get(player.getUniqueId(), "menu.current_language")));
        boolean sb = scoreboardService.isEnabled(player.getUniqueId());
        inv.setItem(12, MenuItems.create(Material.ENDER_EYE, sb ? ChatColor.GREEN + locale.get(player.getUniqueId(), "menu.scoreboard_on")
                : ChatColor.RED + locale.get(player.getUniqueId(), "menu.scoreboard_off")));
        if (player.isOp()) {
            inv.setItem(14, MenuItems.create(Material.EMERALD_BLOCK, ChatColor.GREEN + locale.get(player.getUniqueId(), "menu.event_start")));
            inv.setItem(16, MenuItems.create(Material.REDSTONE_BLOCK, ChatColor.RED + locale.get(player.getUniqueId(), "menu.event_end")));
            inv.setItem(20, MenuItems.create(Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE + "Multiplier: "
                    + settingsDao.loadSetting("point_multiplier", "1.0") + "x"));
            inv.setItem(25, MenuItems.create(Material.COMMAND_BLOCK, ChatColor.LIGHT_PURPLE + locale.get(player.getUniqueId(), "menu.event_controls")));
            inv.setItem(31, MenuItems.create(Material.TNT, ChatColor.DARK_RED + locale.get(player.getUniqueId(), "menu.reset_all")));
        }
        inv.setItem(49, MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.back")));
        MenuItems.fillEmpty(inv);
        player.openInventory(inv);
        menuService.setOpen(player, "settings");
    }
}
