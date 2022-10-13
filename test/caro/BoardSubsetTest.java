package caro;

import org.junit.Before;
import org.junit.Test;

import caro.board.BoardSubset;

import static org.junit.Assert.*;

public class BoardSubsetTest {
  BoardSubset sr1;

  /**
   * Set up test cases.
   */
  @Before
  public void setUp() {
    sr1 = new BoardSubset(4, 5, 1, 2);
  }

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    //test case: close to top left
    BoardSubset range1 = new BoardSubset(new int[]{0, 0},10,  Game.WIN_CONDITION);
    assertEquals(range1.getBotRow(), 4);
    assertEquals(range1.getRightCol(), 4);
    assertEquals(range1.getTopRow(), 0);
    assertEquals(range1.getLeftCol(), 0);

    //test case: close to bottom right
    BoardSubset range2 = new BoardSubset(new int[]{8, 8}, 10, Game.WIN_CONDITION);
    assertEquals(range2.getBotRow(), 9);
    assertEquals(range2.getRightCol(), 9);
    assertEquals(range2.getTopRow(), 4);
    assertEquals(range2.getLeftCol(), 4);

    // test case: middle of board
    BoardSubset range3 = new BoardSubset(new int[]{4, 4}, 10, Game.WIN_CONDITION);
    assertEquals(range3.getBotRow(), 8);
    assertEquals(range3.getRightCol(), 8);
    assertEquals(range3.getTopRow(), 0);
    assertEquals(range3.getLeftCol(), 0);
  }

  /**
   * Test bad constructor arguments.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadConstruction() {
    new BoardSubset(-1,1,2,3);
  }

  /**
   * Test getTopRow().
   */
  @Test
  public void testGetTopRow() {
    assertEquals(sr1.getTopRow(),4);
  }

  /**
   * Test getBotRow().
   */
  @Test
  public void testGetBotRow() {
    assertEquals(sr1.getBotRow(),5);
  }

  /**
   * Test getLeftCol().
   */
  @Test
  public void testGetLeftCol() {
    assertEquals(sr1.getLeftCol(),1);
  }

  /**
   * Test getRightCol().
   */
  @Test
  public void testGetRightCol() {
    assertEquals(sr1.getRightCol(),2);
  }
}