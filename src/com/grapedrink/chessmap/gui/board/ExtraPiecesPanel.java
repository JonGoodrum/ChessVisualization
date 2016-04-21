package com.grapedrink.chessmap.gui.board;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.grapedrink.chessmap.gui.GuiConstants;
import com.grapedrink.chessmap.gui.icons.IconHelper;
import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;
import com.grapedrink.chessmap.ui.io.PieceDragListener;

@SuppressWarnings("serial")
public class ExtraPiecesPanel extends JPanel {

	private UserInterfaceFactory userInterfaceFactory;
	private JPanel pieceSelectorLayer;
	private JPanel playerTurnLayer;
	
	public ExtraPiecesPanel(UserInterfaceFactory userInterfaceFactory) {
		this.userInterfaceFactory = userInterfaceFactory;
		createPieceSelectorLayer();
		createPlayerTurnLayer();
		showPieceSelector(true);
	}
	
	public void showPieceSelector(boolean toggle) {
		pieceSelectorLayer.setVisible(toggle);
		playerTurnLayer.setVisible(!toggle);
	}
	
	public JPanel getActivePanel() {
		return pieceSelectorLayer.isVisible() ? pieceSelectorLayer : playerTurnLayer;
	}
	
	private void createPieceSelectorLayer() {
		PieceDragListener pieceDragListener = new PieceDragListener(userInterfaceFactory);
		IconHelper iconHelper = new IconHelper(userInterfaceFactory);
		String[] pieces = {"bB", "wB", "bK", "wK", "bN", "wN", "bP", "wP", "bQ", "wQ", "bR", "wR"};
		pieceSelectorLayer = getEmptyGrid(2);
		pieceSelectorLayer.add(getGridFiller());
		pieceSelectorLayer.add(getGridFiller());
		for (String pieceCode : pieces) {
			pieceSelectorLayer.add(new PieceSelectorSquare(pieceCode, iconHelper.get(pieceCode), pieceDragListener));
		}
		pieceSelectorLayer.add(getGridFiller());
		pieceSelectorLayer.add(getGridFiller());
		super.add(pieceSelectorLayer);
	}
	
	private void createPlayerTurnLayer() {
		playerTurnLayer = getEmptyGrid(1);
		super.add(playerTurnLayer);
	}
	
	private JPanel getEmptyGrid(int columns) {
		JPanel emptyPanel = new JPanel();
		GridLayout grid = new GridLayout(8, columns);
		emptyPanel.setLayout(grid);
		return emptyPanel;
	}
	
	private JPanel getGridFiller() {
		JPanel panel = new JPanel();
		panel.setSize(GuiConstants.ChessBoard.SIDE_LENGTH, GuiConstants.ChessBoard.SIDE_LENGTH);
		panel.setName(null);
		return panel;
	}
}
