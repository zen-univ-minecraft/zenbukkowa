package com.zenbukkowa.gui;

import com.zenbukkowa.domain.PointCategory;
import com.zenbukkowa.domain.PointService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StatsMenu {

    public static void openPersonal(Player player, MenuService menuService, PointService pointService) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Your Stats");
        var progress = pointService.getProgress(player.getUniqueId());

        inv.setItem(10, MenuItems.create(Material.NETHER_STAR, ChatColor.GOLD + "Total: " + format(progress.totalPoints())));
        inv.setItem(12, MenuItems.create(Material.STONE, ChatColor.GREEN + "TERRA: " + format(progress.points(PointCategory.TERRA))));
        inv.setItem(13, MenuItems.create(Material.IRON_ORE, ChatColor.AQUA + "MINERAL: " + format(progress.points(PointCategory.MINERAL))));
        inv.setItem(14, MenuItems.create(Material.OAK_LOG, ChatColor.DARK_GREEN + "ORGANIC: " + format(progress.points(PointCategory.ORGANIC))));
        inv.setItem(15, MenuItems.create(Material.PRISMARINE, ChatColor.BLUE + "AQUATIC: " + format(progress.points(PointCategory.AQUATIC))));
        inv.setItem(16, MenuItems.create(Material.OBSIDIAN, ChatColor.DARK_PURPLE + "VOID: " + format(progress.points(PointCategory.VOID))));
        inv.setItem(22, MenuItems.create(Material.DIAMOND_PICKAXE, ChatColor.YELLOW + "Blocks: " + format(progress.blocksBroken())));
        inv.setItem(18, MenuItems.create(Material.ARROW, ChatColor.WHITE + "Back"));
        fillEmpty(inv);
        player.openInventory(inv);
        menuService.setOpen(player, "stats");
    }

    public static void openLeaderboard(Player player, MenuService menuService, PointService pointService) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Leaderboard");
        List<Map.Entry<UUID, Long>> board = pointService.getLeaderboard(9);

        for (int i = 0; i < board.size(); i++) {
            Map.Entry<UUID, Long> entry = board.get(i);
            org.bukkit.OfflinePlayer op = Bukkit.getOfflinePlayer(entry.getKey());
            String name = op.getName() != null ? op.getName() : "?";
            inv.setItem(10 + i, MenuItems.create(Material.PLAYER_HEAD,
                    ChatColor.GOLD + "#" + (i + 1) + " " + name,
                    ChatColor.YELLOW + "Points: " + format(entry.getValue())));
        }

        inv.setItem(18, MenuItems.create(Material.ARROW, ChatColor.WHITE + "Back to Stats"));
        fillEmpty(inv);
        player.openInventory(inv);
        menuService.setOpen(player, "leaderboard");
    }

    private static void fillEmpty(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, MenuItems.filler(Material.GRAY_STAINED_GLASS_PANE));
            }
        }
    }

    private static String format(long n) {
        return String.format("%,d", n);
    }
}
