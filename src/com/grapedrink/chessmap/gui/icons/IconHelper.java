package com.grapedrink.chessmap.gui.icons;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.grapedrink.chessmap.gui.GuiConstants;
import com.grapedrink.chessmap.gui.board.ChessBoardPanel;
import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;

public class IconHelper {
	
	private UserInterfaceFactory userInterfaceFactory;
	private Map<String, ImageIcon> icons;
	
	public IconHelper(UserInterfaceFactory userInterfaceFactory) {
		this.userInterfaceFactory = userInterfaceFactory;
		this.icons = new HashMap<>();
		String[] pieces = {"bB", "bK", "bN", "bP", "bQ", "bR", "wB", "wK", "wN", "wP", "wQ", "wR"};
		for (String piece : pieces) {
			icons.put(piece, new ImageIcon(IconHelper.class.getResource(String.format("%s.png", piece))));
		}
		resize(GuiConstants.ChessBoard.SIDE_LENGTH);
	}
	
	public void resize(int dim) {
		for (String pieceCode : icons.keySet()) {
			ImageIcon original = icons.get(pieceCode);
			ImageIcon resized = new ImageIcon(original.getImage().getScaledInstance(dim, dim, java.awt.Image.SCALE_SMOOTH));
			icons.put(pieceCode, resized);
		}
	}
	
	public ImageIcon get(String pieceCode) {
		return icons.get(pieceCode);
	}
}
