#include<string.h>
#include<mpi.h>
#include<random>
#include<stdio.h>
#include<math.h>

int main13()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;
	MPI_Datatype vari; // define own datatype

	MPI_Type_contiguous(4, MPI_INT, &vari); //(no of data, old data type, new data type)
	MPI_Type_commit(&vari);

	if (pid == 0)
	{
		int arr[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		MPI_Send(&arr[0], 2, vari, 1, 50, MPI_COMM_WORLD); //2 - 2 blocks of 4 nums
		MPI_Send(&arr[3], 1, vari, 2, 50, MPI_COMM_WORLD);
	}

	if (pid == 1)
	{
		int rarr[8];
		MPI_Recv(&rarr, 2, vari, 0, 50, MPI_COMM_WORLD, &sta);
		printf("pid=%d . received data is ", pid);

		for (int i = 0; i < 8; i++)
			printf(" %d", rarr[i]);
		printf("\n");
	}

	if (pid == 2)
	{
		int rarr[4];
		MPI_Recv(&rarr, 1, vari, 0, 50, MPI_COMM_WORLD, &sta);
		printf("pid=%d . received data is ", pid);

		for (int i = 0; i < 4; i++)
			printf(" %d", rarr[i]);
		printf("\n");
	}


	MPI_Type_free(&vari);
	MPI_Finalize();
	return 0;
}