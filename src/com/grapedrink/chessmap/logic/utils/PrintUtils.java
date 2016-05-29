package com.grapedrink.chessmap.logic.utils;

import java.util.Map;

public class PrintUtils {

	private static final String EMPTY_SQUARE = "•";
	
	private PrintUtils() {}
	
	/**
	 * Prints a long as a board of 1's and 0's
	 * 
	 * @param positions long to print as board
	 */
	public static void printBoard(long positions) {
		StringBuilder binaryBoard = new StringBuilder();
		String binaryString = String.format("%64s", Long.toBinaryString(positions)).replace(" ", EMPTY_SQUARE).replace("0", EMPTY_SQUARE);
		for (int i = 0; i < 64; ++i) {
			binaryBoard.append(binaryString.charAt(i));
			if (i%8==7) {
				binaryBoard.append("\n");
			}
		}
		System.out.println(binaryBoard.toString());
	}
	
	/**
	 * Prints a board of pieces, with capital letters
	 * denoting black pieces and lowercase representing whites.
	 * 
	 * @param pieces
	 */
	public static void printBoard(final Map<String, Integer> pieces) {
		// TODO : auto generated Method Stub
	}
}
