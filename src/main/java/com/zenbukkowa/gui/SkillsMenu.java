package com.zenbukkowa.gui;

import com.zenbukkowa.domain.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SkillsMenu {

    public static void open(Player player, MenuService menuService,
                            SkillService skillService, PointService pointService,
                            LocaleService locale) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + locale.get(player.getUniqueId(), "menu.skills_title"));
        int offset = menuService.getScrollOffset(player);
        SkillTreeViewport.render(inv, player, offset, skillService, pointService, locale);
        player.openInventory(inv);
        menuService.setOpen(player, "skills");
    }
}
