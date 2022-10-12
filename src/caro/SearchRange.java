package caro;

/**
 * Search Range stores a range in which win condition is to be searched.
 */
public class SearchRange {
  private final int topRow;
  private final int botRow;
  private final int leftCol;
  private final int rightCol;

  /**
   * Constructor.
   *
   * @param topRow   row coordinate at the top of range
   * @param botRow   row coordinate at the bottom of range
   * @param leftCol  column coordinate leftmost of range
   * @param rightCol column coordinate rightmost of range
   */
  public SearchRange(int topRow, int botRow, int leftCol, int rightCol)
          throws IllegalArgumentException {
    if ((topRow < 0) || (botRow < 0) || (leftCol < 0) || (rightCol < 0)) {
      throw new IllegalArgumentException("Search Range parameter cannot be negative");
    }

    this.topRow = topRow;
    this.botRow = botRow;
    this.leftCol = leftCol;
    this.rightCol = rightCol;
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
