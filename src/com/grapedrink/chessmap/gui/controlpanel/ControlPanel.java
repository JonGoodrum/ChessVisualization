package com.grapedrink.chessmap.gui.controlpanel;

import java.awt.GridLayout;

import javax.swing.JPanel;

import com.grapedrink.chessmap.ui.factory.GUIReferences;
import com.grapedrink.chessmap.ui.io.NewGameButton;
import com.grapedrink.chessmap.ui.io.NextMoveButton;
import com.grapedrink.chessmap.ui.io.PrevMoveButton;
import com.grapedrink.chessmap.ui.io.RotateBoardButton;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {

	private GUIReferences userInterfaceFactory;
	
	public ControlPanel(GUIReferences userInterfaceFactory) {
		this.userInterfaceFactory = userInterfaceFactory;
		initializeLayout();
		initializeButtons();
		super.setVisible(true);
	}

	private void initializeLayout() {
		GridLayout grid = new GridLayout(6,2);
		super.setLayout(grid);
	}
	
	private void initializeButtons() {
		initializeNewGameButton();
		initializeResetBoardButton();
		initializeRotateBoardButton();
		initializeFreePlayCheckBox();
		initializePrevMoveButton();
		initializeNextMoveButton();
		initializeCheckBoxPanel();
	}
	
	private void initializeFreePlayCheckBox() {
		super.add(userInterfaceFactory.getFreePlayCheckBox());
	}
	
	private void initializeNewGameButton() {
		super.add(userInterfaceFactory.getNewGameButton());
	}
	
	private void initializeRotateBoardButton() {
		super.add(userInterfaceFactory.getRotateBoardButton());
	}
	
	private void initializeResetBoardButton() {
		super.add(userInterfaceFactory.getResetBoardButton());
	}
	
	private void initializePrevMoveButton() {
		super.add(userInterfaceFactory.getPrevMoveButton());
	}
	
	private void initializeNextMoveButton() {
		super.add(userInterfaceFactory.getNextMoveButton());
	}
	
	private void initializeCheckBoxPanel() {
		super.add(userInterfaceFactory.getCheckBoxPanel());
	}
	
	
}
