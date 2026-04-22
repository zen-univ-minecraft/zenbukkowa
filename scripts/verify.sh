#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== compile ==="
gradle compileJava

echo "=== test ==="
gradle test

echo "=== shadowJar ==="
gradle shadowJar

echo "=== line limits ==="
python3 scripts/check_lines.py

echo "=== verify done ==="
