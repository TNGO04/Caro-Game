package caro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import caro.streak.Streak;

public class StreakTest {
  Streak check1, check2, check3;

  /**
   * Test Constructor.
   */
  @Before
  public void setUp() {
    check1 = new Streak();
    check2 = new Streak(4, 2, 1);
  }

  /**
   * Test getStreakLength().
   */
  @Test
  public void testGetStreakLength() {
    assertEquals(0, check1.getStreakLength());
    assertEquals(4, check2.getStreakLength());
  }

  /**
   * Test getCount().
   */
  @Test
  public void testGetCount() {
    assertEquals(0, check1.getCount());
    assertEquals(2, check2.getCount());
  }

  /**
   * Test getUnBlockedCount().
   */
  @Test
  public void testGetUnblockedCount() {
    assertEquals(0, check1.getUnblockedCount());
    assertEquals(1, check2.getUnblockedCount());
  }

  /**
   * Test toString().
   */
  @Test
  public void testToString() {
    assertEquals(check2.toString(),
            "Streak length 4, count: 2, unblockedCount: 1\n");
  }



  /**
   * Test both versions of updateStreak().
   */
  @Test
  public void testUpdateStreak() {
    check3 = new Streak(3, 1, 0);
    check3.updateStreak(1);
    assertEquals(check3.toString(), "Streak length 3, count: 2, unblockedCount: 0\n");

    check3.updateStreak(2);
    assertEquals(check3.toString(), "Streak length 3, count: 2, unblockedCount: 0\n");

    check3.updateStreak(0);
    assertEquals(check3.toString(), "Streak length 3, count: 3, unblockedCount: 1\n");

    check3.updateStreak(check2);
    assertEquals(check3.toString(), "Streak length 3, count: 3, unblockedCount: 1\n");

    check3.updateStreak(check1);
    assertEquals(check3.toString(), "Streak length 3, count: 3, unblockedCount: 1\n");

    check3.updateStreak(check3);
    assertEquals(check3.toString(), "Streak length 3, count: 6, unblockedCount: 2\n");
  }
}