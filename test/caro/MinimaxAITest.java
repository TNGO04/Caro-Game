package caro;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import caro.AI.MinimaxAI;
import caro.board.GameBoard;
import caro.streak.StreakList;

import static org.junit.Assert.*;

public class MinimaxAITest {
  private int dimension = 5;
  GameBoard board = new GameBoard(dimension);
  Player aiPlayer = new Player(Game.X);
  Player opponent = new Player(Game.O);
  MinimaxAI ai = new MinimaxAI(board.getBoardDimension(), aiPlayer, opponent);
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
   * Test getActionSet().
   */
  @Test
  public void testGetActionSet() {
    board.initializeBoard();
    board.addMove(2,3,Game.X);
    int[] lastMove = new int[] {2,2};
    board.addMove(lastMove, Game.O);

    List<int[]> actionSetActual = ai.getActionSet(board, lastMove,3);

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
    actionSetActual = ai.getActionSet(board, lastMove,3);
    assertTrue(Arrays.toString(actionSetActual.get(0)).equals("[0, 2]"));
    assertTrue(Arrays.toString(actionSetActual.get(1)).equals("[0, 3]"));
    assertTrue(Arrays.toString(actionSetActual.get(2)).equals("[0, 4]"));
    assertTrue(Arrays.toString(actionSetActual.get(3)).equals("[1, 1]"));
    assertTrue(Arrays.toString(actionSetActual.get(4)).equals("[1, 2]"));
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
    List<int[]> actionSet = ai.getActionSet(board, lastMove,2);

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
    assertEquals(ai.calculateUtility(list, false), currentUtility, delta);

    list.addStreak(2, 0);
    list.addStreak(3, 0);

    currentUtility += ai.unblockedTwoUtility + ai.unblockedThreeUtility;
    assertEquals(ai.calculateUtility(list, false), currentUtility, delta);

    list.addStreak(4, 1);
    assertEquals(ai.calculateUtility(list, false),1,delta);

    StreakList list2 = new StreakList();
    list2.addStreak(4, 1);
    list2.addStreak(1, 1);
    currentUtility = ai.blockedFourUtility;
    assertEquals(ai.calculateUtility(list2, false), currentUtility, delta);
    list2.addStreak(2,0);
    currentUtility += ai.unblockedTwoUtility;
    assertEquals(ai.calculateUtility(list2, false), currentUtility, delta);

    StreakList list3 = new StreakList();
    list3.addStreak(4, 1);
    list3.addStreak(5, 2);

    assertEquals(ai.calculateUtility(list3, false), 1, delta);
  }

}