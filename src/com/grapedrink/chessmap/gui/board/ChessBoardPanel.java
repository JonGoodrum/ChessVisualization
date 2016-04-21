package com.grapedrink.chessmap.gui.board;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
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
	
	public void setPiece(String pieceCode, String position) {
		board.get(position).setIcon(icons.get(pieceCode));
		super.revalidate();
	}
	
	public void movePiece(String srcSquare, String destSquare) {
		Icon srcIcon = board.get(srcSquare).getIcon();
		board.get(destSquare).setIcon(srcIcon);
		board.get(srcSquare).setIcon(null);
		super.revalidate();
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
