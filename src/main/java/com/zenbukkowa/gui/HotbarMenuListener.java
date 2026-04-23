package com.zenbukkowa.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent event) {
        hotbarMenuService.ensureInstalled(event.getPlayer());
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK
                && action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        ItemStack held = event.getItem() != null ? event.getItem()
                : event.getPlayer().getInventory().getItemInMainHand();
        if (!hotbarMenuService.isToken(held)) return;
        event.setCancelled(true);
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            hotbarMenuService.openMenu(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        hotbarMenuService.ensureInstalled(event.getPlayer());
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
        if (!hotbarMenuService.isToken(event.getPlayer().getInventory().getItemInMainHand())) return;
        event.setCancelled(true);
        hotbarMenuService.openMenu(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        hotbarMenuService.ensureInstalled(event.getPlayer());
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
        if (!hotbarMenuService.isToken(event.getPlayer().getInventory().getItemInMainHand())) return;
        event.setCancelled(true);
        hotbarMenuService.openMenu(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        hotbarMenuService.ensureInstalled(event.getPlayer());
        if (!hotbarMenuService.isToken(event.getItemDrop().getItemStack())) return;
        event.setCancelled(true);
        event.getItemDrop().remove();
        hotbarMenuService.openMenu(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        hotbarMenuService.ensureInstalled(player);
        int topSize = event.getView().getTopInventory().getSize();
        int playerHotbarRawSlot = topSize + HotbarMenuService.SLOT;
        boolean clickedHotbarSlot = event.getRawSlot() == playerHotbarRawSlot;
        boolean numberKeyOnHotbarSlot = event.getClick() == ClickType.NUMBER_KEY
                && event.getHotbarButton() == HotbarMenuService.SLOT;
        boolean clickedToken = hotbarMenuService.isToken(event.getCurrentItem());
        boolean cursorToken = hotbarMenuService.isToken(event.getCursor());
        boolean numberKeyUsesToken = event.getClick() == ClickType.NUMBER_KEY
                && event.getHotbarButton() >= 0
                && hotbarMenuService.isToken(player.getInventory().getItem(event.getHotbarButton()));

        // Number-key swap involving token: cancel only, do not open menu
        if (numberKeyOnHotbarSlot || numberKeyUsesToken) {
            event.setCancelled(true);
            return;
        }
        // Direct click on token slot or token item: cancel and open menu
        if (clickedHotbarSlot || clickedToken || cursorToken) {
            event.setCancelled(true);
            hotbarMenuService.openFromInventoryInteraction(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        int topSize = event.getView().getTopInventory().getSize();
        int playerHotbarRawSlot = topSize + HotbarMenuService.SLOT;
        if (!event.getRawSlots().contains(playerHotbarRawSlot)) return;
        event.setCancelled(true);
        hotbarMenuService.ensureInstalled(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        if (!hotbarMenuService.isToken(event.getMainHandItem()) && !hotbarMenuService.isToken(event.getOffHandItem())) {
            return;
        }
        event.setCancelled(true);
        hotbarMenuService.ensureInstalled(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        if (hotbarMenuService.isToken(player.getInventory().getItem(HotbarMenuService.SLOT))) return;
        if (player.getInventory().getHeldItemSlot() == HotbarMenuService.SLOT
                && hotbarMenuService.isToken(event.getItem().getItemStack())) {
            event.setCancelled(true);
            hotbarMenuService.install(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemHeld(PlayerItemHeldEvent event) {
        if (event.getNewSlot() == HotbarMenuService.SLOT) {
            hotbarMenuService.installIfMissing(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!menuTitleIsPluginMenu(event.getView().getTitle())) return;
        hotbarMenuService.resyncAfterMenuClose(player);
    }

    private boolean menuTitleIsPluginMenu(String title) {
        return title != null && title.startsWith(String.valueOf(org.bukkit.ChatColor.BLACK));
    }
}
