#include<string.h>
#include<mpi.h>
#include<random>

int main12()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;


	const int s = 100;
	const int p = 10;
	int num[s];
	int buf[p];
	int osum[2], esum[2];
	int oc = 0, ec = 0;

	struct
	{
		int value;
		int proc;
	}

	maxIn, maxOut, minIn, minOut;

	if (pid == 0)
	{
		for (int i = 0; i < s; i++)
		{
			num[i] = (rand() % 250);
		}

	}

	MPI_Scatter(&num, 10, MPI_INT, &buf, 10, MPI_INT, 0, MPI_COMM_WORLD);
	//printf("My pid is:%d \n My numbers are: %d to %d \n",pid,buf[0],buf[9]);
	for (int i = 0; i < 10; i++)
	{
		printf("pid: %d \n number: %d ", pid, buf[i]);
	}

	for (int i = 0; i < p; i++)
	{
		if (buf[i] % 2 != 0)
			oc++;
	}

	maxIn.value = oc;
	maxIn.proc = pid;
	int maxpid;
	MPI_Reduce(&maxIn, &maxOut, 1, MPI_2INT, MPI_MAXLOC, 0, MPI_COMM_WORLD);
	printf("pid=%d Local odd count : %d \n", pid, oc);

	if (pid == 0)
	{
		printf("Pid : %d Maximum odd count : %d from %d \n", pid, maxOut.value, maxOut.proc);
	}

	MPI_Finalize();
	return 0;
}