# Práctica de Sockets en Java
## Comunicación Cliente-Servidor

### Descripción
Implementación de comunicación bidireccional entre dos programas Java independientes usando Sockets TCP/IP. El servidor escucha conexiones en el puerto 1234, el cliente se conecta y envía 5 mensajes que el servidor recibe y muestra.

---

## Estructura del Proyecto
```
Paralela3P/
├── Server/
│   └── Server.java          # Programa servidor
├── Client/
│   └── Client.java          # Programa cliente
├── run-server.sh            # Script automatizado para servidor
├── run-client.sh            # Script automatizado para cliente
└── README.md
```

---

## Ejecución Rápida

### Opción 1: Scripts Automatizados (⭐ Mejora Implementada)

**Mac Servidor:**
```bash
./run-server.sh
```
- Obtiene la IP automáticamente
- Compila y ejecuta el servidor
- Muestra la IP para compartir con el cliente

**Mac Cliente:**
```bash
./run-client.sh
```
- Solicita la IP del servidor
- Actualiza el código automáticamente
- Compila y ejecuta el cliente

### Opción 2: Compilación Manual

**Servidor:**
```bash
cd Server
javac Server.java
java Server
```

**Cliente:**
```bash
cd Client
# Editar Client.java línea 19 con la IP del servidor
javac Client.java
java Client
```

---

## Funcionamiento Técnico

1. **ServerSocket** espera conexiones en puerto 1234
2. Cliente establece **Socket** hacia la IP del servidor
3. Servidor acepta conexión con `accept()`
4. Cliente envía 5 mensajes usando `DataOutputStream`
5. Servidor recibe con `BufferedReader` y los muestra
6. Ambos cierran la conexión

---

## Requisitos
- Java JDK 8+
- Dos Macs en la misma red WiFi
- Puerto 1234 disponible

---

## Mejoras Implementadas
✅ **Scripts de automatización** para simplificar compilación y ejecución  
✅ **Detección automática de IP** en el servidor  
✅ **Actualización dinámica** de la IP en el código del cliente  
✅ **Validación y mensajes** informativos durante la ejecución  

---

**Autor:** Andrik Meneses  
**Materia:** Programación Paralela - CETI
