#include<stdio.h>
#include<mpi.h>
int main()
{
	int np, pid;
	MPI_Status sta;
	MPI_Init(NULL, NULL);

	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);

	int a[6];
	int b[2];

	if (pid == 0)
	{
		for (int i = 0; i < 6; i++)
		{
			a[i] = i;
		}
	}

	MPI_Scatter(&a, 2, MPI_INT, &b, 2, MPI_INT, 0, MPI_COMM_WORLD);
	printf("pid is: %d, %d %d \n", pid, b[0], b[1]);

	MPI_Finalize();
	return 0;
}