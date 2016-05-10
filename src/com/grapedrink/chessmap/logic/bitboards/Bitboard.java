package com.grapedrink.chessmap.logic.bitboards;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.grapedrink.chessmap.game.ChessMapLogicEngine;
import com.grapedrink.chessmap.logic.history.MoveHistory;
import com.grapedrink.chessmap.logic.history.Turn;

public class Bitboard extends ChessMapLogicEngine {

	PieceContainer pieces;
	MoveHistory history;
	
	public Bitboard() {
		pieces = new PieceContainer();
		history = new MoveHistory();
	}
	
	@Override
	public void setNewGame() {
		pieces.setNewGame();
		history = new MoveHistory();
	}

	@Override
	public Entry<String, String> getNextMove() throws IndexOutOfBoundsException {
		Map.Entry<String, String> next = history.getNext().getMove();
		long src = BitboardUtils.getPositionAsLong(next.getKey());
		long dst = BitboardUtils.getPositionAsLong(next.getValue());
		pieces.setMove(src, dst);
		return next;
	}

	@Override
	public Entry<String, String> getPrevMove() throws IndexOutOfBoundsException {
		Turn prev = history.getPrev();
		pieces.setMove(prev);
		/*
		Map<String, Long> diffs = prev.getDiffs();
		for (String pieceCode : diffs.keySet()) {
			pieces.setPieceCode(pieceCode, diffs.get(pieceCode));
		}
		 */
		return prev.getMove();
	}

	@Override
	public boolean hasNextMove() {
		return history.hasNext();
	}

	@Override
	public boolean hasPrevMove() {
		return history.hasPrev();
	}

	@Override
	public void setActivePlayer(boolean isBlacksTurn) {
		history.setActivePlayer(isBlacksTurn);
	}

	@Override
	public void setMove(String source, String destination) {
		long src = BitboardUtils.getPositionAsLong(source);
		long dst = BitboardUtils.getPositionAsLong(destination);
		Turn turn = pieces.setMove(src, dst);
		history.addMove(turn);
	}

	@Override
	public boolean isValidMove(String source, String destination) {
		long src = BitboardUtils.getPositionAsLong(source);
		long dst = BitboardUtils.getPositionAsLong(destination);
		return pieces.isValidMove(src, dst, history.mostRecent(), history.isBlacksTurn());
	}

	@Override
	public Iterable<String> getValidMoves(String source) {
		InputValidation.validatePosition(source);
		long src = BitboardUtils.getPositionAsLong(source);
		return BitboardUtils.getPositions(pieces.getValidMoves(src, history.mostRecent()));
	}

	@Override
	public Map<String, String> getBoard() {
		Map<String, String> board = new HashMap<>();
		long position;
		String pieceCode;
		for (int i=0; i<64; ++i) {
			position = 1L << i;
			if ((pieceCode = pieces.getPieceCode(position)) != null) {
				board.put(BitboardUtils.getPositionAsString(position), pieceCode);
			}
		}
		return board;
	}

	@Override
	public void loadGame(Map<Integer, Entry<String, String>> game) {
		// TODO Auto-generated method stub
	}

	@Override
	public void resetBoard() {
		pieces = new PieceContainer();
		history = new MoveHistory();
	}

}
