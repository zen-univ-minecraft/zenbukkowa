# Hotbar Menu Entrypoint

## Goal

Expose all point features through a deterministic right-end hotbar control.

## Slot Contract

1. Slot `8` (rightmost hotbar slot) holds a Nether Star named `Menu`.
2. Slot `8` lock is hard-enforced: item replacement/removal attempts are cancelled.
3. Lock applies to click, drag, number-key swap, offhand swap, and inventory transfer.
4. Player join/respawn restores the menu item in slot `8`.
5. If slot `8` is missing or tampered, it is reinstalled immediately.

## Interaction Contract

1. Clicking/using the slot `8` item opens the root menu.
2. Drop intent (`Q`) while targeting the menu item is cancelled and opens root menu.
3. Clicking slot `8` while any inventory is open also opens root menu.
4. Menu item interaction opens menu even when the underlying interaction is cancelled.
5. Menu item interactions are silent on success.

## Failure Contract

1. If menu cannot open, player receives explicit failure reason.
2. Failure to open does not unlock or remove slot `8` reservation.
