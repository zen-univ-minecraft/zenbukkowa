package com.zenbukkowa.gui;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
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
        hotbarMenuService.installDelayed(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null) {
            return;
        }
        ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());
        if (!hotbarMenuService.isToken(item)) {
            return;
        }
        if (event.getHand() == org.bukkit.inventory.EquipmentSlot.OFF_HAND
                && hotbarMenuService.isToken(event.getPlayer().getInventory().getItemInMainHand())) {
            return;
        }
        event.setCancelled(true);
        hotbarMenuService.openMenu(event.getPlayer());
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
        if (event.getClick() == ClickType.NUMBER_KEY && event.getHotbarButton() == 8) {
            event.setCancelled(true);
            return;
        }
        if (isSlot8Click(event, player)) {
            event.setCancelled(true);
            if (event.getAction() == InventoryAction.PICKUP_ALL
                    || event.getAction() == InventoryAction.SWAP_WITH_CURSOR
                    || event.getAction() == InventoryAction.PLACE_ALL
                    || event.getAction() == InventoryAction.PLACE_ONE) {
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

    private boolean isSlot8Click(InventoryClickEvent event, org.bukkit.entity.Player player) {
        if (event.getClickedInventory() != player.getInventory()) {
            return false;
        }
        int rawSlot = event.getRawSlot();
        if (event.getView().getTopInventory().getSize() > 0) {
            return rawSlot == 44;
        }
        return rawSlot == 8;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!hotbarMenuService.isToken(event.getOldCursor())) {
            return;
        }
        int topSize = event.getView().getTopInventory().getSize();
        for (int slot : event.getRawSlots()) {
            if (slot == 8 || (topSize > 0 && slot == 44)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSwap(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            if (event.getHotbarButton() == 8 || hotbarMenuService.isToken(event.getCurrentItem())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        if (hotbarMenuService.isToken(event.getMainHandItem()) || hotbarMenuService.isToken(event.getOffHandItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        ItemStack slot8 = player.getInventory().getItem(8);
        if (hotbarMenuService.isToken(slot8)) {
            return;
        }
        if (player.getInventory().getHeldItemSlot() == 8 && hotbarMenuService.isToken(event.getItem().getItemStack())) {
            event.setCancelled(true);
            hotbarMenuService.install(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemHeld(PlayerItemHeldEvent event) {
        if (event.getNewSlot() == 8) {
            hotbarMenuService.installIfMissing(event.getPlayer());
        }
    }
}
