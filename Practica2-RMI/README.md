# Práctica de RMI en Java
## Remote Method Invocation (Invocación Remota de Métodos)

### Descripción
Implementación de comunicación cliente-servidor usando RMI (Remote Method Invocation) en Java. El servidor expone objetos remotos que pueden ser invocados por clientes en diferentes máquinas, permitiendo la ejecución distribuida de métodos.

---

## Estructura del Proyecto
```
Practica2-RMI/
├── Server/
│   ├── MiInterfazRemota.java    # Interfaz que define métodos remotos
│   └── MiClaseRemota.java       # Implementación del servidor RMI
├── Client/
│   ├── MiInterfazRemota.java    # Copia de la interfaz remota
│   └── MiClienteRMI.java        # Cliente que invoca métodos remotos
├── run-server.sh                # Script automatizado para servidor
├── run-client.sh                # Script automatizado para cliente
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
- Solicita el puerto (default: 1234)
- Compila y ejecuta el servidor RMI
- Registra el objeto remoto en el registro RMI

**Mac Cliente:**
```bash
./run-client.sh
```
- Solicita IP del servidor
- Solicita puerto (default: 1234)
- Compila el cliente
- Se conecta al servidor y ejecuta métodos remotos

### Opción 2: Compilación Manual

**Servidor:**
```bash
cd Server
javac MiInterfazRemota.java
javac MiClaseRemota.java
# Opcional en Java antiguo: rmic -d . MiClaseRemota
java MiClaseRemota 1234
```

**Cliente:**
```bash
cd Client
javac MiInterfazRemota.java
javac MiClienteRMI.java
java MiClienteRMI <IP_servidor> 1234
```

---

## Funcionamiento Técnico

### Arquitectura RMI
1. **Servidor:**
   - Crea un `Registry` en un puerto específico
   - Instancia el objeto remoto (`MiClaseRemota`)
   - Registra el objeto con `Naming.rebind()`
   - Queda esperando invocaciones

2. **Cliente:**
   - Usa `Naming.lookup()` para obtener referencia al objeto remoto
   - Invoca métodos como si fueran locales
   - El stub (proxy) maneja la comunicación red
   - El skeleton en el servidor ejecuta el método real

### Componentes
- **Interfaz Remota:** Define métodos invocables remotamente (extiende `Remote`)
- **Objeto Remoto:** Implementa la interfaz (extiende `UnicastRemoteObject`)
- **Registro RMI:** Servicio de nombres que mapea nombres a objetos remotos
- **Stub/Skeleton:** Proxies que manejan la comunicación (generados automáticamente en Java moderno)

---

## Flujo de Ejecución

1. Servidor crea objeto remoto y lo registra
2. Cliente busca el objeto por su nombre en el registro
3. Cliente obtiene un stub (proxy local)
4. Cliente invoca `miMetodo2()` → retorna 5
5. Cliente invoca `miMetodo1()` 5 veces
6. Cada invocación se ejecuta en el servidor
7. El servidor imprime "Estoy en miMetodo1()" 5 veces

---

## Requisitos
- Java JDK 8+
- Dos Macs en la misma red WiFi
- Puerto disponible (default: 1234)
- Misma versión de Java en ambas máquinas

---

## Diferencias con Sockets

| Aspecto | Sockets | RMI |
|---------|---------|-----|
| **Nivel** | Bajo nivel (TCP/IP) | Alto nivel (objetos) |
| **Invocación** | Manual (streams) | Transparente (métodos) |
| **Serialización** | Manual | Automática |
| **Complejidad** | Mayor | Menor |
| **Uso** | Comunicación simple | Sistemas distribuidos |

---

## Mejoras Implementadas
✅ **Scripts de automatización** para compilación y ejecución  
✅ **Detección automática de IP** en el servidor  
✅ **Validación de parámetros** y mensajes de error claros  
✅ **Feedback visual mejorado** con emojis y formato  
✅ **Manejo de puertos configurable** con valores por defecto  

---

## Notas Importantes
- El servidor debe iniciarse **antes** que el cliente
- Ambas máquinas deben tener la **misma interfaz remota**
- En macOS no es necesario deshabilitar firewall
- El comando `rmic` es opcional en Java 8+ (stubs dinámicos)

---

**Autor:** Andrés Meneses  
**Materia:** Programación Paralela - CETI
