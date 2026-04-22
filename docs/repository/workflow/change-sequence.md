# Change Sequence

## Steps

1. Identify the docs files that own the behavior.
2. Update docs to the new contract.
3. Update parent README.md files in the same batch.
4. Implement code changes.
5. Run `./gradlew test` locally.
6. Run `python3 scripts/check_lines.py`.
7. Run `docker compose -f docker-compose.verify.yml up --build`.
8. Fix failures and re-run.
9. Commit with a message referencing the docs files changed.
