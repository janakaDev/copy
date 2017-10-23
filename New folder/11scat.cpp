#include<stdio.h>
#include<mpi.h>
#include<iostream>
using namespace std;
int main() {
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;
	int a[32];
	int b[2];
	int PEsum = 0; //inside the 1 PE
	int PEtot = 0; //summation between processors

	if (pid == 0) {
		for (int i = 0; i < 32; i++) {
			a[i] = i;
		}
	}

	MPI_Scatter(&a, 2, MPI_INT, &b, 2, MPI_INT, 0, MPI_COMM_WORLD);
	printf("\n pid is %d msg is %d %d \n", pid, b[0], b[1]);
	PEsum = b[0] + b[1];

	if (pid % 4 == 3) {
		int m1 = PEsum;
		MPI_Send(&m1, 1, MPI_INT, pid - 1, 50, MPI_COMM_WORLD);
		//printf("\n pid is %d msg is %d \n", pid,m1);
	}
	else if (pid % 4 == 2) {
		int r = 0;
		MPI_Recv(&r, 1, MPI_INT, pid + 1, 50, MPI_COMM_WORLD, &sta);
		PEtot = r + PEsum;
		MPI_Send(&PEtot, 1, MPI_INT, pid - 1, 50, MPI_COMM_WORLD);
	}
	else if (pid % 4 == 1) {
		int r = 0;
		MPI_Recv(&r, 1, MPI_INT, pid + 1, 50, MPI_COMM_WORLD, &sta);
		PEtot = r + PEsum;
		MPI_Send(&PEtot, 1, MPI_INT, pid - 1, 50, MPI_COMM_WORLD);
	}
	else if (pid % 4 == 0 && pid != 0) {
		int r = 0;

		MPI_Recv(&r, 1, MPI_INT, pid + 1, 50, MPI_COMM_WORLD, &sta);
		PEtot = r + PEsum;
		MPI_Send(&PEtot, 1, MPI_INT, 0, 50, MPI_COMM_WORLD);
	}

	if (pid == 0) {
		int summation = 0;

		int r1;
		MPI_Recv(&r1, 1, MPI_INT, 1, 50, MPI_COMM_WORLD, &sta);
		PEtot = r1 + PEsum;

		int r;
		for (int i = 4; i <= 12; i = i + 4) {
			MPI_Recv(&r, 1, MPI_INT, pid + i, 50, MPI_COMM_WORLD, &sta);
			summation += r;
		}
		printf("pid %d, summation is %d\n", pid, summation + PEtot);
	}




	MPI_Finalize();
	return 0;
}