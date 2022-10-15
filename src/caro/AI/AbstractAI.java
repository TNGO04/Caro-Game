package caro.AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import caro.board.BoardSubset;
import caro.board.GameBoard;

public class AbstractAI {
  protected int boardDimension;

  public AbstractAI(int boardDimension) throws IllegalArgumentException{
    if (boardDimension <= 0) {
      throw new IllegalArgumentException("Board dimension has to be positive.");
    }
    this.boardDimension = boardDimension;
  }

  /**
   * Get a random move with a bias for center of board.
   * A position at the center of board allows for more opportunities to expand; therefore, this
   * random move generator favors position mid-board.
   */
  public int[] getRandomMove() {
    Random rand = new Random();
    int numAverage = 5;

    int row = 0, column = 0;

    for (int i = 0; i < numAverage; i++) {
      row += rand.nextInt(boardDimension);
      column += rand.nextInt(boardDimension);
    }

    row = (int) Math.ceil(row / numAverage);
    column = (int) Math.ceil(column / numAverage);

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
  public List<int[]> getActionSet(GameBoard board, int[] lastMove, int radius) {
    BoardSubset search = new BoardSubset(lastMove, this.boardDimension, radius);
    List<int[]> actionSet = new ArrayList<int[]>();

    for (int row = search.getTopRow(); row <= search.getBotRow(); row++) {
      for (int col = search.getLeftCol(); col <= search.getRightCol(); col++) {
        if (board.isEmpty(row, col) && !board.isDisconnected(row, col)) {
          actionSet.add(new int[]{row, col});
        }
      }
    }
    return actionSet;
  }

  public List<int[]> getActionSet(GameBoard board) {
    List<int[]> actionSet = new ArrayList<int[]>();

    for (int row = 0; row < board.getBoardDimension(); row++) {
      for (int col = 0; col < board.getBoardDimension(); col++) {
        if (board.isEmpty(row, col) && !board.isDisconnected(row, col)) {
          actionSet.add(new int[]{row, col});
        }
      }
    }
    return actionSet;
  }


}
