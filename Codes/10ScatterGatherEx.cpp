#include<stdio.h>
#include<mpi.h>
int main10()
{
	int np, pid;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	char DNA1[20];
	char DNA2[5];
	int ltrCount[4] = { 0,0,0,0 };
	int ltrCountTotal[16] = { 0,0,0,0 };
	char pair[20];

	if (pid == 0)
	{
		DNA1[0] = 'G';
		DNA1[1] = 'T';
		DNA1[2] = 'A';
		DNA1[3] = 'A';
		DNA1[4] = 'T';
		DNA1[5] = 'C';
		DNA1[6] = 'C';
		DNA1[7] = 'C';
		DNA1[8] = 'G';
		DNA1[9] = 'T';
		DNA1[10] = 'A';
		DNA1[11] = 'G';
		DNA1[12] = 'C';
		DNA1[13] = 'T';
		DNA1[14] = 'T';
		DNA1[15] = 'G';
		DNA1[16] = 'C';
		DNA1[17] = 'C';
		DNA1[18] = 'G';
		DNA1[19] = 'A';


	}

	MPI_Scatter(&DNA1, 5, MPI_CHAR, &DNA2, 5, MPI_CHAR, 0, MPI_COMM_WORLD);

	//printf("%d : %s, %s, %s, %s, %s\n", pid, DNA2[0], DNA2[1], DNA2[2], DNA2[3], DNA2[4]);

	for (int i = 0; i < 5; i++)
	{
		if (DNA2[i] == 'G')
		{
			ltrCount[0] = ltrCount[0] + 1;
		}
		else if (DNA2[i] == 'T')
		{
			ltrCount[1] = ltrCount[1] + 1;
		}
		else if (DNA2[i] == 'A')
		{
			ltrCount[2] = ltrCount[2] + 1;
		}
		else
		{
			ltrCount[3] = ltrCount[3] + 1;
		}
	}

	printf("%d no of G,T,A,C %d , %d, %d, %d\n", pid, ltrCount[0], ltrCount[1], ltrCount[2], ltrCount[3]);

	MPI_Gather(&ltrCount, 4, MPI_INT, &ltrCountTotal, 4, MPI_INT, 0, MPI_COMM_WORLD);

	if (pid == 0)
	{
		int countG = 0;
		int countT = 0;
		int countA = 0;
		int countC = 0;


		for (int i = 0; i < 16; i++)
		{
			ltrCountTotal[i];
			if (i % 4 == 0)
			{
				countG = countG + ltrCountTotal[i];
			}
			else if (i % 4 == 1)
			{
				countT = countT + ltrCountTotal[i];
			}
			else if (i % 4 == 2)
			{
				countA = countA + ltrCountTotal[i];
			}
			else
			{
				countC = countC + ltrCountTotal[i];
			}
		}

		printf("Total Guanine %d \n", countG);
		printf("Total Adenine %d \n", countA);
		printf("Total Thymine %d \n", countT);
		printf("Total Cytosine %d \n", countC);

		for (int i = 0; i < 20; i++)
		{
			if (DNA1[i] == 'G')
			{
				pair[i] = 'C';
			}
			else if (DNA1[i] == 'T')
			{
				pair[i] = 'A';
			}
			else if (DNA1[i] == 'A')
			{
				pair[i] = 'T';
			}
			else
			{
				pair[i] = 'G';
			}
		}
		printf("%d : %c, %c, %c, %c, %c,%c, %c, %c, %c, %c,%c, %c, %c, %c, %c,%c, %c, %c, %c, %c\n", pid, pair[0], pair[1], pair[2], pair[3], pair[4], pair[5], pair[6], pair[7], pair[8], pair[9], pair[10], pair[11], pair[12], pair[13], pair[14], pair[15], pair[16], pair[17], pair[18], pair[19]);
	}


	MPI_Finalize();
	return 0;
}