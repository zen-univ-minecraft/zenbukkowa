package com.zenbukkowa.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class HotbarMenuService {
    private final JavaPlugin plugin;
    private final MenuService menuService;
    private static final int SLOT = 8;
    private static final ItemStack TOKEN = createToken();

    public HotbarMenuService(JavaPlugin plugin, MenuService menuService) {
        this.plugin = plugin;
        this.menuService = menuService;
        startPeriodicEnforcement();
    }

    public void install(Player player) {
        player.getInventory().setItem(SLOT, TOKEN.clone());
    }

    public void installIfMissing(Player player) {
        if (!isToken(player.getInventory().getItem(SLOT))) {
            install(player);
        }
    }

    public void installDelayed(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> installIfMissing(player), 1);
    }

    public boolean isToken(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && (ChatColor.RESET + "Menu").equals(meta.getDisplayName());
    }

    public boolean isSlotLocked(int slot) {
        return slot == SLOT;
    }

    public void openMenu(Player player) {
        RootMenu.open(player, menuService);
    }

    private void startPeriodicEnforcement() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                installIfMissing(player);
            }
        }, 100, 100);
    }

    private static ItemStack createToken() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + "Menu");
            item.setItemMeta(meta);
        }
        return item;
    }
}
