import java.io.*;
import java.util.Scanner;

public class Assignment1 {

    public int[][] denseMatrixMult(int[][] A, int[][] B, int size)
    {
        int [][] C = initMatrix(size);

        if(size==1){
            C[0][0] = (A[0][0])*(B[0][0]);
            return C;
        }
        else{
            int newSize = size/2;
            int [][]C_00, C_01, C_10, C_11;
            int [][]M_0, M_1, M_2, M_3,M_4,M_5, M_6;
            int [][]temp1, temp2;

            //Calculating A_11 and B_11 to be used for M_3 and M_4, respectively
            //The other quarter matrices do not need to be calculated in advance
            //They can be accessed by x1,x2,y1,y2 parameters of sum/sub function
                int [][] A_11 = initMatrix(newSize);
                int [][] B_11 = initMatrix(newSize);
                for(int i=0; i<newSize; i++){
                    for(int j=0; j<newSize; j++){
                        A_11[i][j] = A[i + newSize][j+newSize];
                        B_11[i][j] = B[i + newSize][j+newSize];
                    }
                }

            //Calculating M's
                temp1 = sum(A,A,0,0,newSize,newSize,newSize);
                temp2 = sum(B,B,0,0,newSize,newSize,newSize);
                M_0 = denseMatrixMult(temp1,temp2,newSize);

                temp1 = sum(A,A,newSize,0,newSize,newSize,newSize);
                M_1 = denseMatrixMult(temp1,B,newSize);

                temp1 = sub(B,B,0,newSize,newSize,newSize,newSize);
                M_2 = denseMatrixMult(A,temp1,newSize);

                temp1 = sub(B,B,newSize,0,0,0,newSize);
                M_3 = denseMatrixMult(A_11, temp1,newSize);

                temp1 = sum(A,A,0,0,0,newSize,newSize);
                M_4 = denseMatrixMult(temp1, B_11,newSize);

                temp1 = sub(A,A,newSize,0,0,0,newSize);
                temp2 = sum(B,B,0,0,0,newSize,newSize);
                M_5 = denseMatrixMult(temp1,temp2,newSize);

                temp1 = sub(A,A,0,newSize,newSize,newSize,newSize);
                temp2 = sum(B,B,newSize,0,newSize,newSize,newSize);
                M_6 = denseMatrixMult(temp1,temp2,newSize);


            //Calculating all four quarters of Matrix from M's
                //first step of C_00: M_0 + M_3
                temp1 = sum(M_0, M_3, 0,0,0,0,newSize);
                //second step of C_00: M_0 + M_3 + M_6
                temp2 = sum(temp1, M_6,0,0,0,0,newSize);
                //third step of C_00: M_0 + M_3 + M_6 - M_4
                C_00 = sub(temp2, M_4,0,0,0,0,newSize);

                C_01 = sum(M_2, M_4,0,0,0,0,newSize);
                C_10 = sum(M_1, M_3,0,0,0,0,newSize);

                //first step of C_11: M_0 + M_2
                temp1 = sum(M_0, M_2, 0,0,0,0,newSize);
                //second step of C_11: M_0 + M_2 + M_5
                temp2 = sum(temp1, M_5,0,0,0,0,newSize);
                //third step of C_11: M_0 + M_2 + M_5 - M_1
                C_11 = sub(temp2, M_1,0,0,0,0,newSize);

            //Grouping quarters into one C matrix
            for(int i = 0; i<newSize; i++){
                for(int j=0; j<newSize; j++){
                    C[i][j] = C_00[i][j];
                    C[i][j + newSize] = C_01[i][j];
                    C[i + newSize][j] = C_10[i][j];
                    C[i + newSize][j + newSize] = C_11[i][j];
                }
            }
            return C;
        }
    }

    public int[][] sum(int[][] A, int[][] B, int x1, int y1, int x2, int y2, int n)
    {
        int arr[][] = initMatrix(n);

        for(int i=0; i < n; i++){
            for(int j=0; j<n; j++){
                arr[i][j] = A[x1 + i][y1 + j] + B[x2 + i][y2 + j];
            }
        }
        return arr;
    }

    public int[][] sub(int[][] A, int[][] B, int x1, int y1, int x2, int y2, int n)
    {
        int arr[][] = initMatrix(n);

        for(int i=0; i < n; i++){
            for(int j=0; j<n; j++){
                arr[i][j] = A[x1 + i][y1 + j] - B[x2 + i][y2 + j];
            }
        }
        return arr;
    }


    public int[][] initMatrix(int n) {
        //Initialize Array
        int arr[][] = new int[n][n];
        return arr;
    }

    public void printMatrix(int n, int[][] A) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int[][] readMatrix(String filename, int n) throws Exception {

        Scanner input = new Scanner(new File(filename));
        int arr[][] = initMatrix(n);

        //loop which enters all integers from file into array
        while (input.hasNextInt()) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++)
                    arr[i][j] = input.nextInt();
            }
        }
        return arr;
    }
}