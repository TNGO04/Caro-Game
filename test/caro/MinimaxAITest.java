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
  Player opponent = new Player(Game.O);
  MinimaxAI ai = new MinimaxAI(board, aiPlayer, opponent);
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
    List<int[]> actionSetActual = ai.getActionSet(lastMove,board,3);

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
    actionSetActual = ai.getActionSet(lastMove,board,3);
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

}