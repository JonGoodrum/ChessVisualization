package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.grapedrink.chessmap.ui.factory.GUIReferences;

@SuppressWarnings("serial")
public class ResetBoardButton extends JButton {

	public ResetBoardButton(GUIReferences userInterfaceFactory) {
		super("Clear Board");
		this.addActionListener(new ResetBoardActionListener(userInterfaceFactory));
	}
	
	private class ResetBoardActionListener implements ActionListener {

		private GUIReferences userInterfaceFactory;
		
		protected ResetBoardActionListener(GUIReferences userInterfaceFactory) {
			this.userInterfaceFactory = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			userInterfaceFactory.getChessBoardPanel().resetColor();
			userInterfaceFactory.getChessMapLogicEngine().resetBoard();
			userInterfaceFactory.getChessBoardPanel().resetBoard();
			userInterfaceFactory.enableNextPrevMoveButtons();
		}
	}
}
