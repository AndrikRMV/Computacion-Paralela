#!/bin/bash
# Crea build/matrices.jar a partir de bin/
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
BIN_DIR="$ROOT_DIR/bin"
BUILD_DIR="$ROOT_DIR/build"
mkdir -p "$BUILD_DIR"
if [ ! -d "$BIN_DIR" ]; then
  echo "ERROR: No existe $BIN_DIR. Compila las fuentes primero (javac -d bin ...) o copia bin/ aquí." >&2
  exit 1
fi

echo "Creando JAR en $BUILD_DIR/matrices.jar desde $BIN_DIR"
jar cf "$BUILD_DIR/matrices.jar" -C "$BIN_DIR" .
echo "Hecho: $BUILD_DIR/matrices.jar"
