package com.ejemplo.rmi;

import com.ejemplo.logic.Matrix;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

// Worker remoto: Calcula bloques de la multiplicación
public class Worker extends UnicastRemoteObject implements WorkerInterface {
    private final String id;

    protected Worker() throws RemoteException {
        super();
        this.id = UUID.randomUUID().toString();  // ID único
        System.out.println("Worker instantiated: " + id);
    }

    // Método RMI: Recibe bloque de A y toda B, devuelve bloque de C
    @Override
    public Matrix computeBlock(Matrix aBlock, Matrix bFull) throws RemoteException {
        System.out.println("Worker " + id + " computing block: " + aBlock.getRows() + "x" + aBlock.getCols());
        Matrix result = Matrix.multiplySequential(aBlock, bFull);  // Calcula localmente
        System.out.println("Worker " + id + " finished block");
        return result;
    }

    @Override
    public String getWorkerId() throws RemoteException {
        return id;
    }

    // Main: Conecta el Worker al registro RMI del HOST
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Uso: java Worker <bindName> <registryHost> <registryPort>");
            System.out.println("Ejemplo: java Worker Worker1 192.168.1.100 2099");
            System.exit(1);
        }

        String bindName = args[0];        // Ej: Worker1
        String registryHost = args[1];    // Ej: 172.20.10.13 (IP del HOST)
        int registryPort = Integer.parseInt(args[2]);  // Ej: 2099

        try {
            // Conectar al registro RMI remoto del HOST
            Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
            System.out.println("Conectando al registro RMI en " + registryHost + ":" + registryPort);

            // Crear Worker y registrarlo
            Worker worker = new Worker();
            registry.rebind(bindName, worker);
            System.out.println("Worker registrado con nombre: " + bindName);
            System.out.println("Esperando invocaciones RMI...");

        } catch (Exception e) {
            System.err.println("Error iniciando Worker:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}

