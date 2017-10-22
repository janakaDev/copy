#include<stdio.h>
#include<mpi.h>
#include<random>

int main11()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	int arr[32];
	int buf[2];

	if (pid == 0)
	{
		for (int i = 0; i < 32; i++)
		{
			arr[i] = rand() % 10;
		}

	}

	MPI_Scatter(&arr, 2, MPI_INT, &buf, 2, MPI_INT, 0, MPI_COMM_WORLD);
	printf("pid=%d msg %d , %d \n", pid, buf[0], buf[1]);

	if (pid % 4 == 3)
	{
		int sum = buf[0] + buf[1];
		printf("summation is %d \n \n", sum);
		MPI_Send(&sum, sizeof(sum), MPI_INT, pid - 1, 50, MPI_COMM_WORLD);
	}

	if (pid % 4 == 2 || pid % 4 == 1)
	{
		int sum1 = buf[0] + buf[1];
		printf("summation is %d \n \n", sum1);
		int recv;
		MPI_Recv(&recv, sizeof(recv), MPI_INT, pid + 1, 50, MPI_COMM_WORLD, &sta);
		int sum2 = sum1 + recv;
		MPI_Send(&sum2, sizeof(sum2), MPI_INT, pid - 1, 50, MPI_COMM_WORLD);
	}

	if (pid % 4 == 0 && pid != 0)
	{
		int sum3 = buf[0] + buf[1];
		printf("summation is %d \n \n", sum3);
		int recv1;
		MPI_Recv(&recv1, sizeof(recv1), MPI_INT, pid + 1, 50, MPI_COMM_WORLD, &sta);
		int sum4 = sum3 + recv1;
		MPI_Send(&sum4, sizeof(sum4), MPI_INT, 0, 50, MPI_COMM_WORLD);

	}

	if (pid == 0)
	{
		int sum5 = buf[0] + buf[1];
		printf("summation is %d \n \n", sum5);
		int recv2;
		for (int i = 1; i < 4; i++)
		{
			MPI_Recv(&recv2, sizeof(recv2), MPI_INT, i * 4, 50, MPI_COMM_WORLD, &sta);
			int Gsum = sum5 + Gsum;
			printf("Global summation is %d \n \n", Gsum);
		}

	}

	MPI_Finalize();
	return 0;
}