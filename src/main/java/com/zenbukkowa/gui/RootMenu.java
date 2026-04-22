package com.zenbukkowa.gui;

import com.zenbukkowa.domain.LocaleService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RootMenu {
    public static void open(Player player, MenuService menuService, LocaleService locale) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + locale.get(player.getUniqueId(), "menu.root_title"));
        inv.setItem(11, MenuItems.create(Material.DIAMOND_PICKAXE, ChatColor.AQUA + locale.get(player.getUniqueId(), "menu.skills_button")));
        inv.setItem(12, MenuItems.create(Material.BOOK, ChatColor.GREEN + locale.get(player.getUniqueId(), "menu.stats_button")));
        inv.setItem(13, MenuItems.create(Material.GOLD_INGOT, ChatColor.YELLOW + locale.get(player.getUniqueId(), "menu.leaderboard_button")));
        inv.setItem(14, MenuItems.create(Material.REDSTONE, ChatColor.RED + locale.get(player.getUniqueId(), "menu.settings_button")));
        inv.setItem(15, MenuItems.create(Material.OAK_SIGN, ChatColor.BLUE + locale.get(player.getUniqueId(), "menu.help_button")));
        inv.setItem(49, MenuItems.create(Material.BARRIER, ChatColor.RED + locale.get(player.getUniqueId(), "menu.close")));
        MenuItems.fillEmpty(inv);
        player.openInventory(inv);
        menuService.setOpen(player, "root");
    }
}
