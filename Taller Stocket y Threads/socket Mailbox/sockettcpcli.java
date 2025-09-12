/** 
* ==========================================================
 *           UNIVERSIDAD JAVERIANA – INGENIERÍA DE SISTEMAS
 * ==========================================================
 * Materia:      Introducción a Sistemas Distribuidos
 * Proyecto:     Comunicación mediante Sockets TCP/UDP
 * Archivo:      socketudpser.java
 * Autor:        Tomas Ramirez - Francisco Guzman - Eduardo Valeriano - Santiago Pineda
 * Fecha:        12/09/2025
 * 
 * Clase sockettcpcli
 * ------------------
 * Implementa un cliente TCP simple que se conecta a un servidor en el puerto 6001
 * y envía mensajes introducidos por el usuario hasta que se escriba un mensaje
 * que comience con la palabra "fin".
 *
 * Uso:
 *   java sockettcpcli <direccion_servidor>
 *
 * Parámetros:
 *   argv[0] - Dirección o nombre de host del servidor al que se conectará el cliente.
 *
 * Notas:
 *   - Utiliza DataOutputStream para enviar los datos en formato UTF.
 *   - El programa termina si ocurre una excepción o si se introduce "fin".
 */

import java.net.*;
import java.io.*;

public class sockettcpcli {
   public static void main(String argv[]) {
      // Verifica que se haya pasado al menos un argumento (dirección del servidor)
      if (argv.length == 0) {
         System.err.println("java sockettcpcli servidor");
         System.exit(1); // Finaliza el programa si no hay argumentos
      }

      // BufferedReader para leer desde la entrada estándar (teclado)
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      System.out.println("Prueba de sockets TCP (cliente)");

      Socket socket;              // Objeto socket para la conexión TCP
      InetAddress address;        // Dirección IP del servidor
      byte[] mensaje_bytes = new byte[256]; 
      String mensaje="";          // Cadena para almacenar el mensaje a enviar

      try {
         // Obtiene la dirección IP del servidor a partir del nombre de host proporcionado
         System.out.print("Capturando dirección de host... ");
         address = InetAddress.getByName(argv[0]);
         System.out.println("ok");

         // Crea un socket TCP y se conecta al puerto 6001 del servidor
         System.out.print("Creando socket... ");
         socket = new Socket(address, 6001);
         System.out.println("ok");

         // Prepara un flujo de salida para enviar datos al servidor
         DataOutputStream out = new DataOutputStream(socket.getOutputStream());

         System.out.println("Introduce mensajes a enviar:");

         // Bucle de lectura y envío de mensajes
         do {
            mensaje = in.readLine(); // Lee una línea del teclado
            out.writeUTF(mensaje);   // Envía el mensaje en formato UTF al servidor
         } while (!mensaje.startsWith("fin")); // Sale si el mensaje comienza con "fin"
      }
      catch (Exception e) {
         // Manejo de cualquier excepción (resuelve errores como host no encontrado o E/S)
         System.err.println(e.getMessage());
         System.exit(1);
      }
   }
}

