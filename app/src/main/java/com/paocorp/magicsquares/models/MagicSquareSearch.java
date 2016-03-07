package com.paocorp.magicsquares.models;/*
MagicSquareSearch:

A magic square is a 3x3 matrix where all column
and row sums have the same value.  Note:  We DO NOT
require the diagonals to have the magic sum.

This program generates a 3x3 matrix with N (non-repeating)
random integer entries ranging from 1-9.  The random
integers are placed in N random positions in the matrix. The
program determines if the random grid can be filled out
to a magic square.
*/

public class MagicSquareSearch {

    MagicSquare magicSquare;

    public MagicSquareSearch(int number) {

        //Prompt user for input
/*        Scanner scan = new Scanner(System.in);
        System.out.println("How many blanks should be randomly filled?  (Enter an integer from 1-9) ");
        int number = scan.nextInt();
        while (number < 1 || number > 9) {
            System.out.println("Invalid.  Enter an integer from 0-9: ");
            number = scan.nextInt();
        }*/


        //Get a candidate magic square object
        //	with number entries filled at random.

        magicSquare = new MagicSquare(number);
        int nblanks = magicSquare.blanks;
        int nfills = 9 - magicSquare.blanks;
        int[] Blanks = BruteForce.findBlanks(magicSquare.Square, nblanks);  //Position of blanks
        int[] Fills = BruteForce.findFilled(magicSquare.Square, nfills);      // Position of filled entries
        int[] Used = BruteForce.usedFills(magicSquare.Square, Fills, nfills);  // Filled values
        int[] Remains = BruteForce.remainingFills(Used, nfills);    // Remaining choices


        //Print out some information
        /*System.out.println("\n \n \n");
        System.out.println("%%%%%% MAGIC SQUARE SEARCH %%%%%%%");*/
        //System.out.println("\n Current square: ");
        //magicSquare.printMagicSquare();
        //System.out.println("\n%%%%% STATISTICS %%%%%");
        //magicSquare.printSums();
        //System.out.print("Blanks: " + nblanks + " @ positions ");
        //BruteForce.vectorPrint(Blanks, nblanks);
        //System.out.println();
        //System.out.print("Fills: " + nfills + " @ positions ");
        //BruteForce.vectorPrint(Fills, nfills);
        //System.out.println();
        //System.out.print("Fill values used: ");
        //BruteForce.vectorPrint(Used, nfills);
        //System.out.println();
        //System.out.print("Fill values remaining: ");
        //BruteForce.vectorPrint(Remains, nblanks);
        //System.out.println();

        //System.out.println("%%%%%%%%%%%%%%%%%%%%%%");
        //System.out.println(" \n");


        // Sweep through the permutations of the remaining
        // fill values.  Fill the board and check row and column
        // sums.

        Permutations P = new Permutations(nblanks);    // Permutation object
        boolean solved = false;
        int index_i, index_j;
        int counter = 0;
        int[] R = new int[3];  //Row sum
        int[] C = new int[3];  //Col sum
        int[] currentPermGuess = new int[nblanks];

        while (solved == false && counter <= Permutations.factorial(nblanks) - 1) {
            currentPermGuess = P.currentPermutation; //Current permutation guess
            for (int i = 0; i < nblanks; i++) {
                index_i = Blanks[i] / 3;    // 9 x 1 column ordering -> 3 x 3 matrix ordering
                index_j = Blanks[i] % 3;
                magicSquare.fillEntry(index_i, index_j, Remains[currentPermGuess[i] - 1]);
            }


            R = magicSquare.rowSum;
            C = magicSquare.colSum;

            //Check for solution
            if (R[0] == R[1] && R[1] == R[2] && C[0] == C[1] && C[1] == C[2]) {
                solved = true;
/*                System.out.println("!!!!SOLVED!!!! \n Number of iterations: " + (counter + 1));
                System.out.println("%%%%%%%%%%%%%%%");
                System.out.println("\n Magic square: ");
                magicSquare.printMagicSquare();
                magicSquare.printSums();
                System.out.println("\n");*/
            }

            // Update counter and permutation.
            counter += 1;
            P.getNextPermutation();
        }
        //Output for failed solve.
        if (solved == false) {
            System.out.println("No solution found.  Counter=" + counter);
        }
    }

    public MagicSquare getMagicSquare() {
        return this.magicSquare;
    }
}
