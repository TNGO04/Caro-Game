package caro;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MinimaxAITest {
  private int dimension = 5;
  GameBoard board = new GameBoard(dimension);
  Player aiPlayer = new Player(Game.X);
  MinimaxAI ai = new MinimaxAI(board, aiPlayer);
  int[] lastMove = new int[]{2, 2};

  /**
   * Set up board for testing.
   */
  @Before
  public void setUp() {
    board.initializeBoard();
    board.addMove(2,3,Game.X);
    board.addMove(lastMove, Game.O);
  }

  /**
   * Test getRandomMove().
   */
  @Test
  public void testGetRandomMove() {
    board.initializeBoard();
    for (int i = 0; i < 1000; i++) {
      assertTrue(board.isLegalMove(ai.getRandomMove()));
    }
  }

  /**
   * Test getActionSet().
   */
  @Test
  public void testGetActionSet() {
    List<int[]> actionSetActual = ai.getActionSet(lastMove,2);

    assertTrue(Arrays.toString(actionSetActual.get(0)).equals("[1, 1]"));
    assertTrue(Arrays.toString(actionSetActual.get(1)).equals("[1, 2]"));
    assertTrue(Arrays.toString(actionSetActual.get(2)).equals("[1, 3]"));
    assertTrue(Arrays.toString(actionSetActual.get(3)).equals("[2, 1]"));
    assertTrue(Arrays.toString(actionSetActual.get(4)).equals("[3, 1]"));
    assertTrue(Arrays.toString(actionSetActual.get(5)).equals("[3, 2]"));
    assertTrue(Arrays.toString(actionSetActual.get(6)).equals("[3, 3]"));
  }

  /**
   * Test boardState().
   */
  @Test
  public void testBoardState() {
    GameBoard newBoard = ai.boardState(new int[]{2, 4}, aiPlayer);

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


  /**
   * Test countConsecutive().
   */
  @Test
  public void testCountConsecutive() {
    char[] array1;
    array1 = new char[]{' ', 'X', 'X', ' ', ' ', 'X', 'X', 'X', 'X'};
    assertEquals(ai.countConsecutive(array1, 'X').getMaxStreakLength(), 4);

    array1 = new char[]{' ', 'X', 'X', ' ', 'O', 'X', 'X', 'X', 'X'};
    assertEquals(ai.countConsecutive(array1, 'X').getMaxStreakLength(), 2);

    array1 = new char[]{'O', 'X', 'X', 'O', 'O', 'X', 'X', 'X', 'O'};
    assertEquals(ai.countConsecutive(array1, 'X').getMaxStreakLength(), 0);

    char[] array2 = new char[12];

    for (int i = 0; i < 12; i++) {
      array2[i] = Game.EMPTY;
    }

    array2[1] = Game.X;
    array2[4] = Game.X;
    array2[5] = Game.X;
    array2[7] = Game.X;
    array2[8] = Game.X;
    assertEquals(ai.countConsecutive(array2, 'X').toString(),
                    "Streak length 2, count: 2, unblockedCount: 2\n");

    array2[9] = Game.O;
    assertEquals(ai.countConsecutive(array2, 'X').toString(),
                    "Streak length 2, count: 2, unblockedCount: 1\n");
  }

  @Test
  public void testCheckBoardForStreaks() {
    board = new GameBoard(15);
    ai = new MinimaxAI(board, aiPlayer);
    board.initializeBoard();
    board.addMove(2,1, Game.X);
    board.addMove(2,2, Game.X);
    board.addMove(3,2, Game.X);
    board.addMove(3,3, Game.X);
    board.addMove(4,4, Game.X);

    board.addMove(1,1, Game.O);
    board.addMove(1,2, Game.O);
    board.addMove(2,3, Game.O);

    assertEquals(ai.checkBoardForStreaks(aiPlayer).toString(),
            "Streak length 2, count: 4, unblockedCount: 2\n" +
                    "Streak length 3, count: 1, unblockedCount: 0\n");

    assertEquals(ai.checkBoardForStreaks(new Player(Game.O)).toString(),
            "Streak length 2, count: 2, unblockedCount: 2\n");

  }
}