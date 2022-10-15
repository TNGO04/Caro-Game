package caro;

import java.util.Arrays;
import java.util.HashMap;

import caro.AI.AbstractAI;
import caro.AI.MinimaxAI;
import caro.AI.TrainedAI;
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
   * Loop until a valid move is obtained from command prompt, then return move.
   *
   * @return  valid move
   */
  public int[] getHumanMove() {
    int[] currMove;
    while (true) {
      currMove = this.currentPlayer.obtainMove();
      if ((currMove == null) || (!this.board.addMove(currMove, this.currentPlayer.getSymbol()))) {
        System.out.println("Move is illegal! Please input another move!");
      } else {
        break;
      }
    }
    return currMove;
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
      currMove = this.getHumanMove();
      System.out.println(board.toString(currMove[0],currMove[1]));
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
   * Simulate game play with one AI player (first player X).
   */
  public void gamePlayAI() {
    int[] currMove = new int[0];
    Player aiPlayer = playerX;
    Player humanPlayer = playerO;
    MinimaxAI ai = new MinimaxAI(board.getBoardDimension(), aiPlayer, humanPlayer);
    
    //calculate maximum number of moves possible with this board size
    int maxMoves = (int) Math.pow(this.boardDimension, 2);

    // loop to get moves until run out of possible moves or a player win
    for (int i = 0; i < maxMoves; i++) {
      if (currentPlayer == aiPlayer) {
        // if first move, return random move (biased towards center) on board
        if (i == 0) {
          currMove = ai.getOptimalMove(board, null, true);
        } else {
          currMove = ai.getOptimalMove(board, currMove, false);
        }
        this.board.addMove(currMove, this.currentPlayer.getSymbol());
        System.out.println("AI Player makes move: " + Arrays.toString(currMove));
      }

      // if it is human's turn, loop until valid move is obtained and added to board
      if (currentPlayer == humanPlayer) {
        currMove = this.getHumanMove();
      }

      System.out.println(board.toString(currMove[0],currMove[1]));
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
   * Train reinforcement learning AI against minimax AI.
   * @param numGame number of training game
   */
  public TrainedAI trainAi(int numGame) {
    TrainedAI ai = new TrainedAI(this.boardDimension);
    MinimaxAI ai2 = new MinimaxAI(this.boardDimension,playerO, playerX);

    int[] currMove =  new int[2];
    HashMap<Player, int[]> lastMove = new HashMap<Player, int[]>();
    HashMap<Player, GameBoard> lastState = new HashMap<Player, GameBoard>();
    GameBoard currState, newState;

    Player aiPlayer = playerX;

    //calculate maximum number of moves possible with this board size
    int maxMoves = (int) Math.pow(this.boardDimension, 2);

    for (int i = 0; i < numGame; i++) {
      System.out.println("Training game #" + (i + 1));
      this.setUpGame();

      // loop to get moves until run out of possible moves or a player win
      for (int j = 0; j < maxMoves; j++) {
        currState = new GameBoard(board);
        if (j == 0) {
          currMove = new int[] {7,7};
        }
        else if (j % 2 == 0){
          currMove = ai.getOptimalMove(this.board, currMove, false);
        }
        else {
          currMove = ai2.getOptimalMove(this.board,currMove,false);
        }

        lastState.put(currentPlayer, currState);
        lastMove.put(currentPlayer, currMove);

        board.addMove(currMove, this.currentPlayer.getSymbol());
        newState = new GameBoard(board);

        if (this.board.checkWinningMove(currMove)) {
          if (this.board.returnPosition(currMove) == aiPlayer.getSymbol())
          {
            ai.update(currState, currMove, newState, 1.0);
          }
          else {
            ai.update(lastState.get(aiPlayer), lastMove.get(aiPlayer), currState, -1.0);
          }

          break;
        } else {
          if (lastState.get(currentPlayer) != null) {
            ai.update(currState, currMove, newState, 0.0);
          }
        }
        switchPlayer();
      }
    }
    System.out.println("Training done!");
    return ai;
  }

  /**
   * Simulate game play with one AI player (first player X).
   */
  public void gamePlayTrainedAI() {
    int[] currMove = new int[0];
    Player aiPlayer = playerX;
    Player humanPlayer = playerO;
    TrainedAI ai = this.trainAi(1000);

    //calculate maximum number of moves possible with this board size
    int maxMoves = (int) Math.pow(this.boardDimension, 2);
    this.setUpGame();
    // loop to get moves until run out of possible moves or a player win
    for (int i = 0; i < maxMoves; i++) {
      if (currentPlayer == aiPlayer) {
        // if first move, return random move (biased towards center) on board
        if (i == 0) {
          currMove = ai.getOptimalMove(board, null, true);
        } else {
          currMove = ai.getOptimalMove(board, currMove, false);
        }
        this.board.addMove(currMove, this.currentPlayer.getSymbol());
        System.out.println("AI Player makes move: " + Arrays.toString(currMove));
      }

      // if it is human's turn, loop until valid move is obtained and added to board
      if (currentPlayer == humanPlayer) {
        currMove = this.getHumanMove();
      }

      System.out.println(board.toString(currMove[0],currMove[1]));
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







