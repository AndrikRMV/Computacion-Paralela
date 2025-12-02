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
    
    // Cola de mensajes pendientes por usuario: Map<username, List<mensaje>>
    private Map<String, List<String>> pendingMessages;
    
    /**
     * Constructor del servidor
     */
    public ChatServer() throws RemoteException {
        super();
        connectedClients = new ConcurrentHashMap<>();
        pendingMessages = new ConcurrentHashMap<>();
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
        pendingMessages.putIfAbsent(username, new ArrayList<>());
        
        System.out.println("✅ Usuario conectado: " + username + 
                          " (Total: " + connectedClients.size() + ")");
        
        // Notificar a todos los demás clientes (encolando mensaje)
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
     * Envía un mensaje a todos los clientes conectados (guardando en cola)
     */
    @Override
    public void broadcastMessage(String from, String message) throws RemoteException {
        System.out.println("📢 Broadcast de " + from + ": " + message);
        
        String fullMessage = "[BROADCAST] " + from + ": " + message;
        
        // Agregar mensaje a la cola de TODOS los clientes
        for (String username : connectedClients.keySet()) {
            pendingMessages.computeIfAbsent(username, k -> new ArrayList<>()).add(fullMessage);
        }
        
        System.out.println("✅ Mensaje encolado para " + connectedClients.size() + " usuarios");
    }
    
    /**
     * Envía un mensaje directo a un usuario específico (via cola)
     */
    @Override
    public void sendDirectMessage(String from, String to, String message) throws RemoteException {
        System.out.println("💌 Mensaje directo de " + from + " → " + to + ": " + message);
        
        // Verificar que el destinatario existe
        if (!connectedClients.containsKey(to)) {
            throw new RemoteException("Usuario " + to + " no está conectado");
        }
        
        // Encolar mensaje para el destinatario
        String messageForRecipient = "[DIRECTO de " + from + "] " + message;
        pendingMessages.computeIfAbsent(to, k -> new ArrayList<>()).add(messageForRecipient);
        
        // Encolar confirmación para el remitente
        String confirmationForSender = "[Tú → " + to + " (Directo)] " + message;
        pendingMessages.computeIfAbsent(from, k -> new ArrayList<>()).add(confirmationForSender);
        
        System.out.println("✅ Mensaje directo encolado");
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
     * Notifica a todos los clientes que un usuario se unió (encolando mensaje)
     */
    private void notifyUserJoined(String username) {
        String systemMessage = "[SISTEMA] " + username + " se ha unido al chat";
        
        // Encolar notificación para todos EXCEPTO el que se unió
        for (String clientName : connectedClients.keySet()) {
            if (!clientName.equals(username)) {
                pendingMessages.computeIfAbsent(clientName, k -> new ArrayList<>()).add(systemMessage);
            }
        }
        
        System.out.println("📢 Notificación de ingreso encolada para " + (connectedClients.size() - 1) + " usuarios");
    }
    
    /**
     * Notifica a todos los clientes que un usuario se desconectó (encolando mensaje)
     */
    private void notifyUserLeft(String username) {
        String systemMessage = "[SISTEMA] " + username + " ha salido del chat";
        
        // Encolar notificación para todos los clientes restantes
        for (String clientName : connectedClients.keySet()) {
            pendingMessages.computeIfAbsent(clientName, k -> new ArrayList<>()).add(systemMessage);
        }
        
        // Eliminar cola de mensajes del usuario desconectado
        pendingMessages.remove(username);
        
        System.out.println("📢 Notificación de salida encolada para " + connectedClients.size() + " usuarios");
    }
    
    /**
     * Obtiene y limpia los mensajes pendientes para un usuario
     */
    @Override
    public synchronized Map<String, List<String>> getPendingMessages(String username) throws RemoteException {
        Map<String, List<String>> result = new HashMap<>();
        
        // Obtener mensajes pendientes
        List<String> messages = pendingMessages.getOrDefault(username, new ArrayList<>());
        
        if (!messages.isEmpty()) {
            result.put("messages", new ArrayList<>(messages));
            // Limpiar mensajes entregados
            pendingMessages.put(username, new ArrayList<>());
            System.out.println("📬 Entregados " + messages.size() + " mensajes a " + username);
        } else {
            result.put("messages", new ArrayList<>());
        }
        
        return result;
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
