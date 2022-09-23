package caro;

import java.util.Arrays;

/**
 * Class representing GameBoard object for Caro.
 */
public class GameBoard {
  private final int width; //number of columns
  private final int height; //number of rows
  private char[][] board;

  /**
   * Constructor for GameBoard class.
   * @param width   number of columns
   * @param height  number of rows
   * @throws IllegalArgumentException if width and height are smaller than minimum dimension
   */
  public GameBoard(int width, int height) throws IllegalArgumentException{
    if ((width < Game.MINDIM) || (height < Game.MINDIM)) {
      throw new IllegalArgumentException("Dimension input is below minimum");
    }

    this.width = width;
    this.height = height;
    this.board = new char[height][width];
  }

  /**
   * Initialize board with empty character.
   */
  public void initializeBoard() {
    for (int row = 0; row < this.height; row ++) {
      for (int col = 0; col < this.width; col++) {
        this.board[row][col] = Game.EMPTY;
      }
    }
  }

  /**
   * Return the character at a certain position on board.
   * @param move  position at which the character will be returned
   * @return  character at position
   * @throws IllegalArgumentException if move is out of range on the board
   */
  public char returnPosition(int[] move) throws IllegalArgumentException{
    int r = move[0];
    int c = move[1];

    if ((r >= this.height) || (c >= this.width)
        || (r < 0) || (c < 0)) {
      throw new IllegalArgumentException("Position is out of board range");
    }
    return this.board[r][c];
  }

  /**
   * Check if a move is legal (position not already taken and within the dimension of board).
   * @param move  position to be checked
   * @return  true if legal, false if not
   */
  public boolean isLegalMove(int[] move) {
    int r = move[0];
    int c = move[1];

    if ((r < this.height) && (c < this.width) && (r >= 0) && (c >=0)
      && (this.returnPosition(move) == Game.EMPTY)) {
        return true;
      }
    else {
      return false;
    }
  }

  /**
   * Add a move to the board.
   * @param move    position where the move will be added
   * @param symbol  player symbol to be added at position
   * @return  true if added successfully (legal move), false if not
   */
  public boolean addMove(int[] move, char symbol) {
    if (this.isLegalMove(move)) {
      this.board[move[0]][move[1]] = symbol;
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Represent board with string.
   * @return String object which visually represents the board.
   */
  @Override
  public String toString() {
    String s = "  ";

    // make column header
    for (int col = 0; col < this.width; col++) {
      s +=  "|" + String.format("%02d", col);
    }
    s += "\n";

    for (int row = 0; row < this.height; row++) {
      // row header
      s += String.format("%02d", row);

      //print out character on board, separated by '|'
      for (int col = 0; col < this.width; col++) {
        s += "| " + this.board[row][col];
      }
      s += "\n";
    }

    return s;
  }

  /**
   * Given a move, define the search range around the move in which the program
   * can check for a win condition.
   * @param lastMove  move just made
   * @return  search range to check for win condition
   */
  public SearchRange calculateSearchRange(int[] lastMove) {
    int topRow, botRow, leftCol, rightCol;

    // use min and max to make sure search range is not out of bound
    topRow = Math.max(0, lastMove[0] - (Game.WIN_CONDITION-1));
    leftCol = Math.max(0, lastMove[1] - (Game.WIN_CONDITION-1));
    botRow = Math.min(this.height - 1, lastMove[0] + (Game.WIN_CONDITION-1));
    rightCol = Math.min(this.width - 1, lastMove[1] + (Game.WIN_CONDITION-1));

    return new SearchRange(topRow, botRow, leftCol, rightCol);
  }

  /**
   * Given an array and a symbol, count the maximum amount of time the symbol appears consecutively.
   * @param array   array to be searched
   * @param symbol  symbol to be searched
   * @return max number of time the symbol appears consecutively
   */
  public int countConsecutive(char[] array, char symbol) {
    int consecutiveCount = 0, maxCount = 0;
    boolean inStreak = false;

    for (char c: array) {
      if (c == symbol) {
        // if symbol found, start count and start streak
        consecutiveCount++;
        inStreak = true;
      }
      else {
        // once streak is broken, compare count with previous count to find the max, then reset
        if (inStreak) {
          inStreak = false;
          maxCount = Math.max(consecutiveCount, maxCount);
          consecutiveCount = 0;
        }
      }
    }
    maxCount = Math.max(consecutiveCount, maxCount);
    return maxCount;
  }

  /**
   * On the row the move was made, count the maximum number of times the symbol was repeated consecutively.
   * @param move    move made
   * @param search  search range around the move
   * @return  length of maximum consecutive streak of the symbol in row
   */
  public int checkConsecutiveHorizontal(int[] move, SearchRange search) {
    char[] array;

    // get array subset containing the search range on the row move is made
    array = Arrays.copyOfRange(this.board[move[0]], search.leftCol, search.rightCol+1);
    return countConsecutive(array, this.returnPosition(move));
  }

  /**
   * On the column the move was made, count the maximum number of times the symbol was repeated consecutively.
   * @param move    move made
   * @param search  search range around the move
   * @return  length of maximum consecutive streak of the symbol in column
   */
  public int checkConsecutiveVertical(int [] move, SearchRange search) {
   char[] array = new char[Game.WIN_CONDITION*2 -1];
   int i = 0;

   int[] position = new int[2];
   position[1] = move[1];

   // get array subset on the column the row was made
    for (int row = search.topRow; row <= search.botRow; row++) {
      position[0] = row;
      array[i] = this.returnPosition(position);
      i++;
    }
    return countConsecutive(array, this.returnPosition(move));
  }

  /**
   * On the 2 diagonals the move was made, count the maximum number of times the symbol was repeated consecutively.
   * @param move    move made
   * @param search  search range around the move
   * @return  length of maximum consecutive streak of the symbol in diagonals
   */
  public int checkConsecutiveDiag(int [] move, SearchRange search) {
    int[] position = new int[2];
    // check northeast
    char[] arrayNE = new char[Game.WIN_CONDITION];
    // construct diagonal array northeast by traversing from move to top left, inclusive
    for (int i = 0, row = move[0], col = move[1];
         (row >= search.topRow) && (col >= search.leftCol);
         row--, col--, i++) {
      position[0] = row;
      position[1] = col;
      arrayNE[i] = this.returnPosition(position);
    }

    // check southwest
    char[] arraySW = new char[Game.WIN_CONDITION-1];
    // construct diagonal array southwest by traversing from move (exclusive) to bottom right
    for (int i = 0, row = move[0] + 1, col = move[1] + 1;
         (row <= search.botRow) && (col <= search.rightCol);
         row++, col++, i++) {
      position[0] = row;
      position[1] = col;
      arraySW[i] = this.returnPosition(position);
    }

    // check northwest
    char[] arrayNW = new char[Game.WIN_CONDITION];
    // construct diagonal array northwest by traversing from move (inclusive) to top right
    for (int i = 0, row = move[0], col = move[1];
         (row >= search.topRow) && (col <= search.rightCol);
         row--, col++, i++) {
      position[0] = row;
      position[1] = col;
      arrayNW[i] = this.returnPosition(position);
    }

    // check northwest
    char[] arraySE = new char[Game.WIN_CONDITION-1];
    // construct diagonal array southeast by traversing from move (exclusive) to bottom left
    for (int i = 0, row = move[0] + 1, col = move[1] - 1;
         (row <= search.botRow) && (col >= search.leftCol);
         row++, col--, i++) {
      position[0] = row;
      position[1] = col;
      arraySE[i] = this.returnPosition(position);
    }

    char symbol = this.returnPosition(move);
    // adding northeast and southwest diagonals, and compare it with northwest and southeast
    return Math.max(countConsecutive(arrayNE, symbol) + countConsecutive(arraySW, symbol),
            countConsecutive(arraySE, symbol)+countConsecutive(arrayNW, symbol));
  }

  /**
   * Check if the last move made result in a win condition for last Player.
   * @param lastMove  last move made
   * @return  true if win condition is met, false if not
   */
  public boolean checkWin(int[] lastMove) {
    SearchRange range = this.calculateSearchRange(lastMove);

    // check through the horizontal, vertical, and diagonals to see if win condition is met
    if ((this.checkConsecutiveHorizontal(lastMove, range) >= Game.WIN_CONDITION)
    || (this.checkConsecutiveVertical(lastMove, range) >= Game.WIN_CONDITION)
    || (this.checkConsecutiveDiag(lastMove, range) >= Game.WIN_CONDITION)) {
      return true;
    }
    else {
      return false;
    }
  }
}
