package caro.board;

import caro.Game;
import caro.Player;
import caro.streak.StreakList;
import java.util.ArrayList;
import java.util.List;


/**
 * Class representing GameBoard object for Caro.
 */
public class GameBoard {
  public static final int MINDIM = 5, MAXDIM = 99;
  private final int boardDimension; //number of columns/rows of board
  private final char[][] board;

  /**
   * Constructor for GameBoard.
   *
   * @param boardDimension boardDimension of board (height/width)
   * @throws IllegalArgumentException if boardDimension is not within range
   */
  public GameBoard(int boardDimension) throws IllegalArgumentException {
    if (boardDimension < MINDIM) {
      throw new IllegalArgumentException("BoardDimension input is below minimum");
    } else if (boardDimension > MAXDIM) {
      throw new IllegalArgumentException("BoardDimension input exceeds maximum");
    }
    this.boardDimension = boardDimension;
    this.board = new char[boardDimension][boardDimension];
  }

  /**
   * Copy constructor.
   *
   * @param other GameBoard object to be copied
   */
  public GameBoard(GameBoard other) throws IllegalArgumentException {
    this(other.getBoardDimension());
    for (int i = 0; i < other.getBoardDimension(); i++) {
      this.board[i] = other.board[i].clone();
    }
  }

  /**
   * Getter for boardDimension.
   *
   * @return boardDimension
   */
  public int getBoardDimension() {
    return this.boardDimension;
  }

  /**
   * Initialize board with empty character.
   */
  public void initializeBoard() {
    for (int row = 0; row < this.getBoardDimension(); row++) {
      for (int col = 0; col < this.getBoardDimension(); col++) {
        this.board[row][col] = Game.EMPTY;
      }
    }
  }

  /**
   * Check if the board is out of moves (filled with non-blank symbol).
   *
   * @return true if board is filled, false if not
   */
  public boolean isOutOfMoves() {
    for (int row = 0; row < this.getBoardDimension(); row++) {
      for (int col = 0; col < this.getBoardDimension(); col++) {
        if (this.isEmpty(row, col)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Check if position/move is within the bounds of board.
   *
   * @param row row of position
   * @param col col of position
   * @return true if on board, false if out of bounds
   */
  public boolean isOnBoard(int row, int col) {
    return ((row < this.boardDimension) && (col < this.boardDimension) && (row >= 0) && (col >= 0));
  }

  /**
   * Check if position/move is within the bounds of board.
   *
   * @param move move/position to be checked
   * @return true if on board, false if out of bounds
   */
  public boolean isOnBoard(int [] move) {
    return isOnBoard(move[0], move[1]);
  }

  /**
   * Check if a position on board is empty. 
   *
   * @param row row of position
   * @param col col of position
   * @return true if position is empty, false if not
   */
  public boolean isEmpty(int row, int col) {
    return (this.returnPosition(row, col) == Game.EMPTY);
  }

  /**
   * Return the character at a certain position on board.
   *
   * @param col column of position
   * @param row row of position
   * @return character at position
   * @throws IllegalArgumentException if move is out of range on the board
   */
  public char returnPosition(int row, int col) throws IllegalArgumentException {
    if (!isOnBoard(row, col)) {
      throw new IllegalArgumentException("Position is out of board range");
    }
    return this.board[row][col];
  }

  /**
   * Return the character at a certain position on board.
   *
   * @param move position at which the character will be returned
   * @return character at position
   * @throws IllegalArgumentException if move is out of range on the board
   */
  public char returnPosition(int[] move) throws IllegalArgumentException {
    return this.returnPosition(move[0], move[1]);
  }

  /**
   * Check if a move is legal (position not already taken and within the boardDimension of board).
   *
   * @param row row of position to be checked
   * @param col col of position to be checked
   * @return true if legal, false if not
   */
  public boolean isLegalMove(int row, int col) {
    // check that move is within board's dimension and is not already taken
    return ((this.isOnBoard(row, col)) && (this.isEmpty(row, col)));
  }

  /**
   * Check if a move is legal (position not already taken and within the boardDimension of board).
   *
   * @param move position to be checked
   * @return true if legal, false if not
   */
  public boolean isLegalMove(int[] move) {
    return isLegalMove(move[0], move[1]);
  }

  /**
   * Add a move to the board.
   *
   * @param row    row of position where the move will be added
   * @param col    col of position
   * @param symbol player symbol to be added at position
   * @return true if added successfully (legal move), false if not
   */
  public boolean addMove(int row, int col, char symbol) {
    if (symbol == Game.EMPTY) {
      System.out.println("Player symbol cannot be blank.\n");
      return false;
    }
    // if move is legal, add symbol to board
    if (this.isLegalMove(row, col)) {
      this.board[row][col] = symbol;
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
    return addMove(move[0], move[1], symbol);
  }

  /**
   * Check if a position is adjacent to any move currently on board.
   *
   * @param row row of position
   * @param col col of position
   * @return true if position is adjacent only to empty cells, false if not
   *          a move off-board is technically defined as disconnected because it is not adjacent to
   *          any other moves
   */
  public boolean isDisconnected(int row, int col) {
    if (this.isLegalMove(row, col)) {
      int above = Math.max(0, row - 1);
      int below = Math.min(this.boardDimension - 1, row + 1);
      int left = Math.max(0, col - 1);
      int right = Math.min(this.boardDimension - 1, col + 1);

      return ((this.isEmpty(above, left)) && (this.isEmpty(above, col))
              && (this.isEmpty(above, right)) && (this.isEmpty(row, left))
              && (this.isEmpty(row, right)) && (this.isEmpty(below, left))
              && (this.isEmpty(below, col)) && (this.isEmpty(below, right)));
    }
    return true;
  }

  /**
   * Return an array representing a row of board.
   *
   * @param row row index
   * @return char array representing row
   */
  public char[] getRow(int row) throws IllegalArgumentException {
    if ((row < 0) || (row > this.boardDimension)) {
      throw new IllegalArgumentException("Row index is out of board.");
    }
    return this.board[row];
  }

  /**
   * Return an array representing a column of board.
   *
   * @param col row index
   * @return char array representing column
   */
  public char[] getColumn(int col) throws IllegalArgumentException {
    if ((col < 0) || (col > this.boardDimension)) {
      throw new IllegalArgumentException("Column index is out of board.");
    }
    char[] array = new char[this.boardDimension];
    for (int row = 0; row < this.boardDimension; row++) {
      array[row] = this.returnPosition(row, col);
    }
    return array;
  }

  /**
   * Get an array representing the diagonal between two position 1 and 2.
   *
   * @param row1 row of position 1
   * @param col1 col of position 1
   * @param row2 row of position 2
   * @param col2 col of position 2
   * @return array representing diagonal between 2 points, null if points are not diagonal
   */
  public char[] getDiagonal(int row1, int col1, int row2, int col2) {
    boolean isDiagonal = (((row1 + col1) == (row2 + col2)) || ((row1 - col1) == (row2 - col2)));

    if (isOnBoard(row1, col1) && isOnBoard(row2, col2)
            && isDiagonal) {
      int arrayLength = Math.abs(row1 - row2) + 1;
      char[] array = new char[arrayLength];
      int directionRow = Integer.compare(row1, row2);
      int directionCol = Integer.compare(col1, col2);

      for (int row = row1, col = col1, i = 0; i < arrayLength;
           row -= directionRow, col -= directionCol, i++) {
        array[i] = this.returnPosition(row, col);
      }
      return array;
    }
    return null;
  }

  /**
   * Represent board with string, highlighting last move.
   *
   * @param r   row of last move
   * @param c   column of last move
   * @return String object which visually represents the board.
   */
  public String toString(int r, int c) {
    // make top border
    String s = "____";
    for (int col = 0; col < this.getBoardDimension(); col++) {
      s += "___";
    }
    s += "\n";
    s += "|  |";
    // make column header
    for (int col = 0; col < this.getBoardDimension(); col++) {
      s += String.format("%02d", col) + "|";
    }
    s += "\n";
    for (int row = 0; row < this.getBoardDimension(); row++) {
      // row header
      s += String.format("|%02d|", row);
      //print out character on board, separated by '|'
      for (int col = 0; col < this.getBoardDimension(); col++) {
        if ((r == row) && (c == col)) {
          s += this.returnPosition(row, col) + "_|";
        }
        else {
          s += this.returnPosition(row, col) + " |";
        }
      }
      s += "\n";
    }
    // make bottom border
    s += "----";
    for (int col = 0; col < this.getBoardDimension(); col++) {
      s += "---";
    }
    s += "\n";
    return s;
  }

  /**
   * Print out board without emphasizing last move.
   *
   * @return  String object representing board.
   */
  @Override
  public String toString() {
    return toString(-1, -1);
  }

  /**
   * Find the length of the longest horizontal streak that move was a part of.
   *
   * @param move   represents a move made on the board
   * @param search board subset to search within
   * @return length of the longest horizontal streak that move was a part of
   */
  public int checkConsecutiveHorizontal(int[] move, BoardSubset search) {
    char symbol = this.returnPosition(move);
    int count = 1;
    // traverse horizontally from move to the right
    for (int col = move[1] + 1; col <= search.getRightCol(); col++) {
      if (this.returnPosition(move[0], col) == symbol) {
        count++;
      } else {
        break;
      }
    }
    // traverse horizontally from move to the left
    for (int col = move[1] - 1; col >= search.getLeftCol(); col--) {
      if (this.returnPosition(move[0], col) == symbol) {
        count++;
      } else {
        break;
      }
    }
    return count;
  }

  /**
   * Find the length of the longest vertical streak that move was a part of.
   *
   * @param move   represents a move made on the board
   * @param search board subset to search within
   * @return length of the longest vertical streak that move was a part of
   */
  public int checkConsecutiveVertical(int[] move, BoardSubset search) {
    char symbol = this.returnPosition(move);
    int count = 1;

    // traverse from move to bottom row
    for (int row = move[0] + 1; row <= search.getBotRow(); row++) {
      if (this.returnPosition(row, move[1]) == symbol) {
        count++;
      } else {
        break;
      }
    }
    // traverse from move to top row
    for (int row = move[0] - 1; row >= search.getTopRow(); row--) {
      if (this.returnPosition(row, move[1]) == symbol) {
        count++;
      } else {
        break;
      }
    }
    return count;
  }

  /**
   * On the 2 diagonals the move was made, within board subset, find the length of the longest
   * diagonal streak that move was a part of.
   *
   * @param move   move made
   * @param search board subset around the move
   * @return length of the longest diagonal streak that move was a part of
   */
  public int checkConsecutiveDiag(int[] move, BoardSubset search) {
    char symbol = this.returnPosition(move);
    int count1 = 1;
    // traverse diagonally from move to top left
    for (int row = move[0] - 1, col = move[1] - 1;
         (row >= search.getTopRow()) && (col >= search.getLeftCol());
         row--, col--) {
      if (this.returnPosition(row, col) == symbol) {
        count1++;
      } else {
        break;
      }
    }
    //  traverse diagonally from move to bottom right
    for (int row = move[0] + 1, col = move[1] + 1;
         (row <= search.getBotRow()) && (col <= search.getRightCol());
         row++, col++) {
      if (this.returnPosition(row, col) == symbol) {
        count1++;
      } else {
        break;
      }
    }
    // traverse diagonally from move to top right
    int count2 = 1;
    // construct diagonal array northwest by traversing from move (inclusive) to top right
    for (int row = move[0] - 1, col = move[1] + 1;
         (row >= search.getTopRow()) && (col <= search.getRightCol());
         row--, col++) {
      if (this.returnPosition(row, col) == symbol) {
        count2++;
      } else {
        break;
      }
    }
    // traverse diagonally from move to bottom left
    for (int row = move[0] + 1, col = move[1] - 1;
         (row <= search.getBotRow()) && (col >= search.getLeftCol());
         row++, col--) {
      if (this.returnPosition(row, col) == symbol) {
        count2++;
      } else {
        break;
      }
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
  public boolean checkWinningMove(int[] lastMove) {
    return (this.checkMaximumConsecutive(lastMove) >= Game.WIN_CONDITION);
  }

  /**
   * Find the longest streak length the last move was a part of.
   *
   * @param lastMove last move made on board
   * @return longest streak length containing last move
   */
  public int checkMaximumConsecutive(int[] lastMove) {
    BoardSubset range = new BoardSubset(lastMove, this.getBoardDimension(), Game.WIN_CONDITION);
    return Math.max(Math.max(this.checkConsecutiveHorizontal(lastMove, range),
            this.checkConsecutiveVertical(lastMove, range)),
            this.checkConsecutiveDiag(lastMove, range));
  }

  /**
   * Given an array and a symbol, count the maximum amount of time the symbol appears consecutively.
   * This function ignores streaks that are blocked on both sides. A streak is blocked on one side
   * if it is impossible to expand the streak on that side.
   * e.g: XXXO__OXXXO_XXX:
   * first cluster of XXX: blocked both sides
   * second cluster of XXX: blocked both sides
   * third cluster of XXX:  blocked on the right

   * e.g.: _XXX_OXXX_OXXX -> maxStreakSize = 3, streakCount = 2, unblockedCount = 1
   * first cluster: unblocked both sides
   * second cluster: counted in streakCount because blocked only on one side
   * third cluster: not counted in streakCount because blocked on both sides
   *
   * @param array  array to be searched
   * @param symbol symbol to be searched
   * @return max number of time the symbol appears consecutively
   */
  public StreakList countConsecutive(char[] array, char symbol) {
    int arrayLength = array.length;
    // initialize max streak size, and count of the number of streaks in array that is of that size
    StreakList list = new StreakList();

    // initialize streak marker, block marker, and streak counter
    int blockMarker = 0, maxStreak = 0;
    boolean inStreak = false;

    // loop through elements of array and detect streak
    for (int i = 0; i < arrayLength; i++) {
      // if symbol found, start count and turn streak tracker to true
      if (array[i] == symbol) {
        maxStreak++;
        if (!inStreak) {
          inStreak = true;
          /* if streak is at the start or end of the array, or preceding character is not EMPTY,
           increase block marker*/
          if ((i == 0) || (array[i - 1] != Game.EMPTY)) {
            blockMarker++;
          }
        }
      } else {
        // if streak is broken
        if (inStreak) {
          inStreak = false;
          // if streaked is terminated not by an EMPTY symbol, increase block marker
          if (array[i] != Game.EMPTY) {
            blockMarker++;
          }
          list.addStreak(maxStreak, blockMarker); // update list
          blockMarker = 0;
          maxStreak = 0;
        }
      }
    }
    // Since there could be a streak at the end of array, run a check one last time
    if (inStreak) {
      blockMarker++;
      list.addStreak(maxStreak, blockMarker);
    }
    return list;
  }

  /**
   * Scan the board to check for streaks.
   *
   * @return StreakList object representing all valid streaks found on the board
   */
  public StreakList checkBoardForStreaks(Player player) {
    char symbol = player.getSymbol();
    int dimension = this.getBoardDimension();
    StreakList list = new StreakList();

    // check each row for streak
    for (int row = 0; row < dimension; row++) {
      list.addStreakList(this.countConsecutive(this.getRow(row), symbol));
    }

    // check each column for streak
    for (int col = 0; col < dimension; col++) {
      list.addStreakList(this.countConsecutive(this.getColumn(col), symbol));
    }

    // check diagonals for streaks
    for (int row = Game.WIN_CONDITION - 1, col = 0; row < dimension; row++) {
      list.addStreakList(this.countConsecutive(
              this.getDiagonal(row, col, col, row), symbol));
    }
    for (int col = 1, row = dimension - 1; col <= (dimension - Game.WIN_CONDITION); col++) {
      list.addStreakList(this.countConsecutive(
              this.getDiagonal(row, col, col, row), symbol));
    }
    for (int col = Game.WIN_CONDITION - 1, row = dimension - 1; col < dimension; col++) {
      list.addStreakList(this.countConsecutive(
              this.getDiagonal(row, col, row - col, 0), symbol));
    }
    for (int row = dimension - 2, col = this.getBoardDimension() - 1;
         row >= (Game.WIN_CONDITION - 1); row--) {
      list.addStreakList(this.countConsecutive(
              this.getDiagonal(row, col, 0, col - row), symbol));
    }
    return list;
  }

  /**
   * Return board object after a potential move is made.
   *
   * @param move move to be made
   * @return new board state
   */
  public GameBoard getBoardState(int[] move, Player player) throws IllegalArgumentException {
    // if move is illegal with current board state, throw exception
    if (!this.isLegalMove(move)) {
      throw new IllegalArgumentException("Invalid move");
    }

    //get deep copy of board and add move
    GameBoard newBoard = new GameBoard(this);
    newBoard.addMove(move, player.getSymbol());
    return newBoard;
  }
}

