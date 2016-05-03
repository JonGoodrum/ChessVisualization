package com.grapedrink.chessmap.ui.io;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.grapedrink.chessmap.game.ChessMapLogicEngine;
import com.grapedrink.chessmap.gui.controlpanel.GuiConstants;
import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;

public class PieceDragListener extends MouseAdapter {
	
	private UserInterfaceFactory userInterfaceFactory;
	private static Icon draggedPiece;
	
	public PieceDragListener(UserInterfaceFactory userInterfaceFactory) {
		this.userInterfaceFactory = userInterfaceFactory;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		dragIcon(e);
    }
	
    @Override
    public void mouseReleased(MouseEvent e) {
    	resetColors();
    	if (mouseIsOverChessBoard(e) && draggedPiece != null) {
    		if (getButtonThatWasClicked(e).equals(getButtonUnderMouse(e))) {
    		    highlightValidMoves(e);
        		resetDraggedIcon(e);
    		}
    		else if (gameIsInProgress()) {
    			executePlayerTurn(e);
    		}
    		else {
    			dropDraggedIcon(e);
    		}
    	}
    	else {
    		resetDraggedIcon(e);
    	}
	}
    
    private void highlightValidMoves(MouseEvent e) {
    	Iterable<String> validMoves = logic().getValidMoves(getButtonThatWasClicked(e).getName());
    	userInterfaceFactory.getChessBoardPanel().highlight(validMoves, GuiConstants.Colors.YELLOW);
	}

	private void putIcon(JButton button) {
    	button.setIcon(draggedPiece);
    	draggedPiece = null;
    }
    
    private void resetDraggedIcon(MouseEvent e) {
    	putIcon(getButtonThatWasClicked(e));
    }
    
    private void resetColors() {
    	userInterfaceFactory.getChessBoardPanel().resetColor();
    }
    
    private void dropDraggedIcon(MouseEvent e) {
    	putIcon(getButtonUnderMouse(e));
    }
    
    private boolean gameIsInProgress() {
    	return !userInterfaceFactory.getFreePlayCheckBox().isSelected();
    }
    
    private void executePlayerTurn(MouseEvent e) {
    	String source = getButtonThatWasClicked(e).getName();
    	String destination = getButtonUnderMouse(e).getName();
    	if (logic().isValidMove(source, destination)) {
    		logic().setMove(source, destination);
    		dropDraggedIcon(e);
    		userInterfaceFactory.getChessBoardPanel().setBoard(logic().getBoard());
    	}
    	else {
    		resetDraggedIcon(e);
    	}
    }
    
    private ChessMapLogicEngine logic() {
    	return userInterfaceFactory.getChessMapLogicEngine();
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
    	Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), (Component) userInterfaceFactory.getChessBoardPanel());
    	JButton buttonUnderMouse = getButtonUnderMouse(e);
    	if (buttonUnderMouse == null) {
    		return false;
    	}
    	Rectangle bounds = userInterfaceFactory.getChessBoardPanel().getBounds();
    	return (p.x > 0 && p.y < bounds.getMaxX()) && (p.y > 0 && p.y < bounds.getMaxY());
    }
    
    private JButton getButtonThatWasClicked(MouseEvent e) {
    	return (JButton) e.getComponent();
    }

    /* returns null if not a jbutton */
    private JButton getButtonUnderMouse(MouseEvent e) {
    	Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), (Component) userInterfaceFactory.getChessBoardPanel());    	
    	Component component;
    	if (p.x > 0) {
        	component = userInterfaceFactory.getChessBoardPanel().getComponentAt(p);
    	}
    	else {
    		p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), (Component) userInterfaceFactory.getExtraPiecesPanel());
    		component = userInterfaceFactory.getExtraPiecesPanel().getActivePanel().getComponentAt(p);
    	}
    	return component instanceof JButton ? (JButton) component : null;
    }
}
