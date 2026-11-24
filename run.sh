#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"
BIN_DIR="$PROJECT_DIR/bin"

echo "=== Compilando JuegoDeCombate ==="
mkdir -p "$BIN_DIR"

find "$SRC_DIR" -name "*.java" > /tmp/juego_sources.txt

javac -encoding UTF-8 -d "$BIN_DIR" @/tmp/juego_sources.txt

echo "Compilacion completada."
echo ""
echo "=== Iniciando juego ==="
java -cp "$BIN_DIR" juego.Main
