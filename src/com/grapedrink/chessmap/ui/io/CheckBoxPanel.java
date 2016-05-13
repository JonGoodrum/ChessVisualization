package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
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
		guirefs.getChessBoardPanel().resetColor();
		highlightValidMoves();
		guirefs.enableNextPrevMoveButtons();
	}
	
    private void highlightValidMoves() {
        if (whiteDefense.isSelected()) {
    		showTotalDefense(PieceColor.WHITE);
    	}
        if (blackDefense.isSelected()) {
        	showTotalDefense(PieceColor.BLACK);
    	}
	}
    
    public void showTotalDefense(PieceColor color) {
    	Iterable<String> totalDefense = guirefs.getChessMapLogicEngine().getTotalDefense(color);
    	guirefs.getChessBoardPanel().highlight(totalDefense, GuiConstants.Colors.YELLOW);
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
			guirefs.getCheckBoxPanel().paintSquares();
		}
	}
	
	
}
