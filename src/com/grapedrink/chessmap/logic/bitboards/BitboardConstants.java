package com.grapedrink.chessmap.logic.bitboards;

//	https://chessprogramming.wikispaces.com/Subtracting+a+Rook+from+a+Blocking+Piece
public class BitboardConstants {
	
	public static final long BORDER    = 0xFF818181818181FFL;
	public static final long BL_BORDER = 0x80808080808080FFL;
	public static final long BR_BORDER = 0x01010101010101FFL;
	public static final long TL_BORDER = 0xFF80808080808080L;
	public static final long TR_BORDER = 0xFF01010101010101L;

	public static final long MAIN_DIAGONAL = 0x0102040810204080L;
	public static final long MAIN_ANTI_DIAGONAL = 0x8040201008040201L;
	
	public static final String[] PIECE_CODES = {"bB", "bK", "bN", "bP", "bQ", "bR", "wB", "wK", "wN", "wP", "wQ", "wR"};
	
	/**
	 * The upward diagonals, arranged from left to right.
	 * DIAGONALS[0] passes through a8,
	 * DIAGONALS[8] passes through a1 and h8,
	 * and DIAGONALS[15] passes through h1.
	 */
	public static final long[] DIAGONALS = {
		0x8000000000000000L,
		0x4080000000000000L,
		0x2040800000000000L,
		0x1020408000000000L,
		0x0810204080000000L,
		0x0408804020100000L,
		0x0204081020408000L,
		0x0102040810204080L,
		0x0001020408102040L,
		0x0000010204081020L,
		0x0000000102040810L,
		0x0000000001020408L,
		0x0000000000010204L,
		0x0000000000000102L,
		0x0000000000000001L,
	};
	
	/**
	 * The downward anti-diagonals, arranged from left to right.
	 * ANTI_DIAGONALS[0] passes through a1,
	 * ANTI_DIAGONALS[8] passes through a8 and h1,
	 * and ANTI_DIAGONALS[15] passes through h8.
	 */
	public static final long[] ANTI_DIAGONALS = {
		0x0000000000000080L,
		0x0000000000008040L,
		0x0000000000804020L,
		0x0000000080402010L,
		0x0000008040201008L,
		0x0000804020100804L,
		0x0080402010080402L,
		0x8040201008040201L,
		0x0040201008040201L,
		0x0000201008040201L,
		0x0000001008040201L,
		0x0000000008040201L,
		0x0000000000040201L,
		0x0000000000000201L,
		0x0000000000000001L,
	};
	
	/**
	 * The ranks, with
	 * RANKS[0] representing rank 1
	 * and RANKS[7] representing rank 8.
	 */
	public static final long[] RANKS = {
		0x00000000000000FFL,
		0x000000000000FF00L,
		0x0000000000FF0000L,
		0x00000000FF000000L,
		0x000000FF00000000L,
		0x0000FF0000000000L,
		0x00FF000000000000L,
		0xFF00000000000000L,
	};
	/**
	 * The files, with
	 * FILES[0] representing the a-file
	 * and FILES[7] representing the h-file.
	 */
	public static final long[] FILES = {
		0x8080808080808080L,
		0x4040404040404040L,
		0x2020202020202020L,
		0x1010101010101010L,
		0x0808080808080808L,
		0x0404040404040404L,
		0x0202020202020202L,
		0x0101010101010101L,
	};
	
	public static final long FILLED_UP_DIAGONAL   = 0x000103070F1F3F7FL;
	public static final long FILLED_DOWN_DIAGONAL = 0x0080C0E0F0F8FCFEL;
	
	/**
	 * The starting positions of the pieces, with
	 * INITIAL_POSITIONS[0] representing the black bishops
	 * and INITIAL_POSITIONS[11] representing the white rooks.
	 * The order of these items correlates with the ordering
	 * of the elements in PIECE_CODES.
	 */
	public static final Long[] INITIAL_POSITIONS = {
		0x2400000000000000L,
		0x0800000000000000L,
		0x4200000000000000L,
		0x00FF000000000000L,
		0x1000000000000000L,
		0x8100000000000000L,
		0x0000000000000024L,
		0x0000000000000008L,
		0x0000000000000042L,
		0x000000000000FF00L,
		0x0000000000000010L,
		0x0000000000000081L
	};
	
	
	/**
	 * Determines whether or not value
	 * is a position(aka power of 2).
	 * 
	 * @param value value
	 * @return whether or not it's a position
	 */
	private static boolean isPosition(long value) {
		return value == 0 ? false : (value & (value-1)) == 0;
	}
	
	/**
	 * Converts the String representation of a square,
	 * such as "c2", into a long, such as 8192L.
	 * 
	 * @param position position
	 * @return position as long
	 */
	public static long getPositionAsLong(String position) throws IllegalArgumentException {
		if (position == null || position.length() != 2 || position.charAt(0) < 'a' || position.charAt(0) > 'h' || position.charAt(1) < '1' || position.charAt(1) < '8') {
			throw new IllegalArgumentException();
		}
		return RANKS[position.charAt(1)-48] & FILES[position.charAt(0)-97];
	}
	
	/**
	 * Returns the rank intersecting this position,
	 * or 0L if this is not a valid position.
	 * 
	 * @param position position
	 * @return rank as long
	 */
	public static long getRank(long position) {
		if (!isPosition(position)) {
			return 0L;
		}
		for (long ray : RANKS) {
			if ((ray & position) == position) {
				return ray;
			}
		}
		return 0L;
	}
	
	/**
	 * Returns the file intersecting this position,
	 * or 0L if this is not a valid position.
	 * 
	 * @param position position
	 * @return file as long
	 */
	public static long getFile(long position) {
		if (!isPosition(position)) {
			return 0L;
		}
		for (long ray : FILES) {
			if ((ray & position) == position) {
				return ray;
			}
		}
		return 0L;
	}
	
	/**
	 * Returns the diagonal intersecting this position,
	 * or 0L if this is not a valid position.
	 * 
	 * @param position position
	 * @return diagonal as long
	 */
	public static long getDiagonal(long position) {
		if (!isPosition(position)) {
			return 0L;
		}
		for (long ray : DIAGONALS) {
			if ((ray & position) == position) {
				return ray;
			}
		}
		return 0L;
	}

	/**
	 * Returns the anti-diagonal intersecting this position,
	 * or 0L if this is not a valid position.
	 * 
	 * @param position position
	 * @return anti-diagonal as long
	 */
	public static long getAntiDiagonal(long position) {
		if (!isPosition(position)) {
			return 0L;
		}
		for (long ray : ANTI_DIAGONALS) {
			if ((ray & position) == position) {
				return ray;
			}
		}
		return 0L;
	}
	
	/**
	 * Returns the 3-8 squares adjacent to, but not including, position.
	 * Returns 0L if not a valid position.
	 * 
	 * @param position position
	 * @return adjacent squares as long
	 */
	public static long getAdjacentSquares(long position) {
		if (!isPosition(position)) {
			return 0L;
		}
		long myRow = getRank(position) & ((position << 1) | position | (position >>> 1));
		return position ^ ((myRow << 8) | myRow | (myRow >>> 8));
	}
	
	/**
	 * Returns the 2-8 squares that a knight on this square can access.
	 * Returns 0L if not a valid position
	 * 
	 * @param position position
	 * @return knight squares as long
	 */
	public static long getKnightSquares(long position) {
		if (!isPosition(position)) {
			return 0L;
		}
		long oneaway = ((position << 1) | (position >>> 1)) & getRank(position);
		long twoaway = ((position << 2) | (position >>> 2)) & getRank(position);
		return (oneaway << 16) | (oneaway >>> 16) | (twoaway << 8) | (twoaway >>> 8);
	}
	
	/**
	 * Prints a long as a board of 1's and 0's
	 * 
	 * @param positions long to print as board
	 */
	public static void printBinaryBoard(long positions) {
		String replacementCharacter = "•";
		StringBuilder binaryBoard = new StringBuilder();
		String binaryString = String.format("%64s", Long.toBinaryString(positions)).replace(" ","0").replace("0", replacementCharacter);
		for (int i = 0; i < 64; ++i) {
			binaryBoard.append(binaryString.charAt(i));
			if (i%8==7) {
				binaryBoard.append("\n");
			}
		}
		System.out.println(binaryBoard.toString());
	}
	
}
