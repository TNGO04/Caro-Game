package caro;

/**
 * Game class.
 */
public class Game {
  private Player currentPlayer;
  private Player playerX;
  private Player playerO;
  private GameBoard board;
  private final int boardWidth;
  private final int boardHeight;

  public static int MINDIM = 5;
  public static int MAXDIM = 99;
  public static int WIN_CONDITION = 5;
  public static char X = 'X';
  public static char O = 'O';
  public static char EMPTY = ' ';

  /**
   * Constructor for Game object.
   * @param width   width of game board
   * @param height  height of game board
   * @throws IllegalArgumentException if dimension is less than minimum
   */
  public Game(int width, int height) throws IllegalArgumentException{
    if ((width < this.MINDIM) || (height < this.MINDIM)) {
      throw new IllegalArgumentException("Dimension input is below minimum");
    }

    if ((width > this.MAXDIM) || (height > this.MAXDIM)) {
      throw new IllegalArgumentException("Dimension input exceeds maximum");
    }

    this.boardWidth = width;
    this.boardHeight = height;
  }

  /**
   * Switch current player to opposing player.
   */
  public void switchPlayer() {
    if (this.currentPlayer.getSymbol() == this.X) {
      this.currentPlayer = this.playerO;
    } else {
      this.currentPlayer = this.playerX;
    }
  }

  /**
   * Set up game by setting up players and board.
   */
  public void setUpGame() {
    this.playerX = new Player(X);
    this.playerO = new Player(O);

    this.board = new GameBoard(this.boardWidth, this.boardHeight);
    this.board.initializeBoard();

    //set X as first player;
    this.currentPlayer = this.playerX;
  }

  /**
   * Simulate game play.
   */
  public void gamePlay() {
    int[] currMove;

    //calculate maximum number of moves possible with this board size
    int maxMoves = this.boardWidth * this.boardHeight;

    // loop to get moves until run out of possible moves or a player win
    for (int i = 0; i < maxMoves; i++) {
      // loop until valid move is obtained and added to board
      while (true) {
        currMove = this.currentPlayer.obtainMove();
        if (this.board.addMove(currMove, this.currentPlayer.getSymbol())) {
          break;
        }
        else {
          System.out.println("Move is illegal! Please input another move!");
        }
      }

      System.out.println(board);
      // check for win condition, if found, break out of loop
      if (this.board.checkWin(currMove)) {
        System.out.println("Player " + currentPlayer.getSymbol() + " wins!");
        break;
      }
      this.switchPlayer();
    }

    // if board has no more valid move but no win condition is met, declare draw
    System.out.println("Congrats! You BOTH win!");
  }
}






