#include<stdio.h>
#include<mpi.h>
#include<random>

int main()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	int arr[10];
	int cath[2];
	int total = 0;
	int max;
	int collect[5] = { 0,0,0,0,0};

	struct 
	{
		int value;
		int loc;
	}maxIn,maxOut,minIn,minOut;


	if (pid == 0)
	{
		for (int i = 0; i < 10; i++)
		{
			arr[i] = rand() % 10;
		}
	}

	MPI_Scatter(&arr, 2, MPI_INT, &cath, 2, MPI_INT, 0, MPI_COMM_WORLD);
	printf("PID: %d and num: %d %d\n", pid, cath[0], cath[1]);

	int sum = 0;

	sum = cath[0] + cath[1];
	printf("PID: %d and Sum: %d \n\n", pid, sum);
	/////////////////////////////////////////////////////////
	

	MPI_Reduce(&sum, &total, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);
	
	if (pid == 0)
	{
		printf("\nPID: %d and total is: %d\n", pid, total);		
	}

	////////////////////////////////////////////////	
	
	MPI_Reduce(&sum, &max, 1, MPI_INT, MPI_MAX, 0, MPI_COMM_WORLD);

	if (pid == 0)
	{
		printf("\nPID: %d and Max is: %d\n", pid, max);
	}

	/////////////////////////////////////////////////////

	
	MPI_Gather(&sum, 1, MPI_INT, &collect, 1, MPI_INT,0,MPI_COMM_WORLD);

	if (pid == 0)
	{		
		printf("\nPID: %d and Collect: %d, %d, %d, %d, %d \n", pid, collect[0], collect[1], collect[2], collect[3], collect[4]);
	}

	//////////////////////////////////////////////

	
	maxIn.value = max;
	maxIn.loc = pid;

	MPI_Reduce(&maxIn, &maxOut, 1, MPI_2INT, MPI_MAXLOC, 0, MPI_COMM_WORLD);

	if (pid == 0)
	{
		printf("\nPID: %d and Maximum is: %d from %d\n", pid, maxOut.value,maxOut.loc);
	}
	
	MPI_Finalize();
	return 0;
}