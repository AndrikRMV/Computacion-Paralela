#!/bin/bash

if [ "$#" -lt 3 ]; then
  echo "Usage: ./run-coordinator.sh <rows> <cols> <workerUrl1> [workerUrl2 ...]"
  exit 1
fi

ROWS=$1
COLS=$2
shift 2
WORKERS=("$@")

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
MODULE_SRC="$SCRIPT_DIR/src"
PROJECT_SRC="$(cd "$SCRIPT_DIR/../../.." && pwd)/src"

cd "$MODULE_SRC" || exit 1
find . -name "*.java" > /tmp/java_files_coord.txt
javac -cp .:$PROJECT_SRC @/tmp/java_files_coord.txt

java -cp .:$PROJECT_SRC com.ejemplo.rmi.Coordinator $ROWS $COLS "${WORKERS[@]}"
