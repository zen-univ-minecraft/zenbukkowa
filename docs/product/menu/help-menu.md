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

1. Clicking a topic opens a `HelpTopicMenu` inventory showing the topic text.
2. Text is localized to the player's selected language.
3. Topic text is split across multiple book items with colored lore.
4. The topic menu has a Back button returning to HelpMenu.
5. Empty slots are filled with dark panes.

## Rules

- Rules are no longer a separate root-menu button; they live inside Help.
- Help topics must be readable without leaving the GUI.
- All color codes are translated from locale strings.
