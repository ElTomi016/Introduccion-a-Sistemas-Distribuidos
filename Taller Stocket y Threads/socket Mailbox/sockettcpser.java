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
 * Clase sockettcpser
 * ------------------
 * Implementa un servidor TCP muy básico que:
 *  1. Escucha en el puerto 6001.
 *  2. Acepta la conexión de un cliente.
 *  3. Recibe y muestra en consola los mensajes enviados por el cliente.
 *
 * Uso:
 *   java sockettcpser
 *
 * Notas:
 *   - Este servidor solo acepta una conexión de cliente a la vez.
 *   - Permanece en ejecución recibiendo mensajes de forma indefinida
 *     debido a que la condición del bucle es "while (1 > 0)" (bucle infinito).
 *   - Para detener el servidor se debe terminar manualmente el proceso.
 */

import java.net.*;
import java.io.*;

public class sockettcpser {
   public static void main(String argv[]) {
      System.out.println("Prueba de sockets TCP (servidor)");
      
      ServerSocket socket;  // Socket del lado servidor, escucha conexiones entrantes
      boolean fin = false;

      try {
         // Crea el ServerSocket en el puerto 6001 y queda en modo de escucha
         socket = new ServerSocket(6001);

         // Espera (bloquea) hasta que un cliente se conecte
         Socket socket_cli = socket.accept();

         // Prepara un flujo de entrada para leer datos enviados por el cliente
         DataInputStream in = new DataInputStream(socket_cli.getInputStream());

         // Bucle infinito para recibir y mostrar los mensajes
         do {
            String mensaje = "";
            mensaje = in.readUTF();  // Lee el mensaje enviado por el cliente (UTF)
            System.out.println(mensaje); // Muestra el mensaje en consola
         } while (1 > 0); // Bucle infinito: se repite para siempre

      }
      catch (Exception e) {
         // Manejo genérico de excepciones (IOException, etc.)
         System.err.println(e.getMessage());
         System.exit(1);
      }
   }
}

