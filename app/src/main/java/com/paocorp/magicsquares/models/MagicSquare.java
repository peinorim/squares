package com.paocorp.magicsquares.models;/*
MagicSquare object with random entries
at random positions.
*/

import java.util.*;

public class MagicSquare {
    public int[][] Square = new int[3][3];
    public int[] Column = new int[9];
    public int blanks;
    public int[] rowSum = new int[3];
    public int[] colSum = new int[3];
    String magicString = "";

    // Constructor
    public MagicSquare(int fills) {
        //Set blanks
        blanks = 9 - fills;

        //Create "magic column" to be rearranged as magic square.

        for (int i = 0; i <= 8; i++) {
            Column[i] = 0;
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
            while (Column[index] != 0)  // Get unused index.
            {
                index = rand.nextInt(9);
            }
            Column[index] = used[i];
        }

        // Fill the Square
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                Square[i][j] = 0;
            }
        }

        //Fill the magic square using the magic column.
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                Square[i][j] = Column[3 * i + j];
            }
        }

        //	Set the row and columns sums.
        sumUpdates();
    }


//Public methods

    //  Fill the magic square entry [m,n] with value val.
    public void fillEntry(int m, int n, int val) {
        if (Square[m][n] == 0) {
            blanks -= 1;
        }

        Square[m][n] = val;
        Column[3 * m + n] = val; //Update column, too.
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
                rowSum[i] += Square[i][j];
                colSum[i] += Square[j][i];
            }
        }
    }

    //   Print current magic square to the console.
    public String printMagicSquare() {
        for (int i = 0; i <= 2; i++) {
            magicString += "\t \t \t [ \t";
            for (int j = 0; j <= 2; j++) {
                magicString += Square[i][j];
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


