package com.paocorp.magicsquares.models;/*
    Some miscallaneous methods for the finding
magic squares program.
*/

class BruteForce {

    //Get column of indices for blank slots in MagicSquare
    public static int[] findBlanks(int[][] A, int nblanks) {

        int[] Blanks = new int[nblanks];

        for (int i = 0; i <= nblanks - 1; i++) {
            Blanks[i] = 0;
        }

        int index = 0;
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (A[i][j] == 0) {
                    Blanks[index] = 3 * i + j;
                    index += 1;
                }
            }
        }
        return Blanks;
    }

    //  Find filled indices.
    public static int[] findFilled(int A[][], int nfills) {
        int[] Filled = new int[nfills];
        for (int i = 0; i <= nfills - 1; i++) {
            Filled[i] = 0;
        }

        int index = 0;
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (A[i][j] != 0) {
                    Filled[index] = 3 * i + j;
                    index += 1;
                }
            }
        }
        return Filled;
    }

    // Determine values used in filling the magic square.
    public static int[] usedFills(int[][] A, int[] Filled, int nfills) {
        int[] Used = new int[nfills];
        int index_i, index_j;
        for (int i = 0; i <= nfills - 1; i++) {
            index_i = Filled[i] / 3;
            index_j = Filled[i] % 3;
            Used[i] = A[index_i][index_j];
        }
        return Used;
    }

    //  What values remain available?
    public static int[] remainingFills(int[] Used, int nfills) {
        int nblanks = 9 - nfills;
        int[] Remains = new int[nblanks];
        int index = 0;

        for (int i = 1; i <= 9; i++) {
            if (checkDupes(Used, i, nfills) == false) {
                Remains[index] = i;
                index += 1;
            }
        }
        return Remains;
    }

    //  Useful printer for rows and columns.
    public static String vectorPrint(int[] A, int N) {
        String result = "{";
        if (N > 0) {
            for (int k = 0; k <= N - 2; k++) {
                result += A[k] + ",";
            }
            result += A[N - 1] + "}";
        } else {
            result += "}";
        }
        return result;
    }

    //  Method to check if a Square contains duplicates
    public static boolean checkDupes(int[] A, int val, int N) {
        boolean dupes = false;
        for (int j = 0; j <= N - 1; j++) {
            if (A[j] == val) {
                dupes = true;
                break;
            }
        }
        return dupes;
    }
}
