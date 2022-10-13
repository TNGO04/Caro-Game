package caro;

import org.junit.Before;
import org.junit.Test;

import caro.Game;
import caro.streak.StreakList;

import static org.junit.Assert.*;

/**
 * Test class for StreakList.
 */
public class StreakListTest {
  StreakList list1, list2;

  /**
   * Test cases.
   */
  @Before
  public void setUp() {
    list1 = new StreakList();
    list2 = new StreakList();
  }

  /**
   * Test getLength().
   */
  @Test
  public void testGetLength() {
    assertEquals(Game.WIN_CONDITION - 1, list1.getLength());
  }

  /**
   * Test getStreak().
   */
  @Test
  public void testGetStreak() {
    assertEquals(list1.getStreak(2).toString(),
            "Streak length 4, count: 0, unblockedCount: 0\n");
  }

  @Test
  public void testToString() {
    assertEquals(list1.toString(), "There is no streak.\n");
    list1.addStreak(4,1);
    assertEquals(list1.toString(), "Streak length 4, count: 1, unblockedCount: 0\n");
  }

  /**
   * Test addStreak().
   */
  @Test
  public void testAddStreak() {
    list1.addStreak(4,1);
    assertEquals(list1.toString(), "Streak length 4, count: 1, unblockedCount: 0\n");
    list1.addStreak(3,2);
    assertEquals(list1.toString(), "Streak length 4, count: 1, unblockedCount: 0\n");
    list1.addStreak(3,0);
    assertEquals(list1.toString(), "Streak length 3, count: 1, unblockedCount: 1\n" +
            "Streak length 4, count: 1, unblockedCount: 0\n");
  }

  /**
   * Test addStreakList().
   */
  @Test
  public void testAddStreakList() {
    list2.addStreak(4,1);
    list1.addStreakList(list2);
    assertEquals(list1.toString(), "Streak length 4, count: 1, unblockedCount: 0\n");
  }

  /**
   * Test getMaxStreakLength().
   */
  @Test
  public void testGetMaxStreakLength() {
    list1.addStreak(4,1);
    list1.addStreak(2,0);
    assertEquals(4,list1.getMaxStreakLength());
  }


}