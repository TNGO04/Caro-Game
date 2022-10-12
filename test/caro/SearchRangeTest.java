package caro;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SearchRangeTest {
  SearchRange sr1;

  /**
   * Set up test cases.
   */
  @Before
  public void setUp() {
    sr1 = new SearchRange(4, 5, 1, 2);
  }

  /**
   * Test bad constructor arguments.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadConstruction() {
    new SearchRange(-1,1,2,3);
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