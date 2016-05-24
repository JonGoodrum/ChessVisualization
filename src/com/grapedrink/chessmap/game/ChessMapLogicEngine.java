package com.grapedrink.chessmap.game;

import java.util.Collection;
import java.util.Map;

import com.grapedrink.chessmap.logic.bitboards.PieceColor;

/*
 * TODO : implement painting/teaching NON-ABSTRACT functions such as:
 *            getValidMoves()
 *            getPointDistribution(boolean isBlack)
 *            getNetPointDistribution()
 *            getDefendedSquares(boolean isBlack)
 *            getNetDefendedSquares()
 *            getDefenseCount(boolean isBlack)
 *            getNetDefenseCount()
 *            
 *        Default function value will be return null, so UI can
 *        know whether or not the subclass has overriden it
 */
public abstract class ChessMapLogicEngine {
	
	
	/**
	 * Initializes a new game in the board's internal representation
	 */
	public abstract void setNewGame();

	
	/**
	 * Returns the next stored move.
	 * 
	 * ex: returns <"e2", "e4">, a piece moving
	 * from e2 to e4
	 * 
	 * @return move
	 */
	public abstract Map.Entry<String, String> getNextMove() throws IndexOutOfBoundsException;

	
	/**
	 * Returns the previous stored move.
	 * 
	 * ex: returns <"e2", "e4">, a piece moving
	 * from e2 to e4
	 * 
	 * @return move
	 */
	public abstract Map.Entry<String, String> getPrevMove() throws IndexOutOfBoundsException;

	
	/**
	 * Returns whether or not the game has a next move stored.
	 * 
	 * @return move available
	 */
	public abstract boolean hasNextMove();

	
	/**
	 * Returns whether or not the game has a next move stored.
	 * 
	 * @return move available
	 */
	public abstract boolean hasPrevMove();

	
	/**
	 * Sets the active player in the logic's internal representation
	 * Useful during game imports.
	 * 
	 * @param playerColor
	 */
	public abstract void setActivePlayer(boolean isBlacksTurn);
	
	
	/**
	 * Moves the piece at source to destination.
	 * 
	 * @param source source
	 * @param destination destination
	 */
	public abstract void setMove(String source, String destination);
	
	
	/**
	 * Returns whether or not the move is valid, given the circumstances
	 * of the game (active player, piece positions, etc)
	 * @param source
	 * @param destination
	 * @return
	 */
	public abstract boolean isValidMove(String source, String destination);
	
	
	/**
	 * Returns an Iterable of positions which are valid moves for the square
	 * at source.  If a piece has no valid moves, or the square is empty,
	 * then this returns an empty list.
	 * 
	 * @param source
	 * @return
	 */
	public abstract Iterable<String> getValidMoves(String source);
	
	
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

	
	public abstract void addPiece(String pieceCode, String position);


	public abstract PieceColor getActivePlayer();


	public abstract Collection<String> getTotalDefense(PieceColor activePlayer);

	/**
	 * Returns null if nobody has won.
	 * 
	 * @return
	 */
	public abstract PieceColor getWinner();
}
