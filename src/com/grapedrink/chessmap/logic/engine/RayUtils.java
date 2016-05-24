package com.grapedrink.chessmap.logic.engine;

import java.util.Map;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;
import com.grapedrink.chessmap.logic.bitboards.PieceUtils;

public class RayUtils {

	private RayUtils() {}
	
	/**
	 * Returns the defended ray traveling from position in the specified direction.
	 * This ray does not include position but includes the blocking piece, regardless of color.
	 * 
	 * Returns 0L if this piece is at a border and direction extends off the board.
	 * 
	 * @param position
	 * @param pieces
	 * @param direction
	 * @return
	 */
	public static long getDefendedRay(final long position, final Map<String, Long> pieces, final int direction) {
		InputValidation.validatePosition(position);
		long ray = 0L;
		long allPieces = PieceUtils.getAllPieces(pieces);
		long iterator = position;
		while (BoardUtils.hasNeighbor(iterator, direction)) {
			iterator = BoardUtils.getNeighbor(iterator, direction);
			ray |= iterator;
			if ((iterator & allPieces) == iterator) {
				return ray;
			}
		}
		return 0L;
	}
}
