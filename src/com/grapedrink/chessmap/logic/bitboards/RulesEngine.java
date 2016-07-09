package com.grapedrink.chessmap.logic.bitboards;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.grapedrink.chessmap.game.ChessMapLogicEngine;
import com.grapedrink.chessmap.logic.history.MoveHistory;
import com.grapedrink.chessmap.logic.history.Turn;
import com.grapedrink.chessmap.logic.utils.ConvertUtils;
import com.grapedrink.chessmap.logic.utils.DefenseUtils;
import com.grapedrink.chessmap.logic.utils.PieceUtils;

public class RulesEngine extends ChessMapLogicEngine {

	PieceContainer pieceContainer;
	MoveHistory history;
	
	public RulesEngine() {
		pieceContainer = new PieceContainer();
		history = new MoveHistory();
	}
	
	@Override
	public void setNewGame() {
		pieceContainer.setNewGame();
		history = new MoveHistory();
	}

	@Override
	public Entry<String, String> getNextMove() throws IndexOutOfBoundsException {
		Map.Entry<String, String> next = history.getNext().getMove();
		if (PieceUtils.isPieceCode(next.getKey())) {
			pieceContainer.addPieceToBoard(next.getKey(), next.getValue());
		}
		else {
			long src = ConvertUtils.getPositionAsLong(next.getKey());
			long dst = ConvertUtils.getPositionAsLong(next.getValue());
			pieceContainer.setMove(src, dst);
		}
		return next;
	}

	@Override
	public Entry<String, String> getPrevMove() throws IndexOutOfBoundsException {
		Turn prev = history.getPrev();
		pieceContainer.setMove(prev);
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
		long src = ConvertUtils.getPositionAsLong(source);
		long dst = ConvertUtils.getPositionAsLong(destination);
		Turn turn = pieceContainer.setMove(src, dst);
		history.addMove(turn);
	}

	@Override
	public boolean isValidMove(String source, String destination) {
		long src = ConvertUtils.getPositionAsLong(source);
		long dst = ConvertUtils.getPositionAsLong(destination);
		return pieceContainer.isValidMove(src, dst, history.mostRecent(), history.isBlacksTurn());
	}

	@Override
	public Iterable<String> getValidMoves(String source) {
		InputValidation.validatePosition(source);
		long src = ConvertUtils.getPositionAsLong(source);
		return ConvertUtils.getPositionsAsStrings(pieceContainer.getValidMoves(src, history.mostRecent()));
	}

	@Override
	public Map<String, String> getBoard() {
		Map<String, String> board = new HashMap<>();
		long position;
		String pieceCode;
		for (int i=0; i<64; ++i) {
			position = 1L << i;
			if ((pieceCode = pieceContainer.getPieceCodeAtPosition(position)) != null) {
				board.put(ConvertUtils.getPositionAsString(position), pieceCode);
			}
		}
		return board;
	}

	@Override
	public void loadGame(Map<Integer, Entry<String, String>> game) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void addPiece(String pieceCode, String position) {
		Turn turn = pieceContainer.addPieceToBoard(pieceCode, position);
		history.addMove(turn);
	}

	@Override
	public void resetBoard() {
		pieceContainer = new PieceContainer();
		history = new MoveHistory();
	}

	@Override
	public PieceColor getActivePlayer() {
		return history.isBlacksTurn() ? PieceColor.BLACK : PieceColor.WHITE;
	}

	@Override
	public Collection<String> getTotalDefense(PieceColor color) {
		long totalDefense = DefenseUtils.getDefendedSquaresForColor(pieceContainer.getPieces(), color);;
		return ConvertUtils.getPositionsAsStrings(totalDefense);
	}

	@Override
	public PieceColor getWinner() {
		long availableMoves = 0L;
		
		Iterable<Long> blackPieces = ConvertUtils.getPositionsAsLongs(pieceContainer.getBlackPieces());
		for (long piece : blackPieces) {
			if ((availableMoves = pieceContainer.getValidMoves(piece, null)) != 0L) {
				break;
			}
        }
		if (availableMoves == 0L) {
			return PieceColor.WHITE;
		}
		
		Iterable<Long> whitePieces = ConvertUtils.getPositionsAsLongs(pieceContainer.getWhitePieces());
		for (long piece : whitePieces) {
			if ((availableMoves = pieceContainer.getValidMoves(piece, null)) != 0L) {
				break;
			}
        }
		if (availableMoves == 0L) {
			return PieceColor.BLACK;
		}
		return null;
	}
}
