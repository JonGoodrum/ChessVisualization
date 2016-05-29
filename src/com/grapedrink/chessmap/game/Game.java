package com.grapedrink.chessmap.game;

import com.grapedrink.chessmap.logic.bitboards.RulesEngine;
import com.grapedrink.chessmap.ui.factory.GUIReferences;


public class Game {
	
	public static void main ( String[] args )
	  {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @SuppressWarnings("unused")
			public void run() {
            	ChessMapLogicEngine logic = new RulesEngine();
            	GUIReferences inputListener = new GUIReferences(logic);
            }
        });
	  }
}
