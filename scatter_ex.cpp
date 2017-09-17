#include <string.h>
#include <mpi.h>
#include <cstdlib> 
#include <ctime> 
#include <iostream>



int main()
{
	int np;
	int pid;

	MPI_Status sta;

	MPI_Init(NULL, NULL);

	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);

	int a[100];
	int b[10];

	if (pid == 0)
	{
		srand((unsigned)time(0));
		int random_integer;
		for (int index = 0; index<100; index++) {
			random_integer = (rand() % 10) + 1;
			a[index] = random_integer;
			//std::cout << index << std::endl;
		}
	}

	MPI_Scatter(&a, 10, MPI_INT, &b, 10, MPI_INT, 0, MPI_COMM_WORLD);    //  0 - the process which is doing scattering


	printf("I am pid %d. I got: ", pid);

	bool found3 = false;
	bool found5 = false;
	bool found9 = false;

	for (int i = 0; i < 10; i++)
	{
		printf("%d,", b[i]);

		if (b[i] == 3) found3 = true;
		else if (b[i] == 5) found5  = true;
		else if (b[i] == 9) found9 = true;
	}

	printf("finding for 3,5, & 9 \n");
	
	if(found3) printf("3 found\n"); else printf("3 not found\n");
	if (found5) printf("5 found\n"); else printf("5 not found\n");
	if (found9) printf("9 found\n"); else printf("9 not found\n");


	printf("\n");
	//printf("pid is %d, %d %d \n", pid, b[0], b[1]);

	MPI_Finalize();

	return 0;
}
