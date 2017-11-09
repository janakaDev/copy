#include<stdio.h>
#include<mpi.h>
int main()
{
	int np, pid;
	MPI_Init(NULL , NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &np);
	MPI_Comm_rank(MPI_COMM_WORLD, &pid);
	MPI_Status sta;

	int matrixA[16], matrixB[16];

	MPI_Datatype vari1;
	MPI_Datatype vari2;
	MPI_Type_vector(4, 1, 1, MPI_INT, &vari1);
	MPI_Type_vector(4, 1, 4, MPI_INT, &vari2);
	MPI_Type_commit(&vari1);
	MPI_Type_commit(&vari2);

	if (pid==0)
	{
		for (int i = 0; i < 16; i++)
		{
			matrixA[i] = i + 1;
			matrixB[i] = i + 1;
		}
		
		for (int i = 1; i < 16; i++)
		{
			if (i<4)
			{
				MPI_Send(&matrixA[0], 1, vari1, i, 20, MPI_COMM_WORLD);
				MPI_Send(&matrixB[i], 1, vari2, i, 21, MPI_COMM_WORLD);
			}
			else if (i<8)
			{
				MPI_Send(&matrixA[4], 1, vari1, i, 20, MPI_COMM_WORLD);
				MPI_Send(&matrixB[i%4], 1, vari2, i, 21, MPI_COMM_WORLD);
			}
			else if (i<12)
			{
				MPI_Send(&matrixA[8], 1, vari1, i, 20, MPI_COMM_WORLD);
				MPI_Send(&matrixB[i%4], 1, vari2, i, 21, MPI_COMM_WORLD);
			}
			else
			{
				MPI_Send(&matrixA[12], 1, vari1, i, 20, MPI_COMM_WORLD);
				MPI_Send(&matrixB[i%4], 1, vari2, i, 21, MPI_COMM_WORLD);
			}
		}
		printf("PID: %d\n", pid);
		
		int sum = 0;
		for (int i = 0; i < 4; i++)
		{
			sum += matrixA[i] * matrixB[i*4];
			printf("%d--%d\n", matrixA[i], matrixB[i*4]);
			
		}
		printf("Sum: %d\n", sum);

	}
	else
	{
		int mA[4], mB[4];
		printf("PID: %d\n", pid);
		MPI_Recv(&mA, 4, MPI_INT, 0, 20, MPI_COMM_WORLD, &sta);
		MPI_Recv(&mB, 4, MPI_INT, 0, 21, MPI_COMM_WORLD, &sta);

		int sum = 0;
		for (int i = 0; i < 4; i++)
		{
			sum += mA[i] * mB[i];
			printf("%d--%d\n", mA[i], mB[i]);
		}
		printf("Sum: %d\n",sum);
		//printf("\n");
	}

	MPI_Type_free(&vari1);
	MPI_Type_free(&vari2);
	MPI_Finalize();
	return 0;
}