package caro;

import caro.streak.StreakList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Class for MinimaxAI object, using minimax algorithm to make game moves.
 */
public class MinimaxAI {
  GameBoard board;
  Player aiPlayer;
  Player opponent;

  private final double unblockedFourUtility = 1.0, blockedFourUtility = 0.5;
  private final double unblockedThreeUtility = 0.5, blockedThreeUtility = 0.2;
  private final double unblockedTwoUtility = 0.15, blockedTwoUtility = 0.05;

  /**
   * Constructor for MinimaxAI.
   *
   * @param board GameBoard object
   */
  public MinimaxAI(GameBoard board, Player aiPlayer, Player opponent) throws IllegalArgumentException {
    if ((board == null) || (aiPlayer == null) || (opponent == null)) {
      throw new IllegalArgumentException("Input object is null.");
    }
    this.board = board;
    this.aiPlayer = aiPlayer;
    this.opponent = opponent;
  }

  /**
   * Get a random move.
   */
  public int[] getRandomMove() {
    Random rand = new Random();
    int row = rand.nextInt(0,this.board.getBoardDimension());
    int column = rand.nextInt(0,this.board.getBoardDimension());
    return new int[]{row, column};
  }

  /**
   * Get a random move from list of moves.
   */
  public int[] getRandomMove(List<int[]> actionList) {
    Random rand = new Random();
    int index = rand.nextInt(actionList.size());
    return actionList.get(index);
  }


  /**
   * Get a set of potential action (move) for MinimaxAI to take.
   * Given the size of the board, it is computationally expensive to list all possible move.
   * Instead, in this method, we only consider moves within a radius of the last move made by
   * player. In addition, since the objective of a move is either blocking an opponent or expanding
   * one's streak, we only consider potential moves that are adjacent to another previously-made
   * moves. Disconnected potential moves are therefore not added to actionSet.
   *
   * @param lastMove :   last move made in the game
   * @return list of possible moves to make
   */
  public List<int[]> getActionSet(int[] lastMove, GameBoard boardState, int radius) {
    SearchRange search = boardState.calculateSearchRange(lastMove, radius);
    List<int[]> actionSet = new ArrayList<int[]>();

    for (int row = search.getTopRow(); row <= search.getBotRow(); row++) {
      for (int col = search.getLeftCol(); col <= search.getRightCol(); col++) {
        if (boardState.isEmpty(row, col) && !boardState.isDisconnected(row, col)) {
          actionSet.add(new int[]{row, col});
        }
      }
    }
    return actionSet;
  }

  /**
   * Return board object after a potential move is made.
   *
   * @param move move to be made
   * @return new board state
   */
  public GameBoard boardState(int[] move, Player player) throws IllegalArgumentException {
    // if move is illegal with current board state, throw exception
    if (!this.board.isLegalMove(move)) {
      throw new IllegalArgumentException("Invalid move");
    }

    //get deep copy of board and add move
    GameBoard newBoard = new GameBoard(this.board);
    newBoard.addMove(move, player.getSymbol());
    return newBoard;
  }



  /**
   * Given a list of streaks, calculate the utility score.
   *
   * @param list StreakList object
   * @return utility of StreakList, ranging [0...1]
   */
  public double calculateUtility(StreakList list) {
    int maxStreak = list.getMaxStreakLength();

    if (maxStreak == Game.WIN_CONDITION) {
      return 1.0;
    } else if (maxStreak == 0) {
      return 0;
    } else {
      double utility = 0.0;
      for (int i = maxStreak - 2; i >= 0; i--) {
        int unblockedCount = list.getStreak(i).getUnblockedCount();
        int blockedCount = list.getStreak(i).getCount() - unblockedCount;

        switch (i + 2) {
          case 2:
            utility += (unblockedCount * unblockedTwoUtility + blockedCount * blockedTwoUtility);
            break;
          case 3:
            utility += (unblockedCount * unblockedThreeUtility + blockedCount * blockedThreeUtility);
            break;
          case 4:
            utility += (unblockedCount * unblockedFourUtility + blockedCount * blockedFourUtility);
            break;
          default:
            break;
        }
        if (utility > 1) {
          return 1.0;
        }
      }
      return utility;
    }
  }

  public double calculateUtilityOfBoardState(GameBoard boardState) {
    return (this.calculateUtility(boardState.checkBoardForStreaks(aiPlayer))
            - this.calculateUtility(boardState.checkBoardForStreaks(opponent)));
  }

  /*
  public double maximizer(GameBoard boardState, double maxUtility, int[] lastMove) {
    if (boardState.isOutOfMoves()) {
      return this.calculateUtilityOfBoardState(boardState);
    }

    double utility = Double.NEGATIVE_INFINITY;


  }
*/
}
