package com.zenbukkowa.gui;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class HotbarMenuListener implements Listener {
    private final HotbarMenuService hotbarMenuService;

    public HotbarMenuListener(HotbarMenuService hotbarMenuService) {
        this.hotbarMenuService = hotbarMenuService;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        hotbarMenuService.install(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRespawn(PlayerRespawnEvent event) {
        hotbarMenuService.install(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        if (hotbarMenuService.isToken(event.getItem())) {
            event.setCancelled(true);
            hotbarMenuService.openMenu(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrop(PlayerDropItemEvent event) {
        if (hotbarMenuService.isToken(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            hotbarMenuService.openMenu(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof org.bukkit.entity.Player player)) {
            return;
        }
        int slot = event.getRawSlot();
        if (event.getClick() == ClickType.NUMBER_KEY && event.getHotbarButton() == 8) {
            event.setCancelled(true);
            return;
        }
        if (slot == 8 && event.getClickedInventory() == player.getInventory()) {
            event.setCancelled(true);
            if (event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
                hotbarMenuService.openMenu(player);
            }
            return;
        }
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        if (hotbarMenuService.isToken(current) || hotbarMenuService.isToken(cursor)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (hotbarMenuService.isToken(event.getOldCursor())) {
            for (int slot : event.getRawSlots()) {
                if (slot == 8 || (slot >= 36 && slot <= 44 && slot == 44)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSwap(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            if (event.getHotbarButton() == 8 || (event.getCurrentItem() != null && hotbarMenuService.isToken(event.getCurrentItem()))) {
                event.setCancelled(true);
            }
        }
    }
}
