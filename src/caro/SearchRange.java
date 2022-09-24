package caro;

/**
 * Search Range stores a range in which win condition is to be searched.
 */
public class SearchRange {
  public int topRow, botRow, leftCol, rightCol;

  /**
   * Constructor
   * @param topRow    row coordinate at the top of range
   * @param botRow    row coordinate at the bottom of range
   * @param leftCol   column coordinate leftmost of range
   * @param rightCol  column coordinate rightmost of range
   */
  public SearchRange(int topRow, int botRow, int leftCol, int rightCol) {
    this.topRow = topRow;
    this.botRow = botRow;
    this.leftCol = leftCol;
    this.rightCol = rightCol;
  }
}
