#include<stdio.h>
#include<mpi.h>
int main3()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	if (pid == 0)
	{
		char a[50] = "Hello world";
		MPI_Send(&a, sizeof(a), MPI_CHAR, 1, 50, MPI_COMM_WORLD);
		printf("\n My proccessor id is %d and I'm the sender, The message is %s", pid, a);
	}

	if (pid == 1)
	{
		char b[50];
		MPI_Recv(&b, 50, MPI_CHAR, 0, 50, MPI_COMM_WORLD, &sta);
		MPI_Send(&b, sizeof(b), MPI_CHAR, 2, 50, MPI_COMM_WORLD);

	}

	if (pid == 2)
	{
		char c[50];
		MPI_Recv(&c, 50, MPI_CHAR, 1, 50, MPI_COMM_WORLD, &sta);
		printf("\n My proccessor id is %d and I'm the receiver, The message is %s", pid, c);
	}

	MPI_Finalize();
	return 0;
}