package com.grapedrink.chessmap.gui.mainwindow;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.grapedrink.chessmap.gui.controlpanel.ControlPanel;
import com.grapedrink.chessmap.ui.factory.GUIReferences;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	GUIReferences userInterfaceFactory;
	ControlPanel controlPanel;
	
	public MainWindow(GUIReferences userInterfaceFactory) {
		super("ChessMap");
		super.setLayout(new BorderLayout());
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.userInterfaceFactory = userInterfaceFactory;
		initializeContainerElements();
		super.pack();
		super.setLocationRelativeTo(null);
		super.setVisible(true);
	}
	
	private void initializeContainerElements() {
		super.add(userInterfaceFactory.getMasterBoardPanel(), BorderLayout.CENTER);
		super.add(userInterfaceFactory.getControlPanel(), BorderLayout.LINE_END);
	}
}
