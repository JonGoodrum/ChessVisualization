package com.grapedrink.chessmap.gui.board;

import javax.swing.Icon;
import javax.swing.JButton;

import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
import com.grapedrink.chessmap.ui.io.PieceDragListener;

@SuppressWarnings("serial")
public class PieceSelectorSquare extends JButton {

	public PieceSelectorSquare(String name, Icon icon,  PieceDragListener pieceDragListener) {
		super.setName(name);
		super.setBorderPainted(false);
		super.setBackground(null);
		super.setOpaque(false);
		super.setContentAreaFilled(false);
		super.setPreferredSize(GuiConstants.ChessBoard.TILE_SIZE);
		super.setIcon(icon);
		super.addMouseListener(pieceDragListener);
	}
}
