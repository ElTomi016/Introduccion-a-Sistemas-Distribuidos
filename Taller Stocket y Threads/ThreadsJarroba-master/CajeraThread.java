/**
* ==========================================================
*           UNIVERSIDAD JAVERIANA – INGENIERÍA DE SISTEMAS
* ==========================================================
* Materia:      Introducción a Sistemas Distribuidos
* Proyecto:     Simulación de Cajeras — Concurrencia con Threads
* Archivo:      CajeraThread.java
* Autor:        Tomas Ramirez - Francisco Guzman - Eduardo Valeriano - Santiago Pineda
* Fecha:        12/09/2025
* 
* Clase CajeraThread
* ------------------
* Variante de cajera que **extiende `Thread`**.
* Recibe una referencia a un `Cliente` y una marca de tiempo inicial para medir duración relativa.
* La lógica de procesamiento se ejecuta dentro de `run()`, permitiendo concurrencia al hacer `start()`. 
*
* Notas:
*   - No se modificó ninguna línea de código original; solo se añadieron comentarios.
*   - Se mantiene el paquete original (threadsJarroba), por lo que debe compilarse con `javac -d . *.java`.
*/

// Paquete del proyecto; organiza las clases bajo un namespace lógico.
package threadsJarroba;


// Declaración de la clase CajeraThread; define el comportamiento principal descrito en el membrete.
public class CajeraThread extends Thread {

// Atributo/estado de la clase.
	private String nombre;

// Atributo/estado de la clase.
	private Cliente cliente;

// Atributo/estado de la clase.
	private long initialTime;


// Constructor de CajeraThread; inicializa el estado del objeto.
	public CajeraThread() {
	}

// Constructor de CajeraThread; inicializa el estado del objeto.
	public CajeraThread(String nombre, Cliente cliente, long initialTime) {
		this.nombre = nombre;
		this.cliente = cliente;
		this.initialTime = initialTime;
	}

// Método: encapsula una operación de la clase.
	public String getNombre() {
// Retorno del método con el valor especificado.
		return nombre;
	}

// Método: encapsula una operación de la clase.
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

// Método: encapsula una operación de la clase.
	public long getInitialTime() {
// Retorno del método con el valor especificado.
		return initialTime;
	}

// Método: encapsula una operación de la clase.
	public void setInitialTime(long initialTime) {
		this.initialTime = initialTime;
	}

// Método: encapsula una operación de la clase.
	public Cliente getCliente() {
// Retorno del método con el valor especificado.
		return cliente;
	}

// Método: encapsula una operación de la clase.
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
// Método: encapsula una operación de la clase.
	public void run() {

// Salida por consola para observar el flujo/tiempos.
		System.out.println("La cajera " + this.nombre + " COMIENZA A PROCESAR LA COMPRA DEL CLIENTE " 
					+ this.cliente.getNombre() + " EN EL TIEMPO: " 
					+ (System.currentTimeMillis() - this.initialTime) / 1000 
					+ "seg");

// Bucle for: recorre elementos (por ejemplo, productos del carro).
		for (int i = 0; i < this.cliente.getCarroCompra().length; i++) {
			// Se procesa el pedido en X segundos
			this.esperarXsegundos(cliente.getCarroCompra()[i]);
// Salida por consola para observar el flujo/tiempos.
			System.out.println("Procesado el producto " + (i + 1) 
						+ " del cliente " + this.cliente.getNombre() + "->Tiempo: " 
						+ (System.currentTimeMillis() - this.initialTime) / 1000 
						+ "seg");
		}

// Salida por consola para observar el flujo/tiempos.
		System.out.println("La cajera " + this.nombre + " HA TERMINADO DE PROCESAR " 
						+ this.cliente.getNombre() + " EN EL TIEMPO: " 
						+ (System.currentTimeMillis() - this.initialTime) / 1000 
						+ "seg");
	}

// Método: encapsula una operación de la clase.
	private void esperarXsegundos(int segundos) {
// Bloque try: código que podría generar excepciones.
		try {
// Pausa el hilo actual para simular tiempo de procesamiento.
			Thread.sleep(segundos * 1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}