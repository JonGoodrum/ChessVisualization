package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;

@SuppressWarnings("serial")
public class PreviousMoveButton extends JButton {

	public PreviousMoveButton(UserInterfaceFactory userInterfaceFactory) {
		super("Previous Move");
		this.addActionListener(new PreviousMoveActionListener(userInterfaceFactory));
	}
	
	private class PreviousMoveActionListener implements ActionListener {

		private UserInterfaceFactory userInterfaceFactory;
		
		protected PreviousMoveActionListener(UserInterfaceFactory userInterfaceFactory) {
			this.userInterfaceFactory = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			userInterfaceFactory.getChessMapLogicEngine().getPrevMove();
			userInterfaceFactory.getChessBoardPanel().setBoard(userInterfaceFactory.getChessMapLogicEngine().getBoard());
		}
	}
}
