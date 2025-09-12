import java.net.*;
import java.io.*;

/**
 * Clase socketudpser
 * ------------------
 * Implementa un servidor que escucha mensajes UDP en el puerto 6000.
 *
 * Funcionamiento:
 *   1. Crea un DatagramSocket asociado al puerto 6000.
 *   2. Permanece en un bucle recibiendo mensajes enviados por clientes.
 *   3. Imprime los mensajes recibidos en la consola.
 *   4. Termina cuando recibe un mensaje que empieza con "fin".
 *
 * Notas:
 *   - El servidor usa un buffer de 256 bytes para cada mensaje.
 *   - No se garantiza el orden ni la entrega de mensajes (propio de UDP).
 *   - Si se recibe un mensaje parcial (por ser mayor al buffer), se cortará.
 */
public class socketudpser {
   public static void main(String argv[]) {
      System.out.println("Prueba de sockets UDP (servidor)");

      DatagramSocket socket;  // Socket para escuchar en el puerto 6000
      boolean fin = false;    // Bandera para controlar la finalización del servidor

      try {
         // Crea un socket UDP en el puerto 6000
         System.out.print("Creando socket... ");
         socket = new DatagramSocket(6000);
         System.out.println("ok");

         System.out.println("Recibiendo mensajes... ");

         // Bucle de recepción de mensajes
         do {
           byte[] mensaje_bytes = new byte[256]; // Buffer para almacenar el mensaje recibido
           DatagramPacket paquete = new DatagramPacket(mensaje_bytes, 256);

           // Espera (bloquea) hasta recibir un datagrama
           socket.receive(paquete);

           String mensaje = "";
           mensaje = new String(mensaje_bytes); // Convierte el arreglo de bytes a String
           System.out.println(mensaje);         // Muestra el mensaje en la consola

           // Si el mensaje comienza con "fin", se activa la bandera para salir del bucle
           if (mensaje.startsWith("fin")) fin = true;
         } while (!fin); // Repite mientras no se haya recibido "fin"
      }
      catch (Exception e) {
         // Manejo genérico de excepciones (IOException, SocketException, etc.)
         System.err.println(e.getMessage());
         System.exit(1);
      }
   }
}
