package com.grapedrink.chessmap.gui.board;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPanel;

import com.grapedrink.chessmap.gui.colors.SquareColor;
import com.grapedrink.chessmap.gui.icons.IconHelper;
import com.grapedrink.chessmap.logic.bitboards.PieceColor;
import com.grapedrink.chessmap.ui.factory.GUIReferences;
import com.grapedrink.chessmap.ui.io.PieceDragListener;

@SuppressWarnings("serial")
public class ChessBoardPanel extends JPanel {

	private boolean rank1AtBottom;
	private Map<String, ChessBoardSquare> board;
	private GUIReferences userInterfaceFactory;
	
	public ChessBoardPanel(GUIReferences userInterfaceFactory) {
		this.userInterfaceFactory = userInterfaceFactory;
		initializeLayout();
		initializeBoard();
	    setVisible(true);
	}
	
	private void initializeLayout() {
		GridLayout grid = new GridLayout(8, 8);
		super.setLayout(grid);
	}
	
	private void initializeBoard() {
		ChessBoardSquare square;
		String position;
		rank1AtBottom = true;
		board = new HashMap<>();
		PieceDragListener pieceDragListener = new PieceDragListener(userInterfaceFactory);
		
		for (int rank = 8; rank > 0; --rank) {
			for (char file = 'a'; file <= 'h'; ++file) {
				position = String.format("%s%d", file, rank);
				square = new ChessBoardSquare(rank, file, pieceDragListener);
				board.put(position, square);
				super.add(square);
			}
		}
	}
	
	public void setPiece(String position, String pieceCode) {
		board.get(position).setIcon(getIcons().get(pieceCode));
		super.revalidate();
	}
	
	private IconHelper getIcons() {
		return userInterfaceFactory.getIconHelper();
	}

	public void movePiece(String srcSquare, String destSquare) {
		Icon srcIcon = board.get(srcSquare).getIcon();
		board.get(destSquare).setIcon(srcIcon);
		board.get(srcSquare).setIcon(null);
		super.revalidate();
	}
	
	/**
	 * Loads a board into the game.
	 * 
	 * Example: a Map<String, String> containing:
	 * <"e2", "wK">
	 * <"c3", "bK">
	 * <"d2", "bP">
	 * 
	 * ...would load a black pawn onto d2,
	 * a black king onto c3, and a white king
	 * onto e2
	 * 
	 * @param squaresAndPieces map
	 */
	public void setBoard(Map<String, String> squaresAndPieces) {
		this.resetBoard();
		for (String square : squaresAndPieces.keySet()) {
			setPiece(square, squaresAndPieces.get(square));
		}
	}
	
	public void resetBoard() {
		for (String square : board.keySet()) {
			setPiece(square, null);
		}
	}
	
	/**
	 * Loads an opening into the game.  Program
	 * searches Wikipedia for this particular opening,
	 * and loads the search result (if any) onto the board.
	 * 
	 * @param positionName ex: Ruy Lopez
	 */
	public void loadBoard(String positionName) {
	}
	
	public void rotateBoard() {
		String position;
		if (rank1AtBottom) {
			for (int rank = 1; rank <= 8; ++rank) {
				for (char file='h'; file >='a'; --file) {
					position = String.format("%s%d", file, rank);
					super.add(board.get(position));
				}
			}
		}
		else {
			for (int rank = 8; rank > 0; --rank) {
				for (char file = 'a'; file <= 'h'; ++file) {
					position = String.format("%s%d", file, rank);
					super.add(board.get(position));
				}
			}
		}
		rank1AtBottom = !rank1AtBottom;
		super.revalidate();
	}

	public void resetColors() {
		for (ChessBoardSquare square : board.values()) {
			square.resetColor();
			square.resetBorder();
		}
	}
	
	public void highlight(Iterable<String> positions, SquareColor squareColor, PieceColor pieceColor) {
		for (String position : positions) {
			board.get(position).setColor(squareColor, pieceColor);
		}
	}

	public void highlightBorders(Iterable<String> positions) {
		for (String position : positions) {
			board.get(position).setBorderPainted(true);
		}
	}
}
