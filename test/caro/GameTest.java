package caro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class GameTest {
  private int dimension = 15;
  private Game caro;
  /**
   * Set up Game object.
   */
  @Before
  public void setUp() {
    caro = new Game(dimension);
  }

  /**
   * Test getBoardDimension().
   */
  @Test
  public void testGetBoardDimension() {
    assertEquals(dimension, caro.getBoardDimension());
  }

  /**
   * Test getCurrentPlayer().
   */
  @Test
  public void testGetCurrentPlayer() {
    assertTrue(caro.getCurrentPlayer() == null);
  }

  /**
   * Test setUpGame().
   */
  @Test
  public void testSetUpGame() {
    caro.setUpGame();
    assertTrue(caro.getGameBoard().returnPosition(5,5) == Game.EMPTY);
    assertTrue(caro.getCurrentPlayer().getSymbol() == Game.X);
  }


  /**
   * Play game.
   * @param args
   */
  public static void main(String [] args) {
    Game caro = new Game(15);
    caro.setUpGame();
    caro.gamePlay();
  }
}