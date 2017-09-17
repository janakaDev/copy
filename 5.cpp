#include<stdio.h>
#include<mpi.h>
int main()
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
		MPI_Send(&num1,1,MPI_INT, 2, 50, MPI_COMM_WORLD);
		MPI_Recv(&sum, 50, MPI_INT, 2, 50, MPI_COMM_WORLD, &sta);
		printf("\n My proccesoor id is %d and I'm receiving %d",pid,sum);
	}

	if (pid == 1)
	{
		num2 = 2;
		MPI_Send(&num2,1, MPI_INT, 2, 50, MPI_COMM_WORLD);
	}

	if (pid == 2)
	{
		int recv1;
		int recv2;
		MPI_Recv(&recv1,50,MPI_INT,0,50,MPI_COMM_WORLD,&sta);
		printf("\n My proccesoor id is %d and I'm receiving %d", pid, recv1);
		MPI_Recv(&recv2, 50, MPI_INT, 1, 50, MPI_COMM_WORLD, &sta);
		printf("\n My proccesoor id is %d and I'm receiving %d", pid, recv2);
		sum = recv1, recv2;
		MPI_Send(&sum,1, MPI_INT, 0, 50, MPI_COMM_WORLD);
	}

	MPI_Finalize();
	return 0;
}