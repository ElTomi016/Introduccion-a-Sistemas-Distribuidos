# Cliente-Servidor con Sockets Multihilo en Java  

Este proyecto implementa un sistema **Cliente-Servidor multihilo** en Java utilizando **sockets TCP**.  
Cada cliente puede enviar un número al servidor, y este le responde con el **cuadrado del número ingresado**.  

## Características  
- Soporte para múltiples clientes en paralelo (multithreading).  
- Comunicación en tiempo real entre clientes y servidor.  
- Cálculo del cuadrado de un número enviado desde el cliente.  
- Mensajes personalizados indicando qué cliente envió la información.  

---

## Archivos principales  
- `MultithreadedSocketServer.java` → Código del servidor principal.  
- `ServerClientThread.java` → Manejo de cada cliente en un hilo separado.  
- `TCPClient.java` → Código del cliente que envía números al servidor.  

---

## Ejecución  

1. Compilar los archivos Java:  
   ```bash
   javac MultithreadedSocketServer.java ServerClientThread.java TCPClient.java
   ```

2. Iniciar el servidor:  
   ```bash
   java MultithreadedSocketServer
   ```

3. Ejecutar uno o varios clientes en diferentes terminales:  
   ```bash
   java TCPClient
   ```

4. Ingresar números y observar la comunicación con el servidor.  


---

##  Informe breve  

- El servidor inicia y queda a la espera de conexiones.  
- Los clientes se conectan correctamente y pueden enviar números.  
- El servidor procesa cada número en **hilos independientes** y devuelve el resultado.  
- Las pruebas muestran que con varios clientes simultáneos el sistema responde de manera estable y eficiente.  

---

##  Autor  
Proyecto desarrollado como práctica de **Redes en Java**.  
