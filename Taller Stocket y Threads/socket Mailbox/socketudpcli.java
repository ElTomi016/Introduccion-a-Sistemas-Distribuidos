import java.net.*;
import java.io.*;

/**
 * Clase socketudpcli
 * ------------------
 * Implementa un cliente que envía mensajes a través de UDP a un servidor.
 *
 * Uso:
 *   java socketudpcli <direccion_servidor>
 *
 * Funcionamiento:
 *   1. Crea un socket UDP sin asociarlo a un puerto específico (el sistema asigna uno disponible).
 *   2. Captura la dirección IP del servidor desde el argumento.
 *   3. Lee mensajes desde el teclado y los envía en datagramas (paquetes) al puerto 6000 del servidor.
 *   4. El envío se repite hasta que el mensaje empiece con "fin".
 *
 * Notas:
 *   - Usa DatagramPacket para encapsular los mensajes.
 *   - No hay confirmación de entrega (propio de UDP).
 *   - El tamaño del buffer es 256 bytes (puede ajustarse para mensajes más largos).
 */
public class socketudpcli {
   public static void main(String argv[]) {
      // Verifica que se haya pasado la dirección del servidor como argumento
      if (argv.length == 0) {
         System.err.println("Java socketudpcli servidor");
         System.exit(1);
      }

      // Lector para capturar mensajes desde el teclado
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      System.out.println("Prueba de sockets UDP (cliente)");

      DatagramSocket socket;      // Socket para enviar datagramas
      InetAddress address;        // Dirección IP del servidor
      byte[] mensaje_bytes = new byte[256]; // Buffer de bytes para el mensaje
      String mensaje="";          // Almacena el mensaje en formato String
      DatagramPacket paquete;     // Objeto que encapsula el mensaje y destino

      // Inicializa mensaje_bytes con una cadena vacía (no es estrictamente necesario aquí)
      mensaje_bytes = mensaje.getBytes();

      try {
         // Crea un socket UDP (puerto asignado dinámicamente)
         System.out.print("Creando socket... ");
         socket = new DatagramSocket();
         System.out.println("ok");

         // Obtiene la dirección del host (servidor) a partir del nombre pasado por parámetro
         System.out.print("Capturando dirección de host... ");
         address = InetAddress.getByName(argv[0]);
         System.out.println("ok");

         System.out.println("Introduce mensajes a enviar:");

         // Bucle de lectura de mensajes y envío de datagramas
         do {
            mensaje = in.readLine(); // Captura texto desde el teclado
            mensaje_bytes = mensaje.getBytes(); // Convierte String a bytes
            // Crea un datagrama con el mensaje, su longitud, la dirección y el puerto de destino
            paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, 6000);
            socket.send(paquete); // Envía el paquete
         } while (!mensaje.startsWith("fin")); // Se detiene si el mensaje comienza con "fin"
      }
      catch (Exception e) {
         // Manejo genérico de excepciones (UnknownHostException, IOException, etc.)
         System.err.println(e.getMessage());
         System.exit(1);
      }
   }
}
