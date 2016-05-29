package com.grapedrink.chessmap.logic.bitboards;

public enum PieceType {
	BISHOP,
	KING,
	KNIGHT,
	PAWN,
	QUEEN,
	ROOK,
	NONE;
	
	public static PieceType get(String pieceCode) {
		return get(pieceCode.charAt(1));
	}
	
	public static PieceType get(char code) {
		switch (code) {
		case 'B':
			return BISHOP;
		case 'K':
			return KING;
		case 'N':
			return KNIGHT;
		case 'P':
			return PAWN;
		case 'Q':
			return QUEEN;
		case 'R':
			return ROOK;
		default:
			return NONE;
		}
	}

	public static boolean isSlidingPiece(String pieceCode) {
		PieceType code = get(pieceCode);
		return BISHOP.equals(code) || QUEEN.equals(code) || ROOK.equals(code);
	}
}
