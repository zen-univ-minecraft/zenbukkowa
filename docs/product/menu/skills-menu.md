# Skills Menu

## Goal

Purchase and upgrade skills using category points.

## Layout

- 54 slots (6 rows).
- Each skill occupies one slot.
- Skill item shows:
  - Name: `{SkillName} (Tier {N})`
  - Lore lines:
    - Category: `{Category}`
    - Current effect
    - Next tier effect (if applicable)
    - Cost: `{Cost} {Category} Points`
    - Prerequisites status
- Green glass pane: purchasable.
- Red glass pane: locked (insufficient points or prerequisites).
- Gray glass pane: maxed.

## Navigation

- Slot 49: `Back` (arrow) -> Root menu.
- Slot 52: `Next Page` if more skills than 45 slots.
- Slot 46: `Previous Page` if not on first page.

## Interaction

1. Left-click a skill item to purchase next tier.
2. Confirmation is immediate; no secondary confirm screen.
3. Sound and message on success or failure.
4. Menu refreshes after purchase.
