package com.zenbukkowa.gui;

import com.zenbukkowa.domain.LocaleService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class HelpTopicMenu {

    public static void open(Player player, MenuService menuService, LocaleService locale, String topicKey) {
        String titleKey = "menu.help_title_" + topicKey;
        String title = locale.get(player.getUniqueId(), titleKey);
        if (title.startsWith("menu.")) title = topicKey;
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + title);

        List<String> lines = locale.getList(player.getUniqueId(), "help." + topicKey);
        int slot = 10;
        int row = 1;
        for (String line : lines) {
            if (slot >= 44) break;
            inv.setItem(slot, MenuItems.create(Material.BOOK, line));
            slot++;
            if (slot % 9 == 8) {
                slot += 2;
                row++;
            }
        }

        inv.setItem(49, MenuItems.create(Material.ARROW, ChatColor.WHITE + locale.get(player.getUniqueId(), "menu.back")));
        MenuItems.fillEmpty(inv);
        player.openInventory(inv);
        menuService.setOpen(player, "help_" + topicKey);
    }
}
