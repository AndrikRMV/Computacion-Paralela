#!/bin/bash

# Script automatizado para compilar y ejecutar el cliente de chat RMI con GUI

echo "🚀 Configurando Cliente de Chat RMI..."
echo ""

# Ir al directorio base
cd "$(dirname "$0")"

echo "🔨 Compilando archivos del cliente..."

# Compilar las interfaces comunes primero
cd Common
javac ChatClientInterface.java
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar ChatClientInterface.java"
    exit 1
fi

javac ChatServerInterface.java
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar ChatServerInterface.java"
    exit 1
fi

# Copiar interfaces al directorio del cliente
cp *.class ../Client/
cd ../Client

# Compilar el cliente
javac ChatClientGUI.java
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar ChatClientGUI.java"
    exit 1
fi

echo "✅ Compilación exitosa"
echo ""
echo "🎮 Iniciando interfaz gráfica del chat..."
echo ""

# Ejecutar el cliente
java ChatClientGUI

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Error al ejecutar el cliente"
    exit 1
fi
