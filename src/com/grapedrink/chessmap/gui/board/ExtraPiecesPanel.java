package com.grapedrink.chessmap.gui.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
import com.grapedrink.chessmap.gui.icons.IconHelper;
import com.grapedrink.chessmap.ui.factory.GUIReferences;
import com.grapedrink.chessmap.ui.io.PieceDragListener;

@SuppressWarnings("serial")
public class ExtraPiecesPanel extends JPanel {

	private GUIReferences guirefs;
	private JPanel pieceSelectorLayer;
	private JPanel playerTurnLayer;
	
	public ExtraPiecesPanel(GUIReferences userInterfaceFactory) {
		this.guirefs = userInterfaceFactory;
		createPieceSelectorLayer();
		createPlayerTurnLayer();
		setActivePanel(true);
	}
	
	public void setActivePanel(boolean pieceSelectorOn) {
		pieceSelectorLayer.setVisible(pieceSelectorOn);
		playerTurnLayer.setVisible(!pieceSelectorOn);
	}
	
	public JPanel getActivePanel() {
		return pieceSelectorLayer.isVisible() ? pieceSelectorLayer : playerTurnLayer;
	}
	
	private void createPieceSelectorLayer() {
		PieceDragListener pieceDragListener = new PieceDragListener(guirefs);
		IconHelper iconHelper = new IconHelper(guirefs);
		String[] pieces = {"bB", "wB", "bK", "wK", "bN", "wN", "bP", "wP", "bQ", "wQ", "bR", "wR"};
		pieceSelectorLayer = getEmptyGrid(2);
		pieceSelectorLayer.add(getGridFiller(1));
		pieceSelectorLayer.add(getGridFiller(1));
		for (String pieceCode : pieces) {
			pieceSelectorLayer.add(new PieceSelectorSquare(pieceCode, iconHelper.get(pieceCode), pieceDragListener));
		}
		pieceSelectorLayer.add(getGridFiller(1));
		pieceSelectorLayer.add(getGridFiller(1));
		super.add(pieceSelectorLayer);
	}
	
	private void createPlayerTurnLayer() {
		playerTurnLayer = getEmptyGrid(1);
		playerTurnLayer.add(getGridFiller(2));
		playerTurnLayer.add(getGridFiller(2));
		playerTurnLayer.add(getPlayerIndicator("bK", 2));
		playerTurnLayer.add(getGridFiller(2));
		playerTurnLayer.add(getGridFiller(2));
		playerTurnLayer.add(getPlayerIndicator("wK", 2));
		playerTurnLayer.add(getGridFiller(2));
		playerTurnLayer.add(getGridFiller(2));
		super.add(playerTurnLayer);
	}
	
	private JPanel getEmptyGrid(int columns) {
		JPanel emptyPanel = new JPanel();
		GridLayout grid = new GridLayout(8, columns);
		emptyPanel.setLayout(grid);
		return emptyPanel;
	}
	
	private JPanel getGridFiller(int width) {
		JPanel panel = new JPanel();
		Dimension preferredSize = new Dimension(GuiConstants.ChessBoard.SIDE_LENGTH*width, GuiConstants.ChessBoard.SIDE_LENGTH);
		panel.setPreferredSize(preferredSize);
		panel.setName(null);
		return panel;
	}
	
	private JPanel getPlayerIndicator(String pieceCode, int width) {
		JPanel panel = getGridFiller(width);
		JLabel label = new JLabel(guirefs.getIconHelper().get(pieceCode));
		panel.add(label);
		return panel;
	}

	public void flipActivePanel() {
		setActivePanel(pieceSelectorLayer.isVisible() ? false : true);
	}
}
