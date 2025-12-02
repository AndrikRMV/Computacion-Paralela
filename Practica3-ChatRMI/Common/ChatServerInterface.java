import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaz remota del servidor de chat
 * Define los métodos que el servidor expone a los clientes
 */
public interface ChatServerInterface extends Remote {
    
    /**
     * Registra un nuevo cliente en el servidor
     * @param username Nombre de usuario del cliente
     * @param clientRef Referencia remota al cliente para callbacks
     * @return true si el registro fue exitoso, false si el nombre ya existe
     */
    boolean registerClient(String username, ChatClientInterface clientRef) throws RemoteException;
    
    /**
     * Desregistra un cliente del servidor
     * @param username Nombre de usuario a desregistrar
     */
    void unregisterClient(String username) throws RemoteException;
    
    /**
     * Envía un mensaje broadcast a todos los clientes conectados
     * @param from Usuario que envía el mensaje
     * @param message Contenido del mensaje
     */
    void broadcastMessage(String from, String message) throws RemoteException;
    
    /**
     * Obtiene la lista de usuarios conectados
     * @return Lista de nombres de usuarios activos
     */
    List<String> getOnlineUsers() throws RemoteException;
    
    /**
     * Obtiene la referencia remota de un cliente específico
     * @param username Nombre del usuario
     * @return Referencia al cliente o null si no existe
     */
    ChatClientInterface getClientReference(String username) throws RemoteException;
}
