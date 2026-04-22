# Module Map

## Top-Level Packages

- `com.zenbukkowa.plugin`: Bukkit entrypoint and wiring
- `com.zenbukkowa.domain`: pure gameplay services, models, and policies
- `com.zenbukkowa.persistence`: SQLite repositories
- `com.zenbukkowa.gui`: inventory menu orchestration
- `com.zenbukkowa.breaker`: block break handling and area destruction
- `com.zenbukkowa.scoreboard`: sidebar scoreboard management
- `com.zenbukkowa.structure`: structure detection and bonuses
- `com.zenbukkowa.command`: command handlers

## Dependency Rules

1. `domain` does not depend on Bukkit APIs.
2. `command`, `gui`, `break`, `scoreboard`, `structure` depend on `domain` interfaces.
3. `persistence` implements repository interfaces owned by `domain`.
4. `plugin` performs composition root responsibilities only.

## Cross-Cutting Services

- `PointService`: category point balances and mutations
- `SkillService`: skill ownership, tiers, and prerequisites
- `BreakService`: area break execution, drop consolidation, durability
- `EventService`: timer, start, end, winner calculation
- `ScoreboardService`: sidebar update and player sync
- `MenuService`: inventory GUI lifecycle
- `HotbarMenuService`: slot-8 token lifecycle and lock enforcement
