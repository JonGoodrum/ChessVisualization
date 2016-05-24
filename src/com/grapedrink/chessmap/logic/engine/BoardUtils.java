package com.grapedrink.chessmap.logic.engine;

import java.util.Map;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;
import com.grapedrink.chessmap.logic.bitboards.PieceUtils;

public class BoardUtils {
	
	public static final long UNIVERSE    = 0xFFFFFFFFFFFFFFFFL;
	public static final long BORDER      = 0xFF818181818181FFL;
	public static final long L_BORDER    = 0x8080808080808080L;
	public static final long R_BORDER    = 0x0101010101010101L;
	public static final long T_BORDER    = 0xFF00000000000000L;
	public static final long B_BORDER    = 0x00000000000000FFL;
	public static final long BL_BORDER   = 0x80808080808080FFL;
	public static final long BR_BORDER   = 0x01010101010101FFL;
	public static final long TL_BORDER   = 0xFF80808080808080L;
	public static final long TR_BORDER   = 0xFF01010101010101L;
	
	private BoardUtils(){}
	
	/**
	 * Determines whether the board has a neighbor in a given direction.
	 * Directions are numbered in a clockwise manner, with 0 representing
	 * the north neighbor and 7 representing the northwest neighbor.
	 * 
	 * @param position a position
	 * @param direction direction
	 * @return existence of a neighbor
	 * @throws IllegalArgumentException
	 */
	public static boolean hasNeighbor(long position, int direction) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		switch (direction) {
		case 0:
			return (position & T_BORDER) == 0L;
		case 1:
			return (position & TR_BORDER) == 0L;
		case 2:
			return (position & R_BORDER) == 0L;
		case 3:
			return (position & BR_BORDER) == 0L;
		case 4:
			return (position & B_BORDER) == 0L;
		case 5:
			return (position & BL_BORDER) == 0L;
		case 6:
			return (position & L_BORDER) == 0L;
		case 7:
			return (position & TL_BORDER) == 0L;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Returns the bitshift corresponding with a neighboring square.
	 * Directions are numbered in a clockwise manner, with 0 representing
	 * the north neighbor and 7 representing the northwest neighbor.
	 * 
	 * This method will return a bitshift regardless of whether or not
	 * position has a neighbor in that direction, so it's advised to use
	 * the hasNeighbor(position, direction) method accordingly.
	 * 
	 * @param position
	 * @param direction
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static long getNeighbor(long position, int direction) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		switch (direction) {
		case 0:
			return position << 8;
		case 1:
			return position << 7;
		case 2:
			return position >>> 1;
		case 3:
			return position >>> 9;
		case 4:
			return position >>> 8;
		case 5:
			return position >>> 7;
		case 6:
			return position << 1;
		case 7:
			return position << 9;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Given a position and a direction, returns the location of the next piece on this ray.
	 * Returns 0L if there is no neighbor.
	 * 
	 * @param position position to seek from
	 * @param pieces game pieces
	 * @param direction
	 * @return neighbor's position
	 */
	public static long getNeighboringPiece(final long position, final Map<String, Long> pieces, final int direction) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		long allPieces = PieceUtils.getAllPieces(pieces);
		
		long iterator = position;
		while (hasNeighbor(iterator, direction)) {
			iterator = getNeighbor(iterator, direction);
			if ((iterator & allPieces) == iterator) {
				return iterator;
			}
		}
		return 0L;
	}
}
