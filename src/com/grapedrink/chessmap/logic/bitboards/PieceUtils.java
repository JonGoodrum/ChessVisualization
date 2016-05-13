package com.grapedrink.chessmap.logic.bitboards;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PieceUtils {

	private PieceUtils() {}
	
	public static final String[] PIECE_CODES = {"bB", "bK", "bN", "bP", "bQ", "bR", "wB", "wK", "wN", "wP", "wQ", "wR"};

	private static long _getPcs(Map<String, Long> pieces, int start, int stop) {
		long pcs = 0L;
		for (int i=start; i<stop; ++i) {
			pcs |= pieces.get(PIECE_CODES[i]);
		}
		return pcs;
	}

	public static boolean isBlack(long position, Map<String, Long> pieces) {
		InputValidation.validatePosition(position);
		if ((getAllPieces(pieces) & position) == 0L) {
			throw new IllegalArgumentException();
		}
		return PieceColor.BLACK.equals(getPieceColor(position, pieces));
	}
	
	public static PieceColor getPieceColor(String pieceCode) {
		return PieceColor.get(pieceCode);
	}

	public static PieceColor getPieceColor(long position, Map<String, Long> pieces) {
		String pieceCode = getPieceCode(position, pieces);
		return getPieceColor(pieceCode);
	}

	public static PieceType getPieceType(String pieceCode) {
		return PieceType.get(pieceCode);
	}

	public static PieceType getPieceType(long position, Map<String, Long> pieces) {
		String pieceCode = getPieceCode(position, pieces);
		return getPieceType(pieceCode);
	}
	
	public static String getPieceCode(long position, Map<String, Long> pieces) {
		for (String pieceCode : pieces.keySet()) {
			if ((pieces.get(pieceCode) & position) == position) {
				return pieceCode;
			}
		}
		return null;
	}

	public static long getFriendlyPieces(long position, Map<String, Long> pieces) {
		if (isUnoccupied(position, pieces)) {
			return 0L;
		}
		int x = isBlack(position, pieces) ? 0 : 6;
		return _getPcs(pieces, 0+x, 6+x);
	}

	private static boolean isUnoccupied(long position, Map<String, Long> pieces) {
		return (0L == (position & getAllPieces(pieces)));
	}

	public static long getEnemyPieces(long position, Map<String, Long> pieces) {
		if (isUnoccupied(position, pieces)) {
			return 0L;
		}
		int x = isBlack(position, pieces) ? 6 : 0;
		return _getPcs(pieces, 0+x, 6+x);
	}

	public static long getAllPieces(Map<String, Long> pieces) {
		return _getPcs(pieces, 0, 12);
	}

	public static long getPawnStructure(Map<String, Long> pieces) {
		return pieces.get("bP") | pieces.get("wP");
	}

	/**
	 * For the player at position, this returns the direction of travel for
	 * their pawns.  Throws an exception if position is not on the board
	 * @param position
	 * @param pieces
	 * @return
	 */
	public static int getPawnDirection(long position, Map<String, Long> pieces) {
		return isBlack(position, pieces) ? 4 : 0;
	}

	public static long getFriendlyKing(long position, Map<String, Long> pieces) {
		return isBlack(position, pieces) ? pieces.get("bK") : pieces.get("wK");
	}
	
	/**
	 * Returns a set of longs which represent the positions of this position's
	 * teammates.
	 * 
	 * @param position
	 * @param pieces
	 * @return
	 */
	public static Set<Long> getIndividualPieces(long position, Map<String, Long> pieces) {
		Set<Long> individualPieces = new HashSet<>();
		long myPieces = getFriendlyPieces(position, pieces);
		long currentPosition;
		for (int i=0; i<64; ++i) {
			currentPosition = (1L << i);
			if ((currentPosition & myPieces) != 0L) {
				individualPieces.add(currentPosition);
			}
		}
		return individualPieces;
	}

	public static long getEnemyKing(long position, Map<String, Long> pieces) {
		return isBlack(position, pieces) ? pieces.get("wK") : pieces.get("bK");
	}

	public static long getSlidingPieces(Map<String, Long> pieces, PieceColor color) {
		int x = PieceColor.BLACK.equals(color) ? 0 : 6;
		return pieces.get(PIECE_CODES[0+x]) | pieces.get(PIECE_CODES[4+x]) | pieces.get(PIECE_CODES[5+x]);
	}

	public static boolean isPieceCode(String s) {
		for (String pieceCode : PieceUtils.PIECE_CODES) {
			if (pieceCode.equals(s)) {
				return true;
			}
		}
		return false;
	}
}
