package com.paocorp.magicsquares.models;

public class MagicSquareSearch {

    MagicSquare magicSquare;
    int a = 0;
    int b = 0;
    int c = 0;

    public MagicSquareSearch() {

        while (!this.validate()) {
            a = 1 + (int) (Math.random() * ((50 - 1) + 1));

            b = a + (int) (Math.random() * ((50 - a) + 1));
            while (b == 2 * a) {
                b = a + (int) (Math.random() * ((50 - a) + 1));
            }

            c = (a + b) + (int) (Math.random() * ((60 - (a + b)) + 1));
        }

        int[][] square = new int[3][3];
        square[0][0] = c - b;
        square[0][1] = c + a + b;
        square[0][2] = c - a;
        square[1][0] = c - (a - b);
        square[1][1] = c;
        square[1][2] = c + (a - b);
        square[2][0] = c + a;
        square[2][1] = c - (a + b);
        square[2][2] = c + b;

        magicSquare = new MagicSquare(square);
    }

    private boolean validate() {
        return !(c - (a + b) <= 0 || b == 2 * a || b > (c - a) || a > b);
    }

    public MagicSquare getMagicSquare() {
        return this.magicSquare;
    }
}
