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

  private final double unblockedFourUtility = 1.0;
  private final double blockedFourUtility = 0.5;
  private final double unblockedThreeUtility = 0.5;
  private final double blockedThreeUtility = 0.2;
  private final double unblockedTwoUtility = 0.15;
  private final double blockedTwoUtility = 0.05;

  /**
   * Constructor for MinimaxAI.
   *
   * @param board GameBoard object
   */
  public MinimaxAI(GameBoard board, Player aiPlayer) throws IllegalArgumentException {
    if ((board == null) || (aiPlayer == null)) {
      throw new IllegalArgumentException("Input object is null.");
    }
    this.board = board;
    this.aiPlayer = aiPlayer;
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
   * Instead, in this method, we only consider moves within SEARCH_RANGE
   * of the last move made by player.
   *
   * @param lastMove :   last move made in the game
   * @return list of possible moves to make
   */
  public List<int[]> getActionSet(int[] lastMove, int radius) {
    SearchRange search = this.board.calculateSearchRange(lastMove, radius);
    List<int[]> actionSet = new ArrayList<int[]>();

    for (int row = search.getTopRow(); row <= search.getBotRow(); row++) {
      for (int col = search.getLeftCol(); col <= search.getRightCol(); col++) {
        if (this.board.returnPosition(row, col) == Game.EMPTY) {
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
   * Given an array and a symbol, count the maximum amount of time the symbol appears consecutively.
   * This function ignores streaks that are blocked on both sides. A streak is unblocked on one side
   * if it is preceded or succeeded by an EMPTY symbol.
   * e.g: XXXO__OXXXO_XXX:
   * first cluster of XXX: blocked both sides, since not preceded by an EMPTY symbol, and
   * succeeded by 'O'
   * second cluster of XXX: blocked both sides, preceded and succeeded by 'O'
   * third cluster of XXX:  blocked on one side (right-most) since preceded by EMPTY symbol
   * This function also keeps track of how many times a streak of maximum size appears in the array.
   * and how many of those streaks are unblocked (meaning the streak is surrounded by EMPTY symbols
   * on both sides).
   * e.g.: _XXX_OXXX_OXXX -> maxStreakSize = 3, streakCount = 2, unblockedCount = 1
   * first cluster: unblocked both sides
   * second cluster: counted in streakCount because blocked only on one side
   * third cluster: not counted in streakCount because blocked on both sides
   *
   * @param array  array to be searched
   * @param symbol symbol to be searched
   * @return max number of time the symbol appears consecutively
   */
  public StreakList countConsecutive(char[] array, char symbol) {
    int arrayLength = array.length;
    // initialize max streak size, and count of the number of streaks in array that is of that size
    StreakList list = new StreakList();

    // initialize streak marker, block marker, and streak counter
    int blockMarker = 0, maxStreak = 0;
    boolean inStreak = false;

    // loop through elements of array and detect streak
    for (int i = 0; i < arrayLength; i++) {
      if (array[i] == symbol) {
        // if symbol found, start count and turn streak tracker to true
        maxStreak++;
        if (!inStreak) {
          inStreak = true;
          /* if streak is at the start of the array or preceding character is not EMPTY, set block
          marker to 1 to note that streak is blocked left-side */
          if ((i == 0) || (array[i - 1] != Game.EMPTY)) {
            blockMarker = 1;
          }
          // if streak is at the end of the array, increase block marker
          if (i == (arrayLength - 1)) {
            blockMarker++;
          }
        }
      } else {
        if (inStreak) {
          // if streak ends
          inStreak = false;
          // if streaked is terminated not by an EMPTY symbol, increase block marker
          if (array[i] != Game.EMPTY) {
            blockMarker++;
          }
          list.addStreak(maxStreak, blockMarker);
          blockMarker = 0;
          maxStreak = 0;
        }
      }
    }
    // Since there could be a streak at the end of array, run a check one last time
    if (inStreak) {
      blockMarker++;
      list.addStreak(maxStreak, blockMarker);
    }
    return list;
  }

  /**
   * Scan the board to check for streaks.
   *
   * @return StreakList object representing all valid streaks found on the board
   */
  public StreakList checkBoardForStreaks(Player player) {
    char symbol = player.getSymbol();
    int dimension = this.board.getBoardDimension();
    StreakList list = new StreakList();

    // check each row for streak
    for (int row = 0; row < dimension; row++) {
      list.addStreakList(this.countConsecutive(this.board.getRow(row), symbol));
    }

    // check each column for streak
    for (int col = 0; col < dimension; col++) {
      list.addStreakList(this.countConsecutive(this.board.getColumn(col), symbol));
    }

    // check diagonals for streaks
    for (int row = Game.WIN_CONDITION - 1, col = 0; row < dimension; row++) {
      list.addStreakList(this.countConsecutive(
              this.board.getDiagonal(row, col, col, row), symbol));
    }

    for (int col = 1, row = dimension - 1; col <= (dimension - Game.WIN_CONDITION); col++) {
      list.addStreakList(this.countConsecutive(
              this.board.getDiagonal(row, col, col, row), symbol));
    }

    for (int col = Game.WIN_CONDITION - 1, row = dimension - 1; col < dimension; col++) {
      list.addStreakList(this.countConsecutive(
              this.board.getDiagonal(row, col, row - col, 0), symbol));
    }

    for (int row = dimension - 2, col = board.getBoardDimension() - 1;
         row >= (Game.WIN_CONDITION - 1); row--) {
      list.addStreakList(this.countConsecutive(
              this.board.getDiagonal(row, col, 0, col - row), symbol));
    }

    return list;
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
}
