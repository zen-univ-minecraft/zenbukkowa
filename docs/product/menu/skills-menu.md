# Skills Menu

## Goal

Purchase and upgrade skills using category points inside a scrollable tree-shaped inventory.

## Layout

- 54 slots (6 rows).
- Rows 0–4: viewport into the virtual skill tree grid.
- Row 5 (bottom row of inventory):
  - Slot 45: `Scroll Up` (arrow) — moves viewport toward higher rows.
  - Slot 46: `Page Indicator` (paper) — shows current page.
  - Slot 49: `Back` (arrow) — Root menu.
  - Slot 53: `Scroll Down` (arrow) — moves viewport toward lower rows.

## Skill Node Item

- Name: `{CategoryColor}{SkillName} (Tier {N})`
- Lore lines:
  - Description from locale.
  - Current effect.
  - Next tier effect (if applicable).
  - Cost: `{Cost} {Category} Points`.
  - Player balance.
  - Prerequisite status.
- Green glass pane: purchasable.
- Red glass pane: locked (prerequisite or insufficient points).
- Lime glass pane: maxed.

## Connection Items

- `STICK` items fill grid cells between parent and child nodes.
- Sticks have blank names and no lore; they are not interactive.

## Navigation

- Slot 49: `Back` (arrow) → Root menu.
- Slot 45: `Scroll Up` → show higher rows (toward tree top).
- Slot 53: `Scroll Down` → show lower rows (toward tree roots).

## Interaction

1. Left-click a skill node to purchase the next tier.
2. Confirmation is immediate; no secondary confirm screen.
3. Sound and localized message on success or failure.
4. Menu refreshes after purchase at the same scroll offset.
5. Scroll arrows re-render the viewport.
6. Clicks in the bottom inventory (player inventory) are ignored.
7. Held items on the cursor do not block slot-based click detection.
