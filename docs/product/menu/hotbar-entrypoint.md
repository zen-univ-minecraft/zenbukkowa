# Hotbar Menu Entrypoint

## Goal

Expose all point features through a deterministic right-end hotbar control that opens reliably on every valid interaction.

## Slot Contract

1. Slot `8` (rightmost hotbar slot) holds a Nether Star named `Menu`.
2. Slot `8` lock is hard-enforced: item replacement/removal attempts are cancelled.
3. Lock applies to click, drag, number-key swap, offhand swap, inventory transfer, and item pickup.
4. Player join/respawn restores the menu item in slot `8`.
5. If slot `8` is missing or tampered, it is reinstalled immediately via periodic scan.
6. On player death, the token is removed from drops and restored on respawn.

## Opening Contract

1. Right-clicking air or a block while holding the menu item opens the root menu.
2. Left-click is ignored for opening; it is reserved for breaking blocks.
3. Drop intent (`Q`) while targeting the menu item is cancelled and opens root menu.
4. Clicking slot `8` while any inventory is open also opens root menu.
5. Menu item interaction opens menu even when the underlying interaction is cancelled by protection.
6. Menu item interactions are silent on success.
7. Off-hand events are ignored if the main hand holds the token (prevents double-open).
8. Entity interaction (right-click mob, armor stand, etc.) while holding the token cancels the interaction and opens the menu.

## Raw Slot Mapping

- In a player inventory view: raw slot `44` is hotbar slot `8`.
- In a custom inventory view: raw slot `topSize + 35` is hotbar slot `8` (bottom inventory has 36 slots; hotbar is the last 9).
- Both raw slots must be treated as the menu trigger.

## Item Identification

- The token carries a `PersistentDataContainer` marker (`zenbukkowa:menu-token`).
- `HotbarMenuService.isToken()` validates the marker, not the display name.
- This prevents forgery via anvil renaming or command-given items.

## Failure Contract

1. If menu cannot open, player receives explicit failure reason.
2. Failure to open does not unlock or remove slot `8` reservation.

## Cursor Resync

- After any inventory interaction that involves the token, schedule a 1-tick deferred task to:
  1. Reinstall the token if missing.
  2. Clear the token from the cursor if present.
  3. Call `player.updateInventory()` to force client sync.
