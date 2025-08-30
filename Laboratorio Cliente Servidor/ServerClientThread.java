/*#######################################################################################
 #* Fecha:29/08/2025
 #* Autor:Francisco Guzman, Santiago Pineda, Tomas Roa, Eduardo Valeriano
 #* Tema: Hilo servidor por cliente
######################################################################################*/


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Clase que maneja la comunicación entre el servidor y un cliente específico.
 * Extiende de Thread para que cada cliente sea atendido de manera concurrente.
 */
class ServerClientThread extends Thread {
  Socket serverClient;  // Socket que conecta con el cliente
  int clientNo;         // Número o identificador del cliente
  int squre;            // Variable para almacenar el cuadrado de un número

  /**
   * Constructor: inicializa el socket del cliente y su identificador.
   * @param inSocket socket correspondiente al cliente
   * @param counter número asignado al cliente
   */
  ServerClientThread(Socket inSocket, int counter) {
    serverClient = inSocket;
    clientNo = counter;
  }

  /**
   * Método principal que se ejecuta al iniciar el hilo.
   * Escucha mensajes del cliente, calcula el cuadrado de números
   * y responde con el resultado hasta que el cliente escriba "bye".
   */
  public void run() {
    try {
      // Flujo de entrada para recibir datos del cliente
      DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
      // Flujo de salida para enviar datos al cliente
      DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());

      String clientMessage = "", serverMessage = "";

      // Bucle hasta que el cliente envíe "bye"
      while (!clientMessage.equals("bye")) {
        // Leer el mensaje enviado por el cliente
        clientMessage = inStream.readUTF();
        System.out.println("From Client-" + clientNo + ": Number is :" + clientMessage);

        // Calcular el cuadrado del número recibido
        squre = Integer.parseInt(clientMessage) * Integer.parseInt(clientMessage);

        // Preparar mensaje de respuesta para el cliente
        serverMessage = "From Server to Client-" + clientNo + " Square of " 
                        + clientMessage + " is " + squre;

        // Enviar la respuesta al cliente
        outStream.writeUTF(serverMessage);
        outStream.flush();
      }

      // Cerrar los flujos y el socket cuando el cliente termine
      inStream.close();
      outStream.close();
      serverClient.close();
    } catch (Exception ex) {
      // Captura de errores durante la comunicación
      System.out.println(ex);
    } finally {
      // Mensaje al cerrar la conexión del cliente
      System.out.println("Client -" + clientNo + " exit!! ");
    }
  }
}
