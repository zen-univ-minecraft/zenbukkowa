# Menu Click Detection

## Goal

Define unambiguous slot-to-action mapping for every menu.

## Rule

Use `event.getSlot()` (slot within the clicked top inventory) rather than `ItemStack.equals()` to determine which item was clicked.

## Held-Item Guard

- A player may hold an item on their cursor and click a menu slot.
- `event.getCurrentItem()` can return `null` in this case on some server versions.
- Do **not** return early when `getCurrentItem()` is `null`.
- Only return early when the current item is confirmed to be a filler glass pane.
- Slot-based handling must execute regardless of cursor state.

## Root Menu

| Slot | Action |
|---|---|
| 11 | Open Skills menu |
| 12 | Open Stats menu |
| 13 | Open Leaderboard menu |
| 14 | Open Settings menu |
| 15 | Open Help menu |
| 49 | Close inventory |
| Any other slot | No action |

## Skills Menu (Scrollable Tree)

| Slot Range | Action |
|---|---|
| 0–44 | Purchase skill at the grid position mapped by current scroll offset |
| 45 | Scroll up |
| 49 | Back to Root menu |
| 53 | Scroll down |
| Any other slot | No action |

## Stats Menu

| Slot | Action |
|---|---|
| 49 | Back to Root menu |
| Any other slot | No action |

## Leaderboard Menu

| Slot | Action |
|---|---|
| 49 | Back to Stats menu |
| Any other slot | No action |

## Settings Menu

| Slot | Action |
|---|---|
| 10 | Toggle language |
| 12 | Toggle scoreboard visibility |
| 14 | Start event (op) |
| 16 | End event (op) |
| 25 | Event controls label (no action) |
| 31 | Reset all player data (op) |
| 49 | Back to Root menu |
| Any other slot | No action |

## Help Menu

| Slot | Action |
|---|---|
| 11 | Open Rules topic |
| 12 | Open Points topic |
| 13 | Open Skills topic |
| 14 | Open Breaking topic |
| 15 | Open Structures topic |
| 16 | Open Commands topic |
| 49 | Back to Root menu |
| Any other slot | No action |

## Guard

- Ignore clicks in the bottom inventory (player inventory).
- Cancel all clicks in the top inventory unconditionally.
