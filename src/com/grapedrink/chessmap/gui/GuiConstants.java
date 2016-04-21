package com.grapedrink.chessmap.gui;

import java.awt.Color;
import java.awt.Dimension;

public class GuiConstants {

	public static class ChessBoard {
		public static final Color DARK = Color.decode("#769656");
		public static final Color LIGHT = Color.decode("#eeeed2");
		public static final int SIDE_LENGTH = 80;
		public static final Dimension TILE_SIZE = new Dimension(SIDE_LENGTH, SIDE_LENGTH);
	}
}
