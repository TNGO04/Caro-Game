package caro;

import java.util.Arrays;

/**
 * Class representing GameBoard object for Caro.
 */
public class GameBoard {
  private final int width; //number of columns
  private final int height; //number of rows
  private char[][] board;
  private int searchSize = Game.WIN_CONDITION * 2 - 1;

  /**
   * Constructor for GameBoard class.
   *
   * @param width  number of columns
   * @param height number of rows
   * @throws IllegalArgumentException if width and height are smaller than minimum dimension
   */
  public GameBoard(int width, int height) throws IllegalArgumentException {
    if ((width < Game.MINDIM) || (height < Game.MINDIM)) {
      throw new IllegalArgumentException("Dimension input is below minimum");
    }

    if ((width > Game.MAXDIM) || (height > Game.MAXDIM)) {
      throw new IllegalArgumentException("Dimension input exceeds maximum");
    }

    this.width = width;
    this.height = height;
    this.board = new char[height][width];
  }

  /**
   * Initialize board with empty character.
   */
  public void initializeBoard() {
    for (int row = 0; row < this.height; row++) {
      for (int col = 0; col < this.width; col++) {
        this.board[row][col] = Game.EMPTY;
      }
    }
  }

  /**
   * Return the character at a certain position on board.
   *
   * @param move position at which the character will be returned
   * @return character at position
   * @throws IllegalArgumentException if move is out of range on the board
   */
  public char returnPosition(int[] move) throws IllegalArgumentException {
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
   *
   * @param move position to be checked
   * @return true if legal, false if not
   */
  public boolean isLegalMove(int[] move) {
    int r = move[0];
    int c = move[1];

    // check that move is within board's dimension
    if ((r < this.height) && (c < this.width) && (r >= 0) && (c >= 0)
            // check that position is not already taken
            && (this.returnPosition(move) == Game.EMPTY)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Add a move to the board.
   *
   * @param move   position where the move will be added
   * @param symbol player symbol to be added at position
   * @return true if added successfully (legal move), false if not
   */
  public boolean addMove(int[] move, char symbol) {
    // if move is legal, add symbol to board
    if (this.isLegalMove(move)) {
      this.board[move[0]][move[1]] = symbol;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Represent board with string.
   *
   * @return String object which visually represents the board.
   */
  @Override
  public String toString() {
    String s = "  ";

    // make column header
    for (int col = 0; col < this.width; col++) {
      s += "|" + String.format("%02d", col);
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
   * <p>
   * The program only needs to check WIN_CONDITION - 1 spaces around the last-made move in the
   * horizontal, vertical, and diagonal dimension; therefore, a search range is calculated to
   * make the win condition checking process more efficient.
   *
   * @param lastMove move just made
   * @return search range to check for win condition
   */
  public SearchRange calculateSearchRange(int[] lastMove) {
    int topRow, botRow, leftCol, rightCol;

    // use min and max to make sure search range is not out of the dimension of the board
    topRow = Math.max(0, lastMove[0] - (Game.WIN_CONDITION - 1));
    leftCol = Math.max(0, lastMove[1] - (Game.WIN_CONDITION - 1));
    botRow = Math.min(this.height - 1, lastMove[0] + (Game.WIN_CONDITION - 1));
    rightCol = Math.min(this.width - 1, lastMove[1] + (Game.WIN_CONDITION - 1));

    return new SearchRange(topRow, botRow, leftCol, rightCol);
  }

  /**
   * Given an array and a symbol, count the maximum amount of time the symbol appears consecutively.
   *
   * @param array  array to be searched
   * @param symbol symbol to be searched
   * @return max number of time the symbol appears consecutively
   */
  public int countConsecutive(char[] array, char symbol) {
    // initialize count and streak tracker
    int consecutiveCount = 0, maxCount = 0;
    boolean inStreak = false;

    // loop through elements of array and compare with symbol
    for (char c : array) {
      if (c == symbol) {
        // if symbol found, start count and turn streak tracker to true
        consecutiveCount++;
        inStreak = true;
      } else {
        // once streak is broken, compare count with previous max count to find the max, then reset
        if (inStreak) {
          inStreak = false;
          maxCount = Math.max(consecutiveCount, maxCount);
          consecutiveCount = 0;
        }
      }
    }
    /* in the case the last element of the array contains the symbol and streak was not terminated,
     * compare current streak with max count one last time */
    maxCount = Math.max(consecutiveCount, maxCount);
    return maxCount;
  }

  /**
   * On the row of the move, within search range, count the maximum number of times the symbol was
   * repeated consecutively.
   *
   * @param move   move made
   * @param search search range around the move
   * @return length of maximum consecutive streak of the symbol in row
   */
  public int checkConsecutiveHorizontal(int[] move, SearchRange search) {
    char symbol = this.returnPosition(move);
    /* blockCount keeps track of if the consecutive streak is blocked by the board's edge or opponent
        on no, one, or both sides */
    int count = 1, blockCount = 0;

    // traverse from move to horizontal right
    for (int col = move[1] + 1; col <= search.rightCol; col++) {
      if (this.board[move[0]][col] == symbol) {
        count++;
      } else {
        if (this.board[move[0]][col] != ' ') {
          blockCount++;
        }
        break;
      }
    }
    // traverse from move to horizontal left
    for (int col = move[1] - 1; col >= search.leftCol; col--) {
      if (this.board[move[0]][col] == symbol) {
        count++;
      } else {
        if (this.board[move[0]][col] != ' ') {
          blockCount++;
        }
        break;
      }
    }

    // if move is blocked on both sides, consecutive streak is 0
    if (blockCount == 2) {
      count = 0;
    }
    return count;
  }

  /**
   * On the column of the move, within the search range, count the maximum number of times the
   * symbol was repeated consecutively.
   *
   * @param move   move made
   * @param search search range around the move
   * @return length of maximum consecutive streak of the symbol
   */
  public int checkConsecutiveVertical(int[] move, SearchRange search) {
    char symbol = this.returnPosition(move);
    /* blockCount keeps track of if the consecutive streak is blocked by the board's edge or opponent
      on no, one, or both sides */
    int count = 1, blockCount = 0;

    // traverse from move to bottom row
    for (int row = move[0] + 1; row <= search.botRow; row++) {
      if (this.board[row][move[1]] == symbol) {
        count++;
      } else {
        if (this.board[row][move[1]] != ' ') {
          blockCount++;
        }
        break;
      }
    }
    // traverse from move to top row
    for (int row = move[0] - 1; row >= search.topRow; row--) {
      if (this.board[row][move[1]] == symbol) {
        count++;
      } else {
        if (this.board[row][move[1]] != ' ') {
          blockCount++;
        }
        break;
      }
    }

    // if streak is blocked on both sides, reset count to 0
    if (blockCount == 2) {
      count = 0;
    }
    return count;
  }

  /**
   * On the 2 diagonals the move was made, within search range, count the maximum number of times
   * the symbol was repeated consecutively.
   *
   * @param move   move made
   * @param search search range around the move
   * @return length of maximum consecutive streak of the symbol in diagonals
   */
  public int checkConsecutiveDiag(int[] move, SearchRange search) {
    char symbol = this.returnPosition(move);
    /* blockCount keeps track of if the consecutive streak is blocked by the board's edge or opponent
      on no, one, or both sides */
    int count1 = 1, blockCount1 = 0;
    // traverse diagonally from move to top left
    for (int row = move[0] - 1, col = move[1] - 1;
         (row >= search.topRow) && (col >= search.leftCol);
         row--, col--) {
      if (this.board[row][col] == symbol) {
        count1++;
      } else {
        if (this.board[row][col] != ' ') {
          blockCount1++;
        }
        break;
      }
    }
    //  traverse diagonally from move to bottom right
    for (int row = move[0] + 1, col = move[1] + 1;
         (row <= search.botRow) && (col <= search.rightCol);
         row++, col++) {
      if (this.board[row][col] == symbol) {
        count1++;
      } else {
        if (this.board[row][col] != ' ') {
          blockCount1++;
        }
        break;
      }
    }
    // traverse diagonally from move to top right
    int count2 = 1, blockCount2 = 0;
    // construct diagonal array northwest by traversing from move (inclusive) to top right
    for (int row = move[0] - 1, col = move[1] + 1;
         (row >= search.topRow) && (col <= search.rightCol);
         row--, col++) {
      if (this.board[row][col] == symbol) {
        count2++;
      } else {
        if (this.board[row][col] != ' ') {
          blockCount2++;
        }
        break;
      }
    }
    // traverse diagonally from move to bottom left
    for (int row = move[0] + 1, col = move[1] - 1;
         (row <= search.botRow) && (col >= search.leftCol);
         row++, col--) {
      if (this.board[row][col] == symbol) {
        count2++;
      } else {
        if (this.board[row][col] != ' ') {
          blockCount2++;
        }
        break;
      }
    }

    // if streak is blocked on both sides, reset count to 0
    if (blockCount1 == 2) {
      count1 = 0;
    }
    if (blockCount2 == 2) {
      count2 = 0;
    }
    //return max count of the two diagonals
    return Math.max(count1, count2);
  }

  /**
   * Check if the last move made result in a win condition for last Player.
   *
   * @param lastMove last move made
   * @return true if win condition is met, false if not
   */
  public boolean checkWin(int[] lastMove) {
    SearchRange range = this.calculateSearchRange(lastMove);

    // check through the horizontal, vertical, and diagonals to see if win condition is met
    if ((this.checkConsecutiveHorizontal(lastMove, range) >= Game.WIN_CONDITION)
            || (this.checkConsecutiveVertical(lastMove, range) >= Game.WIN_CONDITION)
            || (this.checkConsecutiveDiag(lastMove, range) >= Game.WIN_CONDITION)) {
      return true;
    } else {
      return false;
    }
  }
}
