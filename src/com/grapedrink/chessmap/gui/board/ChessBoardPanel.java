package com.grapedrink.chessmap.gui.board;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.grapedrink.chessmap.gui.icons.IconHelper;
import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;
import com.grapedrink.chessmap.ui.io.PieceDragListener;

@SuppressWarnings("serial")
public class ChessBoardPanel extends JPanel {

	private IconHelper icons;
	private boolean rank1AtBottom;
	private Map<String, JButton> board;
	private UserInterfaceFactory userInterfaceFactory;
	
	public ChessBoardPanel(UserInterfaceFactory userInterfaceFactory) {
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
		JButton square;
		String position;
		rank1AtBottom = true;
		board = new HashMap<>();
		icons = new IconHelper(userInterfaceFactory);
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
		board.get(position).setIcon(icons.get(pieceCode));
		super.revalidate();
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
}
