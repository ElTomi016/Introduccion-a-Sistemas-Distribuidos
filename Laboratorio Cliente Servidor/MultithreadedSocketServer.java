/*#######################################################################################
 #* Fecha:29/08/2025
 #* Autor:Francisco Guzman, Santiago Pineda, Tomas Roa, Eduardo Valeriano
 #* Tema: Servidor de sockets multihilo
######################################################################################*/

import java.net.*;

/**
 * Clase que implementa un servidor de sockets multihilo.
 */
public class MultithreadedSocketServer {
  /**
   * Método principal que inicia el servidor.
   * @param args argumentos de la línea de comandos (no utilizados)
   * @throws Exception si ocurre un error al iniciar el servidor
   */
  public static void main(String[] args) throws Exception {
    try {
      // Crear un socket servidor en el puerto 8888
      ServerSocket server = new ServerSocket(8888);
      
      // Inicializar un contador para clientes
      int counter = 0;
      
      // Imprimir mensaje de inicio del servidor
      System.out.println("Server Started ....");
      
      // Bucle infinito para aceptar conexiones de clientes
      while (true) {
        // Incrementar el contador de clientes
        counter++;
        
        // Aceptar la conexión de un cliente
        Socket serverClient = server.accept();
        
        // Imprimir mensaje de conexión de cliente
        System.out.println(" >> " + "Client No:" + counter + " started!");
        
        // Crear un hilo para manejar la conexión del cliente
        ServerClientThread sct = new ServerClientThread(serverClient, counter);
        
        // Iniciar el hilo
        sct.start();
      }
    } catch (Exception e) {
      // Imprimir mensaje de error si ocurre una excepción
      System.out.println(e);
    }
  }
}