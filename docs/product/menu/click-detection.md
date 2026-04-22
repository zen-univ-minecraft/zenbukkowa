# Menu Click Detection

## Goal

Define unambiguous slot-to-action mapping for every menu.

## Rule

Use `event.getSlot()` (slot within the clicked top inventory) rather than `ItemStack.equals()` to determine which item was clicked.

## Root Menu

| Slot | Action |
|---|---|
| 10 | Open Skills menu |
| 12 | Open Stats menu |
| 14 | Open Leaderboard menu |
| 16 | Toggle scoreboard visibility |
| Any other slot | No action |

## Skills Menu

| Slot Range | Action |
|---|---|
| 0–9 | Purchase skill at index `slot` in `SkillType.values()` order |
| 49 | Back to Root menu |
| Any other slot | No action |

## Stats Menu

| Slot | Action |
|---|---|
| 18 | Back to Root menu |
| Any other slot | No action |

## Leaderboard Menu

| Slot | Action |
|---|---|
| 18 | Back to Stats menu |
| Any other slot | No action |

## Guard

- Ignore clicks in the bottom inventory (player inventory).
- Cancel all clicks in the top inventory unconditionally.
