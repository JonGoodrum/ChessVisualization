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
	private final Map<String, Long> positions;
	private Map<String, Long> pieces;
	private Map<Integer, Map.Entry<String, String>> history;
	
	private static final long TR_BORDER = 0xFF01010101010101L;
	private static final long TL_BORDER = 0xFF80808080808080L;
	private static final long BR_BORDER = 0x01010101010101FFL;
	private static final long BL_BORDER = 0x80808080808080FFL;
	private static final long UP_DIAGONAL = 0x0102040810204080L;
	private static final long DOWN_DIAGONAL = 0x8040201008040201L;
	private static final long FILLED_UP_DIAGONAL   = 0x000103070F1F3F7FL;
	private static final long FILLED_DOWN_DIAGONAL = 0x0080C0E0F0F8FCFEL;
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
		positions = new HashMap<>();
		initializePositions();
	}

	private void initializePositions() {
		String rankAndFile;
		for (int rank = 1; rank <= 8; ++rank) {
			for (char file = 'a'; file <= 'h'; ++file) {
				rankAndFile = String.format("%s%d", file, rank);
				positions.put(rankAndFile, getPosition(rankAndFile));
			}
		}
	}

	private long getBlackPieces() {
		return getPiecesFromPieceCodes(0,6);
	}

	private long getWhitePieces() {
		return getPiecesFromPieceCodes(6,12);
	}
	
	private long getPiecesFromPieceCodes(int startIndex, int stopIndexExclusive) {
		long result = 0L;
		for (int i=startIndex; i < stopIndexExclusive; ++i) {
			result |= pieces.get(PIECE_CODES[i]);
		}
		return result;
	}
	
	
	private void move(String source, String destination) {
		long src = getPosition(source);
		long dest = getPosition(destination);
		long previousOpponentPieces = blacksTurn ? getWhitePieces() : getBlackPieces();
		long orig;
		for (String pieceCode : pieces.keySet()) {
			if (((orig = pieces.get(pieceCode)) & src) != 0) {
				pieces.put(pieceCode, (orig ^ src) | dest);
				for (String removedPiece : pieces.keySet()) {
					if (removedPiece.equals(pieceCode)) {
						continue;
					}
					else if (((orig = pieces.get(removedPiece)) & dest) != 0L) {
						pieces.put(removedPiece, orig ^ dest);
					}
				}
			}
		}
		long currentOpponentPieces = blacksTurn ? getWhitePieces() : getBlackPieces();
		System.out.println("isEnPassant(): " + isEnPassant(src, dest, previousOpponentPieces, currentOpponentPieces));
		if (isEnPassant(src, dest, previousOpponentPieces, currentOpponentPieces)) {
			String pawnType = src > dest ? "wP" : "bP";
			long oldPawnStructure = pieces.get(pawnType);
			long deadPawn = src > dest ? dest << 8 : dest >>> 8;
			pieces.put(pawnType, oldPawnStructure ^ deadPawn);
		}
	}
	
	private boolean isEnPassant(long src, long dest, long previousOpponentPieces, long currentOpponentPieces) {
	    long pawns = pieces.get("bP") | pieces.get("wP");
	    boolean isDiagonalMove = src << 7 == dest ? true : src << 9 == dest ? true : src >>> 7 == dest ? true : src >>> 9 == dest ? true : false; 
		if ((dest & pawns) == 0L) {
	    	return false;
	    }
    	else {
            return isDiagonalMove && (previousOpponentPieces == currentOpponentPieces);
	    }
	}

	public void setMove(String source, String destination) {
		move(source, destination);
		blacksTurn = !blacksTurn;
		addToHistory(source, destination);
		printBoard();
	}
	
	private void printBoard() {
		String[] pieces = new String[64];
		for (int i=0; i<64; ++i) {
			pieces[i] = getPieceCode(1L << i);
		}
		for (int i=63; i>-1; --i) {
			System.out.print(" " + (pieces[i]==null ? "• " : pieces[i]) + " ");
			if (i%8==0) {
				System.out.println("\n");
			}
		}
		System.out.println("moveCount: " + moveCount);
		System.out.println("Next move: " + (blacksTurn ? "black" : "white"));
		System.out.print("\n\n");
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
    
    private String getPosition(long position) {
    	String pos = "";
    	for (char file='a'; file <='h'; ++file) {
    		if ((RANKS_AND_FILES.get(""+file) & position) == position) {
    			pos += file;
    			break;
    		}
    	}
    	for (int rank=1; rank <=8; ++rank) {
    		if ((RANKS_AND_FILES.get(""+rank) & position) == position) {
    			pos += rank;
    			break;
    		}
    	}
    	return pos;
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
		String pieceCode;
		for (String position : positions.keySet()) {
			if ((pieceCode = getPieceCode(positions.get(position))) != null) {
				board.put(position, pieceCode);
			}
		}
		return board;
	}
	
	public boolean isValidMove(String source, String destination) {
		long src = getPosition(source);
		long currentPlayersPieces = blacksTurn ? getBlackPieces() : getWhitePieces();
		if ((currentPlayersPieces & src) == 0L) {
			return false;
		}
		else {
			long dest = getPosition(destination);
			return (getValidMoves(src) & dest) != 0L ;
		}
	}

	public Iterable<String> getValidMoves(String source) {
		List<String> moves = new ArrayList<>();
		long validMoves = getValidMoves(getPosition(source));
		String position;
		
		for (int rank=1; rank<=8; ++rank) {
			for (char file='a'; file <='h'; ++file) {
				position = ""+file+rank;
				if ((validMoves & getPosition(position)) != 0L) {
					moves.add(position);
				}
			}
		}
		return moves;
	}
	
	/**
	 * returns a long containing valid squares for a piece.
	 * Returns 0L if no piece is at this position, or if this
	 * piece has no valid moves (ex: queen on 1st turn for either player)
	 * 
	 * @param position
	 * @return
	 */
	private long getValidMoves(long position) {
		String pieceCode = getPieceCode(position);
		if (pieceCode == null) {
			return 0L;
		}
		else {
			long emptyBoardMoves = getEmptyBoardMoves(pieceCode, position);
			long friendlyPieces = pieceCode.charAt(0) == 'b' ? getBlackPieces() : getWhitePieces();
			if (pieceCode.charAt(1) == 'N' || pieceCode.charAt(1) == 'K') {
				return emptyBoardMoves ^ (emptyBoardMoves & friendlyPieces);
			}
			else if (pieceCode.charAt(1) == 'P') {
				return getPawnMoves(pieceCode, position);
			}
			else if (pieceCode.charAt(1) == 'B') {
			    return fillDiagonals(position, emptyBoardMoves);
			}
			else if (pieceCode.charAt(1) == 'R') {
			    return fillRanksAndFiles(position, emptyBoardMoves);
			}
			else if (pieceCode.charAt(1) == 'Q') {
			    return fillDiagonals(position, fillRanksAndFiles(position, emptyBoardMoves));
			}
			return 0L;
		}
	}

	/*
	 * Fills ranks and files with 0s where inaccessible.
	 */
	private long fillRanksAndFiles(long position, long emptyBoardMoves) {
		long myRank = getRank(position);
		long myFile = getFile(position);
		long nextSquare = 0L;
		long allPieces = getBlackPieces() | getWhitePieces();
		long enemyPieces = (getBlackPieces() & position) == 0L ? getBlackPieces() : getWhitePieces();
		boolean fillUp = false;
		boolean fillLeft = false;
		boolean fillDown = false;
		boolean fillRight = false;
		for (int i=1; i<8; ++i) {
			// RIGHT
			nextSquare = position >>> i;
            if ((myRank & nextSquare) != 0L) {
            	if (!fillRight) {
            		if ((nextSquare & allPieces) != 0L) {
            			fillRight = true;
            			if ((nextSquare & enemyPieces) == 0L) {
            				emptyBoardMoves ^= nextSquare;
            			}
            		}
            	}
            	else {
            		emptyBoardMoves ^= nextSquare;
            	}
			}
            
            // LEFT
            nextSquare = position << i;
            if ((myRank & nextSquare) != 0L) {
            	if (!fillLeft) {
            		if ((nextSquare & allPieces) != 0L) {
            			fillLeft = true;
            			if ((nextSquare & enemyPieces) == 0L) {
            				emptyBoardMoves ^= nextSquare;
            			}
            		}
            	}
            	else {
            		emptyBoardMoves ^= nextSquare;
            	}
            }
            
            // UP
            nextSquare = position << 8*i;
            if ((myFile & nextSquare) != 0L) {
            	if (!fillUp) {
            		if ((nextSquare & allPieces) != 0L) {
            			fillUp = true;
            			if ((nextSquare & enemyPieces) == 0L) {
            				emptyBoardMoves ^= nextSquare;
            			}
            		}
            	}
            	else {
            		emptyBoardMoves ^= nextSquare;
            	}
            }
            
            // DOWN
            nextSquare = position >>> 8*i;
            if ((myFile & nextSquare) != 0L) {
            	if (!fillDown) {
            		if ((nextSquare & allPieces) != 0L) {
            			fillDown = true;
            			if ((nextSquare & enemyPieces) == 0L) {
            				emptyBoardMoves ^= nextSquare;
            			}
            		}
            	}
            	else {
            		emptyBoardMoves ^= nextSquare;
            	}
            }
		}
		return emptyBoardMoves;
	}

	// do while rank != edge or (file != edge)
	private long fillDiagonals(long position, long emptyBoardMoves) {
		
		long allPieces = getBlackPieces() | getWhitePieces();
		long enemyPieces = (getBlackPieces() & position) == 0L ? getBlackPieces() : getWhitePieces();
		boolean fill;
		long nextSquare;

		// TOP RIGHT
		fill = false;
	    nextSquare = position;
		while ((nextSquare & TR_BORDER) == 0L) {
			nextSquare <<= 7;
			if (!fill) {
				if ((nextSquare & allPieces) != 0L) {
        			fill = true;
        			if ((nextSquare & enemyPieces) == 0L) {
        				emptyBoardMoves ^= nextSquare;
        			}
        		}
			}
			else {
				emptyBoardMoves ^= nextSquare;
			}
		}

		// TOP LEFT
		fill = false;
	    nextSquare = position;
		while ((nextSquare & TL_BORDER) == 0L) {
			nextSquare <<= 9;
			if (!fill) {
				if ((nextSquare & allPieces) != 0L) {
        			fill = true;
        			if ((nextSquare & enemyPieces) == 0L) {
        				emptyBoardMoves ^= nextSquare;
        			}
        		}
			}
			else {
				emptyBoardMoves ^= nextSquare;
			}
		}

		// BOTTOM RIGHT
		fill = false;
	    nextSquare = position;
		while ((nextSquare & BR_BORDER) == 0L) {
			nextSquare >>>= 9;
			if (!fill) {
				if ((nextSquare & allPieces) != 0L) {
        			fill = true;
        			if ((nextSquare & enemyPieces) == 0L) {
        				emptyBoardMoves ^= nextSquare;
        			}
        		}
			}
			else {
				emptyBoardMoves ^= nextSquare;
			}
		}

		// BOTTOM LEFT
		fill = false;
	    nextSquare = position;
		while ((nextSquare & BL_BORDER) == 0L) {
			nextSquare >>>= 7;
			if (!fill) {
				if ((nextSquare & allPieces) != 0L) {
        			fill = true;
        			if ((nextSquare & enemyPieces) == 0L) {
        				emptyBoardMoves ^= nextSquare;
        			}
        		}
			}
			else {
				emptyBoardMoves ^= nextSquare;
			}
		}
		
		return emptyBoardMoves;
	}

	private long getEmptyBoardMoves(String pieceCode, long position) {
		char type = pieceCode.charAt(1);
		switch (type) {
		case 'B':
			return getUpDiagonal(position) ^ getDownDiagonal(position);
		case 'K':
			return getNeighboringSquares(position);
		case 'N':
			return getKnightMoves(position);
		case 'Q':
			return getRank(position) ^ getFile(position) ^ getUpDiagonal(position) ^ getDownDiagonal(position);
		case 'R':
			return getRank(position) ^ getFile(position);
		default:
			return 0L;
		}
	}
	
	private long getKnightMoves(long position) {
		long rank = getRank(position);
		long oneaway = ((position << 1) & rank) | ((position >>> 1) & rank);
		long twoaway = ((position << 2) & rank) | ((position >>> 2) & rank);
		return (oneaway << 16) | (oneaway >>> 16) | (twoaway << 8) | (twoaway >>> 8);
	}
	
	private long getPawnMoves(String pieceCode, long position) {
		char playerTurn = pieceCode.charAt(0);
		char myFile;
		long moveset;
		long enemyPieces;
		long rank;
		long attacks;
		String lastMovesSrc;
		String lastMovesDest;
		switch (playerTurn) {
		case 'w':
			enemyPieces = getBlackPieces();
			moveset = ((position << 8) & ~enemyPieces);
			if (((position & 0x000000000000FF00L) == position) && (moveset != 0L) && (((position << 16) & enemyPieces) == 0)) {
				moveset |= position << 16;
			}
			rank = getRank(position);
			attacks = (((position << 1) & rank) | ((position >>> 1) & rank)) << 8;
			
			// handle en passant
			if (moveCount > 0 && rank == 0x000000FF00000000L) {
				lastMovesSrc = history.get(moveCount).getKey();
				lastMovesDest = history.get(moveCount).getValue();
				// if a pawn was the last piece moved
				if ((getPosition(lastMovesDest) & pieces.get("bP")) != 0L) {
					myFile = getPosition(position).charAt(0);
					// if that pawn was moved from Rank 7 to Rank 5 on an adjacent file:
					if (lastMovesSrc.charAt(1) == '7' && lastMovesDest.charAt(1) == '5') {
						if (lastMovesDest.charAt(0) == myFile-1) {
							moveset |= (position << 9); 
						}
						else if (lastMovesDest.charAt(0) == myFile+1) {
							moveset |= (position << 7);
						}
					}
				}
			}
			
			return moveset | (attacks & enemyPieces);
		case 'b':
			enemyPieces = getWhitePieces();
			moveset = ((position >>> 8) & ~enemyPieces);
			if(((position & 0x00FF000000000000L) == position) && (moveset != 0L) && (((position >>> 16) & enemyPieces) == 0)) {
				moveset |= position >>> 16;
			}
			rank = getRank(position);
			attacks = (((position << 1) & rank) | ((position >>> 1) & rank)) >>> 8;
			
			// handle en passant
			if (moveCount > 0 && rank == 0x00000000FF000000L) {
				lastMovesSrc = history.get(moveCount).getKey();
				lastMovesDest = history.get(moveCount).getValue();
				System.out.println("lastMovesSrc: " + lastMovesSrc);
				System.out.println("lastMovesDest: " + lastMovesDest);
				// if a pawn was the last piece moved
				if ((getPosition(lastMovesDest) & pieces.get("wP")) != 0L) {
					System.out.println("position: " + position);
					myFile = getPosition(position).charAt(0);
					// if that pawn was moved from Rank 2 to Rank 4 on an adjacent file:
					if (lastMovesSrc.charAt(1) == '2' && lastMovesDest.charAt(1) == '4') {
						if (lastMovesDest.charAt(0) == myFile-1) {
							moveset |= (position >>> 7); 
						}
						else if (lastMovesDest.charAt(0) == myFile+1) {
							moveset |= (position >>> 9);
						}
					}
				}
			}
			
			return moveset | (attacks & enemyPieces);
		default:
			return 0L;
		}
	}

	private long getUpDiagonal(long position) {
		if ((UP_DIAGONAL & position) == position) {
			return UP_DIAGONAL;
		}
		long diag;
		for (int shift=1; shift<8; ++shift) {
			diag = (FILLED_UP_DIAGONAL) & (UP_DIAGONAL >>> shift);
			if ((diag & position) == position) {
				return diag;
			}
			diag = (~FILLED_UP_DIAGONAL ^ UP_DIAGONAL) & (UP_DIAGONAL << shift);
			if ((diag & position) == position) {
				return diag;
			}
		}
		return 0L;
	}

	private long getDownDiagonal(long position) {
		long diag;
		for (int shift=0; shift<8; ++shift) {
			diag = (FILLED_DOWN_DIAGONAL | DOWN_DIAGONAL) & (DOWN_DIAGONAL << shift);
			if ((diag & position) == position) {
				return diag;
			}
			diag = (~FILLED_DOWN_DIAGONAL | DOWN_DIAGONAL) & (DOWN_DIAGONAL >>> shift);
			if ((diag & position) == position) {
				return diag;
			}
		}
		return 0L;
	}
	
	private long getRank(long position) {
		long currentRank;
    	for (int rank=1; rank <=8; ++rank) {
    		currentRank = RANKS_AND_FILES.get(""+rank);
    		if ((currentRank & position) == position) {
    			return currentRank;
    		}
    	}
		return 0L;
	}

	private long getFile(long position) {
		long currentFile;
    	for (char file='a'; file <= 'h'; ++file) {
    		currentFile = RANKS_AND_FILES.get(""+file);
    		if ((currentFile & position) == position) {
    			return currentFile;
    		}
    	}
		return 0L;
	}
	
	private long getNeighboringSquares(long position) {
		long myRank = getRank(position);
		long myRow = ((position << 1) & myRank) | position | ((position >>> 1) & myRank);
		return position ^ ((myRow << 8) | myRow | (myRow >>> 8));
	}
	
	/**
	 * Returns the pieceCode at a given position,
	 * or null if no piece occupies that square.
	 * 
	 * @param position
	 * @return
	 */
	private String getPieceCode(long position) {
		for (String pieceCode : pieces.keySet()) {
			if ((pieces.get(pieceCode) & position) != 0L) {
				return pieceCode;
			}
		}
		return null;
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
