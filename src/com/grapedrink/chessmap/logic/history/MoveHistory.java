package com.grapedrink.chessmap.logic.history;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the moves taken and the current player's turn
 */
public class MoveHistory {

	private int moveCount;
	private boolean isBlacksTurn;
	private Map<Integer, Map.Entry<String, String>> history;

	public MoveHistory() {
		moveCount = 0;
		isBlacksTurn = false;
		history = new HashMap<>();
	}
	
	public void addMove(String src, String dst) {
		history.put(++moveCount, new AbstractMap.SimpleEntry<String, String>(src, dst));
		int i = moveCount;
		while (history.remove(++i) != null);
		switchTurns();
	}
	
	public boolean isBlacksTurn() {
		return isBlacksTurn;
	}
	
	public void setBlacksTurn(boolean isBlacksTurn) {
		this.isBlacksTurn = isBlacksTurn;
	}
	
	/**
	 * Returns whether or not there is a move stored after the current move
	 * @return
	 */
	public boolean hasNext() {
		return moveCount < history.size();
	}
	
	/**
	 * Returns whether or not there is a move stored before the current move
	 * @return
	 */
	public boolean hasPrev() {
		return moveCount > 0;
	}
	
	/**
	 * Returns the next stored move, or null if none
	 * 
	 * @return move
	 * @throws IllegalAccessException 
	 */
	public Map.Entry<String, String> getNext() throws IllegalAccessException {
		if (moveCount < history.size()) {
			switchTurns();
			return history.get(++moveCount);
		}
		throw new IllegalAccessException();
	}
	
	/**
	 * Returns the previous stored move.
	 * 
	 * @return move
	 * @throws IllegalAccessException 
	 */
	public Map.Entry<String, String> getPrev() throws IllegalAccessException {
		if (moveCount > 0) {
			switchTurns();
			return history.get(moveCount--);
		}
		throw new IllegalAccessException();
	}
	
	/**
	 *  Returns the current number of turns.
	 *  
	 *  ex: a game beginning with "1. e4 e5 2. d4"
	 *  would return 3 if this function was called.
	 * 
	 * @return moveCount
	 */
	public int getMoveCount() {
		return moveCount;
	}
	
	private void switchTurns() {
		isBlacksTurn = !isBlacksTurn;
	}
}
