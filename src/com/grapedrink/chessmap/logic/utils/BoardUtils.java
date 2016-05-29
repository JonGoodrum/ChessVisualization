package com.grapedrink.chessmap.logic.utils;

import java.util.Map;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;

public class BoardUtils {
	
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
	public static boolean hasNeighboringSquare(long position, int direction) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		switch (direction) {
		case 0:
			return (position & BoardConstants.T_BORDER) == 0L;
		case 1:
			return (position & BoardConstants.TR_BORDER) == 0L;
		case 2:
			return (position & BoardConstants.R_BORDER) == 0L;
		case 3:
			return (position & BoardConstants.BR_BORDER) == 0L;
		case 4:
			return (position & BoardConstants.B_BORDER) == 0L;
		case 5:
			return (position & BoardConstants.BL_BORDER) == 0L;
		case 6:
			return (position & BoardConstants.L_BORDER) == 0L;
		case 7:
			return (position & BoardConstants.TL_BORDER) == 0L;
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
	public static long getNeighboringSquare(long position, int direction) throws IllegalArgumentException {
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
		while (hasNeighboringSquare(iterator, direction)) {
			iterator = getNeighboringSquare(iterator, direction);
			if ((iterator & allPieces) == iterator) {
				return iterator;
			}
		}
		return 0L;
	}
	
	
	/**
	 * Returns the rank intersecting this position.
	 * 
	 * @param position position
	 * @return rank as long
	 * @throws IllegalArgumentException
	 */
	public static long getRank(long position) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		for (long ray : BoardConstants.RANKS) {
			if ((ray & position) == position) {
				return ray;
			}
		}
		return 0L;
	}

	
	/**
	 * Returns the file intersecting this position.
	 * 
	 * @param position position
	 * @return file as long
	 * @throws IllegalArgumentException
	 */
	public static long getFile(long position) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		for (long ray : BoardConstants.FILES) {
			if ((ray & position) == position) {
				return ray;
			}
		}
		return 0L;
	}


	/**
	 * Returns the diagonal intersecting this position.
	 * 
	 * @param position position
	 * @return diagonal as long
	 * @throws IllegalArgumentException
	 */
	public static long getDiagonal(long position) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		for (long ray : BoardConstants.DIAGONALS) {
			if ((ray & position) == position) {
				return ray;
			}
		}
		return 0L;
	}

	
	/**
	 * Returns the anti-diagonal intersecting this position.
	 * 
	 * @param position position
	 * @return rank as long
	 * @throws IllegalArgumentException
	 */
	public static long getAntiDiagonal(long position) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		for (long ray : BoardConstants.ANTI_DIAGONALS) {
			if ((ray & position) == position) {
				return ray;
			}
		}
		return 0L;
	}

	
	public static long getRaystar(long position) throws IllegalArgumentException {
		return position ^ (getRank(position) | getFile(position) | getDiagonal(position) | getAntiDiagonal(position));
	}
	
	
	/**
	 * Returns the 2-8 squares that a knight on this square can access.
	 * 
	 * @param position position
	 * @return rank as long
	 * @throws IllegalArgumentException
	 */
	public static long getKnightSquares(long position) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		long oneaway = ((position << 1) | (position >>> 1)) & getRank(position);
		long twoaway = ((position << 2) | (position >>> 2)) & getRank(position);
		return (oneaway << 16) | (oneaway >>> 16) | (twoaway << 8) | (twoaway >>> 8);
	}


	/**
	 * Returns the 3-8 squares adjacent to, but not including, position.
	 * 
	 * @param position position
	 * @return rank as long
	 * @throws IllegalArgumentException
	 */
	public static long getAdjacentSquares(long position) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		long myRow = getRank(position) & ((position << 1) | position | (position >>> 1));
		return position ^ ((myRow << 8) | myRow | (myRow >>> 8));
	}


	/**
	 * Returns the ray intersecting two squares.
	 * Returns 0L if no ray intersects these squares. 
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long getConnectingRay(long a, long b) {
		if ((a & getRank(b)) == a) {
			return getRank(a);
		}
		else if ((a & getFile(b)) == a) {
			return getFile(a);
		}
		else if ((a & getDiagonal(b)) == a) {
			return getDiagonal(a);
		}
		else if ((a & getAntiDiagonal(b)) == a) {
			return getAntiDiagonal(a);
		}
		return 0L;
	}
}
