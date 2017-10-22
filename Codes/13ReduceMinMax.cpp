#include<stdio.h>
#include<mpi.h>
#include<random>

int main6()
{
	int np, pid;
	MPI_Status sta;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);

	int arr[10];
	int min;
	int max;
	int rec;

	if (pid == 0)
	{
		for (int i = 0; i < 10; i++)
		{
			arr[i] = rand() % 10;
		}
	}

	MPI_Scatter(&arr, 1, MPI_INT, &rec, 1, MPI_INT, 0, MPI_COMM_WORLD);
	printf("\n My pid is %d. I received %d", pid, rec);

	int num = rec;
	for (int i = 0; i < pid; i++)
	{
		num = num*rec;
	}
	printf("\n%d", num);

	MPI_Reduce(&num, &max, 1, MPI_INT, MPI_MAX, 0, MPI_COMM_WORLD);
	MPI_Reduce(&num, &min, 1, MPI_INT, MPI_MIN, 0, MPI_COMM_WORLD);

	if (pid == 0)
	{
		printf("\n Max is %d", max);
		printf("\n Min is %d", min);
	}

	MPI_Finalize();
	return 0;
}