package com.grapedrink.chessmap.logic.utils;

import java.util.Map;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;

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
	
	
	
}
