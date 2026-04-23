# Menu Item Protection

## Goal

Prevent the menu token from leaving slot `8` and prevent all menu inventory items from being moved or dropped.

## Enforcement Vectors — Hotbar Token

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

## Enforcement Vectors — Menu Inventories

### InventoryClickEvent
- Any click in a custom menu top inventory: cancel unconditionally.
- Shift-clicks that would move items into or out of the menu: cancel.
- Number-key presses while a menu is open: cancel.

### InventoryDragEvent
- Any drag operation where at least one raw slot is inside the top inventory: cancel.
- This prevents dragging items out of or into the menu.

### InventoryCloseEvent
- On close, clear the player's open-menu state so periodic scans resume normally.
