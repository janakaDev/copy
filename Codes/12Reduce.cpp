#include<stdio.h>
#include<mpi.h>

int main4()
{
	int np, pid;
	MPI_Status sta;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);

	int arr[6];
	int total;

	if (pid == 0)
	{
		for (int i = 0; i < 6; i++)
		{
			arr[i] = i + 1;
		}
	}

	int recvarr[2];
	MPI_Scatter(&arr, 2, MPI_INT, &recvarr, 2, MPI_INT, 0, MPI_COMM_WORLD);
	printf("\nMy processor id is %d. Received data are ", pid);
	printf("%d and %d", recvarr[0], recvarr[1]);
	int sum = recvarr[0] + recvarr[1];

	MPI_Reduce(&sum, &total, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);

	if (pid == 0)
	{
		printf("\nMy processor id is %d. Total received is %d ", pid, total);
	}

	MPI_Finalize();
	return 0;
}