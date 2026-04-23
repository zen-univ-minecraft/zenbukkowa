package com.zenbukkowa.gui;

import com.zenbukkowa.domain.LocaleService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class HotbarMenuService {
    private final JavaPlugin plugin;
    private final MenuService menuService;
    private final LocaleService localeService;
    public static final int SLOT = 8;
    private static final byte TOKEN_VALUE = 1;
    private final NamespacedKey tokenKey;

    public HotbarMenuService(JavaPlugin plugin, MenuService menuService, LocaleService localeService) {
        this.plugin = plugin;
        this.menuService = menuService;
        this.localeService = localeService;
        this.tokenKey = new NamespacedKey(plugin, "menu-hotbar-token");
        startPeriodicEnforcement();
    }

    public void install(Player player) {
        player.getInventory().setItem(SLOT, createToken());
    }

    public void installIfMissing(Player player) {
        if (!isToken(player.getInventory().getItem(SLOT))) install(player);
    }

    public void ensureInstalled(Player player) {
        installIfMissing(player);
    }

    public void installDelayed(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> installIfMissing(player), 1);
    }

    public boolean isToken(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        Byte marker = meta.getPersistentDataContainer().get(tokenKey, PersistentDataType.BYTE);
        return marker != null && marker == TOKEN_VALUE;
    }

    public boolean isSlotLocked(int slot) {
        return slot == SLOT;
    }

    public void openMenu(Player player) {
        RootMenu.open(player, menuService, localeService);
    }

    public void openFromInventoryInteraction(Player player) {
        ensureInstalled(player);
        clearGhostCursor(player);
        openMenu(player);
        resyncNextTick(player);
    }

    public void resyncAfterMenuClose(Player player) {
        resyncNextTick(player);
    }

    public void clearGhostCursor(Player player) {
        if (isToken(player.getItemOnCursor())) {
            player.setItemOnCursor(null);
        }
    }

    private void resyncNextTick(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ensureInstalled(player);
            clearGhostCursor(player);
            player.updateInventory();
        }, 1);
    }

    private void startPeriodicEnforcement() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) installIfMissing(player);
        }, 100, 100);
    }

    private ItemStack createToken() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + "Menu");
            meta.getPersistentDataContainer().set(tokenKey, PersistentDataType.BYTE, TOKEN_VALUE);
            item.setItemMeta(meta);
        }
        return item;
    }
}
