import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota del cliente de chat
 * Define los métodos que el cliente expone para recibir mensajes
 */
public interface ChatClientInterface extends Remote {
    
    /**
     * Recibe un mensaje del servidor (broadcast) o de otro cliente (P2P)
     * @param from Usuario que envía el mensaje
     * @param message Contenido del mensaje
     * @param isDirect true si es mensaje directo, false si es broadcast
     */
    void receiveMessage(String from, String message, boolean isDirect) throws RemoteException;
    
    /**
     * Notifica que un usuario se ha conectado
     * @param username Nombre del usuario que se conectó
     */
    void userJoined(String username) throws RemoteException;
    
    /**
     * Notifica que un usuario se ha desconectado
     * @param username Nombre del usuario que se desconectó
     */
    void userLeft(String username) throws RemoteException;
    
    /**
     * Obtiene el nombre de usuario del cliente
     * @return Nombre de usuario
     */
    String getUsername() throws RemoteException;
}
