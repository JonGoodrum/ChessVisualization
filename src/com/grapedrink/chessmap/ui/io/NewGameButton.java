package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;

@SuppressWarnings("serial")
public class NewGameButton extends JButton {

	public NewGameButton(UserInterfaceFactory userInterfaceFactory) {
		super("New Game");
		this.addActionListener(new NewGameActionListener(userInterfaceFactory));
	}
	
	private class NewGameActionListener implements ActionListener {

		private UserInterfaceFactory userInterfaceFactory;
		
		protected NewGameActionListener(UserInterfaceFactory userInterfaceFactory) {
			this.userInterfaceFactory = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			userInterfaceFactory.getChessMapLogicEngine().setNewGame();
            userInterfaceFactory.getChessBoardPanel().setBoard(userInterfaceFactory.getChessMapLogicEngine().getBoard());
		}
	}
}
