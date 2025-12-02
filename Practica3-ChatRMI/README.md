# Práctica 3: Chat RMI con Interfaz Gráfica
## Aplicación de Chat Distribuido con RMI y GUI

### Descripción
Aplicación de chat completa con interfaz gráfica que implementa comunicación mediante RMI. Incluye dos modos de comunicación:
1. **Broadcast (Grupo)**: Los mensajes se envían al servidor y este los distribuye a todos los clientes
2. **Peer-to-Peer (Directo)**: Los clientes se comunican directamente sin pasar por el servidor

---

## Estructura del Proyecto
```
Practica3-ChatRMI/
├── Common/
│   ├── ChatServerInterface.java     # Interfaz del servidor
│   └── ChatClientInterface.java     # Interfaz del cliente
├── Server/
│   └── ChatServer.java              # Servidor de chat RMI
├── Client/
│   └── ChatClientGUI.java           # Cliente con GUI Swing
├── run-server.sh                    # Script para servidor
├── run-client.sh                    # Script para cliente
└── README.md
```

---

## Características Implementadas

### ✅ 1. Comunicación Broadcast (Grupo)
- El cliente envía mensaje al servidor
- El servidor lo distribuye a **todos** los clientes conectados
- Ideal para chat grupal o anuncios generales

### ✅ 2. Comunicación Peer-to-Peer (Directo)
- El cliente selecciona un destinatario de la lista
- El mensaje va **directamente** de cliente a cliente
- **NO pasa por el servidor** (solo obtiene la referencia)
- Comunicación privada y eficiente

### ✅ 3. Interfaz Gráfica (GUI)
- Ventana moderna con Swing
- Área de chat con historial
- Lista de usuarios conectados
- Botones separados para cada tipo de mensaje
- Notificaciones de conexión/desconexión

### ✅ 4. Gestión de Usuarios
- Registro automático al conectar
- Lista actualizada en tiempo real
- Notificaciones cuando usuarios entran/salen
- Validación de nombres únicos

---

## Ejecución Rápida

### Opción 1: Scripts Automatizados (⭐ Recomendado)

**Mac Servidor:**
```bash
./run-server.sh
```
- Detecta IP automáticamente
- Solicita puerto (default: 1099)
- Compila y ejecuta el servidor
- Muestra información de conexión

**Macs Clientes:**
```bash
./run-client.sh
```
- Compila automáticamente
- Abre diálogo de configuración
- Solicita: nombre de usuario, IP del servidor y puerto
- Lanza la interfaz gráfica

### Opción 2: Compilación Manual

**Servidor:**
```bash
cd Common
javac *.java
cp *.class ../Server/
cd ../Server
javac ChatServer.java
java -Djava.rmi.server.hostname=<TU_IP> ChatServer 1099
```

**Cliente:**
```bash
cd Common
javac *.java
cp *.class ../Client/
cd ../Client
javac ChatClientGUI.java
java ChatClientGUI
```

---

## Cómo Usar la Aplicación

### 1️⃣ Iniciar el Servidor
- Ejecuta el servidor primero
- Anota la IP que muestra
- Deja la terminal abierta

### 2️⃣ Conectar Clientes
- Ejecuta el cliente en cada Mac
- Ingresa un nombre de usuario único
- Ingresa la IP del servidor
- Click en OK

### 3️⃣ Enviar Mensajes Broadcast
1. Escribe tu mensaje
2. Click en **"📢 Enviar a Todos"** o presiona Enter
3. El mensaje llega a todos los usuarios conectados

### 4️⃣ Enviar Mensajes Directos (P2P)
1. Selecciona un usuario de la lista
2. Escribe tu mensaje
3. Click en **"📨 Mensaje Directo"**
4. Solo el usuario seleccionado recibe el mensaje

---

## Funcionamiento Técnico

### Arquitectura Broadcast
```
Cliente A                    Servidor                     Cliente B
   |                            |                            |
   |------ Mensaje broadcast -->|                            |
   |                            |------ Distribuye --------->|
   |                            |------ Distribuye --------->| Cliente C
```

### Arquitectura Peer-to-Peer
```
Cliente A                    Servidor                     Cliente B
   |                            |                            |
   |--- Solicita referencia --->|                            |
   |<--- Devuelve referencia ---|                            |
   |                                                          |
   |------------------- Mensaje directo -------------------->|
```

### Componentes Clave

**ChatServerInterface:**
- `registerClient()`: Registra nuevos clientes
- `broadcastMessage()`: Distribuye mensajes a todos
- `getClientReference()`: Obtiene referencia para P2P
- `getOnlineUsers()`: Lista de usuarios conectados

**ChatClientInterface:**
- `receiveMessage()`: Recibe mensajes (broadcast o directo)
- `userJoined()`: Notificación de nuevo usuario
- `userLeft()`: Notificación de desconexión

**ChatServer:**
- Mantiene mapa de clientes conectados (`ConcurrentHashMap`)
- Thread-safe para múltiples conexiones simultáneas
- Notifica eventos a todos los clientes

**ChatClientGUI:**
- Interfaz Swing con JFrame, JTextArea, JList
- Implementa callback interface para recibir mensajes
- Maneja dos tipos de envío (broadcast y directo)

---

## Requisitos
- Java JDK 8+
- Dos o más Macs en la misma red WiFi
- Puerto disponible (default: 1099)
- Java Swing (incluido en JDK)

---

## Diferencias entre Comunicación

| Característica | Broadcast | Peer-to-Peer |
|---------------|-----------|--------------|
| **Ruta** | Cliente → Servidor → Todos | Cliente → Cliente |
| **Destinatarios** | Todos los conectados | Usuario específico |
| **Servidor** | Procesa y distribuye | Solo da referencia |
| **Privacidad** | Público | Privado |
| **Eficiencia** | Depende del servidor | Directa |
| **Uso** | Chat grupal, anuncios | Mensajes privados |

---

## Mejoras Implementadas
✅ **Interfaz gráfica moderna** con Swing  
✅ **Dos modos de comunicación** (broadcast y P2P)  
✅ **Scripts de automatización** para compilación y ejecución  
✅ **Gestión robusta de conexiones** con manejo de errores  
✅ **Notificaciones en tiempo real** de usuarios  
✅ **Lista actualizada automáticamente** de usuarios online  
✅ **Thread-safe** con `ConcurrentHashMap` y `SwingUtilities`  

---

## Solución de Problemas

**El cliente no se conecta:**
- Verifica que el servidor esté corriendo
- Confirma la IP y puerto correctos
- Asegúrate de estar en la misma red

**No aparecen usuarios en la lista:**
- Refresca la lista (se actualiza automáticamente)
- Verifica la conexión de red

**Mensajes directos no llegan:**
- Asegúrate de seleccionar un usuario
- Verifica que el usuario sigue conectado

**Error de nombre duplicado:**
- Cada usuario debe tener un nombre único
- Cierra el cliente y reinicia con otro nombre

---

**Autor:** Andrés Meneses  
**Materia:** Programación Paralela - CETI  
**Cuenta por:** 2 prácticas
