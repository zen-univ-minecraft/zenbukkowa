# Skill Tree

## Goal

Define the visual tree layout, 2D scroll behavior, and node coordinates for the skills menu.

## Rules

1. The tree grows bottom-to-top: roots at the bottom, advanced skills higher.
2. TERRA is the central trunk; all other categories branch from it.
3. Parent-child skills are connected by `GREEN_STAINED_GLASS_PANE` items.
4. The viewport is a 54-slot inventory showing a 9x5 slice of a larger virtual grid.
5. Scroll arrows move the viewport one row or one column at a time.

---

## Virtual Grid

- **Width:** 15 columns (0–14).
- **Height:** 12 rows (0–11).
- **Viewport:** 9 columns wide x 5 rows tall.
- **Max vertical scroll:** 7 (`GRID_ROWS - VIEWPORT_ROWS`).
- **Max horizontal scroll:** 6 (`GRID_COLS - VIEWPORT_COLS`).

---

## Node Coordinates (bottom-to-top)

### TERRA Trunk (column 7)

| Skill | Row | Col | Parent |
|---|---|---|---|
| AREA_RADIUS | 11 | 7 | — |
| AREA_DEPTH | 9 | 7 | AREA_RADIUS |
| PILLAR_BREAK | 7 | 7 | AREA_DEPTH |
| GRAVITY_WELL | 5 | 7 | AREA_DEPTH |
| TERRA_BLESSING | 3 | 7 | PILLAR_BREAK |

### MINERAL Branch (column 4)

| Skill | Row | Col | Parent |
|---|---|---|---|
| HASTE_AURA | 10 | 4 | — (requires AREA_RADIUS) |
| FORTUNE_TOUCH | 8 | 4 | HASTE_AURA |
| VEIN_MINER | 6 | 4 | FORTUNE_TOUCH |
| MAGNET | 4 | 4 | VEIN_MINER |
| CRYSTAL_VISION | 2 | 4 | MAGNET |

### ORGANIC Branch (column 2)

| Skill | Row | Col | Parent |
|---|---|---|---|
| LEAF_CONSUME | 8 | 2 | — (requires AREA_RADIUS) |
| ROOT_RAZE | 6 | 2 | LEAF_CONSUME |
| SAPLING_REPLANT | 4 | 2 | ROOT_RAZE |
| BONEMEAL_AURA | 2 | 2 | SAPLING_REPLANT |
| NATURE_TOUCH | 0 | 2 | BONEMEAL_AURA |

### AQUATIC Branch (columns 10–11)

| Skill | Row | Col | Parent |
|---|---|---|---|
| TIDE_BREAKER | 10 | 10 | — (requires AREA_RADIUS) |
| SALVAGE | 10 | 12 | — (requires AREA_RADIUS) |
| CONDUIT_AURA | 6 | 10 | TIDE_BREAKER |
| FROST_WALKER | 8 | 11 | TIDE_BREAKER |
| DEEP_DIVE | 2 | 10 | CONDUIT_AURA |

### VOID Branch (columns 12–14)

| Skill | Row | Col | Parent |
|---|---|---|---|
| VOID_SIPHON | 8 | 12 | — (requires AREA_RADIUS) |
| STRUCTURE_SENSE | 8 | 14 | — (requires AREA_RADIUS) |
| NIGHT_VISION | 4 | 14 | STRUCTURE_SENSE |
| FIRE_RESISTANCE | 2 | 14 | NIGHT_VISION |
| VOID_WALK | 4 | 12 | VOID_SIPHON |

### CROP Branch (columns 0–1)

| Skill | Row | Col | Parent |
|---|---|---|---|
| GREEN_THUMB | 10 | 0 | — (requires AREA_RADIUS) |
| HARVEST_AURA | 8 | 0 | GREEN_THUMB |
| COMPOST_MASTER | 6 | 0 | HARVEST_AURA |
| SEED_SATCHEL | 4 | 1 | COMPOST_MASTER |
| FARMERS_FORTUNE | 2 | 0 | SEED_SATCHEL |

### Cross-Branch

| Skill | Row | Col | Parent |
|---|---|---|---|
| EFFICIENCY | 9 | 5 | HASTE_AURA |

---

## Connection Rendering

- A `GREEN_STAINED_GLASS_PANE` is placed in every grid cell between a parent and child node.
- Panes have no lore and a blank name (` `) so they are not interactive.
- Horizontal connections (cross-branch) use `GREEN_STAINED_GLASS_PANE` as well.
- When a connection cell overlaps with a node cell, the node takes precedence.
- **No connection path may pass through an unrelated node.** This is a hard layout invariant.

---

## 2D Scroll Contract

### Controls
- **Slot 45:** Scroll up (show higher rows). Arrow item.
- **Slot 53:** Scroll down (show lower rows). Arrow item.
- **Slot 47:** Scroll left (show lower columns). Arrow item.
- **Slot 51:** Scroll right (show higher columns). Arrow item.
- **Slot 49:** Back to Root menu.
- **Slot 46:** Page indicator (`V X / Y · H A / B`).

### State
- Scroll offsets are stored per-player in `MenuService`.
- Default vertical offset is `7` (bottom of tree visible; rows 7–11).
- Default horizontal offset is `0` (leftmost columns visible; cols 0–8).
- Up-arrow is disabled when vertical offset == 0.
- Down-arrow is disabled when vertical offset == max.
- Left-arrow is disabled when horizontal offset == 0.
- Right-arrow is disabled when horizontal offset == max.
- **Scroll state persists across inventory transitions and after close.**
- It is reset to default only when the Skills menu is opened from the Root menu.

### Interaction
1. Clicking a skill node attempts purchase of the next tier.
2. Clicking a connection pane does nothing.
3. Clicking scroll arrows updates the offset and re-renders.
