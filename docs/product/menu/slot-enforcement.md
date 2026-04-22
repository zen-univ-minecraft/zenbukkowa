# Slot Enforcement

## Goal

Prevent the menu token from leaving slot `8` through any vector.

## Enforcement Vectors

### PlayerInteractEvent
- Right-click air/block with token: open menu, cancel event.
- Left-click: ignored (vanilla block break).

### PlayerDropItemEvent
- Drop token: cancel drop, open menu.

### InventoryClickEvent
- Click raw slot `8` or `44` in player inventory: cancel, open menu.
- Number-key swap to slot `8`: cancel.
- Hotbar swap involving token: cancel.
- Any click where current item or cursor is the token: cancel.

### InventoryDragEvent
- Drag token to raw slot `8` or `44`: cancel.

### PlayerSwapHandItemsEvent
- Either hand holds the token: cancel swap.

### PlayerPickupItemEvent
- Pickup would place item in slot `8`: cancel.

### PlayerItemHeldEvent
- If switching to slot `8` and token is missing: reinstall immediately.

### Periodic Scan
- Every 5 seconds, scan all online players.
- If slot `8` does not contain the token: reinstall.

### Respawn
- On respawn: install token immediately.
- Also schedule reinstall 1 tick later (inventory may not be ready).
