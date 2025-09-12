/**
* ==========================================================
*           UNIVERSIDAD JAVERIANA – INGENIERÍA DE SISTEMAS
* ==========================================================
* Materia:      Introducción a Sistemas Distribuidos
* Proyecto:     Simulación de Cajeras — Concurrencia con Threads
* Archivo:      Main.java
* Autor:        Tomas Ramirez - Francisco Guzman - Eduardo Valeriano - Santiago Pineda
* Fecha:        12/09/2025
* 
* Clase Main
* ------------------
* Punto de entrada **secuencial**: crea dos clientes y dos cajeras.
* Procesa las compras **una detrás de otra** en el mismo hilo principal.
* Permite contrastar luego con las versiones concurrentes (`MainThread`, `MainRunnable`).
*
* Notas:
*   - No se modificó ninguna línea de código original; solo se añadieron comentarios.
*   - Se mantiene el paquete original (threadsJarroba), por lo que debe compilarse con `javac -d . *.java`.
*/

// Paquete del proyecto; organiza las clases bajo un namespace lógico.
package threadsJarroba;

// Declaración de la clase Main; define el comportamiento principal descrito en el membrete.
public class Main {

	public static void main(String[] args) {

		Cliente cliente1 = new Cliente("Cliente 1", new int[] { 2, 2, 1, 5, 2, 3 });
		Cliente cliente2 = new Cliente("Cliente 2", new int[] { 1, 3, 5, 1, 1 });

		Cajera cajera1 = new Cajera("Cajera 1");
		Cajera cajera2 = new Cajera("Cajera 2");

		// Tiempo inicial de referencia
		long initialTime = System.currentTimeMillis();

		cajera1.procesarCompra(cliente1, initialTime);
		cajera2.procesarCompra(cliente2, initialTime);
	}
}