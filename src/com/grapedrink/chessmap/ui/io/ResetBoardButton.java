package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;

@SuppressWarnings("serial")
public class ResetBoardButton extends JButton {

	public ResetBoardButton(UserInterfaceFactory userInterfaceFactory) {
		super("Clear Board");
		this.addActionListener(new ResetBoardActionListener(userInterfaceFactory));
	}
	
	private class ResetBoardActionListener implements ActionListener {

		private UserInterfaceFactory userInterfaceFactory;
		
		protected ResetBoardActionListener(UserInterfaceFactory userInterfaceFactory) {
			this.userInterfaceFactory = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			userInterfaceFactory.getChessMapLogicEngine().resetBoard();
			userInterfaceFactory.getChessBoardPanel().resetBoard();
		}
	}
}
