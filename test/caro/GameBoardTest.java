package caro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import caro.board.BoardSubset;
import caro.board.GameBoard;

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

  @Test
  public void testIsEmpty() {
    this.board.initializeBoard();
    assertTrue(board.isEmpty(5,5));
    board.addMove(5,5, Game.X);
    assertFalse(board.isEmpty(5,5));
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
   * Test for isDisconnected().
   */
  @Test
  public void isDisconnected() {
    board.initializeBoard();
    assertTrue(board.isDisconnected(0,0));
    board.addMove(0,1, Game.X);
    assertFalse(board.isDisconnected(0,0));
    assertFalse(board.isDisconnected(1,1));
    assertTrue(board.isDisconnected(5,5));
    board.addMove(4,5, Game.X);
    assertFalse(board.isDisconnected(5,5));
    assertTrue(board.isDisconnected(20,20));
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

  @Test
  public void testIsOutOfMoves() {
    GameBoard smallBoard = new GameBoard(5);
    assertTrue(smallBoard.isOutOfMoves());
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
   * Test checkConsecutiveHorizontal().
   */
  @Test
  public void testCheckConsecutiveHorizontal() {
    board.initializeBoard();
    int[] move = new int[]{0, 0};
    board.addMove(move, Game.X);
    assertEquals(1, board.checkConsecutiveHorizontal(
            move, new BoardSubset(move, DIMENSION, Game.WIN_CONDITION)));

    board.addMove(new int[]{0, 1}, Game.X);
    board.addMove(new int[]{0, 2}, Game.X);
    board.addMove(new int[]{0, 3}, Game.X);
    move[1] = 4;
    board.addMove(move, Game.X);

    assertEquals(5,
            board.checkConsecutiveHorizontal(
                    move, new BoardSubset(move, DIMENSION, Game.WIN_CONDITION)));
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
            board.checkConsecutiveVertical(move, new BoardSubset(move, DIMENSION, Game.WIN_CONDITION)));

    board.addMove(new int[]{1, 0}, Game.X);
    board.addMove(new int[]{2, 0}, Game.X);
    board.addMove(new int[]{3, 0}, Game.X);
    move[0] = 4;
    board.addMove(move, Game.X);

    assertEquals(5,
            board.checkConsecutiveVertical(move, new BoardSubset(move, DIMENSION, Game.WIN_CONDITION)));
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
            board.checkConsecutiveDiag(move, new BoardSubset(move, DIMENSION, Game.WIN_CONDITION)));

    move = new int[]{1, 1};
    board.addMove(move, Game.X);
    assertEquals(2,
            board.checkConsecutiveDiag(move, new BoardSubset(move, DIMENSION, Game.WIN_CONDITION)));

    board.addMove(new int[]{2, 3}, Game.X);
    board.addMove(new int[]{1, 4}, Game.X);
    board.addMove(new int[]{4, 1}, Game.X);
    board.addMove(new int[]{4, 3}, Game.X);
    board.addMove(new int[]{3, 2}, Game.X);
    move = new int[]{3, 2};
    assertEquals(4,
            board.checkConsecutiveDiag(move, new BoardSubset(move, DIMENSION, Game.WIN_CONDITION)));
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


  /**
   * Test countConsecutive().
   */
  @Test
  public void testCountConsecutive() {
    char[] array1;
    array1 = new char[]{' ', 'X', 'X', ' ', ' ', 'X', 'X', 'X', 'X'};
    assertEquals(board.countConsecutive(array1, 'X').getMaxStreakLength(), 4);

    array1 = new char[]{' ', 'X', 'X', ' ', 'O', 'X', 'X', 'X', 'X'};
    assertEquals(board.countConsecutive(array1, 'X').getMaxStreakLength(), 2);

    array1 = new char[]{'O', 'X', 'X', 'O', 'O', 'X', 'X', 'X', 'O'};
    assertEquals(board.countConsecutive(array1, 'X').getMaxStreakLength(), 0);

    char[] array2 = new char[12];

    for (int i = 0; i < 12; i++) {
      array2[i] = Game.EMPTY;
    }

    array2[1] = Game.X;
    array2[4] = Game.X;
    array2[5] = Game.X;
    array2[7] = Game.X;
    array2[8] = Game.X;
    assertEquals(board.countConsecutive(array2, 'X').toString(),
            "Streak length 2, count: 2, unblockedCount: 2\n");

    array2[9] = Game.O;
    assertEquals(board.countConsecutive(array2, 'X').toString(),
            "Streak length 2, count: 2, unblockedCount: 1\n");
  }

  @Test
  public void testCheckBoardForStreaks() {
    board = new GameBoard(15);
    board.initializeBoard();
    board.addMove(2,1, Game.X);
    board.addMove(2,2, Game.X);
    board.addMove(3,2, Game.X);
    board.addMove(3,3, Game.X);
    board.addMove(4,4, Game.X);

    board.addMove(1,1, Game.O);
    board.addMove(1,2, Game.O);
    board.addMove(2,3, Game.O);

    assertEquals(board.checkBoardForStreaks(new Player(Game.X)).toString(),
            "Streak length 2, count: 4, unblockedCount: 2\n" +
                    "Streak length 3, count: 1, unblockedCount: 0\n");

    assertEquals(board.checkBoardForStreaks(new Player(Game.O)).toString(),
            "Streak length 2, count: 2, unblockedCount: 2\n");

  }

  /**
   * Test getActionSet().
   */
  @Test
  public void testGetActionSet() {
    board.initializeBoard();
    board.addMove(2,3,Game.X);
    int[] lastMove = new int[] {2,2};
    board.addMove(lastMove, Game.O);

    List<int[]> actionSetActual = board.getActionSet(lastMove,3);

    assertTrue(Arrays.toString(actionSetActual.get(0)).equals("[1, 1]"));
    assertTrue(Arrays.toString(actionSetActual.get(1)).equals("[1, 2]"));
    assertTrue(Arrays.toString(actionSetActual.get(2)).equals("[1, 3]"));
    assertTrue(Arrays.toString(actionSetActual.get(3)).equals("[1, 4]"));
    assertTrue(Arrays.toString(actionSetActual.get(4)).equals("[2, 1]"));
    assertTrue(Arrays.toString(actionSetActual.get(5)).equals("[2, 4]"));
    assertTrue(Arrays.toString(actionSetActual.get(6)).equals("[3, 1]"));
    assertTrue(Arrays.toString(actionSetActual.get(7)).equals("[3, 2]"));
    assertTrue(Arrays.toString(actionSetActual.get(8)).equals("[3, 3]"));
    assertTrue(Arrays.toString(actionSetActual.get(9)).equals("[3, 4]"));

    board.addMove(1,3,Game.X);
    actionSetActual = board.getActionSet(lastMove,3);
    assertTrue(Arrays.toString(actionSetActual.get(0)).equals("[0, 2]"));
    assertTrue(Arrays.toString(actionSetActual.get(1)).equals("[0, 3]"));
    assertTrue(Arrays.toString(actionSetActual.get(2)).equals("[0, 4]"));
    assertTrue(Arrays.toString(actionSetActual.get(3)).equals("[1, 1]"));
    assertTrue(Arrays.toString(actionSetActual.get(4)).equals("[1, 2]"));
  }

  /**
   * Test boardState().
   */
  @Test
  public void testBoardState() {
    board.initializeBoard();
    board.addMove(2,3,Game.X);
    int[] lastMove = new int[] {2,2};
    board.addMove(lastMove, Game.O);
    GameBoard newBoard = board.getBoardState(new int[]{2, 4}, new Player(Game.X));

    newBoard.toString().equals(
            "___________________\n" +
                    "|  |00|01|02|03|04|\n" +
                    "|00|  |  |  |  |  |\n" +
                    "|01|  |  |  |  |  |\n" +
                    "|02|  |  |O |X |X |\n" +
                    "|03|  |  |  |  |  |\n" +
                    "|04|  |  |  |  |  |\n" +
                    "-------------------\n");
    board.toString().equals(
            "___________________\n" +
                    "|  |00|01|02|03|04|\n" +
                    "|00|  |  |  |  |  |\n" +
                    "|01|  |  |  |  |  |\n" +
                    "|02|  |  |O |X |  |\n" +
                    "|03|  |  |  |  |  |\n" +
                    "|04|  |  |  |  |  |\n" +
                    "-------------------\n");
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