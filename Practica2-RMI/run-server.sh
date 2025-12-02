#!/bin/bash

# Script automatizado para compilar y ejecutar el servidor RMI
# Automatiza el proceso de configuración del servidor

echo "🚀 Configurando Servidor RMI..."
echo ""

# Obtener la IP automáticamente
echo "📡 Tu dirección IP es:"
IP=$(ifconfig | grep "inet " | grep -v 127.0.0.1 | awk '{print $2}' | head -1)
echo "   → $IP"
echo ""
echo "⚠️  COMPARTE ESTA IP con la otra Mac (cliente)"
echo ""

# Solicitar el puerto
read -p "📝 Ingresa el puerto para RMI (recomendado: 1234): " PUERTO

# Usar puerto por defecto si no se ingresa nada
if [ -z "$PUERTO" ]; then
    PUERTO=1234
    echo "   → Usando puerto por defecto: $PUERTO"
fi

echo ""
echo "🔨 Compilando archivos del servidor..."

# Ir al directorio del servidor
cd "$(dirname "$0")/Server"

# Compilar la interfaz y la implementación
javac MiInterfazRemota.java
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar MiInterfazRemota.java"
    exit 1
fi

javac MiClaseRemota.java
if [ $? -ne 0 ]; then
    echo "❌ Error al compilar MiClaseRemota.java"
    exit 1
fi

echo "✅ Compilación exitosa"
echo ""

# Opcional: Generar stub con rmic (en versiones antiguas de Java)
# rmic -d . MiClaseRemota

echo "🎯 Iniciando servidor RMI..."
echo ""

# Configurar la propiedad java.rmi.server.hostname con la IP detectada
# Esto soluciona el problema de "Connection refused to host: 127.0.0.1"
echo "🔧 Configurando java.rmi.server.hostname=$IP"
echo ""

# Ejecutar el servidor con la propiedad configurada
java -Djava.rmi.server.hostname=$IP MiClaseRemota $PUERTO
