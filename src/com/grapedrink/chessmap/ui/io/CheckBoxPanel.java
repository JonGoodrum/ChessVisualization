package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.grapedrink.chessmap.gui.colors.SquareColor;
import com.grapedrink.chessmap.logic.bitboards.PieceColor;
import com.grapedrink.chessmap.ui.factory.GUIReferences;

@SuppressWarnings("serial")
public class CheckBoxPanel extends JPanel {

	private GUIReferences guirefs;
	private JCheckBox whiteDefense;
	private JCheckBox blackDefense;
	
	public CheckBoxPanel(GUIReferences guirefs) {
		this.guirefs = guirefs;
		this.whiteDefense = new JCheckBox("w");
		whiteDefense.addActionListener(new BoxCheckedActionListener(guirefs));
		this.blackDefense = new JCheckBox("b");
		blackDefense.addActionListener(new BoxCheckedActionListener(guirefs));
		super.add(whiteDefense);
		super.add(blackDefense);
	}
	
	public void paintSquares() {
		highlightValidMoves();
	}
	
    private void highlightValidMoves() {
        if (whiteDefense.isSelected() || blackDefense.isSelected()) {
            if (whiteDefense.isSelected() && blackDefense.isSelected()) {
        	    showTotalDefense();
            }
            else if (whiteDefense.isSelected()) {
            	showTotalDefense(PieceColor.WHITE);
            }
            else {
            	showTotalDefense(PieceColor.BLACK);
            }
    	}
	}

    public void showTotalDefense() {
    	Set<String> whitePositions = (Set<String>) guirefs.getChessMapLogicEngine().getTotalDefense(PieceColor.WHITE);
    	Set<String> blackPositions = (Set<String>) guirefs.getChessMapLogicEngine().getTotalDefense(PieceColor.BLACK);
    	Set<String> bothPositions = new HashSet<>();
    	for (String position : whitePositions) {
    		if (blackPositions.contains(position)) {
    			bothPositions.add(position);
    		}
    	}
    	
    	for (String position : bothPositions) {
    		blackPositions.remove(position);
    		whitePositions.remove(position);
    	}
    	
    	guirefs.getChessBoardPanel().highlight(whitePositions, SquareColor.DEFENDED_SQUARES, PieceColor.WHITE);
    	guirefs.getChessBoardPanel().highlight(blackPositions, SquareColor.DEFENDED_SQUARES, PieceColor.BLACK);
    	guirefs.getChessBoardPanel().highlight(bothPositions, SquareColor.DEFENDED_SQUARES, PieceColor.BOTH);
    }

    public void showTotalDefense(PieceColor pieceColor) {
    	Iterable<String> totalDefense = guirefs.getChessMapLogicEngine().getTotalDefense(pieceColor);
    	guirefs.getChessBoardPanel().highlight(totalDefense, SquareColor.DEFENDED_SQUARES, pieceColor);
    }
	
	public void uncheckAll() {
		
	}
	
	private class BoxCheckedActionListener implements ActionListener {

		private GUIReferences guirefs;
		
		protected BoxCheckedActionListener(GUIReferences guirefs) {
			this.guirefs = guirefs;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			guirefs.getChessBoardPanel().resetColors();
			guirefs.getCheckBoxPanel().paintSquares();
			guirefs.enableNextPrevMoveButtons();
		}
	}
	
	
}
