package com.grapedrink.chessmap.gui.board;

import java.awt.Color;

import javax.swing.JButton;

import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
import com.grapedrink.chessmap.ui.io.PieceDragListener;

@SuppressWarnings("serial")
public class ChessBoardSquare extends JButton {

	private Color defaultColor;
	
	public ChessBoardSquare(int rank, char file, PieceDragListener pieceDragListener) {
		defaultColor = (rank+file-97) % 2 == 0 ? GuiConstants.Colors.DEFAULT_LIGHT : GuiConstants.Colors.DEFAULT_DARK;
		super.setName(String.format("%s%d", file, rank));
		super.setBorderPainted(false);
		super.setBackground(defaultColor);
		super.setPreferredSize(GuiConstants.ChessBoard.TILE_SIZE);
		super.addMouseListener(pieceDragListener);
	}
	
	public void setColor(Color color) {
		super.setBackground(GuiConstants.Colors.tint(color, defaultColor));
	}
	
	public void resetColor() {
		super.setBackground(defaultColor);
	}
}
