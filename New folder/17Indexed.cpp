#include<stdio.h>
#include<mpi.h>
int main15()
{
	const int s = 4; //define own datatype
	int bl[s] = { 4, 3, 2, 1 }, ind[s] = { 0, 5, 10, 15 };

	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;
	MPI_Datatype vari;
	MPI_Type_indexed(4, bl, ind, MPI_INT, &vari);//(count,block[],indices[],old datatype, new datatype)
	MPI_Type_commit(&vari);

	if (pid == 0)
	{
		int arr[16];
		for (int i = 0; i < 16; i++)
			arr[i] = i + 1;
		//int arr[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		MPI_Send(&arr[0], 1, vari, 1, 50, MPI_COMM_WORLD);
	}
	else
	{
		int n = 10;
		int rarr[10];
		MPI_Recv(&rarr, n, MPI_INT, 0, 50, MPI_COMM_WORLD, &sta);
		printf("pid = %d . received data is ", pid);

		for (int i = 0; i < n; i++)
			printf(" %d", rarr[i]);
		printf("\n");
	}

	MPI_Type_free(&vari);
	MPI_Finalize();
	return 0;
}