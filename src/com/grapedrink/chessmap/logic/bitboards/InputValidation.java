package com.grapedrink.chessmap.logic.bitboards;

public class InputValidation {
	
	private InputValidation() {}
	
	
	private static boolean isPosition(long value) {
		return (value & (value-1)) == 0;
	}
	
	private static boolean isPosition(String str) {
		return !(str.length() != 2 || str.charAt(0) < 'a' || str.charAt(0) > 'h' || str.charAt(1) < '1' || str.charAt(1) > '8');
	}
	
	/**
	 * Wrapper class for throwing exceptions.  Throws an exception if
	 * a long represents more than one position (ex: 0b00010100L).
	 * 
	 * @param position position to validate
	 * @throws IllegalArgumentException
	 */
	public static void validatePosition(long position) throws IllegalArgumentException {
		if (!isPosition(position)) {
			throw new IllegalArgumentException(String.format("Invalid Position detected.  Position = {%s}", Long.toHexString(position)));
		}
	}
	
	/**
	 * Wrapper class for throwing exceptions.  Throws an exception if
	 * a String is not a valid position (ex: "a" or "foo").
	 * Null is considered a valid position.
	 * 
	 * @param position position to validate
	 * @throws IllegalArgumentException
	 */
	public static void validatePosition(String position) throws IllegalArgumentException {
		if (!isPosition(position)) {
			throw new IllegalArgumentException(String.format("Invalid Position detected.  Position = {%s}", position));
		}
	}

	/**
	 * Wrapper code for throwing exceptions.  Throws an
	 * exception if a String is not a valid pieceCode (ex: "bK").
	 * 
	 * @param pieceCode piece code
	 * @throws IllegalArgumentException
	 */
	public static void validatePieceCode(String pieceCode) throws IllegalArgumentException {
		for (String savedPieceCode : PieceContainer.PIECE_CODES) {
			if (savedPieceCode.equals(pieceCode)) {
				return;
			}
		}
		throw new IllegalArgumentException(String.format("Invalid PieceCode detected.  PieceCode = {%s}", pieceCode));
	}
}
