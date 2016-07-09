package com.grapedrink.chessmap.logic.utils;

import java.util.Map;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;
import com.grapedrink.chessmap.logic.bitboards.PieceColor;

public class GameUtils {

	private GameUtils(){}
	
	/**
	 * Determines if the player at position is in check.
	 * Returns false if there is no player at position.
	 */
	public static boolean isInCheck(long position, Map<String, Long> pieces) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		long king = PieceUtils.getFriendlyKing(position, pieces);
		return king == (king & DefenseUtils.getDefendedSquaresEnemyTeam(king, pieces));
	}
	
	/**
	 * Determines whether or not the piece at position is pinned.
	 * Returns false if there is no piece at position, the piece
	 * is a king, or the piece is not pinned.
	 * 
	 * @param position
	 * @param pieces
	 * @return
	 * @throws IllegalArgumentException if the position is invalid
	 */
	public static boolean isPinned(long position, Map<String, Long> pieces) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		long myKing = PieceUtils.getFriendlyKing(position, pieces);
		
		boolean thePieceAtPositionIsKing = position == myKing;
		boolean thePieceAtPositionIsNotInLineWithItsKing = (BoardUtils.getRaystar(position) & myKing) == 0L;
		if (thePieceAtPositionIsKing || thePieceAtPositionIsNotInLineWithItsKing) {
			return false;
		}
		
		long iterator = myKing;
		long allPieces = PieceUtils.getAllPieces(pieces);
		int direction = RayUtils.getConnectingDirection(myKing, position);
		while ((iterator = BoardUtils.getNeighboringSquare(iterator, direction)) != position) {
			if ((iterator & allPieces) == iterator) {
				return false;
			}
		}
		
		PieceColor enemyColor = PieceUtils.isBlack(position, pieces) ? PieceColor.WHITE : PieceColor.BLACK;
		long attackersThatCanPin = PieceUtils.getSlidingPieces(pieces, enemyColor);
		while (BoardUtils.hasNeighboringSquare(iterator, direction)) {
			iterator = BoardUtils.getNeighboringSquare(iterator, direction);
			if ((iterator & attackersThatCanPin) == iterator) {
				return (DefenseUtils.getDefendedSquares(iterator, pieces) & position) != 0L;
			}
		}
		return false;
	}
	
}
