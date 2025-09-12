/**
* ==========================================================
*           UNIVERSIDAD JAVERIANA – INGENIERÍA DE SISTEMAS
* ==========================================================
* Materia:      Introducción a Sistemas Distribuidos
* Proyecto:     Simulación de Cajeras — Concurrencia con Threads
* Archivo:      MainRunnable.java
* Autor:        Tomas Ramirez - Francisco Guzman - Eduardo Valeriano - Santiago Pineda
* Fecha:        12/09/2025
* 
* Clase MainRunnable
* ------------------
* Demostración de concurrencia usando la interfaz **`Runnable`**.
* Crea dos procesos (`Runnable`) que, al lanzarse en hilos distintos, ejecutan el procesamiento en paralelo.
* Cada hilo invoca `cajera.procesarCompra(cliente, initialTime)`. 
*
* Notas:
*   - No se modificó ninguna línea de código original; solo se añadieron comentarios.
*   - Se mantiene el paquete original (threadsJarroba), por lo que debe compilarse con `javac -d . *.java`.
*/

// Paquete del proyecto; organiza las clases bajo un namespace lógico.
package threadsJarroba;

// Declaración de la clase MainRunnable; define el comportamiento principal descrito en el membrete.
public class MainRunnable implements Runnable{
	
// Atributo/estado de la clase.
	private Cliente cliente;
// Atributo/estado de la clase.
	private Cajera cajera;
// Atributo/estado de la clase.
	private long initialTime;
	
// Constructor de MainRunnable; inicializa el estado del objeto.
	public MainRunnable (Cliente cliente, Cajera cajera, long initialTime){
		this.cajera = cajera;
		this.cliente = cliente;
		this.initialTime = initialTime;
	}

	public static void main(String[] args) {
		
		Cliente cliente1 = new Cliente("Cliente 1", new int[] { 2, 2, 1, 5, 2, 3 });
		Cliente cliente2 = new Cliente("Cliente 2", new int[] { 1, 3, 5, 1, 1 });
		
		Cajera cajera1 = new Cajera("Cajera 1");
		Cajera cajera2 = new Cajera("Cajera 2");
		
		// Tiempo inicial de referencia
		long initialTime = System.currentTimeMillis();
		
		Runnable proceso1 = new MainRunnable(cliente1, cajera1, initialTime);
		Runnable proceso2 = new MainRunnable(cliente2, cajera2, initialTime);
		
		new Thread(proceso1).start();
		new Thread(proceso2).start();

	}

	@Override
// Método: encapsula una operación de la clase.
	public void run() {
		this.cajera.procesarCompra(this.cliente, this.initialTime);
	}

}