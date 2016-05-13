package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;

import com.grapedrink.chessmap.ui.factory.GUIReferences;

@SuppressWarnings("serial")
public class PrevMoveButton extends JButton {

	public PrevMoveButton(GUIReferences userInterfaceFactory) {
		super("Previous Move");
		super.setEnabled(false);
		this.addActionListener(new PreviousMoveActionListener(userInterfaceFactory));
	}
	
	private class PreviousMoveActionListener implements ActionListener {

		private GUIReferences guirefs;
		
		protected PreviousMoveActionListener(GUIReferences userInterfaceFactory) {
			this.guirefs = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			guirefs.getChessBoardPanel().resetColor();
			guirefs.getChessMapLogicEngine().getPrevMove();
			guirefs.getChessBoardPanel().setBoard(guirefs.getChessMapLogicEngine().getBoard());
			guirefs.enableNextPrevMoveButtons();
	    	guirefs.paintSquares();
		}
	}
}
