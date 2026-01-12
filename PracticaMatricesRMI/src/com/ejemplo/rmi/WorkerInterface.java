package com.ejemplo.rmi;

import com.ejemplo.logic.Matrix;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerInterface extends Remote {
    Matrix computeBlock(Matrix aBlock, Matrix bFull) throws RemoteException;
    String getWorkerId() throws RemoteException;
}
