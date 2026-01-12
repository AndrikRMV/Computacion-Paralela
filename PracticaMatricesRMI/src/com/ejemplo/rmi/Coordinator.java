package com.ejemplo.rmi;

import com.ejemplo.logic.Matrix;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Coordinator: Distribuye la multiplicación de matrices entre Workers remotos vía RMI
 */
public class Coordinator {

    // Lista de Workers remotos conectados
    private final List<WorkerInterface> workers = new ArrayList<>();

    public Coordinator() {
    }

    // Conecta a un Worker remoto usando su URL (ej: //192.168.1.100:2099/Worker1)
    public void addWorkerByUrl(String url) throws Exception {
        WorkerInterface w = (WorkerInterface) Naming.lookup(url);  // RMI lookup
        workers.add(w);
        System.out.println("Worker añadido: " + url + " id=" + w.getWorkerId());
    }

    public Matrix multiplyParallelRMI(Matrix A, Matrix B) throws Exception {
        return multiplyParallelRMI(A, B, null);
    }

    // Multiplicación paralela distribuida con RMI
    public Matrix multiplyParallelRMI(Matrix A, Matrix B, CoordinatorProgressListener listener) throws Exception {
        if (A.getCols() != B.getRows()) throw new IllegalArgumentException("Dimensiones incompatibles");
        int rows = A.getRows();
        int cols = B.getCols();
        Matrix result = new Matrix(rows, cols);  // Matriz resultado

        int nWorkers = Math.max(1, workers.size());
        int blockSize = (rows + nWorkers - 1) / nWorkers; // Cuántas filas por Worker

        ExecutorService exe = Executors.newFixedThreadPool(nWorkers);  // Pool de threads
        List<Future<WorkerResult>> futures = new ArrayList<>();

        long start = System.nanoTime();

        for (int i = 0; i < nWorkers; i++) {
            final int startRow = i * blockSize;
        // Dividir matriz A en bloques de filas y asignar a cada Worker
        for (int i = 0; i < nWorkers; i++) {
            final int startRow = i * blockSize;  // Fila inicial
            final int endRow = Math.min(rows, startRow + blockSize);  // Fila final
            if (startRow >= endRow) break;
            final WorkerInterface worker = workers.get(i);
            final int workerIndex = i;

            Callable<WorkerResult> task = () -> {
                if (listener != null) listener.onBlockStart(workerIndex, startRow, endRow);
                
                // Extraer bloque de filas de A para este Worker
                int aRows = endRow - startRow;
                Matrix aBlock = new Matrix(aRows, A.getCols());
                for (int r = 0; r < aRows; r++) {
                    for (int c = 0; c < A.getCols(); c++) {
                        aBlock.setValue(r, c, A.getValue(startRow + r, c));
                    }
                }
                
                // Llamada RMI remota: Worker calcula su bloque
                Matrix cBlock = worker.computeBlock(aBlock, B);
                
        // Recolectar y ensamblar resultados de todos los Workers
        for (Future<WorkerResult> f : futures) {
            WorkerResult wr = f.get();  // Esperar resultado
            Matrix cBlock = wr.cBlock;
            int baseRow = wr.startRow;
            
            // Copiar bloque al resultado final
            for (int r = 0; r < cBlock.getRows(); r++) {
                for (int c = 0; c < cBlock.getCols(); c++) {
                    result.setValue(baseRow + r, c, cBlock.getValue(r, c));
                }
            }
        }

        long end = System.nanoTime();
        exe.shutdown();

        System.out.println("Tiempo paralelo RMI (ms): " + ((end - start) / 1_000_000.0));
        return result;
    }

    // Clase auxiliar para guardar resultado de cada Worker
    private static class WorkerResult {
        final int startRow;    // Fila donde empieza el bloque
        final Matrix cBlock;   // Bloque calculado

        WorkerResult(int startRow, Matrix cBlock) {
            this.startRow = startRow;
            this.cBlock = cBlock;
    // Main de prueba: ejecuta multiplicación con Workers remotos
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Uso: java Coordinator <rows> <cols> <workerUrl1> [workerUrl2 ...]");
            System.exit(1);
        }

        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);

        // Crear matrices aleatorias
        Matrix A = new Matrix(rows, cols);
        Matrix B = new Matrix(cols, cols);
        A.fillRandom();
        B.fillRandom();

        // Conectar Workers
        Coordinator co = new Coordinator();
        for (int i = 2; i < args.length; i++) {
            co.addWorkerByUrl(args[i]);
        }

        // Ejecutar secuencial para comparar
        long t0 = System.nanoTime();
        Matrix s = Matrix.multiplySequential(A, B);
        long t1 = System.nanoTime();
        System.out.println("Tiempo secuencial (ms): " + ((t1 - t0) / 1_000_000.0));

        // Ejecutar paralelo RMI
        Matrix p = co.multiplyParallelRMI(A, B);

        // Verificar que los resultados sean iguales
        boolean equal = true;
        for (int i = 0; i < rows && equal; i++) {
            for (int j = 0; j < cols; j++) {
                if (s.getValue(i, j) != p.getValue(i, j)) {
                    equal = false; break;
                }
            }
        }
        System.out.println("Resultado igual al secuencial: " + equal);
    }   for (int i = 0; i < rows && equal; i++) {
            for (int j = 0; j < cols; j++) {
                if (s.getValue(i, j) != p.getValue(i, j)) {
                    equal = false; break;
                }
            }
        }
        System.out.println("Resultado igual al secuencial: " + equal);
    }
}
