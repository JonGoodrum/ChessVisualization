package com.grapedrink.chessmap.logic.utils;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;

public class RayUtils {

	private RayUtils() {}
	
	
	public static int getConnectingDirection(long src, long dst) {
		if ((BoardUtils.getRank(src) & dst) == dst) {
			return src > dst ? 2 : 6;
		}
		else if ((BoardUtils.getFile(src) & dst) == dst) {
			return src < dst ? 0 : 4;
		}
		else if ((BoardUtils.getDiagonal(src) & dst) == dst) {
			return src < dst ? 1 : 5;
		}
		return src > dst ? 3 : 7;
	}
	
	/**
	 * Returns the ray connecting the piece at src
	 * with the piece at dst.
	 * Connecting ray does not include src or dst.
	 * 
	 * Returns 0L if the pieces are disjoint
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static long getConnectingRay(long src, long dst) throws IllegalArgumentException {
		InputValidation.validatePosition(src);
		InputValidation.validatePosition(dst);
		if ((BoardUtils.getRank(src) & dst) == dst) {
			int srcdir = src > dst ? 2 : 6;
			int dstdir = src < dst ? 2 : 6;
			return getDirectionalRay(src, srcdir) & getDirectionalRay(dst, dstdir); 
		}
		else if ((BoardUtils.getFile(src) & dst) == dst) {
			int srcdir = src < dst ? 0 : 4;
			int dstdir = src > dst ? 0 : 4;
			return getDirectionalRay(src, srcdir) & getDirectionalRay(dst, dstdir); 
		}
		else if ((BoardUtils.getDiagonal(src) & dst) == dst) {
			int srcdir = src < dst ? 1 : 5;
			int dstdir = src > dst ? 1 : 5;
			return getDirectionalRay(src, srcdir) & getDirectionalRay(dst, dstdir); 
		}
		else if ((BoardUtils.getAntiDiagonal(src) & dst) == dst) {
			int srcdir = src > dst ? 3 : 7;
			int dstdir = src < dst ? 3 : 7;
			return getDirectionalRay(src, srcdir) & getDirectionalRay(dst, dstdir); 
		}
		return 0L;
	}
	
	/**
	 * Returns the ray of squares emanating from the square at position
	 * and travelling in direction.
	 * 
	 * @param position
	 * @param direction
	 * @return
	 * @throws IllegalArgumentException If the position is not
	 * a valid square or the direction is not 0 through 7
	 */
	public static long getDirectionalRay(long position, int direction) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		long iterator = position;
		long ray = 0L;
		while (BoardUtils.hasNeighboringSquare(iterator, direction)) {
			iterator = BoardUtils.getNeighboringSquare(iterator, direction);
			ray |= iterator;
		}
		return ray;
	}
	
	
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
	public static long getDefendedRay(final long position, final Map<String, Long> pieces, final int direction) {
		InputValidation.validatePosition(position);
		long ray = 0L;
		long allPieces = PieceUtils.getAllPieces(pieces);
		long iterator = position;
		while (BoardUtils.hasNeighboringSquare(iterator, direction)) {
			iterator = BoardUtils.getNeighboringSquare(iterator, direction);
			ray |= iterator;
			if ((iterator & allPieces) == iterator) {
				return ray;
			}
		}
		return ray;
	}
	 */
	
}
