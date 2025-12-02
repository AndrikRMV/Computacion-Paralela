import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación del servidor de chat RMI
 * Administra las conexiones de clientes y maneja el enrutamiento de mensajes
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    
    // Mapa thread-safe de usuarios conectados
    private Map<String, ChatClientInterface> connectedClients;
    
    /**
     * Constructor del servidor
     */
    public ChatServer() throws RemoteException {
        super();
        connectedClients = new ConcurrentHashMap<>();
        System.out.println("✅ Servidor de chat inicializado");
    }
    
    /**
     * Registra un nuevo cliente en el chat
     */
    @Override
    public synchronized boolean registerClient(String username, ChatClientInterface clientRef) 
            throws RemoteException {
        
        // Verificar si el nombre de usuario ya existe
        if (connectedClients.containsKey(username)) {
            System.out.println("❌ Intento de registro fallido: " + username + " (nombre ya existe)");
            return false;
        }
        
        // Registrar el cliente
        connectedClients.put(username, clientRef);
        System.out.println("✅ Usuario conectado: " + username + 
                          " (Total: " + connectedClients.size() + ")");
        
        // Notificar a todos los demás clientes
        notifyUserJoined(username);
        
        return true;
    }
    
    /**
     * Desregistra un cliente del chat
     */
    @Override
    public synchronized void unregisterClient(String username) throws RemoteException {
        if (connectedClients.remove(username) != null) {
            System.out.println("👋 Usuario desconectado: " + username + 
                              " (Total: " + connectedClients.size() + ")");
            
            // Notificar a todos los clientes
            notifyUserLeft(username);
        }
    }
    
    /**
     * Envía un mensaje a todos los clientes conectados
     */
    @Override
    public void broadcastMessage(String from, String message) throws RemoteException {
        System.out.println("📢 Broadcast de " + from + ": " + message);
        
        // Lista de clientes que fallaron
        List<String> failedClients = new ArrayList<>();
        
        // Enviar a todos los clientes
        for (Map.Entry<String, ChatClientInterface> entry : connectedClients.entrySet()) {
            String username = entry.getKey();
            ChatClientInterface client = entry.getValue();
            
            // No enviar el mensaje de vuelta al remitente
            if (!username.equals(from)) {
                try {
                    client.receiveMessage(from, message, false);
                } catch (RemoteException e) {
                    System.err.println("❌ Error al enviar a " + username);
                    failedClients.add(username);
                }
            }
        }
        
        // Limpiar clientes desconectados
        for (String failed : failedClients) {
            connectedClients.remove(failed);
        }
    }
    
    /**
     * Obtiene la lista de usuarios conectados
     */
    @Override
    public List<String> getOnlineUsers() throws RemoteException {
        return new ArrayList<>(connectedClients.keySet());
    }
    
    /**
     * Obtiene la referencia remota de un cliente específico
     */
    @Override
    public ChatClientInterface getClientReference(String username) throws RemoteException {
        return connectedClients.get(username);
    }
    
    /**
     * Notifica a todos los clientes que un usuario se unió
     */
    private void notifyUserJoined(String username) {
        List<String> failedClients = new ArrayList<>();
        
        for (Map.Entry<String, ChatClientInterface> entry : connectedClients.entrySet()) {
            String clientName = entry.getKey();
            ChatClientInterface client = entry.getValue();
            
            if (!clientName.equals(username)) {
                try {
                    client.userJoined(username);
                } catch (RemoteException e) {
                    failedClients.add(clientName);
                }
            }
        }
        
        // Limpiar clientes desconectados
        for (String failed : failedClients) {
            connectedClients.remove(failed);
        }
    }
    
    /**
     * Notifica a todos los clientes que un usuario se desconectó
     */
    private void notifyUserLeft(String username) {
        List<String> failedClients = new ArrayList<>();
        
        for (Map.Entry<String, ChatClientInterface> entry : connectedClients.entrySet()) {
            String clientName = entry.getKey();
            ChatClientInterface client = entry.getValue();
            
            try {
                client.userLeft(username);
            } catch (RemoteException e) {
                failedClients.add(clientName);
            }
        }
        
        // Limpiar clientes desconectados
        for (String failed : failedClients) {
            connectedClients.remove(failed);
        }
    }
    
    /**
     * Método main - Inicia el servidor de chat
     */
    public static void main(String[] args) {
        
        if (args.length < 2) {
            System.out.println("Uso: java ChatServer <IP> <puerto>");
            System.out.println("Ejemplo: java ChatServer 192.168.1.100 1099");
            System.exit(1);
        }
        
        try {
            String serverIP = args[0];
            int port = Integer.parseInt(args[1]);
            
            // Configurar la propiedad del sistema para RMI
            System.setProperty("java.rmi.server.hostname", serverIP);
            
            // Crear el registro RMI
            Registry registry = LocateRegistry.createRegistry(port);
            
            // Crear e instancia del servidor
            ChatServer server = new ChatServer();
            
            // Registrar el servidor
            registry.rebind("ChatServer", server);
            
            System.out.println("========================================");
            System.out.println("   🚀 SERVIDOR DE CHAT RMI ACTIVO");
            System.out.println("========================================");
            System.out.println("IP del Servidor: " + serverIP);
            System.out.println("Puerto: " + port);
            System.out.println("========================================");
            System.out.println("Esperando conexiones de clientes...\n");
            
        } catch (Exception e) {
            System.err.println("❌ Error en el servidor:");
            e.printStackTrace();
        }
    }
}
