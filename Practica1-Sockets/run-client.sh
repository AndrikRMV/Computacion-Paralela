#!/bin/bash

# Script para configurar y ejecutar el CLIENTE
# Ejecutar este script en la Mac que será el cliente

echo "🚀 Configurando Cliente..."
echo ""

# Solicitar la IP del servidor
read -p "📝 Ingresa la IP del servidor: " SERVER_IP

# Validar que se ingresó algo
if [ -z "$SERVER_IP" ]; then
    echo "❌ Error: Debes ingresar una IP"
    exit 1
fi

echo ""
echo "🔧 Actualizando Client.java con IP: $SERVER_IP"

# Ir al directorio del cliente
cd "$(dirname "$0")/Client"

# Crear una copia temporal del archivo modificado
sed "s/127.0.0.1/$SERVER_IP/g" Client.java > Client.java.tmp
mv Client.java.tmp Client.java

echo "✅ IP actualizada"
echo ""

# Compilar
echo "🔨 Compilando Client.java..."
javac Client.java

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    echo ""
    echo "📤 Conectando con el servidor y enviando mensajes..."
    echo ""
    # Ejecutar el cliente
    java Client
    echo ""
    echo "✅ Mensajes enviados correctamente"
else
    echo "❌ Error en la compilación"
    exit 1
fi
