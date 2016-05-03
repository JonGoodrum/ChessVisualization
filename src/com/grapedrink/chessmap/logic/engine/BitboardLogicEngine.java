package com.grapedrink.chessmap.logic.engine;

import java.util.Map;
import java.util.Map.Entry;

import com.grapedrink.chessmap.game.ChessMapLogicEngine;
import com.grapedrink.chessmap.logic.bitboards.Bitboard;

public class BitboardLogicEngine extends ChessMapLogicEngine {

	private Bitboard bitboard;
	
	public BitboardLogicEngine() {
		this.bitboard = new Bitboard();
	}
	
	@Override
	public void setNewGame() {
		bitboard.setNewGame();
	}

	@Override
	public Entry<String, String> getNextMove() {
		return bitboard.getNextMove();
	}

	@Override
	public void setActivePlayer(boolean isBlacksTurn) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setMove(String source, String destination) {
		bitboard.setMove(source, destination);
	}

	@Override
	public boolean isValidMove(String source, String destination) {
		return bitboard.isValidMove(source, destination);
	}

	@Override
	public void undoMove() {
		bitboard.undoMove();
	}

	@Override
	public Map<String, String> getBoard() {
		return bitboard.getBoard();
	}

	@Override
	public void loadGame(Map<Integer, Entry<String, String>> game) {
		// TODO Auto-generated method stub
	}

	@Override
	public void resetBoard() {
		bitboard.resetBoard();
	}

	@Override
	public Iterable<String> getValidMoves(String source) {
		return bitboard.getValidMoves(source);
	}

}
