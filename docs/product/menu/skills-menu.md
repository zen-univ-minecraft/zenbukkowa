# Skills Menu

## Goal

Purchase and upgrade skills using category points.

## Layout

- 54 slots (6 rows).
- Each skill occupies one slot in index order (`SkillType.values()`).
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
- Lime glass pane: maxed.

## Navigation

- Slot 49: `Back` (arrow) -> Root menu.

## Interaction

1. Left-click a skill item in the top inventory to purchase next tier.
2. Confirmation is immediate; no secondary confirm screen.
3. Sound and message on success or failure.
4. Menu refreshes after purchase.
5. Clicks in the bottom inventory are ignored.
