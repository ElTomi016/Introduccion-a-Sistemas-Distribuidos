/*#######################################################################################
 #* Fecha: 05/09/2025
 #* Autor: Francisco Guzman, Santiago Pineda, Tomas Roa, Eduardo Valeriano
 #* Materia: Introduccion a los Sistemas Distribuidos
 #* Tema: Cliente RMI que se conecta al servidor de biblioteca y permite realizar
 #*       prestamos, consultas y devoluciones de libros de forma remota.
######################################################################################*/
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

// ===== Interfaz (misma que el servidor) =====
// Define los metodos que el servidor expone y que el cliente puede invocar de forma remota
interface IBibliotecaRemote extends java.rmi.Remote {
    long registrarCliente(String sessionKey, String alias) throws java.rmi.RemoteException;

    RespuestaPrestamo prestamoPorIsbn(long clientId, String isbn) throws java.rmi.RemoteException;
    RespuestaPrestamo prestamoPorTitulo(long clientId, String titulo) throws java.rmi.RemoteException;
    String consulta(long clientId, String isbn) throws java.rmi.RemoteException;
    boolean devolucion(long clientId, String isbn) throws java.rmi.RemoteException;
}

// ===== DTO (objeto de transferencia de datos) =====
// Clase que transporta la respuesta del servidor hacia el cliente
class RespuestaPrestamo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private boolean aprobado;
    private String codigo;
    private String mensaje;
    private java.time.LocalDate fechaDevolucion;

    public boolean isAprobado(){ return aprobado; }
    public String getCodigo(){ return codigo; }
    public String getMensaje(){ return mensaje; }
    public java.time.LocalDate getFechaDevolucion(){ return fechaDevolucion; }

    @Override
    public String toString() {
        return "aprobado=" + aprobado +
               ", codigo=" + codigo +
               ", mensaje=" + mensaje +
               (aprobado && fechaDevolucion!=null ? (", fechaDevolucion=" + fechaDevolucion) : "");
    }
}

// === Helper para generar una clave de sesion unica por PC ===
// Se construye con el usuario y el hostname de la maquina
class SessionKey {
    static String build() {
        String user = System.getProperty("user.name", "user");
        String host = "host";
        try {
            host = System.getenv("COMPUTERNAME");
            if (host == null || host.isBlank()) host = System.getenv("HOSTNAME");
            if (host == null || host.isBlank()) {
                host = java.net.InetAddress.getLocalHost().getHostName();
            }
        } catch (Exception ignored) {}
        return (user + "@" + host).toLowerCase();
    }
}

// ===== Clase principal del cliente =====
public class Client {
    public static void main(String[] args) throws Exception {
        // Validar argumentos: host y puerto del servidor
        if (args.length < 1) {
            System.out.println("Uso: java Client <IP_O_HOST_SERVIDOR> [PUERTO_REGISTRY=1099]");
            return;
        }
        String host = args[0];
        int port = (args.length >= 2) ? Integer.parseInt(args[1]) : 1099;

        // Conectarse al registro RMI en el host y puerto indicados
        Registry reg = LocateRegistry.getRegistry(host, port);
        IBibliotecaRemote svc = (IBibliotecaRemote) reg.lookup("BibliotecaService");

        // Registrar el cliente con un alias y clave de sesion
        String alias = System.getProperty("user.name");
        String sessionKey = SessionKey.build();
        long clientId = svc.registrarCliente(sessionKey, alias);
        System.out.println("Conectado como Cliente #" + clientId);

        // Menu interactivo
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== CLIENTE BIBLIOTECA RMI ===");
            System.out.println("1) Prestamo por ISBN");
            System.out.println("2) Prestamo por Titulo");
            System.out.println("3) Consulta por ISBN");
            System.out.println("4) Devolucion por ISBN");
            System.out.println("0) Salir");
            System.out.print("Elige opcion: ");
            String op = sc.nextLine().trim();

            switch (op) {
                case "1" -> {
                    System.out.print("ISBN: ");
                    String isbn = sc.nextLine().trim();
                    RespuestaPrestamo r = svc.prestamoPorIsbn(clientId, isbn);
                    imprimir(r);
                }
                case "2" -> {
                    System.out.print("Titulo: ");
                    String titulo = sc.nextLine().trim();
                    RespuestaPrestamo r = svc.prestamoPorTitulo(clientId, titulo);
                    imprimir(r);
                    // Si el servidor devuelve multiples coincidencias, mostrar sugerencias
                    if (!r.isAprobado() && "MULTIPLE_CHOICES".equalsIgnoreCase(r.getCodigo())) {
                        System.out.println("Sugerencias del servidor: " + r.getMensaje());
                    }
                }
                case "3" -> {
                    System.out.print("ISBN: ");
                    String isbn = sc.nextLine().trim();
                    System.out.println("[CONSULTA] " + svc.consulta(clientId, isbn));
                }
                case "4" -> {
                    System.out.print("ISBN: ");
                    String isbn = sc.nextLine().trim();
                    boolean ok = svc.devolucion(clientId, isbn);
                    System.out.println(ok ? "[DEVOLUCION] OK" : "[DEVOLUCION] No se pudo devolver (ISBN inexistente o sin prestamos).");
                }
                case "0" -> { System.out.println("Saliendo..."); return; }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    // Metodo auxiliar para imprimir la respuesta de un prestamo
    private static void imprimir(RespuestaPrestamo r) {
        System.out.println("[RESPUESTA] codigo=" + r.getCodigo() + " | mensaje=" + r.getMensaje());
        if (r.isAprobado() && r.getFechaDevolucion() != null) {
            System.out.println("→ Fecha de devolucion: " + r.getFechaDevolucion() + " (una semana desde hoy)");
        }
    }
}