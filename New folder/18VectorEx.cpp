#include<stdio.h>
#include<mpi.h>
int main16()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;
	MPI_Datatype vari; //define own datatype
	MPI_Type_vector(2, 3, 7, MPI_INT, &vari); //( no of blocks, no of data in 1 block, gap between each data, old data type, new data type)

	MPI_Type_commit(&vari);

	if (pid == 0)
	{
		int arr[36];
		for (int i = 0; i < 36; i++)
			arr[i] = i + 1;
		MPI_Send(&arr[0], 1, vari, 1, 50, MPI_COMM_WORLD);
		MPI_Send(&arr[2], 1, vari, 2, 50, MPI_COMM_WORLD);
		//MPI_Send(&arr[15], 1, vari, 2, 50, MPI_COMM_WORLD);
	}

	if (pid == 1)
	{
		int rarr[6];
		MPI_Recv(&rarr, 6, MPI_INT, 0, 50, MPI_COMM_WORLD, &sta);
		printf("pid=%d . received data is ", pid);

		for (int i = 0; i < 6; i++)
			printf(" %d", rarr[i]);
		printf("\n");
	}

	if (pid == 2)
	{
		int rarr[6];
		MPI_Recv(&rarr, 6, MPI_INT, 0, 50, MPI_COMM_WORLD, &sta);
		printf("pid=%d . received data is ", pid);

		for (int i = 0; i < 6; i++)
			printf(" %d", rarr[i]);
		printf("\n");
	}
	MPI_Type_free(&vari);
	MPI_Finalize();
	return 0;
}
