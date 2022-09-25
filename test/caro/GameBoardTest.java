package caro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for GameBoard.
 */
public class GameBoardTest {
  private final int WIDTH = 10;
  private final int HEIGHT = 10;
  private GameBoard board;

  /**
   * Set up test case.
   */
  @Before
  public void setUp() {
    board = new GameBoard(WIDTH, HEIGHT);
  }

  /**
   * Test for bad construction of GameBoard object.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadConstruction() {
    new GameBoard(2, 2);
  }

  /**
   * Test for illegal argument for returnPosition() method.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadArgument() {
    board.returnPosition(new int[]{20, 18});
  }

  /**
   * Test initializeBoard() method.
   */
  @Test
  public void testInitializeBoard() {
    this.board.initializeBoard();

    for (int row = 0; row < this.HEIGHT; row++) {
      for (int col = 0; col < this.WIDTH; col++) {
        assertEquals(Game.EMPTY, this.board.returnPosition(new int[]{row, col}));
      }
    }
  }

  /**
   * Test returnPosition() method.
   */
  @Test
  public void testReturnPosition() {
    this.board.initializeBoard();
    assertEquals(Game.EMPTY, this.board.returnPosition(new int[]{0, 0}));
  }

  /**
   * Test isLegalMove() method.
   */
  @Test
  public void isLegalMove() {
    this.board.initializeBoard();

    int[] move = new int[]{1, 4};
    assertTrue(board.isLegalMove(move));
    board.addMove(move, Game.X);
    assertFalse(board.isLegalMove(move));

    assertFalse(board.isLegalMove(new int[]{20, 10}));
  }

  /**
   * Test addMove().
   */
  @Test
  public void addMove() {
    board.initializeBoard();
    int[] move = new int[]{1, 1};
    assertTrue(board.addMove(move, Game.X));
    assertEquals(Game.X, board.returnPosition(move));

    assertFalse(board.addMove(move, Game.X));
  }

  /**
   * Test calculateSearchRange().
   */
  @Test
  public void testCalculateSearchRange() {

    //test case: close to top left
    SearchRange range1 = board.calculateSearchRange(new int[]{0, 0});
    assertEquals(range1.botRow, 4);
    assertEquals(range1.rightCol, 4);
    assertEquals(range1.topRow, 0);
    assertEquals(range1.leftCol, 0);

    //test case: close to bottom right
    SearchRange range2 = board.calculateSearchRange(new int[]{8, 8});
    assertEquals(range2.botRow, 9);
    assertEquals(range2.rightCol, 9);
    assertEquals(range2.topRow, 4);
    assertEquals(range2.leftCol, 4);

    // test case: middle of board
    SearchRange range3 = board.calculateSearchRange(new int[]{4, 4});
    assertEquals(range3.botRow, 8);
    assertEquals(range3.rightCol, 8);
    assertEquals(range3.topRow, 0);
    assertEquals(range3.leftCol, 0);
  }

  /**
   *
   */
  @Test
  public void testCountConsecutive() {
    char[] array1 = new char[]{' ', 'X', 'X', ' ', ' ', 'X', 'X', 'X', 'X'};
    assertEquals(board.countConsecutive(array1, 'X'), 4);

    char[] array2 = new char[12];
    array2[0] = ' ';
    array2[1] = 'X';
    array2[4] = 'X';
    array2[5] = 'X';

    assertEquals(board.countConsecutive(array2, 'X'), 2);
  }

  /**
   * Test checkConsecutiveHorizontal().
   */
  @Test
  public void testCheckConsecutiveHorizontal() {
    board.initializeBoard();
    int[] move = new int[]{0, 0};
    board.addMove(move, Game.X);
    assertEquals(1, board.checkConsecutiveHorizontal(move, board.calculateSearchRange(move)));

    board.addMove(new int[]{0, 1}, Game.X);
    board.addMove(new int[]{0, 2}, Game.X);
    board.addMove(new int[]{0, 3}, Game.X);
    move[1] = 4;
    board.addMove(move, Game.X);

    assertEquals(5, board.checkConsecutiveHorizontal(move, board.calculateSearchRange(move)));
  }

  /**
   * Test checkConsecutiveVertical().
   */
  @Test
  public void testCheckConsecutiveVertical() {
    board.initializeBoard();
    int[] move = new int[]{0, 0};
    board.addMove(move, Game.X);
    assertEquals(1, board.checkConsecutiveVertical(move, board.calculateSearchRange(move)));

    board.addMove(new int[]{1, 0}, Game.X);
    board.addMove(new int[]{2, 0}, Game.X);
    board.addMove(new int[]{3, 0}, Game.X);
    move[0] = 4;
    board.addMove(move, Game.X);

    assertEquals(5, board.checkConsecutiveVertical(move, board.calculateSearchRange(move)));
    //board.addMove(new int[]{5, 0}, Game.O);
    //assertEquals(0, board.checkConsecutiveVertical(move, board.calculateSearchRange(move)));
  }

  /**
   * Test checkConsecutiveDiag().
   */
  @Test
  public void testCheckConsecutiveDiag() {
    board.initializeBoard();
    int[] move = new int[]{0, 0};
    board.addMove(move, Game.X);
    assertEquals(1, board.checkConsecutiveDiag(move, board.calculateSearchRange(move)));

    move = new int[]{1, 1};
    board.addMove(move, Game.X);
    assertEquals(2, board.checkConsecutiveDiag(move, board.calculateSearchRange(move)));

    board.addMove(new int[]{2, 3}, Game.X);
    board.addMove(new int[]{1, 4}, Game.X);
    board.addMove(new int[]{4, 1}, Game.X);
    board.addMove(new int[]{4, 3}, Game.X);
    board.addMove(new int[]{3, 2}, Game.X);
    move = new int[]{3, 2};
    assertEquals(4, board.checkConsecutiveDiag(move, board.calculateSearchRange(move)));

    board.addMove(new int[]{0, 5}, Game.O);
    board.addMove(new int[]{5, 0}, Game.O);
    assertEquals(2, board.checkConsecutiveDiag(move, board.calculateSearchRange(move)));
  }

  /**
   * Test checkWin().
   */
  @Test
  public void testCheckWin() {
    testCheckConsecutiveHorizontal();

    int[] lastMove = new int[]{0, 4};
    assertTrue(board.checkWin(lastMove));
  }

  public static void main(String[] args) {
    GameBoard board = new GameBoard(10, 10);
    board.initializeBoard();
    board.addMove(new int[]{1, 1}, Game.X);
    board.addMove(new int[]{2, 2}, Game.X);
    board.addMove(new int[]{3, 3}, Game.X);

    System.out.println(board.toString());
  }
}