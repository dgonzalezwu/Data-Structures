/*
@name Danielle Gonzalez-Wu
@date 4/30/2021
@course CSC201
 */

package com.company;

public class nqueenproblem {
  //Creating 2-D arrays to serve as the chessboard, the first number in [][] is for the rows, and the second number is the columns

  int[][] fourbyfourboard = {{0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0}};
  int[][] fivebyfiveboard = {{0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0}};
  int[][] sixbysixboard = {{0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0}};

  int numqueens = 0;

  /*Function to make sure the placement of the Queen satisfies all specifications
  1. No two queens placed on the same row
  2. No two queens placed on the same column
  3. No two queens placed on the same diagonal
   */

  boolean checkifsafe(int board[][], int row, int column) {
    int a;
    int b;
    int i;
    int j;
    //Check if no other queens are placed on the same column are denoted by 0 first
    for (int c = 0; c < column; c++) {
      if (board[row][c] == 1) {
        return false;
      }
    }

    //Check if no other queens are placed on the same row
    for (int d = 0; d < row; d++) {
      if (board[d][column] == 1) {
        return false;
      }
    }

    //Check if no other queens are placed on the same diagonal
    //Diagonal 1 which looks like this: \

    for (a = row, b = column; a < numqueens && b < numqueens; a++, b++) {
      if (board[a][b] == 1) {
        return false;
      }
    }

    for (a = row, b = column; a >= 0 && b >= 0; a--, b--) {
      if (board[a][b] == 1) {
        return false;
      }
    }

    //Diagonal 2 which looks like this: /

    for (a = row, b = column; a >= 0 && b >= 0 && a < numqueens && b < numqueens; a++, b--) {
      if (board[a][b] == 1) {
        return false;
      }
    }

    for (a = row, b = column; a >= 0 && b >= 0 && a < numqueens && b < numqueens; a--, b++) {
      if (board[a][b] == 1) {
        return false;
      }
    }

    //if everything passes and no falses are returned we will return true
    return true;
  }

  //Recursive function to solve Queens placement
  boolean recursivelysolve(int chessboard[][], int col) {
    //first we make a loop to see how many columns there are to make an amount of queens needed to place on the board
    if (chessboard == fourbyfourboard) {
      numqueens = 4;
    }

    if (chessboard == fivebyfiveboard) {
      numqueens = 5;
    }

    if (chessboard == sixbysixboard) {
      numqueens = 6;
    }

    //base case: stops when each column has a queen in it and it is safe so column must equal queens

    if (col == numqueens) {
      return true;
    }

    //placing queens in rows one by one
    for (int q = 0; q < numqueens; q++) {
      //if safe we will place the first queen on the board, this is a simplification of checkifsafe(chessboard,q,col == true)
      if (checkifsafe(chessboard, q, col) == true) {
        chessboard[q][col] = 1;

        if (recursivelysolve(chessboard, col + 1) == true) {
          return true;
        }

        //if it cannot find a solution with the placements it must backtrack and remove the previously placed queen
        chessboard[q][col] = 0;

      }
    }
    //return false if no solution can be found
    return false;
  }

  //Function to print out the solution of the chessboard


  public static void main(String[] args) {
    nqueenproblem solveprob = new nqueenproblem();
    solveprob.recursivelysolve(solveprob.fourbyfourboard, 0);

    //printing out the now solved four by four board
    System.out.println("Solution for 4 by 4 Chess Board: ");
    int N = 4;
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        System.out.print(" " + solveprob.fourbyfourboard[i][j]
            + " ");
      }
      System.out.println();
    }

    System.out.println("");
    solveprob.recursivelysolve(solveprob.fivebyfiveboard, 0);

    //printing out the now solved five by five board
    System.out.println("Solution for 5 by 5 Chess Board: ");
    int A = 5;
    for (int i = 0; i < A; i++) {
      for (int j = 0; j < A; j++) {
        System.out.print(" " + solveprob.fivebyfiveboard[i][j]
            + " ");
      }
      System.out.println();
    }

    System.out.println("");
    solveprob.recursivelysolve(solveprob.sixbysixboard, 0);

    //printing out the now solved six by six board
    System.out.println("Solution for 6 by 6 Chess Board: ");
    int B = 6;
    for (int i = 0; i < B; i++) {
      for (int j = 0; j < B; j++) {
        System.out.print(" " + solveprob.sixbysixboard[i][j]
            + " ");
      }
      System.out.println();
    }
  }
}






