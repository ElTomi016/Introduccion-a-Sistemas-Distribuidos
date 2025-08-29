/*#######################################################################################
 #* Fecha:28/08/2025
 #* Autor:Tomás Ramírez
 #* Tema: Laboratorio 1
 #* 	- Programa Multiplicación de Matrices algoritmo clásico
 #* 	- Paralelismo con OpenMP
######################################################################################*/

#include <stdio.h>      
#include <stdlib.h>    
#include <string.h>  
#include <time.h>      
#include <sys/time.h>   
#include <omp.h>        

// Variables
struct timeval inicio, fin; 

// funcion que mide el tiempo
void InicioMuestra(){
	gettimeofday(&inicio, (void *)0);
}

// funcion para finalizar el tiempo y mostrar el resultado
void FinMuestra(){
	gettimeofday(&fin, (void *)0);
	fin.tv_usec -= inicio.tv_usec;
	fin.tv_sec  -= inicio.tv_sec;
	double tiempo = (double) (fin.tv_sec*1000000 + fin.tv_usec); 
	printf("%9.0f \n", tiempo);   
}

// imprimir una matriz (solo si D < 9)
void impMatrix(double *matrix, int D){
	printf("\n");
	if(D < 9){
		for(int i=0; i<D*D; i++){
			if(i%D==0) printf("\n");
			printf("%f ", matrix[i]);
		}
		printf("\n**-----------------------------**\n");
	}
}

// iniciar 2 matrices con valores aleatorios de maximo 100
void iniMatrix(double *m1, double *m2, int D){
	for(int i=0; i<D*D; i++, m1++, m2++){
		*m1 =  rand()%100;	
		*m2 =  rand()%100;	
	}
}

// multiplicacion de matrices
void multiMatrix(double *mA, double *mB, double *mC, int D){
	double Suma, *pA, *pB;
	#pragma omp parallel   //bloque de OpenMP
	{
		#pragma omp for    // Distribuye las iteraciones del for entre hilos
		for(int i=0; i<D; i++){
			for(int j=0; j<D; j++){
				pA = mA+i*D;	
				pB = mB+j;    
				Suma = 0.0;
				for(int k=0; k<D; k++, pA++, pB+=D){
					Suma += *pA * *pB;
				}
				mC[i*D+j] = Suma;
			}
		}
	}
}

// funcion main
int main(int argc, char *argv[]){
	// Ingreso de parametros al sistema
	if(argc < 3){
		printf("\n Use: $./clasicaOpenMP SIZE Hilos \n\n");
		exit(0);
	}

	// Lectura de parametros
	int N = atoi(argv[1]);    // tamaño de la matriz
	int TH = atoi(argv[2]);   // numero de hilos

	// memoria dinamica
	double *matrixA  = (double *)calloc(N*N, sizeof(double));
	double *matrixB  = (double *)calloc(N*N, sizeof(double));
	double *matrixC  = (double *)calloc(N*N, sizeof(double));

	// inicia los numeros aleatorios
	srand(time(NULL));

	// Declara los hilos para OpenMP
	omp_set_num_threads(TH);

	// inicia las matrices con los valores aleatoreos
	iniMatrix(matrixA, matrixB, N);

	// imprime las matrices (Si d es < 9)
	impMatrix(matrixA, N);
	impMatrix(matrixB, N);

	// Cuenta el tiempo de la medicion
	InicioMuestra();
	multiMatrix(matrixA, matrixB, matrixC, N);
	FinMuestra();

	// imprime el resultado de la matriz (con la misma condicion)
	impMatrix(matrixC, N);

	/* Liberación de memoria */
	free(matrixA);
	free(matrixB);
	free(matrixC);
	
	return 0;
}
