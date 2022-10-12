package caro;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Player.
 */
public class PlayerTest {
  /**
   * Test bad construction.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadConstruction() {
    new Player(Game.EMPTY);
  }

  /**
   * Test getSymbol().
   */
  @Test
  public void testGetSymbol() {
    Player playerX = new Player(Game.X);
    assertEquals(playerX.getSymbol(), Game.X);
  }
}