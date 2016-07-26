package com.paocorp.magicsquares.models;

import java.io.Serializable;
import java.util.*;

public class MagicSquare implements Serializable {

    private int[][] square = new int[3][3];
    private int[] column = new int[9];
    private int blanks;
    private int[] rowSum = new int[3];
    private int[] colSum = new int[3];
    private int magicConstant = 0;

    public MagicSquare(int[][] grid) {
        this.square = grid;
        sumUpdates();
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
        if (getDiagonalFirst() == getDiagonalSecond()) {
            this.magicConstant = getDiagonalFirst();
        }
    }

    public boolean compareSquares(MagicSquare squareToCheck) {
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

    public int getDiagonalFirst() {
        return this.square[0][0] + this.square[1][1] + this.square[2][2];
    }

    public int getDiagonalSecond() {
        return this.square[2][0] + this.square[1][1] + this.square[0][2];
    }

    public boolean valid() {
        sumUpdates();
        return getDiagonalFirst() == getDiagonalSecond() && this.rowSum[0] == this.rowSum[1] && this.rowSum[1] == this.rowSum[2]
                && this.colSum[0] == this.colSum[1] && this.colSum[1] == this.colSum[2];
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

    public int[][] getSquare() {
        return this.square;
    }

    public void setSquare(int[][] square) {
        this.square = square;
    }
}


