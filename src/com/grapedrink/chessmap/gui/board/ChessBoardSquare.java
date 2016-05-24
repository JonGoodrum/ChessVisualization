package com.grapedrink.chessmap.gui.board;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.grapedrink.chessmap.gui.colors.SquareColor;
import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
import com.grapedrink.chessmap.logic.bitboards.PieceColor;
import com.grapedrink.chessmap.ui.io.PieceDragListener;

@SuppressWarnings("serial")
public class ChessBoardSquare extends JButton {

	private Color defaultColor;
	
	public ChessBoardSquare(int rank, char file, PieceDragListener pieceDragListener) {
		String position = String.format("%s%d", file, rank);
		defaultColor = SquareColor.get(SquareColor.DEFAULT_SQUARE_COLOR, position, null);
		Color borderColor = SquareColor.get(SquareColor.AVAILABLE_MOVES_FOR_PIECE, position, null);
		super.setName(position);
		super.setBorderPainted(false);
		super.setBackground(defaultColor);
		super.setBorder(new LineBorder(borderColor, 10));
		super.setPreferredSize(GuiConstants.ChessBoard.TILE_SIZE);
		super.addMouseListener(pieceDragListener);
	}

	public void activateBorder() {
		super.setBorderPainted(true);
	}
	
	public void setColor(SquareColor color, PieceColor pieceColor) {
		super.setBackground(SquareColor.get(color, super.getName(), pieceColor));
	}
	
	public void resetColor() {
		super.setBackground(defaultColor);
	}
	
	public void resetBorder() {
		super.setBorderPainted(false);
	}
}
