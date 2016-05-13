package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
import com.grapedrink.chessmap.logic.bitboards.PieceColor;
import com.grapedrink.chessmap.ui.factory.GUIReferences;

@SuppressWarnings("serial")
public class TotalDefenseButton extends JButton {

	public TotalDefenseButton(GUIReferences userInterfaceFactory) {
		super("Show all defense");
		this.addActionListener(new TotalDefenseActionListener(userInterfaceFactory));
	}
	
	private class TotalDefenseActionListener implements ActionListener {

		private GUIReferences userInterfaceFactory;

		protected TotalDefenseActionListener(GUIReferences userInterfaceFactory) {
			this.userInterfaceFactory = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			userInterfaceFactory.getChessBoardPanel().resetColor();
			highlightValidMoves();
			userInterfaceFactory.enableNextPrevMoveButtons();
		}
		
	    private void highlightValidMoves() {
	    	PieceColor activePlayer = userInterfaceFactory.getChessMapLogicEngine().getActivePlayer();
	    	Iterable<String> totalDefense = userInterfaceFactory.getChessMapLogicEngine().getTotalDefense(activePlayer);
	    	userInterfaceFactory.getChessBoardPanel().highlight(totalDefense, GuiConstants.Colors.YELLOW);
		}
	}
}
