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
- Tier values are converted to radius/depth via `(tierValue - 1) / 2`.
- `AREA_RADIUS` tier values: `[3, 5, 7, 9, 11]` (tiers 1-5).
- `AREA_DEPTH` tier values: `[3, 5, 7, 9, 11]` (tiers 1-5).
- Max area: 11x11x11 when both skills are at tier 5.

## Block Filtering

- Only blocks mappable to a `PointCategory` are broken.
- Bedrock, barrier, command blocks, jigsaw, structure blocks, spawners, trial spawners, vaults, and other admin blocks are excluded.
- If `LEAF_CONSUME` is off, leaves are excluded from area breaks.
- Chests, ender chests, shulker boxes, barrels, hoppers, dispensers, and droppers are excluded.
- Player-placed blocks are excluded from point awards but are still broken.
- Immature crops are excluded from point awards but are still broken.

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
- Points are only awarded for blocks that are actually broken (respects protection cancellations).

## Safety

- Fire `BlockBreakEvent` for each additional block so protection plugins can cancel.
- If any individual block break is cancelled, skip that block and its points.
- Player gamemode creative bypasses durability and drop logic.
- The center block is always processed first to guarantee it receives points even if the tool breaks during area destruction.
