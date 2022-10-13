package caro.streak;

import caro.Game;

/**
 * StreakList class represents an object that stores multiple Streak objects,
 * each representing streaks of a certain length.
 */
public class StreakList {
  private int length = Game.WIN_CONDITION - 1;
  private final Streak[] list;

  /**
   * Constructor.
   */
  public StreakList() {
    this.list = new Streak[length];
    for (int i = 2; i <= (length + 1); i++) {
      this.list[i - 2] = new Streak(i, 0, 0);
    }
  }

  /**
   * Add a streak to streak list.
   *
   * @param currentStreak length of streak being added
   * @param blockMarker   block marker of streak being added
   */
  public void addStreak(int currentStreak, int blockMarker) throws IllegalArgumentException {
    if (currentStreak < 0) {
      throw new IllegalArgumentException("Streak Length cannot be negative.");
    }
    if ((currentStreak == 0) || (currentStreak == 1)) {
      return;
    }
    if (currentStreak >= Game.WIN_CONDITION) {
      this.list[this.length - 1].updateStreak(blockMarker);
    } else {
      this.list[currentStreak - 2].updateStreak(blockMarker);
    }
  }

  /**
   * Return number of Streak in StreakList.
   *
   * @return length of StreakList
   */
  public int getLength() {
    return this.length;
  }

  /**
   * Add counts from another StreakList to this StreakList.
   *
   * @param other other StreakList to be added
   */
  public void addStreakList(StreakList other) {
    if (this.getLength() != other.getLength()) {
      return;
    }

    for (int i = 0; i < this.getLength(); i++) {
      this.list[i].updateStreak(other.list[i]);
    }
  }

  /**
   * Within StreakList, find the largest streak's length.
   *
   * @return maximum streak length
   */
  public int getMaxStreakLength() {
    for (int i = this.length - 1; i >= 0; i--) {
      if (this.list[i].getCount() > 0) {
        return this.list[i].getStreakLength();
      }
    }
    return 0;
  }

  /**
   * Return Streak at index of StreakList.
   *
   * @param index index of StreakList
   * @return Streak at index
   */
  public Streak getStreak(int index) throws IllegalArgumentException {
    if ((index < 0) || (index >= this.length)) {
      throw new IllegalArgumentException("Index out of range.");
    }
    return this.list[index];
  }

  /**
   * toString method for StreakList object.
   *
   * @return String object
   */
  @Override
  public String toString() {
    String s = "";
    for (Streak streak : this.list) {
      if (streak.getCount() > 0) {
        s += streak.toString();
      }
    }

    if (s == "") {
      s += "There is no streak.\n";
    }
    return s;
  }
}
