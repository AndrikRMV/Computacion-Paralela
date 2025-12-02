#!/bin/bash

# Script para configurar y ejecutar el SERVIDOR
# Ejecutar este script en la Mac que será el servidor

echo "🚀 Configurando Servidor..."
echo ""

# Obtener la IP automáticamente
echo "📡 Tu dirección IP es:"
IP=$(ifconfig | grep "inet " | grep -v 127.0.0.1 | awk '{print $2}' | head -1)
echo "   → $IP"
echo ""
echo "⚠️  COMPARTE ESTA IP con la otra Mac (cliente)"
echo ""

# Ir al directorio del servidor
cd "$(dirname "$0")/Server"

# Compilar
echo "🔨 Compilando Server.java..."
javac Server.java

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    echo ""
    echo "🎯 Iniciando servidor en puerto 1234..."
    echo "   Esperando conexión del cliente..."
    echo ""
    # Ejecutar el servidor
    java Server
else
    echo "❌ Error en la compilación"
    exit 1
fi
