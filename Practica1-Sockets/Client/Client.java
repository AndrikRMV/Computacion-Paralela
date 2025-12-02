import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Clase Client - Implementación del lado cliente usando Sockets
 * Esta clase crea un cliente que se conecta a un servidor en una
 * dirección IP y puerto específicos para enviar mensajes.
 */
public class Client {
    protected Socket serverSocket;
    protected DataOutputStream outputServer;

    /**
     * Constructor - Establece conexión con el servidor
     * IMPORTANTE: Cambiar "127.0.0.1" por la IP real del servidor
     * Para obtener la IP en Mac: ifconfig | grep "inet " | grep -v 127.0.0.1
     */
    public Client() throws IOException {
        // TODO: Reemplazar 127.0.0.1 con la IP de la Mac servidor
        serverSocket = new Socket("127.0.0.1", 1234);
    }

    /**
     * Método que inicia el cliente y envía mensajes al servidor
     */
    public void startClient() {
        try {
            // Flujo de datos hacia el servidor
            outputServer = new DataOutputStream(serverSocket.getOutputStream());
            
            // Envía 5 mensajes al servidor
            for (int i = 0; i < 5; i++) {
                outputServer.writeUTF("Este es el mensaje número " + (i + 1) + "\n");
            }
            
            serverSocket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        System.out.println("Iniciando cliente...");
        client.startClient();
    }
}
