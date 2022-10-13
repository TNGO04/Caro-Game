package caro;

import caro.board.GameBoard;

/**
 * Game class.
 */
public class Game {
  private Player currentPlayer = null;
  private Player playerX, playerO;
  private GameBoard board;
  private final int boardDimension;
  public static int WIN_CONDITION = 5;
  public static char X = 'X', O = 'O', EMPTY = ' ';

  /**
   * Constructor for Game object.
   *
   * @param boardDimension   width and height of game board
   * @throws IllegalArgumentException if dimension is less than minimum
   */
  public Game(int boardDimension) throws IllegalArgumentException {
    this.board = new GameBoard(boardDimension);
    this.boardDimension = boardDimension;
    this.playerX = new Player(this.X);
    this.playerO = new Player(this.O);
  }

  /**
   * Getter for boardDimension.
   *
   * @return dimension used for width and height of board
   */
  public int getBoardDimension() {
    return this.boardDimension;
  }

  /**
   * Getter for current player pointer.
   *
   * @return currPlayer pointer
   */
  public Player getCurrentPlayer() {
    return this.currentPlayer;
  }

  /**
   * Getter for GameBoard object.
   *
   * @return  GameBoard board
   */
  public GameBoard getGameBoard() {
    return this.board;
  }


  /**
   * Switch current player to opposing player.
   */
  public void switchPlayer() {
    if (this.currentPlayer.equals(this.playerX)) {
      this.currentPlayer = this.playerO;
    } else {
      this.currentPlayer = this.playerX;
    }
  }

  /**
   * Set up game by setting up players and board.
   */
  public void setUpGame() {
    //set X as first player;
    this.currentPlayer = this.playerX;
    this.board.initializeBoard();
  }

  /**
   * Simulate game play with two human players.
   */
  public void gamePlay() {
    int[] currMove;

    //calculate maximum number of moves possible with this board size
    int maxMoves = (int) Math.pow(this.boardDimension, 2);

    // loop to get moves until run out of possible moves or a player win
    for (int i = 0; i < maxMoves; i++) {
      // loop until valid move is obtained and added to board
      while (true) {
        currMove = this.currentPlayer.obtainMove();
        if ((currMove == null) || (!this.board.addMove(currMove, this.currentPlayer.getSymbol()))) {
          System.out.println("Move is illegal! Please input another move!");
        } else {
          break;
        }
      }

      System.out.println(board);
      // check for win condition, if found, break out of loop
      if (this.board.checkWinningMove(currMove)) {
        System.out.println("Player " + currentPlayer.getSymbol() + " wins!");
        return;
      }
      this.switchPlayer();
    }

    // if board has no more valid move but no win condition is met, declare draw
    System.out.println("Congrats! You BOTH win!");
  }

  /**
   * Simulate game play with one AI player, starting the game. 
   */
  public void gamePlayAI() {
    int[] currMove = new int[0];
    MinimaxAI ai = new MinimaxAI(board, playerX, playerO);
    
    //calculate maximum number of moves possible with this board size
    int maxMoves = (int) Math.pow(this.boardDimension, 2);

    // loop to get moves until run out of possible moves or a player win
    for (int i = 0; i < maxMoves; i++) {
      if (i == 0) {
        currMove = ai.minimax(board, null,true);
        this.board.addMove(currMove, this.currentPlayer.getSymbol());
      }
      else if ((i % 2) == 0) {
        currMove = ai.minimax(board, currMove,false);
        this.board.addMove(currMove, this.currentPlayer.getSymbol());
      }
      
      // loop until valid move is obtained and added to board
      while ((i % 2) == 1) {
        currMove = this.currentPlayer.obtainMove();
        if ((currMove == null) || (!this.board.addMove(currMove, this.currentPlayer.getSymbol()))) {
          System.out.println("Move is illegal! Please input another move!");
        } else {
          break;
        }
      }

      System.out.println(board);
      // check for win condition, if found, break out of loop
      if (this.board.checkWinningMove(currMove)) {
        System.out.println("Player " + currentPlayer.getSymbol() + " wins!");
        return;
      }
      this.switchPlayer();
    }

    // if board has no more valid move but no win condition is met, declare draw
    System.out.println("Congrats! You BOTH win!");
  }


}







