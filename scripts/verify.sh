#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== compile ==="
./gradlew compileJava

echo "=== test ==="
./gradlew test

echo "=== shadowJar ==="
./gradlew shadowJar

echo "=== line limits ==="
python3 scripts/check_lines.py

echo "=== verify done ==="
