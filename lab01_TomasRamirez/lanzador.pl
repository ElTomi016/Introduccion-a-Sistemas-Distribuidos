#!/usr/bin/perl
#**************************************************************
#         		Pontificia Universidad Javeriana
#     Autor: J. Corredor
#     Fecha: Febrero 2024
#     Materia: Sistemas Operativos
#     Tema: Taller de Evaluación de Rendimiento
#     Fichero: script automatización ejecución por lotes 
#****************************************************************/

$Path = `pwd`;
chomp($Path);

# nombre del ejecutable
$Nombre_Ejecutable = "mmClasicaOpenMP.c";

# lista de tamaños de matrices
@Size_Matriz = ("240","320", "400", "480", "560", "640", "720", "800", "880", "1040", "1120");
#numero de hilos a realizar el laboratorios
@Num_Hilos = (1,4,6,8,16,20);
$Repeticiones = 30;

#recorre el tamaño de las matrices
foreach $size (@Size_Matriz){
	#recorre el tamaño de la cantidad de hilos
	foreach $hilo (@Num_Hilos) {
		#guarda los resultados de la ejecucion de ese momento el la carpeta que tenemos el ejecutable
		$file = "$Path/$Nombre_Ejecutable-".$size."-Hilos-".$hilo.".dat";
		#guarda para la cantidad de repeticiones que hayamos designado
		for ($i=0; $i<$Repeticiones; $i++) {
			system("$Path/$Nombre_Ejecutable $size $hilo  >> $file");
			#printf("$Path/$Nombre_Ejecutable $size $hilo \n");
		}
		close($file);
	$p=$p+1;
	}
}
