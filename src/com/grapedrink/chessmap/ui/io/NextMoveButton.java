package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;

import com.grapedrink.chessmap.logic.utils.PieceUtils;
import com.grapedrink.chessmap.ui.factory.GUIReferences;

@SuppressWarnings("serial")
public class NextMoveButton extends JButton {

	public NextMoveButton(GUIReferences userInterfaceFactory) {
		super("Next Move");
		super.setEnabled(false);
		this.addActionListener(new NextMoveActionListener(userInterfaceFactory));
	}
	
	private class NextMoveActionListener implements ActionListener {

		private GUIReferences guirefs;
		
		protected NextMoveActionListener(GUIReferences userInterfaceFactory) {
			this.guirefs = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			guirefs.getChessBoardPanel().resetColors();
			Map.Entry<String, String> nextMove = guirefs.getChessMapLogicEngine().getNextMove();
			if (PieceUtils.isPieceCode(nextMove.getKey())) {
				guirefs.getChessBoardPanel().setPiece(nextMove.getKey(), nextMove.getValue());
			}
			else {
				guirefs.getChessBoardPanel().movePiece(nextMove.getKey(), nextMove.getValue());
			}
	    	guirefs.paintSquares();
			guirefs.enableNextPrevMoveButtons();
		}
	}
}
