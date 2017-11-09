#include<stdio.h>
#include<mpi.h>

int main1()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	int num1, num2, sum;

	if (pid == 0)
	{
		num1 = 5;
		MPI_Send(&num1, 1, MPI_INT, 2, 50, MPI_COMM_WORLD);
		int result;
		MPI_Recv(&result, 1, MPI_INT, 2, 50, MPI_COMM_WORLD, &sta);
		printf("My Processor id is %d and result is %d", pid, result);
	}
	if (pid == 1)
	{
		num2 = 2;
		MPI_Send(&num2, 1, MPI_INT, 2, 50, MPI_COMM_WORLD);
	}
	if (pid == 2)
	{
		int recv[2];
		MPI_Recv(&recv[0], 1, MPI_INT, 0, 50, MPI_COMM_WORLD, &sta);
		MPI_Recv(&recv[1], 1, MPI_INT, 1, 50, MPI_COMM_WORLD, &sta);
		sum = recv[0] + recv[1];
		MPI_Send(&sum, 1, MPI_INT, 0, 50, MPI_COMM_WORLD);
	}
	MPI_Finalize();
	return 0;


}