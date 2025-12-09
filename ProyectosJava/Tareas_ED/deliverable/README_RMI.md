# Deliverable: Matrices RMI - Cómo usar

Este directorio contiene artefactos y scripts para preparar un paquete (JAR) y ejecutar la demo RMI en otra máquina.

Contenido:
- `build_matrices_jar.sh`: script para crear `build/matrices.jar` desde `bin/`.
- `README_RMI.md`: este archivo.

Pasos para preparar y ejecutar en la máquina remota

1) En la máquina de desarrollo (donde ya compilaste y tienes `bin/`) crea el JAR:

```bash
cd /ruta/al/proyecto/Tareas_ED
mkdir -p build
./deliverable/build_matrices_jar.sh
ls -l build/matrices.jar
```

2) Copia el JAR al host remoto (ejemplo):

```bash
# desde la máquina de desarrollo
scp build/matrices.jar remote_user@192.168.2.1:~/ProyectosJava/Tareas_ED/build/
```

3) En la máquina remota verifica y corre el Worker (el script `run-worker.sh` detecta `build/matrices.jar`):

```bash
ssh remote_user@192.168.2.1
cd ~/ProyectosJava/Tareas_ED
ls -l build/matrices.jar
./scripts/run-worker.sh 192.168.2.1
```

4) En la máquina cliente (GUI) añade la URL del worker `//192.168.2.1:2099/Worker1` y ejecuta `Run Parallel (RMI)`.

Notas:
- Si prefieres, en lugar de copiar el JAR puedes copiar la carpeta `bin/` completa.
- El script `run-worker.sh` también soporta la variable `MATRICES_CLASSPATH` para forzar un CLASSPATH alternativo.

Si quieres que empuje este paquete a tu repositorio GitHub (`https://github.com/AndrikRMV/Computacion-Paralela`), dime y lo intento (puede pedirme credenciales o usar clave SSH si está configurada). En caso contrario, te doy los comandos exactos para que lo hagas localmente.
