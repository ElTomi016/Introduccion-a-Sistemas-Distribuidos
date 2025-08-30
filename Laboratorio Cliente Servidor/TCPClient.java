/*#######################################################################################
 #* Fecha:29/08/2025
 #* Autor:Francisco Guzman, Santiago Pineda, Tomas Roa, Eduardo Valeriano
 #* Tema: Cliente TCP
######################################################################################*/



import java.io.*;
import java.net.*;

/**
 * Clase que implementa un cliente de sockets TCP.
 */
public class TCPClient {
  /**
   * Método principal que inicia el cliente.
   * @param args argumentos de la línea de comandos (no utilizados)
   * @throws Exception si ocurre un error al iniciar el cliente
   */
  public static void main(String[] args) throws Exception {
    try {
      // Crear un socket cliente que se conecte al servidor en localhost:8888
      Socket socket = new Socket("127.0.0.1", 8888);
      
      // Crear flujos de entrada y salida para el socket
      DataInputStream inStream = new DataInputStream(socket.getInputStream());
      DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
      
      // Crear un lector de línea para leer la entrada del usuario
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      
      // Variables para almacenar los mensajes del cliente y del servidor
      String clientMessage = "", serverMessage = "";
      
      // Bucle para enviar y recibir mensajes hasta que el usuario escriba "bye"
      while (!clientMessage.equals("bye")) {
        // Pedir al usuario que ingrese un número
        System.out.println("Enter number :");
        
        // Leer el número ingresado por el usuario
        clientMessage = br.readLine();
        
        // Enviar el número al servidor
        outStream.writeUTF(clientMessage);
        
        // Enviar un mensaje de confirmación al servidor
        outStream.flush();
        
        // Leer la respuesta del servidor
        serverMessage = inStream.readUTF();
        
        // Imprimir la respuesta del servidor
        System.out.println(serverMessage);
      }
      
      // Cerrar los flujos y el socket
      outStream.close();
      inStream.close();
      socket.close();
    } catch (Exception e) {
      // Imprimir mensaje de error si ocurre una excepción
      System.out.println(e);
    }
  }
}