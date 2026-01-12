#!/bin/bash

if [ "$#" -lt 2 ]; then
  echo "Usage: ./run-worker.sh <bindName> <port>"
  exit 1
fi

BIND_NAME=$1
PORT=$2

# Compilar si es necesario
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
MODULE_SRC="$SCRIPT_DIR/src"
PROJECT_SRC="$(cd "$SCRIPT_DIR/../../.." && pwd)/src"

cd "$MODULE_SRC" || exit 1
find . -name "*.java" > /tmp/java_files.txt
javac -cp .:$PROJECT_SRC @/tmp/java_files.txt

# Ejecutar Worker (incluir classpath del proyecto principal)
java -cp .:$PROJECT_SRC com.ejemplo.rmi.Worker $BIND_NAME $PORT
