package com.grapedrink.chessmap.gui;

import com.grapedrink.chessmap.ui.factory.UserInterfaceFactory;


public class Game {
	
	public static void main ( String[] args )
	  {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @SuppressWarnings("unused")
			public void run() {
            	UserInterfaceFactory inputListener = new UserInterfaceFactory();
                MainWindow window = new MainWindow(inputListener);
            }
        });
	  }
}
