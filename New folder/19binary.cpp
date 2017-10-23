#include<stdio.h>
#include<mpi.h>

int main()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	int num[9];
	int rnum[3];
	int bc = 0;
	int rbc;

	if (pid==0)
	{
		for (int i = 0; i < 9; i++)
		{
			num[i] = i % 2;
		}
	}

	MPI_Scatter(&num, 3, MPI_INT, &rnum, 3, MPI_INT, 0, MPI_COMM_WORLD);
	printf("Pid : %d and I received %d %d %d \n", pid, rnum[0], rnum[1], rnum[2]);

	for (int i = 0; i < 3; i++)
	{
		if (rnum[i]==1)
		{
			bc++;
		}
	}

	MPI_Reduce(&bc, &rbc, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);

	if (pid==0)
	{
		printf("Pid : %d total bit count %d \n", pid, rbc);
		if (rbc%2==0)
		{
			printf("Input Stream After Parity Check is: 1");
		}
		else
		{
			printf("Input Stream After Parity Check is: 0");
		}
		for (int i = 0; i < 9; i++)
		{
			printf("%d", num[i]);
		}
		printf("\n");
	}

	MPI_Finalize();
	return 0;
}