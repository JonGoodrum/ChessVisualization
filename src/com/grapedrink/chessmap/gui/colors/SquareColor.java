package com.grapedrink.chessmap.gui.colors;

import java.awt.Color;

import com.grapedrink.chessmap.logic.bitboards.PieceColor;

public enum SquareColor {
	DEFAULT_SQUARE_COLOR,
	DEFENDED_SQUARES,
	AVAILABLE_MOVES_FOR_PIECE;
	
	protected static final Color RED    = Color.decode("#FF0000");
	protected static final Color BLUE   = Color.decode("#0000FF");
	protected static final Color PURPLE = Color.decode("#FF00FF");
	protected static final Color YELLOW = Color.decode("#FFFF00");
	
	protected static final Color DEFAULT_DARK = Color.decode("#769656");
	protected static final Color DEFAULT_LIGHT = Color.decode("#eeeed2");
	protected static final Color YELLOW_DARK  = Color.decode("#CFDA1E");
	protected static final Color YELLOW_LIGHT = Color.decode("#E4EA11");
	protected static final Color RED_DARK     = Color.decode("#990000");
	protected static final Color RED_LIGHT    = Color.decode("#E50000");
	protected static final Color BLUE_DARK    = Color.decode("#000099");
	protected static final Color BLUE_LIGHT   = Color.decode("#0000E5");
	protected static final Color PURPLE_DARK  = Color.decode("#990099");
	protected static final Color PURPLE_LIGHT = Color.decode("#E500E5");
	
	/**
	 * Returns the painted color of this square, depending on the position
	 * of this square and the color of the player illuminating it
	 * @param guiColor
	 * @param position
	 * @param occupancy
	 * @return
	 */
	public static Color get(SquareColor guiColor, String position, PieceColor color) {
		switch (guiColor) {
		case DEFAULT_SQUARE_COLOR:
			return isDark(position) ? DEFAULT_DARK : DEFAULT_LIGHT;
		case DEFENDED_SQUARES:
			return getDefendedSquareColor(position, color);
		case AVAILABLE_MOVES_FOR_PIECE:
			return isDark(position) ? YELLOW_DARK : YELLOW_LIGHT;
		default:
			return null;
		}
	}
	
	/**
	 * Returns true if the square at position is dark, false otherwise
	 * 
	 * @param position
	 * @return isDark
	 */
	private static boolean isDark(String position) {
		int rank = position.charAt(1);
		int file = position.charAt(0) - 97;
		return (rank+file) % 2 != 0;
	}
	
	private static Color getDefendedSquareColor(String position, PieceColor color) {
		switch (color) {
		case BLACK:
			return isDark(position) ? BLUE_DARK : BLUE_LIGHT;
		case WHITE:
			return isDark(position) ? RED_DARK : RED_LIGHT;
		case BOTH:
			return isDark(position) ? PURPLE_DARK : PURPLE_LIGHT;
		default:
			return null;
		}
	}
}
