# Hotbar Menu Entrypoint

## Goal

Expose all point features through a deterministic right-end hotbar control that opens reliably on every valid interaction.

## Slot Contract

1. Slot `8` (rightmost hotbar slot) holds a Nether Star named `Menu`.
2. Slot `8` lock is hard-enforced: item replacement/removal attempts are cancelled.
3. Lock applies to click, drag, number-key swap, offhand swap, inventory transfer, and item pickup.
4. Player join/respawn restores the menu item in slot `8`.
5. If slot `8` is missing or tampered, it is reinstalled immediately via periodic scan.

## Opening Contract

1. Right-clicking air or a block while holding the menu item opens the root menu.
2. Left-click is ignored for opening; it is reserved for breaking blocks.
3. Drop intent (`Q`) while targeting the menu item is cancelled and opens root menu.
4. Clicking slot `8` while any inventory is open also opens root menu.
5. Menu item interaction opens menu even when the underlying interaction is cancelled by protection.
6. Menu item interactions are silent on success.
7. Off-hand events are ignored if the main hand holds the token (prevents double-open).

## Raw Slot Mapping

- In a player inventory view: slot `8` is raw slot `8`.
- In a custom inventory view: player hotbar slot `8` is raw slot `44`.
- Both raw slots must be treated as the menu trigger.

## Failure Contract

1. If menu cannot open, player receives explicit failure reason.
2. Failure to open does not unlock or remove slot `8` reservation.
