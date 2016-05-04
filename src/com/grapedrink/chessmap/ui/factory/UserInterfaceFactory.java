package com.grapedrink.chessmap.ui.factory;

import com.grapedrink.chessmap.game.ChessMapLogicEngine;
import com.grapedrink.chessmap.gui.board.ChessBoardPanel;
import com.grapedrink.chessmap.gui.board.ExtraPiecesPanel;
import com.grapedrink.chessmap.gui.board.MasterBoardPanel;
import com.grapedrink.chessmap.gui.controlpanel.ControlPanel;
import com.grapedrink.chessmap.gui.mainwindow.MainWindow;
import com.grapedrink.chessmap.ui.io.FreePlayCheckBox;

public class UserInterfaceFactory {

	private FreePlayCheckBox freeplayModeCheckBox;
	private ChessBoardPanel chessBoardPanel;
	private ExtraPiecesPanel extraPiecesPanel;
	private MasterBoardPanel masterBoardPanel;
	private ControlPanel controlPanel;
	private MainWindow mainWindow;
	private ChessMapLogicEngine chessMapLogicEngine;
	
	// Make sure you instantiate items from inside-out,
	// in order of increasing dependencies.
	public UserInterfaceFactory(ChessMapLogicEngine chessMapLogicEngine) {
		freeplayModeCheckBox = new FreePlayCheckBox();
		extraPiecesPanel = new ExtraPiecesPanel(this);
		chessBoardPanel = new ChessBoardPanel(this);
		masterBoardPanel = new MasterBoardPanel(this);
		controlPanel = new ControlPanel(this);
		mainWindow = new MainWindow(this);
		this.chessMapLogicEngine = chessMapLogicEngine;
	}
	
	public ControlPanel getControlPanel() {
		return this.controlPanel;
	}
	
	public FreePlayCheckBox getFreePlayCheckBox() {
		return this.freeplayModeCheckBox;
	}
	
	public ChessBoardPanel getChessBoardPanel() {
		return this.chessBoardPanel;
	}
	
	public ExtraPiecesPanel getExtraPiecesPanel() {
		return this.extraPiecesPanel;
	}
	
	public MasterBoardPanel getMasterBoardPanel() {
		return this.masterBoardPanel;
	}
	
	public ChessMapLogicEngine getChessMapLogicEngine() {
		return this.chessMapLogicEngine;
	}
}