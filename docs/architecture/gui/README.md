# GUI Architecture

## Goal

Describe the inventory menu system decomposition, state ownership, and cross-module boundaries.

## Modules

### `gui` Package

| Class | Responsibility |
|---|---|
| `MenuService` | Tracks which menu (if any) each player has open. Owns 2D scroll state separately from open-state lifecycle. |
| `HotbarMenuService` | Creates, identifies, and enforces the slot-8 token. Uses `PersistentDataContainer` for unforgeable identification. |
| `MenuListener` | Cancels all clicks/drags in plugin menus and routes slot clicks to handlers. |
| `HotbarMenuListener` | Intercepts all player interactions that could move or use the token. |
| `MenuItems` | Factory for inventory items (named items, fillers). |
| `RootMenu` | Static builder for the 54-slot root navigation inventory. |
| `SkillsMenu` | Static builder that opens the skill tree at the player's current 2D scroll offset. |
| `SkillTreeViewport` | Renders nodes, connections, and scroll controls into a 54-slot inventory. |
| `SkillTreeLayout` | Immutable coordinates of all skill nodes and connection paths on a 15x12 virtual grid. |
| `StatsMenu` | Static builders for personal stats and leaderboard pages. |
| `SettingsMenu` | Static builder for preferences and operator controls. |
| `HelpMenu` | Static builder for the top-level help topic selector. |
| `HelpTopicMenu` | Static builder and click handler for help topic child pages. |

## State Ownership

- `MenuService.openMenus`: which menu ID is currently open per player.
- `MenuService.skillScrollV`: skill-tree vertical scroll offset per player.
- `MenuService.skillScrollH`: skill-tree horizontal scroll offset per player.
- `MenuService` does NOT clear scroll state on inventory close; it persists for UX.
- `HotbarMenuService` owns token creation and validation only.

## Lifecycle

1. Player joins → `HotbarMenuService.install()`.
2. Player right-clicks token → `HotbarMenuListener.onInteract()` → `RootMenu.open()`.
3. Player clicks menu slot → `MenuListener.onClick()` → handler → may open new menu.
4. Inventory closes → `MenuListener.onClose()` → `MenuService.clearOpen()`.
5. Deferred 1-tick resync clears ghost cursor and reinstalls token if needed.

## Dependency Rules

- `gui` depends on `domain` services (`SkillService`, `PointService`, etc.).
- `gui` does NOT depend on `persistence` directly.
- Menu classes are static builders; state lives in `MenuService` and `HotbarMenuService`.
