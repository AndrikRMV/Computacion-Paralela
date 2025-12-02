import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase Server - Implementación del lado servidor usando Sockets
 * Esta clase crea un servidor que escucha en el puerto 1234 y acepta
 * conexiones de clientes para recibir mensajes.
 */
public class Server {
    protected ServerSocket serverSocket;
    protected Socket clientSocket;
    protected DataOutputStream outputClient;
    protected BufferedReader input;
    protected String menssage;

    /**
     * Constructor - Inicializa el ServerSocket en el puerto 1234
     */
    public Server() throws IOException {
        serverSocket = new ServerSocket(1234);
        clientSocket = new Socket();
    }

    /**
     * Método que inicia el servidor y maneja la comunicación con el cliente
     */
    public void startServer() {
        try {
            System.out.println("Esperando...");
            // Espera a que un cliente se conecte
            clientSocket = serverSocket.accept();
            System.out.println("Cliente en línea...");
            
            // Se obtiene el flujo de salida del cliente para enviarle mensajes
            outputClient = new DataOutputStream(clientSocket.getOutputStream());
            
            // Se le envía un mensaje al cliente usando su flujo de salida
            outputClient.writeUTF("Petición recibida y aceptada");
            
            // Se obtiene el flujo entrante desde el cliente
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            // Lee los mensajes del cliente hasta que la conexión se cierre
            while((menssage = input.readLine()) != null) {
                System.out.println(menssage);
            }
            
            System.out.println("Fin de la conexión");
            serverSocket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        System.out.println("Iniciando servidor...");
        server.startServer();
    }
}
