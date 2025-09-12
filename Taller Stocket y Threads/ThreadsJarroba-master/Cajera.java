/**
* ==========================================================
*           UNIVERSIDAD JAVERIANA – INGENIERÍA DE SISTEMAS
* ==========================================================
* Materia:      Introducción a Sistemas Distribuidos
* Proyecto:     Simulación de Cajeras — Concurrencia con Threads
* Archivo:      Cajera.java
* Autor:        Tomas Ramirez - Francisco Guzman - Eduardo Valeriano - Santiago Pineda
* Fecha:        12/09/2025
* 
* Clase Cajera
* ------------------
* Simula el trabajo de una cajera procesando el carro de compras de un cliente de forma secuencial.
* Mide el tiempo transcurrido desde una marca inicial (`timeStamp`) e imprime eventos de inicio/fin y por producto.
* Usa `Thread.sleep` para simular el tiempo de procesamiento de cada producto (segundos).
*
* Notas:
*   - No se modificó ninguna línea de código original; solo se añadieron comentarios.
*   - Se mantiene el paquete original (threadsJarroba), por lo que debe compilarse con `javac -d . *.java`.
*/

// Paquete del proyecto; organiza las clases bajo un namespace lógico.
package threadsJarroba;


// Declaración de la clase Cajera; define el comportamiento principal descrito en el membrete.
public class Cajera {

// Atributo/estado de la clase.
	private String nombre;

// Constructor de Cajera; inicializa el estado del objeto.
	public Cajera() {
	}

// Constructor de Cajera; inicializa el estado del objeto.
	public Cajera(String nombre) {
		this.nombre = nombre;
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
	public void procesarCompra(Cliente cliente, long timeStamp) {

// Salida por consola para observar el flujo/tiempos.
		System.out.println("La cajera " + this.nombre + 
				" COMIENZA A PROCESAR LA COMPRA DEL CLIENTE " + cliente.getNombre() + 
				" EN EL TIEMPO: " + (System.currentTimeMillis() - timeStamp) / 1000	+
				"seg");

// Bucle for: recorre elementos (por ejemplo, productos del carro).
		for (int i = 0; i < cliente.getCarroCompra().length; i++) {
			this.esperarXsegundos(cliente.getCarroCompra()[i]);
// Salida por consola para observar el flujo/tiempos.
			System.out.println("Procesado el producto " + (i + 1) + 
					" ->Tiempo: " + (System.currentTimeMillis() - timeStamp) / 1000 + 
					"seg");
		}

// Salida por consola para observar el flujo/tiempos.
		System.out.println("La cajera " + this.nombre + " HA TERMINADO DE PROCESAR " + 
							cliente.getNombre() + " EN EL TIEMPO: " + 
							(System.currentTimeMillis() - timeStamp) / 1000 + "seg");

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