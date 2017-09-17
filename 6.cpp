#include<stdio.h>
#include<mpi.h>
int main()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	if (pid == 0)
	{
		int p[] = { 1,2,3,4,5,6,7,8,9,10 };
		for (int i = 1; i < np; i++)
		{
			MPI_Send(&p[i * 2], 2, MPI_INT, i, 50, MPI_COMM_WORLD);
		}

		int sum = p[0] + p[1];
		int recv;
		for (int j = 1; j < np; j++)
		{
			MPI_Recv(&recv, 1, MPI_INT, j, 50, MPI_COMM_WORLD, &sta);
			printf("My proccessor id is %d and I'm receiver from %d, The message is %d \n", pid, j, recv);
			sum += recv;
		}

		printf("My proccessor id is %d and total = %d \n", pid, sum);
	}

	else
	{
		int recv1[2];
		int sum1 = 0;
		MPI_Recv(&recv1, 2, MPI_INT, 0, 50, MPI_COMM_WORLD, &sta);

		for (int i = 0; i < 2; i++)
		{
			sum1 += recv1[i];
		}
		MPI_Send(&sum1, 1, MPI_INT, 0, 50, MPI_COMM_WORLD);
	}

	MPI_Finalize();
	return 0;
}