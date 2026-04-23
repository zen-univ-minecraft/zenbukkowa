# Help Menu

## Goal

Provide hierarchical in-game documentation accessible from the root menu.

## Layout

- 54 slots (6 rows).
- Row 2 (centered):
  - Slot 11: `Rules` (book)
  - Slot 12: `Points` (stone)
  - Slot 13: `Skills` (diamond pickaxe)
  - Slot 14: `Breaking` (TNT)
  - Slot 15: `Structures` (prismarine)
  - Slot 16: `Commands` (command block)
- Row 6:
  - Slot 49: `Back` (arrow) → Root menu

## Interaction

1. Clicking a topic opens a `HelpTopicMenu` inventory showing child items for that topic.
2. Each child item is a clean icon button with a short label.
3. Clicking a child item sends its full explanation text to the player's chat.
4. The topic menu has a Back button returning to HelpMenu.
5. Empty slots are filled with dark panes.

## Rules

- Rules are no longer a separate root-menu button; they live inside Help.
- Help topics must be readable without leaving the GUI (chat messages).
- All color codes are translated from locale strings.
- Help is two-level: parent topic → child items → chat explanation.
