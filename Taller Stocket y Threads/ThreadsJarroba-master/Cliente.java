/**
* ==========================================================
*           UNIVERSIDAD JAVERIANA – INGENIERÍA DE SISTEMAS
* ==========================================================
* Materia:      Introducción a Sistemas Distribuidos
* Proyecto:     Simulación de Cajeras — Concurrencia con Threads
* Archivo:      MainThread.java
* Autor:        Tomas Ramirez - Francisco Guzman - Eduardo Valeriano - Santiago Pineda
* Fecha:        12/09/2025
* 
* Clase MainThread
* ------------------
* Demostración de concurrencia usando **herencia de `Thread`**.
* Crea dos instancias de `CajeraThread` y las ejecuta en paralelo con `start()`.
* Facilita la comparación entre `Thread` y `Runnable`. 
*
* Notas:
*   - No se modificó ninguna línea de código original; solo se añadieron comentarios.
*   - Se mantiene el paquete original (threadsJarroba), por lo que debe compilarse con `javac -d . *.java`.
*/

// Paquete del proyecto; organiza las clases bajo un namespace lógico.
package threadsJarroba;


// Declaración de la clase MainThread; define el comportamiento principal descrito en el membrete.
public class MainThread {

	public static void main(String[] args) {

		Cliente cliente1 = new Cliente("Cliente 1", new int[] { 2, 2, 1, 5, 2, 3 });
		Cliente cliente2 = new Cliente("Cliente 2", new int[] { 1, 3, 5, 1, 1 });

		// Tiempo inicial de referencia
		long initialTime = System.currentTimeMillis();
		CajeraThread cajera1 = new CajeraThread("Cajera 1", cliente1, initialTime);
		CajeraThread cajera2 = new CajeraThread("Cajera 2", cliente2, initialTime);

		cajera1.start();
		cajera2.start();
	}
}