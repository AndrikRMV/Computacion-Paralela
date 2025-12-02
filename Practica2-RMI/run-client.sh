#!/bin/bash

# Script automatizado para compilar y ejecutar el cliente RMI
# Simplifica la conexión al servidor remoto

echo "🚀 Configurando Cliente RMI..."
echo ""

# Solicitar IP del servidor
read -p "📝 Ingresa la IP del servidor RMI: " SERVER_IP

# Validar que se ingresó una IP
if [ -z "$SERVER_IP" ]; then
    echo "❌ Error: Debes ingresar la IP del servidor"
    exit 1
fi

# Solicitar puerto
read -p "📝 Ingresa el puerto del servidor (presiona Enter para usar 1234): " PUERTO

# Usar puerto por defecto si no se ingresa nada
if [ -z "$PUERTO" ]; then
    PUERTO=1234
    echo "   → Usando puerto por defecto: $PUERTO"
fi

echo ""
echo "🔨 Compilando archivos del cliente..."

# Ir al directorio del cliente
cd "$(dirname "$0")/Client"

# Compilar la interfaz y el cliente
javac MiInterfazRemota.java
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar MiInterfazRemota.java"
    exit 1
fi

javac MiClienteRMI.java
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar MiClienteRMI.java"
    exit 1
fi

echo "✅ Compilación exitosa"
echo ""
echo "📤 Conectando con el servidor RMI..."
echo ""

# Ejecutar el cliente
java MiClienteRMI $SERVER_IP $PUERTO

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Cliente ejecutado correctamente"
else
    echo ""
    echo "❌ Error al ejecutar el cliente"
    exit 1
fi
