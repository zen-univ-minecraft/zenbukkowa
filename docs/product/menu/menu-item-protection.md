# Menu Item Protection

## Goal

Prevent the menu token from leaving slot `8` and prevent all menu inventory items from being moved, dropped, or desynced.

---

## Token Identification

- The token is identified by a `PersistentDataContainer` byte marker (`zenbukkowa:menu-token`), not by display name.
- Display-name matching is fragile (renaming anvils, locale changes, color code drift).
- `HotbarMenuService.isToken()` checks the PDC marker via `NamespacedKey`.

---

## Enforcement Vectors — Hotbar Token

### PlayerInteractEvent
- Right-click air/block with token: cancel event, open menu.
- Left-click air/block with token: cancel event (do not open menu; reserved for block break).
- Use `event.getItem()` with fallback to `getPlayer().getInventory().getItemInMainHand()`.

### PlayerInteractEntityEvent / PlayerInteractAtEntityEvent
- Main hand holds token: cancel event, open menu.
- Prevents using the token on entities (e.g., naming, mounting, trading).

### PlayerDropItemEvent
- Drop token: cancel drop, remove the dropped item entity (`event.getItemDrop().remove()`), open menu.
- Removing the entity prevents client-side ghost drops.

### InventoryClickEvent
- Click raw slot `8` or `44` in player inventory: cancel, open menu.
- Number-key swap to slot `8`: cancel.
- Hotbar swap involving token: cancel.
- Any click where current item or cursor is the token: cancel.
- After cancelling an inventory interaction, clear ghost cursor and resync inventory.

### InventoryDragEvent
- Drag token to raw slot `8` or `44`: cancel.

### PlayerSwapHandItemsEvent
- Either hand holds the token: cancel swap.

### PlayerAttemptPickupItemEvent
- Pickup would place item in slot `8`: cancel, reinstall token.

### PlayerItemHeldEvent
- If switching to slot `8` and token is missing: reinstall immediately.

### Periodic Scan
- Every 5 seconds, scan all online players.
- If slot `8` does not contain the token: reinstall.

### Respawn
- On respawn: install token immediately.
- Also schedule reinstall 1 tick later (inventory may not be ready).

---

## Enforcement Vectors — Menu Inventories

### InventoryClickEvent
- Any click in a custom menu top inventory: cancel unconditionally.
- Cancel BEFORE any early return so cursor-held items cannot be placed.
- Number-key presses while a menu is open: cancel.
- Drop actions (`DROP_ONE_SLOT`, `DROP_ALL_SLOT`) while hovering top inventory: cancel.

### InventoryDragEvent
- Any drag operation where at least one raw slot is inside the top inventory: cancel.
- This prevents dragging items out of or into the menu.

### InventoryCloseEvent
- On close, clear the player's open-menu state so periodic scans resume normally.
- Do NOT clear scroll state; it persists for UX.
- Schedule a 1-tick deferred resync: clear ghost cursor + `player.updateInventory()`.

---

## Cursor Desync Prevention

### Problem
- After a cancelled inventory click, the client may show a ghost item on the cursor.
- This is especially visible when clicking menu items while holding an item.

### Solution
- After any inventory interaction that opens a menu, call `player.setItemOnCursor(null)` if the cursor is the token.
- Schedule `player.updateInventory()` 1 tick later to force a full client sync.
- `HotbarMenuService.resyncAfterMenuClose(Player)` performs this deferred sync.
