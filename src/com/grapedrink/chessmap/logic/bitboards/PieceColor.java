package com.grapedrink.chessmap.logic.bitboards;

public enum PieceColor {
	BOTH,
	BLACK,
	WHITE,
	NEITHER;
	
	public static PieceColor get(String pieceCode) {
		return get(pieceCode.charAt(0));
	}
	
	public static PieceColor get(char code) {
		switch (code) {
		case 'b':
			return BLACK;
		case 'w':
			return WHITE;
		default:
			return NEITHER;
		}
	}
}
