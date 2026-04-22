# Compose Pipeline

## Goal

Run build, test, and smoke verification in containers.

## Services

### verify

- Image: `gradle:8.10.2-jdk21`
- Command: `bash scripts/verify.sh`
- Steps:
  1. `gradle compileJava`
  2. `gradle test`
  3. `gradle shadowJar`
  4. `python3 scripts/check_lines.py`

### smoke

- Image: `python:3.12-slim`
- Depends on: `paper` service healthy
- Command: `bash scripts/smoke.sh`
- Steps:
  1. Wait for RCON ready.
  2. Run `version` via RCON to confirm plugin loaded.
  3. Run `zenbukkowa status` via RCON.

## Run

```bash
docker compose -f docker-compose.verify.yml up --build
```
