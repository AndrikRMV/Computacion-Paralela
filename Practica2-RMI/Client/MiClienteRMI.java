import java.rmi.Naming;

/**
 * Cliente RMI que invoca métodos remotos en el servidor
 * Obtiene una referencia al objeto remoto y ejecuta sus métodos
 */
public class MiClienteRMI {
    
    public static void main(String[] args) {
        
        if (args.length < 2) {
            System.out.println("Uso: java MiClienteRMI <IP_servidor> <puerto>");
            System.out.println("Ejemplo: java MiClienteRMI 192.168.100.144 1234");
            System.exit(1);
        }
        
        try {
            System.out.println("========================================");
            System.out.println("   CLIENTE RMI");
            System.out.println("========================================");
            System.out.println("Conectando al servidor...");
            System.out.println("IP: " + args[0]);
            System.out.println("Puerto: " + args[1]);
            System.out.println("========================================\n");
            
            // Obtener referencia al objeto remoto
            String url = "//" + args[0] + ":" + args[1] + "/PruebaRMI";
            MiInterfazRemota mir = (MiInterfazRemota) Naming.lookup(url);
            
            System.out.println("✅ Conexión establecida con el servidor");
            System.out.println("\nInvocando métodos remotos...\n");
            
            // Llamar a miMetodo2() para obtener el número de iteraciones
            int iteraciones = mir.miMetodo2();
            System.out.println("→ miMetodo2() retornó: " + iteraciones);
            System.out.println("\nInvocando miMetodo1() " + iteraciones + " veces:\n");
            
            // Invocar miMetodo1() tantas veces como devuelva miMetodo2()
            for (int i = 1; i <= iteraciones; i++) {
                System.out.print("Invocación " + i + ": ");
                mir.miMetodo1();
            }
            
            System.out.println("\n========================================");
            System.out.println("✅ Invocaciones remotas completadas");
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.err.println("\n❌ Error en el cliente RMI:");
            e.printStackTrace();
        }
    }
}
