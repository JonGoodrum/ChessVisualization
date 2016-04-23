package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;

import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;

@SuppressWarnings("serial")
public class NextMoveButton extends JButton {

	public NextMoveButton(UserInterfaceFactory userInterfaceFactory) {
		super("Next Move");
		this.addActionListener(new NextMoveActionListener(userInterfaceFactory));
	}
	
	private class NextMoveActionListener implements ActionListener {

		private UserInterfaceFactory userInterfaceFactory;
		
		protected NextMoveActionListener(UserInterfaceFactory userInterfaceFactory) {
			this.userInterfaceFactory = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Map.Entry<String, String> nextMove = userInterfaceFactory.getChessMapLogicEngine().getNextMove();
			userInterfaceFactory.getChessBoardPanel().movePiece(nextMove.getKey(), nextMove.getValue());
		}
	}
}
