package com.paocorp.magicsquares.models;/*
MagicSquare object with random entries
at random positions.
*/

import java.util.*;

public class MagicSquare {
    public int[][] square = new int[3][3];
    public int[] column = new int[9];
    public int blanks;
    public int[] rowSum = new int[3];
    public int[] colSum = new int[3];
    public int magicConstant;
    String magicString = "";

    public MagicSquare(int[][] grid) {
        this.square = grid;
        sumUpdates();
    }

    // Constructor
    public MagicSquare(int order, int fills) {
        //Set blanks
        blanks = 9 - fills;

        //Create "magic column" to be rearranged as magic square.

        for (int i = 0; i <= 8; i++) {
            column[i] = 0;
        }

        //Fill column in N=fills positions with random entries
        int index, value;
        Random rand = new Random();
        int[] used = new int[fills];

        //Get N=fills random values with no repeats
        used[0] = rand.nextInt(9) + 1;  //Track used values
        for (int i = 1; i <= fills - 1; i++) {
            value = rand.nextInt(9) + 1;
            while (BruteForce.checkDupes(used, value, i)) {
                value = rand.nextInt(9) + 1;
            }
            used[i] = value;
        }

        for (int i = 0; i <= fills - 1; i++) {
            index = rand.nextInt(9);
            while (column[index] != 0)  // Get unused index.
            {
                index = rand.nextInt(9);
            }
            column[index] = used[i];
        }

        // Fill the square
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                square[i][j] = 0;
            }
        }

        //Fill the magic square using the magic column.
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                square[i][j] = column[3 * i + j];
            }
        }

        //	Set the row and columns sums.
        sumUpdates();
    }


//Public methods

    //  Fill the magic square entry [m,n] with value val.
    public void fillEntry(int m, int n, int val) {
        if (square[m][n] == 0) {
            blanks -= 1;
        }

        square[m][n] = val;
        column[3 * m + n] = val; //Update column, too.
        sumUpdates();  //Update row and column sums
    }

    //  Update the row and column sums
    public void sumUpdates() {
        for (int i = 0; i <= 2; i++) {
            rowSum[i] = 0;
            colSum[i] = 0;
        }

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                rowSum[i] += square[i][j];
                colSum[i] += square[j][i];
            }
        }
    }

    public boolean compareSquares (MagicSquare squareToCheck) {
        return Arrays.equals(this.square, squareToCheck.square);
    }

    //   Print current magic square to the console.
    public String printMagicSquare() {
        for (int i = 0; i <= 2; i++) {
            magicString += "\t \t \t [ \t";
            for (int j = 0; j <= 2; j++) {
                magicString += square[i][j];
                if (j != 2) {
                    magicString += "\t \t";
                }
            }
            magicString += " \t ] \n";
        }
        return magicString;
    }

    //  Print current row and columns sums.
    public String printSums() {
        //System.out.print("Column sums: ");
        String sums = BruteForce.vectorPrint(colSum, 3);
        //System.out.println();
        //System.out.print("Row sums: ");
        sums += BruteForce.vectorPrint(rowSum, 3);
        //System.out.println();
        return sums;
    }
}


