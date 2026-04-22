# Area Destruction

## Goal

Break surrounding blocks automatically when a player breaks one target block.

## Trigger

- `BlockBreakEvent` on a block broken by a player.
- If the event is cancelled by another plugin, area break is skipped.

## Area Shape

- Horizontal: square centered on target block, radius from `AREA_RADIUS` skill.
- Vertical: column centered on target block Y, depth from `AREA_DEPTH` skill.
- Max: 9x9x9 when both skills are at tier 5.

## Block Filtering

- Only blocks mappable to a `PointCategory` are broken.
- Bedrock, barrier, command blocks, and other admin blocks are excluded.
- If `LEAF_CONSUME` is off, leaves are excluded from area breaks.
- Chests, shulker boxes, and other container blocks are excluded.

## Tool Durability

- Primary tool takes durability for every block broken in the area.
- `SALVAGE` chance applies per block.
- If tool would break, remaining blocks in the area are not broken.

## Drops

- Drops spawn at the original target block location to reduce entity spam.
- Experience orbs also consolidate.
- Point calculation uses `BlockCategoryMapper` values.

## Safety

- Fire `BlockBreakEvent` for each additional block so protection plugins can cancel.
- If any individual block break is cancelled, skip that block only.
- Player gamemode creative bypasses durability and drop logic.
