import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota que define los métodos accesibles remotamente
 * Debe extender Remote para indicar que puede ser invocada desde cualquier JVM
 */
public interface MiInterfazRemota extends Remote {
    
    /**
     * Método remoto que imprime un mensaje
     * @throws RemoteException si hay error en la comunicación remota
     */
    public void miMetodo1() throws RemoteException;
    
    /**
     * Método remoto que retorna un entero
     * @return valor entero
     * @throws RemoteException si hay error en la comunicación remota
     */
    public int miMetodo2() throws RemoteException;
}
