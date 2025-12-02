# Unidad III - Procesamiento Paralelo# Unidad III - Procesamiento Paralelo# Práctica de Sockets en Java

## Prácticas de Programación Paralela

## Prácticas de Programación Paralela## Comunicación Cliente-Servidor

Este repositorio contiene las prácticas de la Unidad III del curso de Programación Paralela.

Este repositorio contiene las prácticas de la Unidad III del curso de Programación Paralela.### Descripción

---

Implementación de comunicación bidireccional entre dos programas Java independientes usando Sockets TCP/IP. El servidor escucha conexiones en el puerto 1234, el cliente se conecta y envía 5 mensajes que el servidor recibe y muestra.

## 📂 Estructura de Prácticas

---

```

Paralela3P/---

├── Practica1-Sockets/           ✅ Comunicación Cliente-Servidor con Sockets

│   ├── Server/## 📂 Estructura de Prácticas

│   ├── Client/

│   ├── run-server.sh## Estructura del Proyecto

│   ├── run-client.sh

│   └── README.md`````

│

├── Practica2-RMI/               ✅ RMI (Remote Method Invocation)Paralela3P/Paralela3P/

│   ├── Server/

│   ├── Client/├── Practica1-Sockets/           ✅ Comunicación Cliente-Servidor con Sockets├── Server/

│   ├── run-server.sh

│   ├── run-client.sh│   ├── Server/│   └── Server.java          # Programa servidor

│   └── README.md

││   ├── Client/├── Client/

└── Practica3/                   🔜 Próxima práctica

```│   ├── run-server.sh│   └── Client.java          # Programa cliente



---│   ├── run-client.sh├── run-server.sh            # Script automatizado para servidor



## 📋 Prácticas│   └── README.md├── run-client.sh            # Script automatizado para cliente



### ✅ Práctica 1: Comunicación mediante Sockets│└── README.md

- **Tema:** Sockets TCP/IP en Java

- **Descripción:** Implementación de comunicación cliente-servidor entre dos máquinas usando Sockets├── Practica2/                   🔜 Próxima práctica```

- **Estado:** Completada ✅

- **Carpeta:** `Practica1-Sockets/`│



### ✅ Práctica 2: RMI (Remote Method Invocation)└── Practica3/                   🔜 Próxima práctica---

- **Tema:** Invocación remota de métodos en Java

- **Descripción:** Comunicación distribuida mediante objetos remotos accesibles a través de la red```

- **Estado:** Completada ✅

- **Carpeta:** `Practica2-RMI/`## Ejecución Rápida



### 🔜 Práctica 3: [Pendiente]---

- **Tema:** Por definir

- **Carpeta:** `Practica3/`### Opción 1: Scripts Automatizados (⭐ Mejora Implementada)



---## 📋 Prácticas



## 🚀 Cómo usar este repositorio**Mac Servidor:**



Cada práctica está en su propia carpeta con su documentación específica. Para trabajar con una práctica:### ✅ Práctica 1: Comunicación mediante Sockets```bash



```bash- **Tema:** Sockets TCP/IP en Java./run-server.sh

cd Paralela3P/Practica1-Sockets   # o Practica2-RMI

# Leer el README.md de esa práctica- **Descripción:** Implementación de comunicación cliente-servidor entre dos máquinas```

./run-server.sh                    # En la Mac servidor

./run-client.sh                    # En la Mac cliente- **Estado:** Completada- Obtiene la IP automáticamente

```

- **Carpeta:** `Practica1-Sockets/`- Compila y ejecuta el servidor

---

- Muestra la IP para compartir con el cliente

## 📊 Comparación de Tecnologías

### 🔜 Práctica 2: [Pendiente]

| Aspecto | Sockets (Práctica 1) | RMI (Práctica 2) |

|---------|---------------------|------------------|- **Tema:** Por definir**Mac Cliente:**

| **Nivel de abstracción** | Bajo nivel (TCP/IP) | Alto nivel (objetos) |

| **Complejidad** | Manejo manual de streams | Invocación transparente |- **Carpeta:** `Practica2/````bash

| **Serialización** | Manual | Automática |

| **Uso típico** | Protocolos simples | Sistemas distribuidos |./run-client.sh

| **Flexibilidad** | Total control | Limitado a Java |

### 🔜 Práctica 3: [Pendiente]```

---

- **Tema:** Por definir- Solicita la IP del servidor

**Autor:** Andrés Meneses  

**Institución:** CETI  - **Carpeta:** `Practica3/`- Actualiza el código automáticamente

**Materia:** Programación Paralela  

**Periodo:** 3P 2025- Compila y ejecuta el cliente


---

### Opción 2: Compilación Manual

## 🚀 Cómo usar este repositorio

**Servidor:**

Cada práctica está en su propia carpeta con su documentación específica. Para trabajar con una práctica:```bash

cd Server

```bashjavac Server.java

cd Paralela3P/Practica1-Socketsjava Server

# Leer el README.md de esa práctica```

```

**Cliente:**

---```bash

cd Client

**Autor:** Andrés Meneses  # Editar Client.java línea 19 con la IP del servidor

**Institución:** CETI  javac Client.java

**Materia:** Programación Paralela  java Client

**Periodo:** 3P 2025```


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
`````
