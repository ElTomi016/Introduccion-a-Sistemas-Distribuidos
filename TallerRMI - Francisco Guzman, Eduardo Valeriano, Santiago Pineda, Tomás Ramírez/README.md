# 📚 Taller RMI 

Video de Sustentacion: https://youtu.be/ABrUWI_hQak

## 🔹 Introduccion
Este proyecto corresponde al **Taller de Sistemas Distribuidos**, donde se implementa un servicio remoto de **biblioteca** utilizando **RMI (Remote Method Invocation)** en Java.  

El sistema permite **gestionar prestamos, consultas y devoluciones de libros de manera remota**, simulando un escenario real donde un servidor central administra la base de datos de libros y varios clientes pueden conectarse desde diferentes maquinas.

## 🎯 Objetivo General
Implementar un sistema distribuido con RMI que permita administrar de forma remota la disponibilidad y el prestamo de libros en una biblioteca simulada.

## 📝 Objetivos Especificos
- Diseñar la **interfaz remota** con los metodos necesarios para la gestion de libros.  
- Implementar un **servidor** que exponga los servicios definidos y maneje la informacion de la biblioteca (basada en archivos CSV).  
- Desarrollar uno o varios **clientes** que consuman estos servicios desde diferentes maquinas.  
- Validar el funcionamiento del sistema probando el servidor y los clientes en maquinas distintas.  
- Documentar y evidenciar el uso correcto de todas las operaciones.  

---

## 📂 Estructura del Proyecto
```
TallerRMI/
│
├── Client/
│   ├── Client.java
│   ├── Client.class
│   ├── IBibliotecaRemote.class
│   ├── RespuestaPrestamo.class
│   ├── SessionKey.class
│
├── Server/
│   ├── Server.java
│   ├── Server.class
│   ├── BibliotecaRemoteImpl.class
│   ├── IBibliotecaRemote.class
│   ├── RespuestaPrestamo.class
│   │
│   ├── dat/
│   │   └── libros.csv
│   └── data/
│       └── libros.csv
│
└── README.md
```

### 🔹 Archivos del Cliente
- **Client.java**: Programa principal del cliente. Ofrece un menu interactivo para solicitar prestamos, consultas y devoluciones al servidor.  
- **SessionKey.class**: Genera una clave unica por maquina/usuario para identificar sesiones de cliente en el servidor.  
- **IBibliotecaRemote.class**: Interfaz remota compartida con el servidor, necesaria para invocar metodos.  
- **RespuestaPrestamo.class**: Objeto de transferencia que encapsula la respuesta de operaciones de prestamo.  

### 🔹 Archivos del Servidor
- **Server.java**: Programa principal del servidor. Inicia el registro RMI en el puerto 1099 y publica el servicio `BibliotecaService`.  
- **BibliotecaRemoteImpl.class**: Implementacion de la interfaz remota. Gestiona los libros, realiza prestamos, consultas y devoluciones, y persiste cambios en el archivo CSV.  
- **IBibliotecaRemote.class**: Interfaz compartida con los clientes.  
- **RespuestaPrestamo.class**: DTO serializable para respuestas de prestamos.  

### 🔹 Archivos de Datos
- **libros.csv**: Base de datos simple de libros en formato CSV. Contiene columnas `isbn, titulo, total, prestados`.
```

📌 **Nota:** El archivo `libros.csv` contiene la “base de datos” de libros en formato simple:  
```
isbn,titulo,total,prestados
9780001,Algoritmos,3,1
9780002,Sistemas Distribuidos,2,0
9780003,Redes,1,0
```

---

## ⚙️ Funcionamiento del Sistema
### 🔹 Servidor
- Mantiene un **registro de clientes** por sesion.  
- Carga y actualiza la informacion de los libros desde el archivo `libros.csv`.  
- Expone metodos remotos para **consulta, prestamo y devolucion**.  
- Loguea todas las acciones con hora, id de cliente y resultado.  

### 🔹 Cliente
- Se conecta al servidor RMI especificando **host** y **puerto**.  
- Registra al usuario y obtiene un **ID unico**.  
- Ofrece un menu interactivo:  
  1. Prestamo por ISBN  
  2. Prestamo por Titulo  
  3. Consulta por ISBN  
  4. Devolucion por ISBN  
  0. Salir  

---

## 🖥️ Compilacion y Ejecucion

### 1️⃣ Compilar el proyecto
Desde la carpeta raiz `TallerRMI`:

```bash
# Compilar servidor
javac Server/Server.java

# Compilar cliente
javac Client/Client.java
```

### 2️⃣ Iniciar el servidor
En la maquina que actuara como servidor:
```bash
cd Server
java Server
```
👉 Esto crea el **Registry RMI en el puerto 1099** y publica el servicio `BibliotecaService`.

### 3️⃣ Conectar un cliente
En la maquina cliente:
```bash
cd Client
java Client <IP_SERVIDOR> [PUERTO]
```

Ejemplo:
```bash
java Client 127.0.0.1 1099
```

---

## ✅ Ejemplo de Uso
```
=== CLIENTE BIBLIOTECA RMI ===
1) Prestamo por ISBN
2) Prestamo por Titulo
3) Consulta por ISBN
4) Devolucion por ISBN
0) Salir
Elige opcion: 1
ISBN: 9780002
[RESPUESTA] codigo=OK | mensaje=Prestamo aprobado
→ Fecha de devolucion: 2025-09-12 (una semana desde hoy)
```

---

## 📌 Conclusiones
- Este taller busca **familiarizarse con RMI** y los retos de la comunicacion entre procesos distribuidos.  
- Se recomienda **probar en al menos dos maquinas distintas** para comprobar la distribucion real.  
- El sistema puede extenderse facilmente agregando mas operaciones o conectando a una base de datos real.  

---

👨‍💻 **Autores:**  
Francisco Guzman, Santiago Pineda, Tomas Roa, Eduardo Valeriano  
Pontificia Universidad Javeriana – 2025
