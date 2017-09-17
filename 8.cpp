#include<stdio.h>
#include<mpi.h>
int main8()
{
	int np, pid;
	MPI_Status sta;
	MPI_Init(NULL, NULL);

	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);

	int a;
	
	if (pid == 0)
	{
		a = 10;
	}

	MPI_Bcast(&a, 1, MPI_INT, 0, MPI_COMM_WORLD);
	printf("My pid is: %d \n My msg is: %d \n", pid, a);

	MPI_Finalize();
	return 0;
}