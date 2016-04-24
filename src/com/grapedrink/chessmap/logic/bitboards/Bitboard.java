package com.grapedrink.chessmap.logic.bitboards;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This Bitboard class represents the game via longs,
 * with 0L representing an empty board, -1L representing
 * a full board, and 1 representing a piece occupying h1.
 */
public class Bitboard {

	private int moveCount;
	private boolean blacksTurn;
	private Map<String, Long> pieces;
	private Map<Integer, Map.Entry<String, String>> history;
	
	private static final String[] PIECE_CODES = {"bB", "bK", "bN", "bP", "bQ", "bR", "wB", "wK", "wN", "wP", "wQ", "wR"};
	private static final Long[] INITIAL_POSITIONS = {0x2400000000000000L, 0x0800000000000000L, 0x4200000000000000L, 0x00FF000000000000L, 0x1000000000000000L,
		0x8100000000000000L, 0x0000000000000024L, 0x0000000000000008L, 0x0000000000000042L, 0x000000000000FF00L, 0x0000000000000010L, 0x0000000000000081L};
	@SuppressWarnings("serial")
	private static final Map<String, Long> RANKS_AND_FILES = new HashMap<String, Long>() {{
		put("1",255L);
		put("2",65280L);
		put("3",16711680L);
		put("4",4278190080L);
		put("5",1095216660480L);
		put("6",280375465082880L);
		put("7",71776119061217280L);
		put("8",-72057594037927936L);
		put("a",-9187201950435737472L);
		put("b",4629771061636907072L);
		put("c",2314885530818453536L);
		put("d",1157442765409226768L);
		put("e",578721382704613384L);
		put("f",289360691352306692L);
		put("g",144680345676153346L);
		put("h",72340172838076673L);
	}};
	
			
	public Bitboard(){
		resetBoard();
	}
	
	private void move(String source, String destination) {
		long src = getPosition(source);
		long dest = getPosition(destination);
		long tmp;
		for (String pieceCode : pieces.keySet()) {
			if ((tmp = (pieces.get(pieceCode) & src)) != 0) {
				pieces.put(pieceCode, (tmp ^ src) | dest);
			}
		}
	}
	
	public void setMove(String source, String destination) {
		move(source, destination);
		blacksTurn = !blacksTurn;
		addToHistory(source, destination);
	}
	
	private void addToHistory(String source, String destination) {
		history.put(++moveCount, new AbstractMap.SimpleEntry<String, String>(source, destination));
		int i = moveCount;
		while (history.remove(++i) != null);
	}
	
	public void undoMove() {
		if (moveCount > 0) {
			Map.Entry<String, String> previous = history.get(moveCount);
			move(previous.getKey(), previous.getValue());
			--moveCount;
		}
		blacksTurn = !blacksTurn;
	}
	
    private long getPosition(String position) {
    	long file = RANKS_AND_FILES.get(""+position.charAt(0));
    	long rank = RANKS_AND_FILES.get(""+position.charAt(1));
    	return file & rank;
    }
	
	public void resetBoard() {
		pieces = new HashMap<>();
		history = new HashMap<>();
		blacksTurn = false;
		moveCount = 0;
		for (String pieceCode : PIECE_CODES) {
			pieces.put(pieceCode, 0L);
		}
	}
	
	public Map<String, String> getBoard() {
		Map<String, String> board = new HashMap<>();
		for (String pieceCode : pieces.keySet()) {
            for (String position : getLocationOfPieces(pieceCode)) {
	            board.put(position, pieceCode);
			}
		}
		return board;
	}
	
	public boolean isValidMove(String source, String destination) {
		return false;
	}
	
	private List<String> getLocationOfPieces(String pieceCode) {
		List<String> positions = new ArrayList<>();
		long piece = pieces.get(pieceCode);
		if (piece != 0L) {
			for (int rank=1; rank<=8; ++rank) {
				if ((piece & RANKS_AND_FILES.get(""+rank)) != 0) {
					for (char file='a'; file <='h'; ++file) {
						if((piece & RANKS_AND_FILES.get(""+file)) != 0) {
							positions.add(""+file+rank);
						}
					}
				}
			}
		}
		return positions;
	}
	
	public void setNewGame() {
		resetBoard();
		for (int i=0; i<12; ++i) {
			pieces.put(PIECE_CODES[i], INITIAL_POSITIONS[i]);
		}
	}

	public Entry<String, String> getNextMove() {
		if (moveCount < history.size()) {
			return history.get(++moveCount);
		}
		return null;
	}
}
