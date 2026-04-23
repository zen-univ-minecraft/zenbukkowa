# Skills Menu

## Goal

Purchase and upgrade skills using category points inside a 2D scrollable tree-shaped inventory.

## Implementation Status

| Feature | Status |
|---|---|
| 54-slot inventory viewport | ✅ Live |
| 7×5 virtual grid slice | ✅ Live |
| Horizontal arrow row (slots 47–50) | ✅ Live |
| Green/Gray/Emerald wool node items | ✅ Live |
| Green stained glass connections | ✅ Live |
| Light gray / gray bounding-box fillers | ✅ Live |
| Immediate purchase on click | ✅ Live |
| Sound + localized feedback | ✅ Live |
| Drag cancellation | ✅ Live |

## Layout

- 54 slots (6 rows).
- Rows 0–4: viewport into the virtual skill tree grid.
- Row 5 (bottom row of inventory):
  - Slot 45: `Back` (arrow) → Root menu.
  - Slot 46: `Page Indicator` (paper) — shows current vertical and horizontal page.
  - Slot 47: `Scroll Left` (arrow) — moves viewport toward lower columns.
  - Slot 48: `Scroll Up` (arrow) — moves viewport toward higher rows.
  - Slot 49: `Scroll Down` (arrow) — moves viewport toward lower rows.
  - Slot 50: `Scroll Right` (arrow) — moves viewport toward higher columns.

## Skill Node Item

- Name: `{CategoryColor}{SkillName} (Tier {N})`
- Lore lines:
  - Description from locale.
  - Current effect.
  - Next tier effect (if applicable).
  - Cost: category breakdown with player balance per category.
  - Immediate prerequisite skill names.
  - Prerequisite status.
- Green wool block: purchasable / unlocked.
- Gray wool block: locked (prerequisite or insufficient points).
- Emerald block: maxed.

## Connection Items

- `GREEN_STAINED_GLASS_PANE` fills grid cells between parent and child nodes.
- Connections have blank names and no lore; they are not interactive.

## Empty-Cell Differentiation

- `LIGHT_GRAY_STAINED_GLASS_PANE`: inside the tree bounding box but no skill or connection here.
- `GRAY_STAINED_GLASS_PANE`: outside the tree bounding box (scrolled past the tree).

## Navigation

- Slot 45: `Back` (arrow) → Root menu.
- Slot 47: `Scroll Left` → show lower columns (toward left branches).
- Slot 48: `Scroll Up` → show higher rows (toward tree top).
- Slot 49: `Scroll Down` → show lower rows (toward tree roots).
- Slot 50: `Scroll Right` → show higher columns (toward right branches).

## Interaction

1. Left-click a skill node to purchase the next tier.
2. Confirmation is immediate; no secondary confirm screen.
3. Sound and localized message on success or failure.
4. Menu refreshes after purchase at the same scroll offsets.
5. Scroll arrows re-render the viewport.
6. Clicks in the bottom inventory (player inventory) are ignored.
7. Held items on the cursor do not block slot-based click detection.
8. All drags inside the menu are cancelled.
