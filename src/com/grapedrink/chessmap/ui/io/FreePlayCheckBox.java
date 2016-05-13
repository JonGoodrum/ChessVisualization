package com.grapedrink.chessmap.ui.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.grapedrink.chessmap.ui.factory.GUIReferences;

@SuppressWarnings("serial")
public class FreePlayCheckBox extends JCheckBox {

	public FreePlayCheckBox(GUIReferences userInterfaceFactory){
		super("Free Play");
		super.setVisible(true);
		super.setSelected(true);
		super.addActionListener(new FreePlayCheckBoxActionListener(userInterfaceFactory));
	}
	
	private class FreePlayCheckBoxActionListener implements ActionListener {
		
		private GUIReferences userInterfaceFactory;
		
		protected FreePlayCheckBoxActionListener(GUIReferences userInterfaceFactory) {
			this.userInterfaceFactory = userInterfaceFactory;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			userInterfaceFactory.getExtraPiecesPanel().flipActivePanel();
		}
		
	}
	
}
