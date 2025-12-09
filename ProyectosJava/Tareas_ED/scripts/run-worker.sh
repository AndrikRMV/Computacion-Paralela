#!/bin/bash
# Usage: ./run-worker.sh <REGISTRY_HOST> [REGISTRY_PORT]
# REGISTRY_HOST: IP del host donde está el rmiregistry
ROOT_DIR="/Users/andrikmeneses/ProyectosJava/Tareas_ED"
export CLASSPATH="$ROOT_DIR/bin"
REGISTRY_HOST="$1"
REGISTRY_PORT="${2:-2099}"
if [ -z "$REGISTRY_HOST" ]; then
  echo "Usage: $0 <REGISTRY_HOST> [REGISTRY_PORT]"
  echo "Example: $0 192.168.100.144 2099"
  exit 1
fi

echo "Starting Worker connecting to registry at $REGISTRY_HOST:$REGISTRY_PORT"
nohup java -cp "$CLASSPATH" com.ejemplo.rmi.Worker Worker1 "$REGISTRY_HOST" "$REGISTRY_PORT" > /tmp/worker1.log 2>&1 &
echo $! > /tmp/worker1.pid
sleep 0.2
echo "Worker pid: $(cat /tmp/worker1.pid)"
echo "Tailing /tmp/worker1.log..."
tail -f /tmp/worker1.log
