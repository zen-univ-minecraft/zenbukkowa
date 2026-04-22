package com.zenbukkowa.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RootMenu {

    public static void open(Player player, MenuService menuService) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLACK + "zenbukkowa");
        inv.setItem(10, MenuItems.create(Material.DIAMOND_PICKAXE, ChatColor.AQUA + "Skills"));
        inv.setItem(12, MenuItems.create(Material.BOOK, ChatColor.GREEN + "Stats"));
        inv.setItem(14, MenuItems.create(Material.GOLD_INGOT, ChatColor.YELLOW + "Leaderboard"));
        inv.setItem(16, MenuItems.create(Material.REDSTONE, ChatColor.RED + "Settings"));
        fillEmpty(inv);
        player.openInventory(inv);
        menuService.setOpen(player, "root");
    }

    private static void fillEmpty(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));
            }
        }
    }
}
