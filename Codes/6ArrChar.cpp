#include<stdio.h>
#include<mpi.h>
int main6()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	char send[50] = "Hi";
	char recv[50];
	int ln = np - 1;

	if (pid == 0)
	{
		MPI_Send(&send, sizeof(send), MPI_CHAR, 1, 50, MPI_COMM_WORLD);
		printf("I am sending to 1 \n my pid is: %d \n my msg:%s \n", pid, send);

		MPI_Recv(&recv, 50, MPI_CHAR, ln, 50, MPI_COMM_WORLD, &sta);
		printf("I am receiving from %d \n my pid is: %d \n my msg:%s \n", ln, pid, recv);
	}

	else if (pid == ln)
	{
		MPI_Recv(&recv, 50, MPI_CHAR, pid - 1, 50, MPI_COMM_WORLD, &sta);
		printf("I am receiving from %d \n my pid is: %d \n my msg:%s \n", pid - 1, pid, recv);

		MPI_Send(&send, sizeof(send), MPI_CHAR, 0, 50, MPI_COMM_WORLD);
		printf("I am sending to %d \n my pid is: %d \n my msg:%s \n", pid + 1, pid, send);
	}

	else
	{
		MPI_Recv(&recv, 50, MPI_CHAR, pid - 1, 50, MPI_COMM_WORLD, &sta);
		printf("I am receiving from %d \n my pid is: %d \n my msg:%s \n", pid - 1, pid, recv);

		MPI_Send(&send, sizeof(send), MPI_CHAR, pid + 1, 50, MPI_COMM_WORLD);
		printf("I am sending to %d \n my pid is: %d \n my msg:%s \n", pid + 1, pid, send);


	}

	MPI_Finalize();
	return 0;
}