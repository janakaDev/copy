#include<stdio.h>
#include<mpi.h>
#include<random>
int main9()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	const int s = 100;
	const int n = 10;
	int arr[s];
	int buf[n];

	bool found[3] = { false,false,false };
	int sk[3] = { 3,5,9 };

	if (pid == 0)
	{
		for (int i = 0; i < s; i++)
		{
			arr[i] = rand() % n;
		}
	}

	MPI_Scatter(&arr, 10, MPI_INT, &buf, 10, MPI_INT, 0, MPI_COMM_WORLD);
	printf("pid=%d msg %d %d %d %d %d %d %d %d %d %d \n", pid, buf[0], buf[1], buf[2], buf[3], buf[4], buf[5], buf[6], buf[7], buf[8], buf[9]);

	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j < 3; j++)
		{
			if (buf[i] == sk[j])
			{
				found[j] = true;
			}
		}
	}

	for (int j = 0; j < 3; j++)
	{
		if (found[j])
			printf("Element %d is found\n", sk[j]);
		else
			printf("Element %d is not found\n", sk[j]);
	}

	MPI_Finalize();
	return 0;
}