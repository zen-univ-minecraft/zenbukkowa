# Config Management

## config.yml

```yaml
database:
  file: plugins/zenbukkowa/zenbukkowa.db
event:
  duration-minutes: 120
  countdown-seconds: 10
points:
  base-per-block: 1
  ore-multiplier: 5
  ancient-debris-multiplier: 20
  structure-block-multiplier: 2
area:
  max-radius: 4
  max-depth: 4
```

## skills.yml

Defines skill metadata for the menu and validation.

```yaml
skills:
  AREA_RADIUS:
    category: TERRA
    max-tier: 5
    tiers: [1, 3, 5, 7, 9]
  AREA_DEPTH:
    category: TERRA
    max-tier: 5
    tiers: [1, 3, 5, 7, 9]
  HASTE_AURA:
    category: MINERAL
    max-tier: 5
    tiers: [1, 2, 3, 4, 5]
  FORTUNE_TOUCH:
    category: MINERAL
    max-tier: 3
    tiers: [1, 2, 3]
  LEAF_CONSUME:
    category: ORGANIC
    max-tier: 1
    tiers: [1]
  ROOT_RAZE:
    category: ORGANIC
    max-tier: 1
    tiers: [1]
  SALVAGE:
    category: AQUATIC
    max-tier: 3
    tiers: [1, 2, 3]
  TIDE_BREAKER:
    category: AQUATIC
    max-tier: 1
    tiers: [1]
  VOID_SIPHON:
    category: VOID
    max-tier: 3
    tiers: [1, 2, 3]
  STRUCTURE_SENSE:
    category: VOID
    max-tier: 1
    tiers: [1]
```
