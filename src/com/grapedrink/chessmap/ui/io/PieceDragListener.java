package com.grapedrink.chessmap.ui.io;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;

public class PieceDragListener extends MouseAdapter {
	
	private UserInterfaceFactory userInterfaceFactory;
	private static Icon draggedPiece;
	
	public PieceDragListener(UserInterfaceFactory userInterfaceFactory) {
		this.userInterfaceFactory = userInterfaceFactory;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (gameIsInProgress()) {
			executePlayerTurn();
		}
		else {
			dragIcon(e);
		}
    }
	
    @Override
    public void mouseReleased(MouseEvent e) {
    	if (mouseIsOverChessBoard(e) && draggedPiece != null) {
    		JButton destButton = getButtonUnderMouse(e);
    		destButton.setIcon(draggedPiece);
    	}
    	else {
    		JButton srcButton = getSquareThatClicked(e);
    		srcButton.setIcon(draggedPiece);
    	}
    	draggedPiece = null;
	}
    
    private boolean gameIsInProgress() {
    	return !userInterfaceFactory.getFreePlayCheckBox().isSelected();
    }
    
    private void executePlayerTurn() {
    	// execute someone's turn
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
    
    private JButton getSquareThatClicked(MouseEvent e) {
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
