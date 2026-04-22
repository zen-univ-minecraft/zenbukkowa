package com.zenbukkowa.gui;

import com.zenbukkowa.domain.LocaleService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class HelpMenu {
    public static void open(Player player, MenuService menuService, LocaleService locale) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + locale.get(player.getUniqueId(), "menu.help_title"));
        inv.setItem(11, MenuItems.create(Material.BOOK, ChatColor.GREEN + locale.get(player.getUniqueId(), "menu.help_rules")));
        inv.setItem(12, MenuItems.create(Material.STONE, ChatColor.GRAY + locale.get(player.getUniqueId(), "menu.help_points")));
        inv.setItem(13, MenuItems.create(Material.DIAMOND_PICKAXE, ChatColor.AQUA + locale.get(player.getUniqueId(), "menu.help_skills")));
        inv.setItem(14, MenuItems.create(Material.TNT, ChatColor.RED + locale.get(player.getUniqueId(), "menu.help_breaking")));
        inv.setItem(15, MenuItems.create(Material.PRISMARINE, ChatColor.BLUE + locale.get(player.getUniqueId(), "menu.help_structures")));
        inv.setItem(16, MenuItems.create(Material.COMMAND_BLOCK, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.help_commands")));
        inv.setItem(49, MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.back")));
        MenuItems.fillEmpty(inv);
        player.openInventory(inv);
        menuService.setOpen(player, "help");
    }
}
