#include<stdio.h>
#include<mpi.h>
int main1()
{
	int np;
	int pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);

	printf("\n Hello world....\n My pid = %d \n No of proccessors = %d ", pid, np);

	if (pid == 0)
		printf("\n I'm first master", pid, np);

	else if (pid == 1)
		printf("\n I'm second master", pid, np);

	else
		printf("\n I'm salve", pid, np);

	MPI_Finalize();


	return 0;
}