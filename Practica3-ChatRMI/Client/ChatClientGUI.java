import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Cliente de chat con interfaz gráfica (GUI)
 * Implementa tanto comunicación broadcast como peer-to-peer
 */
public class ChatClientGUI extends UnicastRemoteObject implements ChatClientInterface {
    
    // Componentes de la GUI
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private JButton sendButton;
    private JButton directMessageButton;
    
    // Variables del cliente
    private String username;
    private ChatServerInterface server;
    private String serverIP;
    private int serverPort;
    
    /**
     * Constructor del cliente
     */
    public ChatClientGUI(String username, String serverIP, int serverPort) throws RemoteException {
        super(0); // Exportar en puerto anónimo
        this.username = username;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        
        initializeGUI();
        connectToServer();
    }
    
    /**
     * Inicializa la interfaz gráfica
     */
    private void initializeGUI() {
        frame = new JFrame("Chat RMI - " + username);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout(10, 10));
        
        // Panel superior con información
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("  💬 Chat Conectado: " + username, JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Área de chat (centro)
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createTitledBorder("Mensajes"));
        
        // Lista de usuarios (derecha)
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(150, 0));
        userScroll.setBorder(BorderFactory.createTitledBorder("Usuarios Online"));
        
        // Panel de entrada (abajo)
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        sendButton = new JButton("📢 Enviar a Todos");
        directMessageButton = new JButton("📨 Mensaje Directo");
        
        buttonPanel.add(sendButton);
        buttonPanel.add(directMessageButton);
        
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Agregar componentes al frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(chatScroll, BorderLayout.CENTER);
        frame.add(userScroll, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);
        
        // Event listeners
        sendButton.addActionListener(e -> sendBroadcastMessage());
        directMessageButton.addActionListener(e -> sendDirectMessage());
        
        messageField.addActionListener(e -> sendBroadcastMessage());
        
        // Cerrar ventana
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Conecta al servidor de chat
     */
    private void connectToServer() {
        try {
            appendToChat("🔄 Conectando al servidor " + serverIP + ":" + serverPort + "...\n");
            
            String url = "//" + serverIP + ":" + serverPort + "/ChatServer";
            server = (ChatServerInterface) Naming.lookup(url);
            
            boolean registered = server.registerClient(username, this);
            
            if (registered) {
                appendToChat("✅ Conectado exitosamente como: " + username + "\n");
                appendToChat("📢 Los mensajes de broadcast llegarán a todos\n");
                appendToChat("📨 Selecciona un usuario para mensajes directos\n");
                appendToChat("========================================\n\n");
                
                // Actualizar lista de usuarios
                updateUserList();
            } else {
                appendToChat("❌ Error: El nombre de usuario ya está en uso\n");
                JOptionPane.showMessageDialog(frame, 
                    "El nombre de usuario ya está en uso",
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
        } catch (Exception e) {
            appendToChat("❌ Error al conectar: " + e.getMessage() + "\n");
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                "No se pudo conectar al servidor\n" + e.getMessage(),
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    /**
     * Envía un mensaje broadcast a todos los usuarios
     */
    private void sendBroadcastMessage() {
        String message = messageField.getText().trim();
        
        if (message.isEmpty()) {
            return;
        }
        
        try {
            server.broadcastMessage(username, message);
            appendToChat("[Tú → Todos] " + message + "\n");
            messageField.setText("");
        } catch (RemoteException e) {
            appendToChat("❌ Error al enviar mensaje: " + e.getMessage() + "\n");
        }
    }
    
    /**
     * Envía un mensaje directo a un usuario específico (P2P)
     */
    private void sendDirectMessage() {
        String selectedUser = userList.getSelectedValue();
        
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(frame,
                "Selecciona un usuario de la lista",
                "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String message = messageField.getText().trim();
        
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                "Escribe un mensaje",
                "Mensaje vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Obtener referencia del cliente destino directamente
            ChatClientInterface targetClient = server.getClientReference(selectedUser);
            
            if (targetClient != null) {
                // Enviar mensaje directo (P2P - sin pasar por broadcast del servidor)
                targetClient.receiveMessage(username, message, true);
                appendToChat("[Tú → " + selectedUser + " (Directo)] " + message + "\n");
                messageField.setText("");
            } else {
                appendToChat("❌ Usuario " + selectedUser + " no está disponible\n");
            }
            
        } catch (RemoteException e) {
            appendToChat("❌ Error al enviar mensaje directo: " + e.getMessage() + "\n");
        }
    }
    
    /**
     * Actualiza la lista de usuarios conectados
     */
    private void updateUserList() {
        try {
            List<String> users = server.getOnlineUsers();
            System.out.println("DEBUG: Lista de usuarios recibida: " + users);
            SwingUtilities.invokeLater(() -> {
                userListModel.clear();
                for (String user : users) {
                    if (!user.equals(username)) {
                        System.out.println("DEBUG: Agregando usuario a la lista: " + user);
                        userListModel.addElement(user);
                    }
                }
                System.out.println("DEBUG: Lista actualizada. Total en GUI: " + userListModel.size());
            });
        } catch (RemoteException e) {
            System.err.println("DEBUG: Error al actualizar lista: " + e.getMessage());
            appendToChat("❌ Error al actualizar lista de usuarios\n");
            e.printStackTrace();
        }
    }
    
    /**
     * Desconecta del servidor
     */
    private void disconnect() {
        try {
            if (server != null) {
                server.unregisterClient(username);
            }
            appendToChat("👋 Desconectado del servidor\n");
        } catch (RemoteException e) {
            appendToChat("❌ Error al desconectar\n");
        } finally {
            System.exit(0);
        }
    }
    
    /**
     * Agrega texto al área de chat
     */
    private void appendToChat(String text) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(text);
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
    
    // ========== Implementación de ChatClientInterface ==========
    
    @Override
    public void receiveMessage(String from, String message, boolean isDirect) throws RemoteException {
        String prefix = isDirect ? "[" + from + " → Tú (Directo)] " : "[" + from + "] ";
        appendToChat(prefix + message + "\n");
    }
    
    @Override
    public void userJoined(String username) throws RemoteException {
        System.out.println("DEBUG: Notificación recibida - usuario conectado: " + username);
        appendToChat("✅ " + username + " se ha conectado\n");
        updateUserList();
    }
    
    @Override
    public void userLeft(String username) throws RemoteException {
        System.out.println("DEBUG: Notificación recibida - usuario desconectado: " + username);
        appendToChat("👋 " + username + " se ha desconectado\n");
        updateUserList();
    }
    
    @Override
    public String getUsername() throws RemoteException {
        return username;
    }
    
    /**
     * Método main - Inicia el cliente
     */
    public static void main(String[] args) {
        
        // Diálogo de configuración
        JTextField usernameField = new JTextField();
        JTextField serverField = new JTextField("192.168.100.144");
        JTextField portField = new JTextField("1099");
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Nombre de usuario:"));
        panel.add(usernameField);
        panel.add(new JLabel("IP del servidor:"));
        panel.add(serverField);
        panel.add(new JLabel("Puerto:"));
        panel.add(portField);
        
        int result = JOptionPane.showConfirmDialog(null, panel,
            "Configuración del Chat", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String serverIP = serverField.getText().trim();
            String portStr = portField.getText().trim();
            
            if (username.isEmpty() || serverIP.isEmpty() || portStr.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "Todos los campos son obligatorios",
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
            try {
                int port = Integer.parseInt(portStr);
                
                // Configurar la propiedad para RMI
                System.setProperty("java.rmi.server.hostname", 
                    java.net.InetAddress.getLocalHost().getHostAddress());
                
                new ChatClientGUI(username, serverIP, port);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                    "El puerto debe ser un número",
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Error al iniciar cliente: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.exit(0);
        }
    }
}
