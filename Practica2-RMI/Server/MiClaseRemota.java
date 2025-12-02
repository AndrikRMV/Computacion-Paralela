import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Implementación del objeto remoto que será accesible por los clientes
 * Extiende UnicastRemoteObject para habilitar la funcionalidad RMI
 */
public class MiClaseRemota extends UnicastRemoteObject implements MiInterfazRemota {
    
    /**
     * Constructor de la clase remota
     * @throws RemoteException si hay error al crear el objeto remoto
     */
    public MiClaseRemota() throws RemoteException {
        super();
        System.out.println("Objeto remoto creado exitosamente");
    }
    
    /**
     * Implementación de miMetodo1 - Imprime mensaje en el servidor
     */
    public void miMetodo1() throws RemoteException {
        System.out.println("Estoy en miMetodo1()");
    }
    
    /**
     * Implementación de miMetodo2 - Retorna un valor entero
     */
    public int miMetodo2() throws RemoteException {
        return 5;
    }
    
    /**
     * Método local (no remoto) - Solo accesible en el servidor
     */
    public void otroMetodo() {
        System.out.println("Este es un método local, no remoto");
    }
    
    /**
     * Método main - Inicia el servidor RMI
     * @param args[0] Puerto donde se ejecutará el registro RMI
     */
    public static void main(String[] args) {
        
        if (args.length < 1) {
            System.out.println("Uso: java MiClaseRemota <puerto>");
            System.out.println("Ejemplo: java MiClaseRemota 1234");
            System.exit(1);
        }
        
        try {
            // Crear el registro RMI en el puerto especificado
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
            
            // Crear instancia del objeto remoto
            MiInterfazRemota mir = new MiClaseRemota();
            
            // Obtener IP del servidor
            String serverIP = java.net.InetAddress.getLocalHost().getHostAddress();
            
            // Registrar el objeto remoto con un nombre
            String url = "//" + serverIP + ":" + args[0] + "/PruebaRMI";
            java.rmi.Naming.rebind(url, mir);
            
            System.out.println("========================================");
            System.out.println("   SERVIDOR RMI INICIADO");
            System.out.println("========================================");
            System.out.println("IP del Servidor: " + serverIP);
            System.out.println("Puerto: " + args[0]);
            System.out.println("URL: " + url);
            System.out.println("========================================");
            System.out.println("Esperando invocaciones de clientes...\n");
            
        } catch (Exception e) {
            System.err.println("Error en el servidor RMI:");
            e.printStackTrace();
        }
    }
}
