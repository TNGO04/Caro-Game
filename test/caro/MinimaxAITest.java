package caro;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import caro.board.GameBoard;
import caro.streak.StreakList;

import static org.junit.Assert.*;

public class MinimaxAITest {
  private int dimension = 5;
  GameBoard board = new GameBoard(dimension);
  Player aiPlayer = new Player(Game.X);
  Player opponent = new Player(Game.O);
  MinimaxAI ai = new MinimaxAI(board, aiPlayer, opponent);
  int[] lastMove = new int[]{2, 2};
  double delta = 0.0001;

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

    int[] lastMove = new int[] {2, 3};
    board.addMove(lastMove, Game.X);
    List<int[]> actionSet = board.getActionSet(lastMove,2);

    for (int i = 0; i < 100; i++) {
      assertTrue(actionSet.contains(ai.getRandomMove(actionSet)));
    }
  }

  @Test
  public void testCalculateUtility() {
    StreakList list = new StreakList();
    list.addStreak(2, 0);
    list.addStreak(3, 1);

    double currentUtility = ai.unblockedTwoUtility + ai.blockedThreeUtility;
    assertEquals(ai.calculateUtility(list), currentUtility, delta);

    list.addStreak(2, 0);
    list.addStreak(3, 0);

    currentUtility += ai.unblockedTwoUtility + ai.unblockedThreeUtility;
    assertEquals(ai.calculateUtility(list), currentUtility, delta);

    list.addStreak(4, 1);
    assertEquals(ai.calculateUtility(list),1,delta);

    StreakList list2 = new StreakList();
    list2.addStreak(4, 1);
    list2.addStreak(1, 1);
    currentUtility = ai.blockedFourUtility;
    assertEquals(ai.calculateUtility(list2), currentUtility, delta);
    list2.addStreak(2,0);
    currentUtility += ai.unblockedTwoUtility;
    assertEquals(ai.calculateUtility(list2), currentUtility, delta);

    StreakList list3 = new StreakList();
    list3.addStreak(4, 1);
    list3.addStreak(5, 2);

    assertEquals(ai.calculateUtility(list3), 1, delta);
  }

  @Test
  public void testCalculateUtilityOfBoardState() {
  }
}