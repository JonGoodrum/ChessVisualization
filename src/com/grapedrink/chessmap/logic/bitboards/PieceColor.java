package com.grapedrink.chessmap.logic.bitboards;

public enum PieceColor {
	BOTH,
	BLACK,
	WHITE;
	
	public static PieceColor get(String pieceCode) throws IllegalArgumentException {
		InputValidation.validatePieceCode(pieceCode);
		return get(pieceCode.charAt(0));
	}
	
	public static PieceColor get(char code) throws IllegalArgumentException {
		switch (code) {
		case 'b':
			return BLACK;
		case 'w':
			return WHITE;
		default:
			throw new IllegalArgumentException();
		}
	}
}
