# 🧩 Taller: Sockets y Threads en Java

Este repositorio contiene dos proyectos desarrollados en entity["software","Java",1] que abordan conceptos fundamentales de **comunicación en red mediante sockets** y **programación concurrente mediante hilos**.  

El objetivo es comprender el funcionamiento de los protocolos UDP y TCP, así como comparar la ejecución **secuencial** y **paralela** con `Thread` y `Runnable`.

---

## 📡 Proyecto 1 — Comunicación con Sockets (TCP y UDP)

Este proyecto implementa comunicación **cliente-servidor** utilizando sockets, permitiendo enviar y recibir mensajes a través de la red mediante TCP (orientado a conexión) y UDP (no orientado a conexión).

### 📝 Archivos

- `sockettcpser.java` → Servidor TCP (usa `ServerSocket` y `Socket`)
- `sockettcpcli.java` → Cliente TCP
- `socketudpser.java` → Servidor UDP (usa `DatagramSocket` y `DatagramPacket`)
- `socketudpcli.java` → Cliente UDP

### ⚙️ Ejecución

```bash
# Compilar todos los archivos .java
javac -d . *.java

# Ejecutar el servidor TCP
java sockets.sockettcpser

# Ejecutar el cliente TCP
java sockets.sockettcpcli

# Ejecutar el servidor UDP
java sockets.socketudpser

# Ejecutar el cliente UDP
java sockets.socketudpcli
```

---

## ⚡ Proyecto 2 — Concurrencia con Threads

Este proyecto simula un sistema de atención de clientes para analizar el impacto de la concurrencia, comparando la ejecución **secuencial** con la **ejecución en paralelo** usando hilos.

### 📝 Archivos

- `Cliente.java` → Define los datos de cada cliente
- `Cajera.java` → Atiende clientes secuencialmente
- `CajeraThread.java` → Atiende clientes en paralelo (extiende `Thread`)
- `Main.java` → Ejecuta el proceso de forma secuencial
- `MainThread.java` → Ejecuta el proceso de forma concurrente con `Thread`
- `MainRunnable.java` → Ejecuta el proceso de forma concurrente con `Runnable`

### ⚙️ Ejecución

```bash
# Compilar todos los archivos .java
javac -d . *.java

# Ejecutar la versión secuencial
java threadsJarroba.Main

# Ejecutar la versión concurrente usando Thread
java threadsJarroba.MainThread

# Ejecutar la versión concurrente usando Runnable
java threadsJarroba.MainRunnable
```

---

## 📌 Contenido del Informe

El informe incluye:

- Resumen y abstract en español e inglés  
- Introducción
- Objetivos generales y específicos  
- Descripción técnica de ambos proyectos  
- Tablas comparativas de archivos y estructuras  
- Evidencia de ejecución (capturas de pantalla)  
- Conclusiones generales del taller  

📄 [Ver informe completo en PDF](./DistribuidosTaller.pdf)

---

