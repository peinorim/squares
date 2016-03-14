package com.paocorp.magicsquares.models;/*
MagicSquare object with random entries
at random positions.
*/

import java.util.*;

public class MagicSquare {
    private int[][] square = new int[3][3];
    private int[] column = new int[9];
    private int blanks;
    private int[] rowSum = new int[3];
    private int[] colSum = new int[3];
    private int magicConstant;
    private String magicString = "";

    public MagicSquare(int[][] grid) {
        this.square = grid;
        sumUpdates();
    }

    // Constructor
    public MagicSquare(int order, int fills) {
        //Set blanks
        this.blanks = 9 - fills;

        //Create "magic column" to be rearranged as magic square.

        for (int i = 0; i <= 8; i++) {
            this.column[i] = 0;
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
            while (this.column[index] != 0)  // Get unused index.
            {
                index = rand.nextInt(9);
            }
            this.column[index] = used[i];
        }

        // Fill the square
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                this.square[i][j] = 0;
            }
        }

        //Fill the magic square using the magic column.
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                this.square[i][j] = this.column[3 * i + j];
            }
        }

        //	Set the row and columns sums.
        sumUpdates();
    }


//Public methods

    //  Fill the magic square entry [m,n] with value val.
    public void fillEntry(int m, int n, int val) {
        if (this.square[m][n] == 0) {
            this.blanks -= 1;
        }

        this.square[m][n] = val;
        this.column[3 * m + n] = val; //Update column, too.
        sumUpdates();  //Update row and column sums
    }

    //  Update the row and column sums
    public void sumUpdates() {
        for (int i = 0; i <= 2; i++) {
            this.rowSum[i] = 0;
            this.colSum[i] = 0;
        }

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                this.rowSum[i] += this.square[i][j];
                this.colSum[i] += this.square[j][i];
            }
        }
    }

    public boolean compareSquares (MagicSquare squareToCheck) {
        return Arrays.deepEquals(this.square, squareToCheck.square);
    }

    public MagicSquare copy() {
        int[][] newGrid = new int[3][3];
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                newGrid[i][j] = this.square[i][j];
            }
        }
        return new MagicSquare(newGrid);
    }

    public int[] getColumn() {
        return this.column;
    }

    public void setColumn(int[] column) {
        this.column = column;
    }

    public int getBlanks() {
        return this.blanks;
    }

    public void setBlanks(int blanks) {
        this.blanks = blanks;
    }

    public int[] getRowSum() {
        return this.rowSum;
    }

    public void setRowSum(int[] rowSum) {
        this.rowSum = rowSum;
    }

    public int[] getColSum() {
        return colSum;
    }

    public void setColSum(int[] colSum) {
        this.colSum = colSum;
    }

    public int getMagicConstant() {
        return this.magicConstant;
    }

    public void setMagicConstant(int magicConstant) {
        this.magicConstant = magicConstant;
    }

    public String getMagicString() {
        return this.magicString;
    }

    public void setMagicString(String magicString) {
        this.magicString = magicString;
    }

    public int[][] getSquare() {
        return this.square;
    }

    public void setSquare(int[][] square) {
        this.square = square;
    }
}


