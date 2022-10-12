package caro;

import java.util.Scanner;

/**
 * Class for Player object.
 */
public class Player {
  private char symbol;

  /**
   * Constructor.
   *
   * @param symbol player symbol
   */
  public Player(char symbol) throws IllegalArgumentException {
    if (symbol == Game.EMPTY) {
      throw new IllegalArgumentException("Player symbol cannot be empty.");
    }
    this.symbol = symbol;
  }

  /**
   * Obtain move from player.
   *
   * @return move as an array, return null if input is invalid
   */
  public int[] obtainMove() {
    Scanner s = new Scanner(System.in);
    boolean validInput = false;
    int[] move = null;

    System.out.println("Player " + this.symbol + " is making a move!");


    try {
      System.out.println("Input move's row: \n");
      int row = s.nextInt();
      System.out.println("Input move's column: \n");
      int col = s.nextInt();
      move = new int[]{row, col};
    } catch (Exception e) {
      System.out.println("Input can only be integers!\n");
    }


    return move;
  }

  /**
   * Return player symbol.
   *
   * @return player symbol
   */
  public char getSymbol() {
    return this.symbol;
  }
}
