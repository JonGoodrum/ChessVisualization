package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;

@SuppressWarnings("serial")
public class RotateBoardButton extends JButton {
	
	public RotateBoardButton(UserInterfaceFactory userInterfaceFactory) {
		super("Rotate Board");
		this.addActionListener(new RotateBoardActionListener(userInterfaceFactory));
	}
	
	private class RotateBoardActionListener implements ActionListener {

		private UserInterfaceFactory userInterfaceFactory;
		
		protected RotateBoardActionListener(UserInterfaceFactory userInterfaceFactory) {
			this.userInterfaceFactory = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
	    	  userInterfaceFactory.getChessBoardPanel().rotateBoard();
		}
	}
}
