# Help Topic Menu

## Goal

Display child items of a help topic and send detailed explanations to chat on click.

## Layout

- 54 slots (6 rows).
- Title: `{TopicName}` localized.
- Row 1–5: child items as icon buttons with short labels.
  - Each child occupies one slot.
  - Icons are thematic (e.g., book for rules, pickaxe for breaking).
- Row 6:
  - Slot 49: `Back` (arrow) → Help menu

## Child Item Format

- Display name: short label (e.g., `Scoring`, `Area Break`).
- No lore; the full text is sent to chat on click.

## Interaction

1. Clicking a child item sends its full text to the player's chat as a formatted message.
2. The menu stays open so the player can read the chat and click other children.
3. Clicking empty slots or fillers does nothing.
4. Back button returns to HelpMenu.

## Data Source

- Child labels and texts are loaded from locale files under `help.{topic}.children.*`.
- Each child has a `label` and `text` key.
