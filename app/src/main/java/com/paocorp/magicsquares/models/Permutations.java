package com.paocorp.magicsquares.models;/*
    The permutation class generates
the permutations of indices {1,2,,...,N}.
*/

// Permutation object
class Permutations {

    public int[] currentPermutation;
    public int length, current, number;
    public int counter;

    // Constructor.  The initial permutation is the
    // identity permutation {1,2,...,N}
    public Permutations(int N) {
        counter = 1;
        length = N;
        number = factorial(N);
        currentPermutation = new int[N];
        for (int i = 0; i < N; i++) {
            currentPermutation[i] = i + 1;
        }
    }

    //  This method gets the next permutation of
    //  the sequence {1,2,3,...,N}.  It updates the permutation
    //  to the next permutation in a lexicographic ordering.

    public void getNextPermutation() {
        if (counter < factorial(length))  // From the original sequence, (N-1)! perms. remain.
        {
            int maxk = 0;
            int maxl = 0;
            int a, b, temp;
            int N = length;

            // Find largest index j with a[j] < a[j+1]
            int j = N - 2;
            while (currentPermutation[j] > currentPermutation[j + 1]) {
                j--;
            }

            // Find index k such that a[k] is smallest integer
            // greater than a[j] to the right of a[j]

            int k = N - 1;
            while (currentPermutation[j] > currentPermutation[k]) {
                k--;
            }

            // Interchange a[j] and a[k]
            temp = currentPermutation[k];
            currentPermutation[k] = currentPermutation[j];
            currentPermutation[j] = temp;
            // Put sequence in decreasing order after position j+1
            int r = N - 1;
            int s = j + 1;
            while (r > s) {
                temp = currentPermutation[s];
                currentPermutation[s] = currentPermutation[r];
                currentPermutation[r] = temp;
                r--;
                s++;
            }

            counter += 1; //Update counter

        }
    }

    //A factorial function
    public static int factorial(int n) {
        int res = 1;
        if (n < 0) {
            res = -1;
        }

        for (int i = 1; i <= n; i++) {
            res *= i;
        }
        return res;
    }
}
