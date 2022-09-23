package caro;

import java.util.Scanner;

/**
 * Class for Player object.
 */
public class Player {
  private char symbol;

  /**
   * Constructor.
   * @param symbol  player symbol
   */
  public Player(char symbol) {
    this.symbol = symbol;
  }

  /**
   * Obtain move from player.
   * @return move as an array
   */
  public int[] obtainMove() {
    Scanner s = new Scanner(System.in);

    System.out.println("Player " + this.symbol + " is making a move!");
    System.out.println("Input move's row: \n");
    int row = s.nextInt();
    System.out.println("Input move's column: \n");
    int col = s.nextInt();

    int move[] = new int[]{row, col};
    return move;
  }

  /**
   * Return player symbol.
   * @return
   */
  public char getSymbol() {
    return this.symbol;
  }
}
