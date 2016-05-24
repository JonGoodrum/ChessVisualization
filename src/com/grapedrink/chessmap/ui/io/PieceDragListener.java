package com.grapedrink.chessmap.ui.io;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.grapedrink.chessmap.game.ChessMapLogicEngine;
import com.grapedrink.chessmap.gui.colors.SquareColor;
import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
import com.grapedrink.chessmap.logic.bitboards.PieceColor;
import com.grapedrink.chessmap.logic.bitboards.PieceUtils;
import com.grapedrink.chessmap.ui.factory.GUIReferences;

public class PieceDragListener extends MouseAdapter {
	
	private GUIReferences guirefs;
	private static Icon draggedPiece;
	
	public PieceDragListener(GUIReferences userInterfaceFactory) {
		this.guirefs = userInterfaceFactory;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		dragIcon(e);
    }
	
    @Override
    public void mouseReleased(MouseEvent e) {
    	guirefs.resetColors();
    	
    	if (mouseIsOverChessBoard(e) && draggedPiece != null) {
    		if (pressedButtonEqualsReleasedButton(e)) {
    	    	guirefs.paintSquares();
    		    highlightValidMoves(e);
        		resetDraggedIcon(e);
    		}
    		else if (gameIsInProgress()) {
    			executePlayerTurn(e);
    	    	guirefs.paintSquares();
    		}
    		else {
    	    	guirefs.paintSquares();
    			dropDraggedIcon(e);
    			if (PieceUtils.isPieceCode(getButtonThatWasClicked(e).getName())) {
    				logic().addPiece(getButtonThatWasClicked(e).getName(), getButtonUnderMouse(e).getName());
    			}
    			else {
    				logic().setMove(getButtonThatWasClicked(e).getName(), getButtonUnderMouse(e).getName());
    			}
    		}
    	}
    	else {
        	guirefs.paintSquares();
    		resetDraggedIcon(e);
    	}
    	
    	guirefs.enableNextPrevMoveButtons();
	}
    
    private boolean pressedButtonEqualsReleasedButton(MouseEvent e) {
    	return getButtonThatWasClicked(e).equals(getButtonUnderMouse(e));
    }
    
    private void highlightValidMoves(MouseEvent e) {
    	Iterable<String> validMoves = logic().getValidMoves(getButtonThatWasClicked(e).getName());
    	guirefs.getChessBoardPanel().highlightBorders(validMoves);
	}

	private void putIcon(JButton button) {
    	button.setIcon(draggedPiece);
    	draggedPiece = null;
    }
    
    private void resetDraggedIcon(MouseEvent e) {
    	putIcon(getButtonThatWasClicked(e));
    }
    
    private void dropDraggedIcon(MouseEvent e) {
    	putIcon(getButtonUnderMouse(e));
    }
    
    private boolean gameIsInProgress() {
    	return !guirefs.getFreePlayCheckBox().isSelected();
    }
    
    private void executePlayerTurn(MouseEvent e) {
    	String source = getButtonThatWasClicked(e).getName();
    	String destination = getButtonUnderMouse(e).getName();
    	if (logic().isValidMove(source, destination)) {
    		logic().setMove(source, destination);
    		dropDraggedIcon(e);
    		guirefs.getChessBoardPanel().setBoard(logic().getBoard());
    	}
    	else {
    		resetDraggedIcon(e);
    	}
    	if (logic().getWinner() != null) {
    		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Mate");
    		JOptionPane.showMessageDialog(null, String.format("Checkmate. %s wins", logic().getWinner()));
    	}
    }
    
    private ChessMapLogicEngine logic() {
    	return guirefs.getChessMapLogicEngine();
    }
    
    private void dragIcon(MouseEvent e) {
		JButton srcButton = getButtonUnderMouse(e);
		if (srcButton != null) {
			draggedPiece = srcButton.getIcon();
			if (mouseIsOverChessBoard(e)) {
				srcButton.setIcon(null);
			}
		}
    }
    
    private boolean mouseIsOverChessBoard(MouseEvent e) {
    	Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), (Component) guirefs.getChessBoardPanel());
    	JButton buttonUnderMouse = getButtonUnderMouse(e);
    	if (buttonUnderMouse == null) {
    		return false;
    	}
    	Rectangle bounds = guirefs.getChessBoardPanel().getBounds();
    	return (p.x > 0 && p.y < bounds.getMaxX()) && (p.y > 0 && p.y < bounds.getMaxY());
    }
    
    private JButton getButtonThatWasClicked(MouseEvent e) {
    	return (JButton) e.getComponent();
    }

    /* returns null if not a jbutton */
    private JButton getButtonUnderMouse(MouseEvent e) {
    	Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), (Component) guirefs.getChessBoardPanel());    	
    	Component component;
    	if (p.x > 0) {
        	component = guirefs.getChessBoardPanel().getComponentAt(p);
    	}
    	else {
    		p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), (Component) guirefs.getExtraPiecesPanel());
    		component = guirefs.getExtraPiecesPanel().getActivePanel().getComponentAt(p);
    	}
    	return component instanceof JButton ? (JButton) component : null;
    }
}


/*
package com.grapedrink.chessmap.ui.io;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.grapedrink.chessmap.game.ChessMapLogicEngine;
import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
import com.grapedrink.chessmap.logic.bitboards.PieceUtils;
import com.grapedrink.chessmap.ui.factory.GUIReferences;

public class PieceDragListener extends MouseAdapter {
	
	private GUIReferences guirefs;
	private static Icon draggedPiece;
	
	public PieceDragListener(GUIReferences userInterfaceFactory) {
		this.guirefs = userInterfaceFactory;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		dragIcon(e);
    }
	
    @Override
    public void mouseReleased(MouseEvent e) {
    	guirefs.resetColors();
    	if (mouseIsOverChessBoard(e) && draggedPiece != null) {
    		if (pressedButtonEqualsReleasedButton(e)) {
    		    highlightValidMoves(e);
        		resetDraggedIcon(e);
    		}
    		else if (gameIsInProgress()) {
    			executePlayerTurn(e);
    		}
    		else {
    			dropDraggedIcon(e);
    			if (PieceUtils.isPieceCode(getButtonThatWasClicked(e).getName())) {
    				logic().addPiece(getButtonThatWasClicked(e).getName(), getButtonUnderMouse(e).getName());
    			}
    			else {
    				logic().setMove(getButtonThatWasClicked(e).getName(), getButtonUnderMouse(e).getName());
    			}
    		}
    	}
    	else {
    		resetDraggedIcon(e);
    	}
    	guirefs.paintSquares();
    	guirefs.enableNextPrevMoveButtons();
	}
    
    private boolean pressedButtonEqualsReleasedButton(MouseEvent e) {
    	return getButtonThatWasClicked(e).equals(getButtonUnderMouse(e));
    }
    
    private void highlightValidMoves(MouseEvent e) {
    	Iterable<String> validMoves = logic().getValidMoves(getButtonThatWasClicked(e).getName());
    	guirefs.getChessBoardPanel().highlight(validMoves, GuiConstants.Colors.YELLOW);
	}

	private void putIcon(JButton button) {
    	button.setIcon(draggedPiece);
    	draggedPiece = null;
    }
    
    private void resetDraggedIcon(MouseEvent e) {
    	putIcon(getButtonThatWasClicked(e));
    }
    
    private void dropDraggedIcon(MouseEvent e) {
    	putIcon(getButtonUnderMouse(e));
    }
    
    private boolean gameIsInProgress() {
    	return !guirefs.getFreePlayCheckBox().isSelected();
    }
    
    private void executePlayerTurn(MouseEvent e) {
    	String source = getButtonThatWasClicked(e).getName();
    	String destination = getButtonUnderMouse(e).getName();
    	if (logic().isValidMove(source, destination)) {
    		logic().setMove(source, destination);
    		dropDraggedIcon(e);
    		guirefs.getChessBoardPanel().setBoard(logic().getBoard());
    	}
    	else {
    		resetDraggedIcon(e);
    	}
    	if (logic().getWinner() != null) {
    		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Mate");
    		JOptionPane.showMessageDialog(null, String.format("Checkmate. %s wins", logic().getWinner()));
    	}
    }
    
    private ChessMapLogicEngine logic() {
    	return guirefs.getChessMapLogicEngine();
    }
    
    private void dragIcon(MouseEvent e) {
		JButton srcButton = getButtonUnderMouse(e);
		if (srcButton != null) {
			draggedPiece = srcButton.getIcon();
			if (mouseIsOverChessBoard(e)) {
				srcButton.setIcon(null);
			}
		}
    }
    
    private boolean mouseIsOverChessBoard(MouseEvent e) {
    	Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), (Component) guirefs.getChessBoardPanel());
    	JButton buttonUnderMouse = getButtonUnderMouse(e);
    	if (buttonUnderMouse == null) {
    		return false;
    	}
    	Rectangle bounds = guirefs.getChessBoardPanel().getBounds();
    	return (p.x > 0 && p.y < bounds.getMaxX()) && (p.y > 0 && p.y < bounds.getMaxY());
    }
    
    private JButton getButtonThatWasClicked(MouseEvent e) {
    	return (JButton) e.getComponent();
    }

    /// returns null if not a jbutton
    private JButton getButtonUnderMouse(MouseEvent e) {
    	Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), (Component) guirefs.getChessBoardPanel());    	
    	Component component;
    	if (p.x > 0) {
        	component = guirefs.getChessBoardPanel().getComponentAt(p);
    	}
    	else {
    		p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), (Component) guirefs.getExtraPiecesPanel());
    		component = guirefs.getExtraPiecesPanel().getActivePanel().getComponentAt(p);
    	}
    	return component instanceof JButton ? (JButton) component : null;
    }
}

 */