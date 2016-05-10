package com.grapedrink.chessmap.game;

import com.grapedrink.chessmap.logic.bitboards.Bitboard;
import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;


public class Game {
	
	public static void main ( String[] args )
	  {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @SuppressWarnings("unused")
			public void run() {
            	ChessMapLogicEngine logic = new Bitboard();
            	UserInterfaceFactory inputListener = new UserInterfaceFactory(logic);
            }
        });
	  }
}
