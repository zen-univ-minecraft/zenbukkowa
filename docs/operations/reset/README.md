# Reset Runbook

## Goal

Describe how to restart zenbukkowa from scratch.

## Single Player Reset

### Command
```
/zenbukkowa reset <player_name>
```

### Effect
- Clears points, skills, and break log for that player.
- Does not clear structure claims.
- Does not affect other players.

## Full Server Reset

### Command
```
/zenbukkowa resetall
```

### Effect
- Clears all player progress, skills, and break logs.
- Resets event timer.
- Does not clear structure claims.

## Settings Menu Reset

- Operators can also trigger `Reset All Player Data` from the Settings menu.
- This is equivalent to `/zenbukkowa resetall`.

## Verification

1. Run `/zenbukkowa status` to confirm event is waiting.
2. Ask a player to check their Stats menu; all values should be zero.
3. Check the skill tree; no tiers should be purchased.
