package com.grapedrink.chessmap.gui.controlpanel;

import java.awt.Color;
import java.awt.Dimension;

public class GuiConstants {

	public static class Colors {
		public static final Color DEFAULT_DARK = Color.decode("#769656");
		public static final Color DEFAULT_LIGHT = Color.decode("#eeeed2");
		
		public static final Color YELLOW = Color.decode("#FFFF00");
		public static final Color YELLOW_LIGHT = Color.decode("#E4EA11");
		public static final Color YELLOW_DARK = Color.decode("#CFDA1E");
		
		
		
		public static Color tint(Color color, Color defaultColor) {
			if (Colors.YELLOW.equals(color)) {
				return defaultColor == DEFAULT_DARK ? YELLOW_DARK : YELLOW_LIGHT;
			}
			else {
				return null;
			}
		}
	}
	
		
	public static class ChessBoard {
		public static final int SIDE_LENGTH = 80;
		public static final Dimension TILE_SIZE = new Dimension(SIDE_LENGTH, SIDE_LENGTH);
	}
}
