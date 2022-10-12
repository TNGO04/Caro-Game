package caro.streak;

/**
 * Class to store data from checking consecutive streaks in an array.
 */
public class Streak {
  private final int length;
  private int count;
  private int unblockedCount;

  /**
   * Constructor to initialize object.
   */
  public Streak() {
    this.length = 0;
    this.count = 0;
    this.unblockedCount = 0;
  }

  /**
   * Parameterized constructor.
   *
   * @param length         maximum streak length
   * @param count          count of streak of maximum size
   * @param countUnblocked count of unblocked streak of maximum size
   */
  public Streak(int length, int count, int countUnblocked) throws IllegalArgumentException {
    if ((length < 0) || (count < 0) || (countUnblocked < 0)) {
      throw new IllegalArgumentException("CheckResult parameters cannot be negative.");
    }

    this.length = length;
    this.count = count;
    this.unblockedCount = countUnblocked;
  }

  /**
   * Getter for streak length.
   *
   * @return length
   */

  public int getStreakLength() {
    return this.length;
  }

  /**
   * Getter for count.
   *
   * @return count
   */
  public int getCount() {
    return this.count;
  }

  /**
   * Getter for unblockedCount.
   *
   * @return unblockedCount
   */
  public int getUnblockedCount() {
    return this.unblockedCount;
  }


  /**
   * toString() method overriding original Object toString().
   *
   * @return string representation
   */
  @Override
  public String toString() {
    return "Streak length " + this.getStreakLength()
            + ", count: " + this.getCount()
            + ", unblockedCount: " + this.getUnblockedCount() + "\n";
  }

  /**
   * Update CheckResult object with newly found streak.
   *
   * @param blockMarker block marker of streak
   */
  public void updateStreak(int blockMarker) throws IllegalArgumentException {
    if (blockMarker < 0) {
      throw new IllegalArgumentException("Block Marker cannot be negative.");
    }

    // only update CheckResult if other streak is not blocked on both sides
    if (blockMarker < 2) {
      this.count++;
      // increase unblocked count if current streak is not blocked
      if (blockMarker == 0) {
        this.unblockedCount++;
      }
    }
  }

  /**
   * Update Streak by adding elements from another streak of same size.
   *
   * @param other other streak to be added
   */
  public void updateStreak(Streak other) {
    if ((other.getCount() == 0) || (other.getStreakLength() != this.getStreakLength())) {
      return;
    }
    this.count += other.getCount();
    this.unblockedCount += other.getUnblockedCount();

  }
}

