package caro.board;

/**
 * BoardSubset stores row and column indexes that describe a portion of the board.
 */
public class BoardSubset {
  private final int topRow;
  private final int botRow;
  private final int leftCol;
  private final int rightCol;

  /**
   * Constructor.
   *
   * @param topRow   row coordinate at the top of subset
   * @param botRow   row coordinate at the bottom of subset
   * @param leftCol  column coordinate leftmost of subset
   * @param rightCol column coordinate rightmost of subset
   */
  public BoardSubset(int topRow, int botRow, int leftCol, int rightCol)
          throws IllegalArgumentException {
    if ((topRow < 0) || (botRow < 0) || (leftCol < 0) || (rightCol < 0)) {
      throw new IllegalArgumentException("BoardSubset parameter cannot be negative");
    }

    this.topRow = topRow;
    this.botRow = botRow;
    this.leftCol = leftCol;
    this.rightCol = rightCol;
  }

  /**
   * Constructor.
   * Given a center, define the board subset that includes all moves that are RADIUS moves away
   * from the center.
   *
   * @param center center of board subset
   * @param radius   radius of board subset.
   */
  public BoardSubset(int[] center, int boundDimension, int radius)
          throws IllegalArgumentException {
    if (radius < 0) {
      throw new IllegalArgumentException("Search radius cannot be negative.");
    }

    int centerRow = center[0];
    int centerCol = center[1];

    // use min and max to make sure board subset indexes are not beyond the dimension of the board
    this.topRow = Math.max(0, centerRow - (radius - 1));
    this.leftCol = Math.max(0, centerCol - (radius - 1));
    this.botRow = Math.min(boundDimension - 1, centerRow + (radius - 1));
    this.rightCol = Math.min(boundDimension - 1, centerCol + (radius - 1));
  }
  
  /**
   * Getter for topRow.
   *
   * @return topRow
   */
  public int getTopRow() {
    return this.topRow;
  }

  /**
   * Getter of botRow.
   *
   * @return botRow
   */
  public int getBotRow() {
    return this.botRow;
  }

  /**
   * Getter for leftCol.
   *
   * @return leftCol
   */
  public int getLeftCol() {
    return this.leftCol;
  }

  /**
   * Getter of rightCol.
   *
   * @return rightCol
   */
  public int getRightCol() {
    return this.rightCol;
  }
}
