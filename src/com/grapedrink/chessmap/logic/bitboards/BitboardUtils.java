package com.grapedrink.chessmap.logic.bitboards;

//	https://chessprogramming.wikispaces.com/Subtracting+a+Rook+from+a+Blocking+Piece
public class BitboardUtils {
	
	public static final long BORDER = 0xFF818181818181FFL;
	public static final long BL_BORDER   = 0x80808080808080FFL;
	public static final long BR_BORDER   = 0x01010101010101FFL;
	public static final long TL_BORDER   = 0xFF80808080808080L;
	public static final long TR_BORDER   = 0xFF01010101010101L;
	public static final long L_BORDER    = 0x8080808080808080L;
	public static final long R_BORDER    = 0x0101010101010101L;
	public static final long T_BORDER    = 0xFF00000000000000L;
	public static final long B_BORDER    = 0x00000000000000FFL;

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
	
	private static boolean isPosition(String str) {
		return !(str == null || str.length() != 2 || str.charAt(0) < 'a' || str.charAt(0) > 'h' || str.charAt(1) < '1' || str.charAt(1) > '8');
	}
	
	/**
	 * Wrapper class for throwing exceptions.  Throws an exception if
	 * a long is not a valid position (ex: 0b00010100L).
	 * 
	 * @param position position to validate
	 * @throws IllegalArgumentException
	 */
	private static void validatePosition(long position) throws IllegalArgumentException {
		if (!isPosition(position)) {
			throw new IllegalArgumentException();
		}
	}
	
	
	/**
	 * Converts the String representation of a square,
	 * such as "c2", into a long, such as 8192L.
	 * 
	 * @param position position
	 * @return position as long
	 * @throws IllegalArgumentException
	 */
	public static long getPositionAsLong(String position) throws IllegalArgumentException {
		if (!isPosition(position)) {
			throw new IllegalArgumentException();
		}
		return RANKS[position.charAt(1)-49] & FILES[position.charAt(0)-97];
	}
	
	/**
	 * Converts the long representation of a square,
	 * such as 8192L, into a String, such as "c2".
	 * 
	 * @param position
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static String getPositionAsString(long position) throws IllegalArgumentException {
		validatePosition(position);
		int rank = 0;
		int file = 0;
		for (int i=0; i<RANKS.length; ++i) {
			if ((RANKS[i] & position) != 0L) {
				rank = i;
				break;
			}
		}
		for (int i=0; i<FILES.length; ++i) {
			if ((FILES[i] & position) != 0L) {
				file = i;
				break;
			}
		}
		return String.format("%s%d", (char)(file+97), rank+1); 
	}
	
	/**
	 * Returns the rank intersecting this position.
	 * 
	 * @param position position
	 * @return rank as long
	 * @throws IllegalArgumentException
	 */
	public static long getRank(long position) throws IllegalArgumentException {
		validatePosition(position);
		for (long ray : RANKS) {
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
		validatePosition(position);
		for (long ray : FILES) {
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
		validatePosition(position);
		for (long ray : DIAGONALS) {
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
		validatePosition(position);
		for (long ray : ANTI_DIAGONALS) {
			if ((ray & position) == position) {
				return ray;
			}
		}
		return 0L;
	}
	
	/**
	 * Returns the 3-8 squares adjacent to, but not including, position.
	 * 
	 * @param position position
	 * @return rank as long
	 * @throws IllegalArgumentException
	 */
	public static long getAdjacentSquares(long position) throws IllegalArgumentException {
		validatePosition(position);
		long myRow = getRank(position) & ((position << 1) | position | (position >>> 1));
		return position ^ ((myRow << 8) | myRow | (myRow >>> 8));
	}
	
	/**
	 * Returns the 2-8 squares that a knight on this square can access.
	 * 
	 * @param position position
	 * @return rank as long
	 * @throws IllegalArgumentException
	 */
	public static long getKnightSquares(long position) throws IllegalArgumentException {
		validatePosition(position);
		long oneaway = ((position << 1) | (position >>> 1)) & getRank(position);
		long twoaway = ((position << 2) | (position >>> 2)) & getRank(position);
		return (oneaway << 16) | (oneaway >>> 16) | (twoaway << 8) | (twoaway >>> 8);
	}
	
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
		validatePosition(position);
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
		validatePosition(position);
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
