package com.grapedrink.chessmap.gui.board;

import javax.swing.JPanel;

import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;


@SuppressWarnings("serial")
public class MasterBoardPanel extends JPanel {
	
	private UserInterfaceFactory inputManager;
	
	public MasterBoardPanel(UserInterfaceFactory inputManager) {
		this.inputManager = inputManager;
		this.initializeComponents();
	}
	
	private void initializeComponents() {
		super.add(inputManager.getExtraPiecesPanel());
		super.add(inputManager.getChessBoardPanel());
	}

}
