# Event Timer

## Goal

Control the event duration.

## Default Duration

- `2 hours` (7200 seconds).
- Configurable in `config.yml` as `event.duration-minutes`.

## Start

- Command: `/zenbukkowa start` (requires op).
- Broadcast: `Event started! Duration: 2:00:00`.
- Timer begins counting down on scoreboard.

## Stop

- Command: `/zenbukkowa end` (requires op).
- Auto-stop when timer reaches zero.
- Broadcast: `Event finished!`.

## Pause

- Not supported. Event runs until completion or op command.
