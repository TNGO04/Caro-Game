package caro;

import org.junit.Before;
import org.junit.Test;

import caro.board.GameBoard;

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


  @Test
  public void testCalculateUtility() {
  }

  @Test
  public void testCalculateUtilityOfBoardState() {
  }
}