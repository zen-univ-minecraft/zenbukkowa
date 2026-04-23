# Skill Tree

## Goal

Define the visual tree layout, 2D scroll behavior, and node coordinates for the skills menu.

## Rules

1. The tree grows bottom-to-top: roots at the bottom, advanced skills higher.
2. TERRA is the central trunk; all other categories branch from it.
3. Parent-child skills are connected by `GREEN_STAINED_GLASS_PANE` items.
4. The viewport is a 54-slot inventory showing a 9x5 slice of a larger virtual grid.
5. Scroll arrows move the viewport one row or one column at a time.
6. Empty separator columns exist between branches for visual clarity.

---

## Virtual Grid

- **Width:** 20 columns (0-19).
- **Height:** 13 rows (0-12).
- **Viewport:** 9 columns wide x 5 rows tall.
- **Max vertical scroll:** 8 (`GRID_ROWS - VIEWPORT_ROWS`).
- **Max horizontal scroll:** 11 (`GRID_COLS - VIEWPORT_COLS`).

---

## Node Coordinates (bottom-to-top)

### CROP Branch (columns 0-1)

| Skill | Row | Col | Parent |
|---|---|---|---|
| GREEN_THUMB | 11 | 0 | AREA_RADIUS |
| HARVEST_AURA | 9 | 0 | GREEN_THUMB |
| COMPOST_MASTER | 7 | 0 | HARVEST_AURA |
| SEED_SATCHEL | 5 | 1 | COMPOST_MASTER |
| FARMERS_FORTUNE | 3 | 0 | SEED_SATCHEL |
| HARVEST_WAVE | 1 | 0 | FARMERS_FORTUNE |

### ORGANIC Branch (columns 3-4)

| Skill | Row | Col | Parent |
|---|---|---|---|
| LEAF_CONSUME | 11 | 3 | AREA_RADIUS |
| ROOT_RAZE | 9 | 3 | LEAF_CONSUME |
| SAPLING_REPLANT | 7 | 3 | ROOT_RAZE |
| BONEMEAL_AURA | 5 | 3 | SAPLING_REPLANT |
| NATURE_TOUCH | 3 | 3 | BONEMEAL_AURA |
| WILD_GROWTH | 1 | 3 | NATURE_TOUCH |

### MINERAL Branch (columns 6-7)

| Skill | Row | Col | Parent |
|---|---|---|---|
| HASTE_AURA | 11 | 6 | AREA_RADIUS |
| FORTUNE_TOUCH | 9 | 6 | HASTE_AURA |
| VEIN_MINER | 7 | 6 | FORTUNE_TOUCH |
| MAGNET | 5 | 6 | VEIN_MINER |
| CRYSTAL_VISION | 3 | 6 | MAGNET |
| BLAST_MINING | 1 | 6 | CRYSTAL_VISION |

### TERRA Trunk (columns 9-10)

| Skill | Row | Col | Parent |
|---|---|---|---|
| AREA_RADIUS | 12 | 9 | |
| AREA_DEPTH | 10 | 9 | AREA_RADIUS |
| PILLAR_BREAK | 8 | 9 | AREA_DEPTH |
| GRAVITY_WELL | 6 | 9 | AREA_DEPTH |
| TERRA_BLESSING | 4 | 9 | PILLAR_BREAK |
| EFFICIENCY | 10 | 10 | HASTE_AURA |

### AQUATIC Branch (columns 12-13)

| Skill | Row | Col | Parent |
|---|---|---|---|
| TIDE_BREAKER | 11 | 12 | AREA_RADIUS |
| SALVAGE | 11 | 14 | AREA_RADIUS |
| CONDUIT_AURA | 7 | 12 | TIDE_BREAKER |
| FROST_WALKER | 9 | 13 | TIDE_BREAKER |
| DEEP_DIVE | 3 | 12 | CONDUIT_AURA |
| TSUNAMI | 1 | 12 | DEEP_DIVE |

### VOID Branch (columns 15-16)

| Skill | Row | Col | Parent |
|---|---|---|---|
| VOID_SIPHON | 9 | 15 | AREA_RADIUS |
| STRUCTURE_SENSE | 9 | 17 | AREA_RADIUS |
| NIGHT_VISION | 5 | 17 | STRUCTURE_SENSE |
| FIRE_RESISTANCE | 3 | 17 | NIGHT_VISION |
| VOID_WALK | 5 | 15 | VOID_SIPHON |
| VOID_RIFT | 1 | 15 | VOID_WALK |

### DISCOVERY Branch (columns 18-19)

| Skill | Row | Col | Parent |
|---|---|---|---|
| CURIOUS_MINER | 11 | 18 | AREA_RADIUS |
| GEOLOGIST | 9 | 18 | CURIOUS_MINER |
| SURVEYOR | 7 | 18 | GEOLOGIST |
| CARTOGRAPHER | 5 | 18 | SURVEYOR |
| PATHFINDER | 3 | 18 | CARTOGRAPHER |
| WORLD_WALKER | 1 | 18 | PATHFINDER |

### Mythic Apex (columns 8-10, row 0)

| Skill | Row | Col | Prerequisites |
|---|---|---|---|
| TITAN_STRIKE | 0 | 8 | AREA_RADIUS tier 3, NATURE_TOUCH tier 2, FARMERS_FORTUNE tier 2 |
| ANGEL_WINGS | 0 | 10 | AREA_RADIUS tier 3, HASTE_AURA tier 3, VOID_SIPHON tier 2 |

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
- **Slot 46:** Page indicator (`V X / Y . H A / B`).

### State
- Scroll offsets are stored per-player in `MenuService`.
- Default vertical offset is `8` (bottom of tree visible; rows 8-12).
- Default horizontal offset is `0` (leftmost columns visible; cols 0-8).
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
