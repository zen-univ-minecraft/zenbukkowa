# Scripted Checks

## Goal

Define what smoke tests verify.

## Checks

1. Server starts and responds to RCON.
2. Plugin `zenbukkowa` is loaded in plugin list.
3. Command `zenbukkowa status` returns event state.
4. No ERROR logs from `zenbukkowa` during startup.

## Failure

- Any check failure blocks acceptance.
- Fix and re-run.
