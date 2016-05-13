package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.grapedrink.chessmap.ui.factory.GUIReferences;

@SuppressWarnings("serial")
public class RotateBoardButton extends JButton {
	
	public RotateBoardButton(GUIReferences userInterfaceFactory) {
		super("Rotate Board");
		this.addActionListener(new RotateBoardActionListener(userInterfaceFactory));
	}
	
	private class RotateBoardActionListener implements ActionListener {

		private GUIReferences userInterfaceFactory;
		
		protected RotateBoardActionListener(GUIReferences userInterfaceFactory) {
			this.userInterfaceFactory = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
	    	  userInterfaceFactory.getChessBoardPanel().rotateBoard();
		}
	}
}
