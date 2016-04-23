package com.grapedrink.chessmap.gui.board;

import javax.swing.JButton;

import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
import com.grapedrink.chessmap.ui.io.PieceDragListener;

@SuppressWarnings("serial")
public class ChessBoardSquare extends JButton {

	public ChessBoardSquare(int rank, char file, PieceDragListener pieceDragListener) {
		super.setName(String.format("%s%d", file, rank));
		super.setBorderPainted(false);
		super.setBackground((rank+file-97) % 2 == 0 ? GuiConstants.ChessBoard.LIGHT : GuiConstants.ChessBoard.DARK);
		super.setPreferredSize(GuiConstants.ChessBoard.TILE_SIZE);
		super.addMouseListener(pieceDragListener);
	}
}
