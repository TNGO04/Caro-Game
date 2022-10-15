package caro.AI;

import caro.board.GameBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * TrainedAI is obtained through reinforcement learning.
 * This class inherits from AbstractAI.
 */
public class TrainedAI extends AbstractAI {
  private double alpha;
  private double epsilon;
  private Map<GameBoard, Map<int[], Double>> qvalueDict;

  /**
   * Constructor.
   *
   * @param boardDimension    dimension of board
   */
  public TrainedAI(int boardDimension) {
    super(boardDimension);
    this.qvalueDict = new HashMap<GameBoard, Map<int[], Double>>();
    this.alpha = 0.5;
    this.epsilon = 0.3;
  }

  /**
   * Update the q value of a state, move pair in HashMap q.
   *
   * @param oldState    old state
   * @param move        move to be applied to old state
   * @param newState    new state after move
   * @param reward      old reward
   */
  public void update(GameBoard oldState, int[] move, GameBoard newState, Double reward) {
    Double oldQ, maxReward;
    oldQ = this.getqValue(oldState, move);
    maxReward = this.maxFutureRewards(newState);
    this.updateqValue(oldState, move, oldQ, reward, maxReward);
  }

  /**
   * Update the Q-value for the state `state` and the action `action`
   * given the previous Q-value `old_q`, a current reward `reward`,
   * and an estiamte of future rewards `future_rewards`.
   * Use the formula:
   * Q(s, a) <- old value estimate
   * + alpha * (new value estimate - old value estimate)
   * where `old value estimate` is the previous Q-value,
   * `alpha` is the learning rate, and `new value estimate`
   * is the sum of the current reward and estimated future rewards.
   */
  public void updateqValue(GameBoard state, int[] move, double oldq,
                           double reward, double futureReward) {
    if (this.qvalueDict.get(state) == null) {
      HashMap<int[], Double> moveMap = new HashMap<int[], Double>();
      moveMap.put(move, oldq + this.alpha * (reward + futureReward - oldq));
      this.qvalueDict.put(state, moveMap);
    }
    else {
      this.qvalueDict.get(state).put(move, oldq + this.alpha * (reward + futureReward - oldq));
    }
  }

  /**
   * Get q value of a [state, move] pair.
   *
   * @param state board state
   * @param move  move
   * @return      q value of state, move
   */
  public Double getqValue(GameBoard state, int[] move) {
    Double qvalue;
    if (this.qvalueDict.containsKey(state)) {
      qvalue = this.qvalueDict.get(state).get(move);
    } else {
      qvalue = null;
    }
    if (qvalue == null) {
      qvalue = 0.0;
    }
    return qvalue;
  }

  /**
   * Find maximum future rewards.
   *
   * @param state   state of board
   * @return        best reward for state
   */
  public Double maxFutureRewards(GameBoard state) {
    Double maxQ = Double.NEGATIVE_INFINITY;
    // find list of available actions
    List<int[]> actionSet = this.getActionSet(state);

    Double qvalue = 0.0;
    // loop through available action and return maximum Q value found
    for (int[] move : actionSet) {
      qvalue = this.getqValue(state, move);
      maxQ = Math.max(qvalue, maxQ);
    }
    return maxQ;
  }

  /**
   * Get AI's most optimal move.
   *
   * @param state     board state
   * @return          most optimal move
   */
  public int[] getOptimalMove(GameBoard state, int[] lastMove, boolean firstMove) {
    if (firstMove) {
      return this.getRandomMove();
    }

    List<int[]> actionSet = this.getActionSet(state);
    Random rand = new Random();
    Double maxQ = Double.NEGATIVE_INFINITY;
    List<int[]> optimalMove = new ArrayList<int[]>();
    double currQ;

    if (rand.nextDouble(1) < this.epsilon) {
      return this.getRandomMove(actionSet);
    } else {

      for (int[] move : actionSet) {
        currQ = this.getqValue(state, move);
        if (currQ == maxQ) {
          optimalMove.add(move);
        }
        else if (currQ > maxQ) {
          maxQ = currQ;
          optimalMove. clear();
          optimalMove.add(move);
        }
      }
      return this.getRandomMove(optimalMove);
    }
  }

}
