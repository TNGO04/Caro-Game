package caro;

import caro.board.GameBoard;
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
  private int searchRadius;
  private int searchDepth;

  public final static double unblockedFourUtility = 1.0, blockedFourUtility = 0.5;
  public final static double unblockedThreeUtility = 0.5, blockedThreeUtility = 0.1;
  public final static double unblockedTwoUtility = 0.04, blockedTwoUtility = 0.01;

  /**
   * Constructor for MinimaxAI.
   *
   * @param board GameBoard object
   */
  public MinimaxAI(GameBoard board, Player aiPlayer, Player opponent) throws IllegalArgumentException {
    if ((board == null) || (aiPlayer == null) || (opponent == null)) {
      throw new IllegalArgumentException("Input object is null.");
    }
    if (aiPlayer == opponent) {
      throw new IllegalArgumentException("AI Player and opponent must be different.");
    }
    this.board = board;
    this.aiPlayer = aiPlayer;
    this.opponent = opponent;
    this.searchDepth = 2;
    this.searchRadius = 10;
  }

  /**
   * Constructor.
   * @param board GameBoard representing current game board
   * @param aiPlayer  player controlled by AI
   * @param opponent  opponent player of aiPlayer
   * @param searchDepth depth of search tree for minimax
   * @param searchRadius  search radius
   * @throws IllegalArgumentException
   */
  public MinimaxAI(GameBoard board, Player aiPlayer, Player opponent, int searchDepth,
                   int searchRadius) throws IllegalArgumentException {
    this(board, aiPlayer, opponent);
    this.searchDepth = searchDepth;
    this.searchRadius = searchRadius;
  }

  /**
   * Get a random move with a bias for center of board.
   * A position at the center of board allows for more opportunities to expand; therefore, this
   * random move generator favors position mid-board.
   */
  public int[] getRandomMove() {
    Random rand = new Random();
    int bound1 = (int) Math.ceil(this.board.getBoardDimension() / 2);
    int bound2 = (int) Math.floor(this.board.getBoardDimension() / 2) + 1;

    int row = rand.nextInt(bound1) + rand.nextInt(bound2);
    int column = rand.nextInt(bound1) + rand.nextInt(bound2);

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
   * Given a list of streaks, calculate the utility score.
   *
   * @param list StreakList object
   * @return utility of StreakList, ranging [0...1]
   */
  public double calculateUtility(StreakList list, boolean isOpponent) {
    int shift = 0;
    if (isOpponent) {
      shift = 0;
    }

    int maxStreak = list.getMaxStreakLength() + shift;

    if (maxStreak == Game.WIN_CONDITION) {
      return 1.0;
    } else if (maxStreak == 0) {
      return 0;
    } else {
      double utility = 0.0;
      for (int i = maxStreak - 2 - shift; i >= 0; i--) {
        int unblockedCount = list.getStreak(i).getUnblockedCount();
        int blockedCount = list.getStreak(i).getCount() - unblockedCount;

        switch (i + 2 + shift) {
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

  /**
   * Calculate utility value of a board state, based on streaks found for aiPlayer and opponent.
   * @param boardState    boardState to calculate utility from
   * @return  utility
   */
  public double calculateUtilityOfBoardState(GameBoard boardState) {
    double aiUtility = this.calculateUtility(boardState.checkBoardForStreaks(aiPlayer), false);
    double opponentUtility = this.calculateUtility(boardState.checkBoardForStreaks(opponent), true);

    if ((aiUtility == 1) && (opponentUtility < 1)) {
      return 1;
    }

    if ((aiUtility < 1) && (opponentUtility == 1)) {
      return -1;
    }

    return (aiUtility - opponentUtility);
  }


  /**
   *
   * @param boardState
   * @param maxUtility
   * @param lastMove
   * @return
   */

  public double maximizer(GameBoard boardState, double maxUtility, int[] lastMove, int depth) {
    if (boardState.checkWinningMove(lastMove)) {
      if (boardState.returnPosition(lastMove) == aiPlayer.getSymbol()) {
        return 4.0;
      }
      else {
        return -4.0;
      }
    }

    if (boardState.isOutOfMoves() || (depth >= this.searchDepth)) {
      return this.calculateUtilityOfBoardState(boardState);
    }
    double utility = Double.NEGATIVE_INFINITY;

    List<int[]> actionSet = boardState.getActionSet(lastMove, this.searchRadius);

    for(int[] newMove : actionSet) {
      utility = Math.max(utility,
              minimizer(boardState.getBoardState(newMove, aiPlayer),utility, newMove,depth + 1));
      if (utility >= maxUtility) {
        break;
      }
    }
    return utility;
  }

  public double minimizer(GameBoard boardState, double minUtility, int[] lastMove, int depth) {
    if (boardState.checkWinningMove(lastMove)) {
      if (boardState.returnPosition(lastMove) == aiPlayer.getSymbol()) {
        return 4;
      }
      else {
        return -4;
      }
    }

    if (boardState.isOutOfMoves() || (depth >= this.searchDepth)) {
      return this.calculateUtilityOfBoardState(boardState);
    }

    double utility = Double.POSITIVE_INFINITY;

    List<int[]> actionSet = boardState.getActionSet(lastMove, this.searchRadius);

    for(int[] newMove : actionSet) {
      utility = Math.min(utility,
              maximizer(boardState.getBoardState(newMove, opponent),utility, newMove,depth + 1));
      if (utility <= minUtility) {
        break;
      }
    }
    return utility;
  }

  public int[] minimax (GameBoard boardState, int[] lastMove, boolean firstMove) {
    if (firstMove) {
      return this.getRandomMove();
    }
    if (boardState.isOutOfMoves()) {
      return null;
    }

    List<int[]> optimalMoveList = new ArrayList<int[]>();
    List<int[]> actionSet = boardState.getActionSet(lastMove, this.searchRadius);

    double utility = Double.NEGATIVE_INFINITY;
    double moveUtility, maxStreak = Double.NEGATIVE_INFINITY;
    int currStreak;
    GameBoard newBoardState = null;

    for (int[] newMove : actionSet) {
      newBoardState = boardState.getBoardState(newMove, aiPlayer);
      moveUtility = minimizer(newBoardState, utility, newMove,1);
      currStreak = newBoardState.checkMaximumConsecutive(newMove);

      if ((moveUtility == utility)) {
        if (maxStreak == currStreak) {
          optimalMoveList.add(newMove);
        } else if (maxStreak < currStreak) {
          optimalMoveList.clear();
          optimalMoveList.add(newMove);
          maxStreak = currStreak;
        }
      }
      if (moveUtility > utility) {
        utility = moveUtility;
        optimalMoveList.clear();
        optimalMoveList.add(newMove);
        maxStreak = currStreak;
      }
    }
    return this.getRandomMove(optimalMoveList);
  }
}
