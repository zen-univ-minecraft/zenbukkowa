# Area Destruction

## Goal

Break surrounding blocks automatically when a player breaks one target block.

## Trigger

- `BlockBreakEvent` on a block broken by a player.
- If the event is cancelled by another plugin, area break is skipped.
- Held item slot 8 (menu token) bypasses area break to prevent accidental mass destruction while navigating menus.

## Area Shape

- Horizontal: square centered on target block, radius from `AREA_RADIUS` skill.
- Vertical: column centered on target block Y, depth from `AREA_DEPTH` skill.
- Radius formula: `(tierValue - 1) / 2`.
- `AREA_RADIUS` tier values: `[1, 3, 5, 7, 9]` (tiers 1-5).
- `AREA_DEPTH` tier values: `[1, 3, 5, 7, 9]` (tiers 1-5).
- Tier 1 with both skills produces a 1x1x1 area (center block only).
- Tier 5 with both skills produces a 9x9x9 area.
- Absolute caps are read from `config.yml` (`area.max-radius`, `area.max-depth`).

## Block Filtering

- Only blocks mappable to a `PointCategory` are broken.
- Bedrock, barrier, command blocks, jigsaw, structure blocks, spawners, trial spawners, vaults, and other admin blocks are excluded.
- If `LEAF_CONSUME` is off, leaves are excluded from area breaks.
- Chests, ender chests, shulker boxes, barrels, hoppers, dispensers, and droppers are excluded.
- Player-placed blocks are excluded from point awards but are still broken.
- Immature crops are excluded from point awards but are still broken.
- When a block is broken, its coordinate is removed from the player-placed tracking set.

## Tool Durability

- Primary tool takes durability for every block broken in the area.
- `SALVAGE` chance applies per block.
- If tool would break, remaining blocks in the area are not broken.

## Drops

- Drops spawn at the original target block location to reduce entity spam.
- Experience orbs also consolidate.
- `MAGNET` teleports nearby item drops to the player after each break.
- Point calculation uses `BlockCategoryMapper` values.

## Crop Skills

- `SEED_SATCHEL` automatically replants wheat, carrots, potatoes, beetroot, and nether wart on mature crop break.
- Replanting schedules on the next server tick so the break is fully committed first.
- Points are only awarded for blocks that are actually broken (respects protection cancellations).

## Pillar Break

- `PILLAR_BREAK` adds blocks directly above the center block.
- These blocks are deduplicated against the main area list so they are never processed twice.

## Safety

- Fire `BlockBreakEvent` for each additional block so protection plugins can cancel.
- If any individual block break is cancelled, skip that block and its points.
- Player gamemode creative bypasses durability and drop logic.
- The center block is always processed first to guarantee it receives points even if the tool breaks during area destruction.
