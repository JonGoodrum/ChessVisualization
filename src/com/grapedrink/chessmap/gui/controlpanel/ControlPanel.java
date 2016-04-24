package com.grapedrink.chessmap.gui.controlpanel;

import java.awt.GridLayout;

import javax.swing.JPanel;

import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;
import com.grapedrink.chessmap.ui.io.NewGameButton;
import com.grapedrink.chessmap.ui.io.NextMoveButton;
import com.grapedrink.chessmap.ui.io.PreviousMoveButton;
import com.grapedrink.chessmap.ui.io.RotateBoardButton;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {

	private UserInterfaceFactory userInterfaceFactory;
	
	public ControlPanel(UserInterfaceFactory userInterfaceFactory) {
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
		initializeRotateBoardButton();
		initializeFreePlayCheckBox();
		initializePreviousMoveButton();
		initializeNextMoveButton();
	}
	
	private void initializeFreePlayCheckBox() {
		super.add(userInterfaceFactory.getFreePlayCheckBox());
	}
	
	private void initializeNewGameButton() {
		super.add(new NewGameButton(userInterfaceFactory));
	}
	
	private void initializeRotateBoardButton() {
		super.add(new RotateBoardButton(userInterfaceFactory));
	}
	
	private void initializePreviousMoveButton() {
		super.add(new PreviousMoveButton(userInterfaceFactory));
	}
	
	private void initializeNextMoveButton() {
		super.add(new NextMoveButton(userInterfaceFactory));
	}
}
