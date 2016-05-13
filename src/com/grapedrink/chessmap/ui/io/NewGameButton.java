package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.grapedrink.chessmap.ui.factory.GUIReferences;

@SuppressWarnings("serial")
public class NewGameButton extends JButton {

	public NewGameButton(GUIReferences userInterfaceFactory) {
		super("New Game");
		this.addActionListener(new NewGameActionListener(userInterfaceFactory));
	}
	
	private class NewGameActionListener implements ActionListener {

		private GUIReferences guirefs;
		
		protected NewGameActionListener(GUIReferences userInterfaceFactory) {
			this.guirefs = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			guirefs.getChessBoardPanel().resetColor();
			guirefs.getFreePlayCheckBox().setSelected(false);
			guirefs.getChessMapLogicEngine().setNewGame();
            guirefs.getChessBoardPanel().setBoard(guirefs.getChessMapLogicEngine().getBoard());
            guirefs.getExtraPiecesPanel().setActivePanel(false);
            guirefs.getCheckBoxPanel().uncheckAll();
			guirefs.enableNextPrevMoveButtons();
		}
	}
}
