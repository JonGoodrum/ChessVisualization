package com.grapedrink.chessmap.ui.factory;

import java.awt.Component;

import com.grapedrink.chessmap.game.ChessMapLogicEngine;
import com.grapedrink.chessmap.gui.board.ChessBoardPanel;
import com.grapedrink.chessmap.gui.board.ExtraPiecesPanel;
import com.grapedrink.chessmap.gui.board.MasterBoardPanel;
import com.grapedrink.chessmap.gui.controlpanel.ControlPanel;
import com.grapedrink.chessmap.gui.icons.IconHelper;
import com.grapedrink.chessmap.gui.mainwindow.MainWindow;
import com.grapedrink.chessmap.logic.bitboards.PieceColor;
import com.grapedrink.chessmap.ui.io.CheckBoxPanel;
import com.grapedrink.chessmap.ui.io.FreePlayCheckBox;
import com.grapedrink.chessmap.ui.io.NewGameButton;
import com.grapedrink.chessmap.ui.io.NextMoveButton;
import com.grapedrink.chessmap.ui.io.PrevMoveButton;
import com.grapedrink.chessmap.ui.io.ResetBoardButton;
import com.grapedrink.chessmap.ui.io.RotateBoardButton;

public class GUIReferences {

	private CheckBoxPanel checkBoxPanel;
	private FreePlayCheckBox freeplayModeCheckBox;
	private ChessBoardPanel chessBoardPanel;
	private ExtraPiecesPanel extraPiecesPanel;
	private MasterBoardPanel masterBoardPanel;
	private ControlPanel controlPanel;
	private MainWindow mainWindow;
	private ChessMapLogicEngine chessMapLogicEngine;
	private NextMoveButton nextMoveButton;
	private PrevMoveButton prevMoveButton;
	private RotateBoardButton rotateBoardButton;
	private NewGameButton newGameButton;
	private ResetBoardButton resetBoardButton;
	private IconHelper iconHelper;
	
	// Make sure you instantiate items from inside-out,
	// in order of increasing dependencies.
	public GUIReferences(ChessMapLogicEngine chessMapLogicEngine) {
		checkBoxPanel = new CheckBoxPanel(this);
		iconHelper = new IconHelper(this);
		resetBoardButton = new ResetBoardButton(this);
		nextMoveButton = new NextMoveButton(this);
		prevMoveButton = new PrevMoveButton(this);
		rotateBoardButton = new RotateBoardButton(this);
		newGameButton = new NewGameButton(this);
		freeplayModeCheckBox = new FreePlayCheckBox(this);
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
	
	public void enableNextPrevMoveButtons() {
		this.nextMoveButton.setEnabled(chessMapLogicEngine.hasNextMove());
		this.prevMoveButton.setEnabled(chessMapLogicEngine.hasPrevMove());
	}
	
	public void paintSquares() {
		this.checkBoxPanel.paintSquares();
	}

	public NewGameButton getNewGameButton() {
		return this.newGameButton;
	}

	public RotateBoardButton getRotateBoardButton() {
		return this.rotateBoardButton;
	}

	public NextMoveButton getNextMoveButton() {
		return this.nextMoveButton;
	}

	public PrevMoveButton getPrevMoveButton() {
		return this.prevMoveButton;
	}

	public ResetBoardButton getResetBoardButton() {
		return this.resetBoardButton;
	}

	public IconHelper getIconHelper() {
		return this.iconHelper;
	}

	public CheckBoxPanel getCheckBoxPanel() {
		return this.checkBoxPanel;
	}

	public void resetColors() {
		this.chessBoardPanel.resetColors();
	}

	public void highlightActivePlayer() {
		extraPiecesPanel.setActivePlayer(chessMapLogicEngine.getActivePlayer());
	}

}
