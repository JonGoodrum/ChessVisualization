package com.grapedrink.chessmap.game;

import java.util.Map;

public abstract class ChessMapLogicEngine {
	
	
	/**
	 * Initializes a new game in the board's internal representation
	 */
	public abstract void setNewGame();

	/**
	 * Returns the next stored move, if any.
	 * Returns null if no next move.
	 * 
	 * ex: returns <"e2", "e4">, a piece moving
	 * from e2 to e4
	 * 
	 * @return move
	 */
	public abstract Map.Entry<String, String> getNextMove();
	
	/**
	 * Sets the board to the previous position
	 */
	public abstract void undoMove();
	
    /**
     * Returns a Map<String, String> containing
     * the squares and the pieces on the squares.
     * Squares not listed are assumed to be blank.
     * 
     * ex: a return value of
     * <"e5", "bK"> and <"a2", "wK"> indicates that
     * the board contains only a black and white King,
     * positioned on e5 and a2 respectively
     *  
     * @return positions of pieces
     */
	public abstract Map<String, String> getBoard();
	
	/**
	 * Loads a game into the engine.
	 * 
	 * A game such as:
	 *     1.  e4  e5
	 *     2.  d4 Nf6
	 *     3...
	 *     
	 * Would be represented as:
	 *     game.get(1) = Map.Entry<String, String>("e2", "e4")
	 *     game.get(2) = Map.Entry<String, String>("e7", "e5")
	 *     game.get(3) = Map.Entry<String, String>("d2", "d4")
	 *     game.get(4) = Map.Entry<String, String>("g8", "f6")
	 *     ...
	 */
	public abstract void loadGame(Map<Integer, Map.Entry<String, String>> game);
	
	public abstract void resetBoard();
}
