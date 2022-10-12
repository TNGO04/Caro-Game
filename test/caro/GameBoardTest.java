package caro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * JUnit test class for GameBoard.
 */
public class GameBoardTest {
  private final int DIMENSION = 10;
  private GameBoard board;

  /**
   * Set up test case.
   */
  @Before
  public void setUp() {
    board = new GameBoard(DIMENSION);
  }

  /**
   * Test for bad construction of GameBoard object.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadConstruction() {
    new GameBoard(2);
  }

  /**
   * Test for GameBoard copy constructor.
   */
  @Test
  public void testCopyConstructor() {
    GameBoard boardCopy = new GameBoard(board);
    assertNotSame(board, boardCopy);

    boardCopy.initializeBoard();
    assertEquals(boardCopy.returnPosition(1,1), Game.EMPTY);
    assertFalse(board.returnPosition(1,1) == Game.EMPTY);
  }

  /**
   * Test getBoardDimension().
   */
  @Test
  public void testGetBoardDimension() {
    assertEquals(DIMENSION, board.getBoardDimension());
  }

  /**
   * Test for illegal argument for returnPosition() method.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadArgument() {
    board.returnPosition(20,18);
  }


  /**
   * Test initializeBoard() method.
   */
  @Test
  public void testInitializeBoard() {
    this.board.initializeBoard();

    for (int row = 0; row < this.DIMENSION; row++) {
      for (int col = 0; col < this.DIMENSION; col++) {
        assertEquals(Game.EMPTY, board.returnPosition(row, col));
      }
    }
  }

  /**
   * Test isOnBoard().
   */
  @Test
  public void testIsOnBoard() {
    assertFalse(board.isOnBoard(20,20));
    assertTrue(board.isOnBoard(new int[] {6,8}));
  }

  /**
   * Test returnPosition() method.
   */
  @Test
  public void testReturnPosition() {
    this.board.initializeBoard();
    assertEquals(Game.EMPTY, this.board.returnPosition(0, 0));
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

    assertFalse(board.isLegalMove(20, 10));
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
    assertFalse(board.addMove(20,20, Game.X));
  }

  /**
   * Test toString().
   */
  @Test
  public void testToString() {
    GameBoard smallBoard = new GameBoard(5);
    smallBoard.initializeBoard();
    smallBoard.addMove(1,1, Game.X);

    assertEquals(smallBoard.toString(),
                "___________________\n" +
                      "|  |00|01|02|03|04|\n" +
                      "|00|  |  |  |  |  |\n" +
                      "|01|  |X |  |  |  |\n" +
                      "|02|  |  |  |  |  |\n" +
                      "|03|  |  |  |  |  |\n" +
                      "|04|  |  |  |  |  |\n" +
                      "-------------------\n");
  }

  /**
   * Test calculateSearchRange().
   */
  @Test
  public void testCalculateSearchRange() {
    //test case: close to top left
    SearchRange range1 = board.calculateSearchRange(new int[]{0, 0}, Game.WIN_CONDITION);
    assertEquals(range1.getBotRow(), 4);
    assertEquals(range1.getRightCol(), 4);
    assertEquals(range1.getTopRow(), 0);
    assertEquals(range1.getLeftCol(), 0);

    //test case: close to bottom right
    SearchRange range2 = board.calculateSearchRange(new int[]{8, 8}, Game.WIN_CONDITION);
    assertEquals(range2.getBotRow(), 9);
    assertEquals(range2.getRightCol(), 9);
    assertEquals(range2.getTopRow(), 4);
    assertEquals(range2.getLeftCol(), 4);

    // test case: middle of board
    SearchRange range3 = board.calculateSearchRange(new int[]{4, 4}, Game.WIN_CONDITION);
    assertEquals(range3.getBotRow(), 8);
    assertEquals(range3.getRightCol(), 8);
    assertEquals(range3.getTopRow(), 0);
    assertEquals(range3.getLeftCol(), 0);
  }


  /**
   * Test checkConsecutiveHorizontal().
   */
  @Test
  public void testCheckConsecutiveHorizontal() {
    board.initializeBoard();
    int[] move = new int[]{0, 0};
    board.addMove(move, Game.X);
    assertEquals(1,
            board.checkConsecutiveHorizontal(move, board.calculateSearchRange(move,Game.WIN_CONDITION)));

    board.addMove(new int[]{0, 1}, Game.X);
    board.addMove(new int[]{0, 2}, Game.X);
    board.addMove(new int[]{0, 3}, Game.X);
    move[1] = 4;
    board.addMove(move, Game.X);

    assertEquals(5,
            board.checkConsecutiveHorizontal(move, board.calculateSearchRange(move,Game.WIN_CONDITION)));
  }

  /**
   * Test checkConsecutiveVertical().
   */
  @Test
  public void testCheckConsecutiveVertical() {
    board.initializeBoard();
    int[] move = new int[]{0, 0};
    board.addMove(move, Game.X);
    assertEquals(1,
            board.checkConsecutiveVertical(move, board.calculateSearchRange(move, Game.WIN_CONDITION)));

    board.addMove(new int[]{1, 0}, Game.X);
    board.addMove(new int[]{2, 0}, Game.X);
    board.addMove(new int[]{3, 0}, Game.X);
    move[0] = 4;
    board.addMove(move, Game.X);

    assertEquals(5,
            board.checkConsecutiveVertical(move, board.calculateSearchRange(move, Game.WIN_CONDITION)));
  }

  /**
   * Test checkConsecutiveDiag().
   */
  @Test
  public void testCheckConsecutiveDiag() {
    board.initializeBoard();
    int[] move = new int[]{0, 0};
    board.addMove(move, Game.X);
    assertEquals(1,
            board.checkConsecutiveDiag(move, board.calculateSearchRange(move, Game.WIN_CONDITION)));

    move = new int[]{1, 1};
    board.addMove(move, Game.X);
    assertEquals(2,
            board.checkConsecutiveDiag(move, board.calculateSearchRange(move, Game.WIN_CONDITION)));

    board.addMove(new int[]{2, 3}, Game.X);
    board.addMove(new int[]{1, 4}, Game.X);
    board.addMove(new int[]{4, 1}, Game.X);
    board.addMove(new int[]{4, 3}, Game.X);
    board.addMove(new int[]{3, 2}, Game.X);
    move = new int[]{3, 2};
    assertEquals(4,
            board.checkConsecutiveDiag(move, board.calculateSearchRange(move, Game.WIN_CONDITION)));
  }

  /**
   * Test checkWinningMove().
   */
  @Test
  public void testCheckWinningMove() {
    int[] lastMove = new int[]{3,2};
    testCheckConsecutiveDiag();
    assertFalse(board.checkWinningMove(lastMove));

    testCheckConsecutiveHorizontal();
    lastMove = new int[]{0, 4};
    assertTrue(board.checkWinningMove(lastMove));
  }

  /**
   * Test getRow().
   */
  @Test
  public void testGetRow () {
    testCheckConsecutiveHorizontal();
    char [] testArray = new char[]{Game.X, Game.X, Game.X, Game.X, Game.X,
                                  Game.EMPTY, Game.EMPTY, Game.EMPTY, Game.EMPTY, Game.EMPTY};
    assertEquals(Arrays.toString(testArray), Arrays.toString(board.getRow(0)));
    board.addMove(0,5, Game.X);
    assertFalse(Arrays.toString(testArray).equals(Arrays.toString(board.getRow(0))));
  }

  /**
   * Test getColumn().
   */
  @Test
  public void testGetColumn() {
    testCheckConsecutiveVertical();
    char [] testArray = new char[]{Game.X, Game.X, Game.X, Game.X, Game.X,
            Game.EMPTY, Game.EMPTY, Game.EMPTY, Game.EMPTY, Game.EMPTY};
    assertEquals(Arrays.toString(testArray), Arrays.toString(board.getColumn(0)));
    board.addMove(5,0, Game.X);
    assertFalse(Arrays.toString(testArray).equals(Arrays.toString(board.getRow(0))));
  }

  @Test
  public void testGetDiagonal() {
    testCheckConsecutiveDiag();
    assertEquals("[X, X,  ,  ,  ,  ,  ,  ,  ,  ]",
            Arrays.toString(board.getDiagonal(0,0,9,9)));

    assertEquals("[ , X, X, X, X,  ]",
            Arrays.toString(board.getDiagonal(0,5,5,0)));
  }

  public static void main(String[] args) {
    GameBoard board = new GameBoard( 10);
    board.initializeBoard();
    board.addMove(new int[]{1, 1}, Game.X);
    board.addMove(new int[]{2, 2}, Game.X);
    board.addMove(new int[]{3, 3}, Game.X);

    System.out.println(board.toString());
  }
}