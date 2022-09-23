package caro;

public class GameTest {
  /**
   * Play game.
   * @param args
   */
  public static void main(String [] args) {
    Game caro = new Game(15, 15);
    caro.setUpGame();
    caro.gamePlay();
  }
}