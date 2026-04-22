# Verification

## Goal

Run all quality gates before accepting any batch.

## Local Verification

```bash
docker compose -f docker-compose.verify.yml up --build
```

## Gates

1. Gradle compile
2. Unit tests
3. Shadow JAR assembly
4. `scripts/check_lines.py` (docs <= 300 lines, source <= 200 lines)
5. Smoke tests against running Paper server via RCON

## Failure Handling

- Fix the failure.
- Re-run the verification command.
- Commit only after all gates pass.
