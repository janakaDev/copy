#include<stdio.h>
#include<mpi.h>
int main14()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;
	MPI_Datatype vari; //define own datatype
	MPI_Type_vector(4, 2, 4, MPI_INT, &vari); //( no of blocks, no of data in 1 block, gap between each data, old data type, new data type)

	MPI_Type_commit(&vari);

	if (pid == 0)
	{
		int arr[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		MPI_Send(&arr[0], 1, vari, 1, 50, MPI_COMM_WORLD);
		MPI_Send(&arr[1], 1, vari, 2, 50, MPI_COMM_WORLD);
	}

	if (pid == 1)
	{
		int rarr[8];
		MPI_Recv(&rarr, 8, MPI_INT, 0, 50, MPI_COMM_WORLD, &sta);
		printf("pid=%d . received data is ", pid);

		for (int i = 0; i < 8; i++)
			printf(" %d", rarr[i]);
		printf("\n");
	}

	if (pid == 2)
	{
		int rarr[8];
		MPI_Recv(&rarr, 8, MPI_INT, 0, 50, MPI_COMM_WORLD, &sta);
		printf("pid=%d . received data is ", pid);

		for (int i = 0; i < 8; i++)
			printf(" %d", rarr[i]);
		printf("\n");
	}
	MPI_Type_free(&vari);
	MPI_Finalize();
	return 0;
}
