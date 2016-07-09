package com.grapedrink.chessmap.logic.bitboards;

import java.util.HashMap;
import java.util.Map;

import com.grapedrink.chessmap.logic.history.Turn;
import com.grapedrink.chessmap.logic.utils.BoardConstants;
import com.grapedrink.chessmap.logic.utils.ConvertUtils;
import com.grapedrink.chessmap.logic.utils.DefenseUtils;
import com.grapedrink.chessmap.logic.utils.GameUtils;
import com.grapedrink.chessmap.logic.utils.MoveUtils;
import com.grapedrink.chessmap.logic.utils.PieceUtils;

public class PieceContainer {

	private boolean a8CastleAllowed;
	private boolean a1CastleAllowed;
	private boolean h1CastleAllowed;
	private boolean h8CastleAllowed;
	private boolean isPromotion;
	private Map<String, Long> pieces;

	public static final String[] PIECE_CODES = {"bB", "bK", "bN", "bP", "bQ", "bR", "wB", "wK", "wN", "wP", "wQ", "wR"};
	
	/**
	 * The starting positions of the pieces, with
	 * INITIAL_POSITIONS[0] representing the black bishops
	 * and INITIAL_POSITIONS[11] representing the white rooks.
	 * The order of these items correlates with the ordering
	 * of the elements in PIECE_CODES.
	 */
	public static final Long[] INITIAL_POSITIONS = {
		0x2400000000000000L,
		0x0800000000000000L,
		0x4200000000000000L,
		0x00FF000000000000L,
		0x1000000000000000L,
		0x8100000000000000L,
		0x0000000000000024L,
		0x0000000000000008L,
		0x0000000000000042L,
		0x000000000000FF00L,
		0x0000000000000010L,
		0x0000000000000081L
	};
	
	public PieceContainer() {
		isPromotion = false;
		a1CastleAllowed = true;
		a8CastleAllowed = true;
		h1CastleAllowed = true;
		h8CastleAllowed = true;
		resetBoard();
	}
	
	public void setNewGame() {
		isPromotion = false;
		a1CastleAllowed = true;
		a8CastleAllowed = true;
		h1CastleAllowed = true;
		h8CastleAllowed = true;
		for(int i=0; i<PIECE_CODES.length; ++i) {
			pieces.put(PIECE_CODES[i], INITIAL_POSITIONS[i]);
		}
	}
	
	private void resetBoard() {
		pieces = new HashMap<>();
		for(int i=0; i<PIECE_CODES.length; ++i) {
			pieces.put(PIECE_CODES[i], 0L);
		}
	}
	
	public long getAllPieces() {
		return getBlackPieces() | getWhitePieces();
	}

	public long getBlackPieces() {
		long colorPieces = 0L;
		for (int i=0; i<6; ++i) {
			colorPieces |= pieces.get(PIECE_CODES[i]);
		}
		return colorPieces;
	}

	public long getWhitePieces() {
		long colorPieces = 0L;
		for (int i=6; i<12; ++i) {
			colorPieces |= pieces.get(PIECE_CODES[i]);
		}
		return colorPieces;
	}

	/**  
	 * Moves a piece from src to dst,
	 * removing the pieces at src and dst (if any)
	 * 
	 * @param src
	 * @param dst
	 * @return turn
	 * @throws IllegalArgumentException
	 */
	public Turn setMove(long src, long dst) throws IllegalArgumentException {
		InputValidation.validatePosition(src);
		InputValidation.validatePosition(dst);
		String source = ConvertUtils.getPositionAsString(src);
		String destination = ConvertUtils.getPositionAsString(dst);
		boolean isBlack = PieceUtils.isBlack(src, pieces);
		long prevEnemyPieces = isBlack ? getWhitePieces() : getBlackPieces(); 
		Turn turn = new Turn(source, destination);
		
		replacePiece(src, dst, turn);
		long currEnemyPieces = isBlack ? getWhitePieces() : getBlackPieces();
		handleEnPassant(prevEnemyPieces, currEnemyPieces, src, dst, turn);
		handleCastlingAndPromotion(src, dst, turn);
		
		return turn;
	}
	
	private void replacePiece(long src, long dst, Turn turn) {
		long currentPieces;
		for (String pieceCode : pieces.keySet()) {
			currentPieces = pieces.get(pieceCode);
			if ((currentPieces & dst) == dst) {
				turn.addPrevState(pieceCode, currentPieces);
				pieces.put(pieceCode, currentPieces ^ dst);
			}
			if ((currentPieces & src) == src) {
				turn.addPrevState(pieceCode, currentPieces);
				pieces.put(pieceCode, (currentPieces ^ src) | dst);
			}
		}
	}
	
	private void handleCastlingAndPromotion(long src, long dst, Turn turn) {
		handleCastling(src, dst, turn);
		disableCastlingIfNecessary(turn);
		setPromotion(dst);
		
		/*
		 * TODO : turn this into a user prompt, where they can pick any BNRQ piece
		 */
		if (isPromotion()) {
			promotePawn(PieceUtils.isBlack(dst, pieces) ? "bQ" : "wQ", turn);
		}
	}
	
	public void setMove(Turn turn) {
		for (String pieceCode : turn.getPrevStates().keySet()) {
			setPieceCode(pieceCode, turn.getPrevStates().get(pieceCode));
		}
		a1CastleAllowed = turn.getA1();
		a8CastleAllowed = turn.getA8();
		h1CastleAllowed = turn.getH1();
		h8CastleAllowed = turn.getH8();
	}
	
	private void handleCastling(long src, long dst, Turn turn) {
		boolean playerIsBlack = dst == (dst & getBlackPieces());
		long castlingRank = playerIsBlack ? BoardConstants.RANKS[7] : BoardConstants.RANKS[0];
		boolean isKing = playerIsBlack ? (dst == (pieces.get("bK") & dst)) : (dst == (pieces.get("wK") & dst));
		boolean kingWasOnE = isKing && (castlingRank & BoardConstants.FILES[4]) == src;
		boolean pieceMovedToC = (castlingRank & BoardConstants.FILES[2]) == dst;
		boolean pieceMovedToG = (castlingRank & BoardConstants.FILES[6]) == dst;

		if (playerIsBlack && kingWasOnE && pieceMovedToC) {
        	long a8d8 = BoardConstants.RANKS[7] & (BoardConstants.FILES[0] | BoardConstants.FILES[3]);
        	turn.addPrevState("bR", pieces.get("bR"));
        	pieces.put("bR", pieces.get("bR") ^ a8d8);
			a8CastleAllowed = false;
			h8CastleAllowed = false;
		}
        else if (playerIsBlack && kingWasOnE && pieceMovedToG) {
        	long f8h8 = BoardConstants.RANKS[7] & (BoardConstants.FILES[5] | BoardConstants.FILES[7]);
        	turn.addPrevState("bR", pieces.get("bR"));
        	pieces.put("bR", pieces.get("bR") ^ f8h8);
			a8CastleAllowed = false;
			h8CastleAllowed = false;			
		}
        else if (!playerIsBlack && kingWasOnE && pieceMovedToC) {
        	long a1d1 = BoardConstants.RANKS[0] & (BoardConstants.FILES[0] | BoardConstants.FILES[3]);
        	turn.addPrevState("wR", pieces.get("wR"));
        	pieces.put("wR", pieces.get("wR") ^ a1d1);
			a1CastleAllowed = false;
			h1CastleAllowed = false;			
		}
        else if (!playerIsBlack && kingWasOnE && pieceMovedToG) {
        	long f1d1 = BoardConstants.RANKS[0] & (BoardConstants.FILES[5] | BoardConstants.FILES[7]);
        	turn.addPrevState("wR", pieces.get("wR"));
        	pieces.put("wR", pieces.get("wR") ^ f1d1);
        	a1CastleAllowed = false;
			h1CastleAllowed = false;			
		}
	}
	
	private void handleEnPassant(long prevEnemy, long currEnemy, long src, long dst, Turn turn) {
		if (isEnPassant(prevEnemy, currEnemy, src, dst)) {
			String enemyPawnType = src > dst ? "wP" : "bP";
			long oldPawnStructure = pieces.get(enemyPawnType);
			long deadPawn = src > dst ? dst << 8 : dst >>> 8;
			turn.addPrevState(enemyPawnType, oldPawnStructure);
			pieces.put(enemyPawnType, oldPawnStructure ^ deadPawn);
		}
	}
	
	private boolean isEnPassant(long prevEnemy, long currEnemy, long src, long dest) {
		long pawnStructure = pieces.get("wP") | pieces.get("bP");
		boolean enemyPiecesHaveNotChanged = prevEnemy == currEnemy;
	    boolean pawnJustMoved = (dest & pawnStructure) == dest;
	    boolean pieceMovedDiagonally = dest == (dest & ((src << 7) | (src << 9) | (src >>> 7) | (src >>> 9)));
	    return pawnJustMoved && pieceMovedDiagonally && enemyPiecesHaveNotChanged;
	}

	private void disableCastlingIfNecessary(Turn turn) {
        if ((ConvertUtils.getPositionAsLong("a1") & pieces.get("wR")) == 0L) {
        	a1CastleAllowed = false;
		}
        if ((ConvertUtils.getPositionAsLong("h1") & pieces.get("wR")) == 0L) {
        	h1CastleAllowed = false;
		}
        if ((ConvertUtils.getPositionAsLong("e1") & pieces.get("wK")) == 0L) {
        	a1CastleAllowed = false;
        	h1CastleAllowed = false;
		}
        if ((ConvertUtils.getPositionAsLong("a8") & pieces.get("bR")) == 0L) {
        	a8CastleAllowed = false;
		}
        if ((ConvertUtils.getPositionAsLong("h8") & pieces.get("bR")) == 0L) {
        	h8CastleAllowed = false;
		}
        if ((ConvertUtils.getPositionAsLong("e8") & pieces.get("bK")) == 0L) {
        	a8CastleAllowed = false;
        	h8CastleAllowed = false;
		}
        turn.setA1(a1CastleAllowed);
        turn.setH1(h1CastleAllowed);
        turn.setA8(a8CastleAllowed);
        turn.setH8(h8CastleAllowed);
	}
	
	/**
	 * Returns whether or not the 
	 * @return
	 */
	public boolean isPromotion() {
		return isPromotion;
	}
	
	private void setPromotion(long dst) {
		String pieceCode = PieceUtils.getPieceCode(dst, pieces);
		PieceColor player = PieceColor.get(pieceCode);
		boolean isPawn = PieceType.PAWN.equals(PieceType.get(pieceCode));
		boolean isLastRank = PieceColor.BLACK.equals(player) ? ((dst & BoardConstants.RANKS[0]) == dst) : ((dst & BoardConstants.RANKS[7]) == dst);
		isPromotion = isPawn && isLastRank;
	}

	public void promotePawn(String pieceCode, Turn turn) {
		char color = pieceCode.charAt(0);
		long rank = color == 'b' ? BoardConstants.RANKS[0] : BoardConstants.RANKS[7];
		String pawnCode = String.format("%sP", color);
		if (isPromotion) {
			long promotion = rank & pieces.get(String.format("%sP", color));
			turn.addPrevState(pawnCode, pieces.get(pawnCode));
			turn.addPrevState(pieceCode, pieces.get(pieceCode));
			pieces.put(pawnCode, pieces.get(pawnCode) ^ promotion);
			pieces.put(pieceCode, pieces.get(pieceCode) | promotion);
		}
		isPromotion = false;
	}

	public Turn addPieceToBoard(String pieceCode, String position) {
		Turn turn = new Turn(pieceCode, position);
		//turn.addPrevState(pieceCode, pieces.get(pieceCode));
		long pos = ConvertUtils.getPositionAsLong(position);
		replacePiece(pos, pos, turn);
		pieces.put(pieceCode, pieces.get(pieceCode) | pos);
		handleCastlingAndPromotion(0L, ConvertUtils.getPositionAsLong(position), turn);
		return turn;
	}
	
	public void setPieceCode(String pieceCode, long positions) {
		pieces.put(pieceCode, positions);
	}
	
	// TODO : MFD
	public boolean isValidMove(long src, long dst, Turn mostRecent, boolean isBlacksTurn) {
	    long activePlayersPieces = isBlacksTurn ? getBlackPieces() : getWhitePieces();
	    if ((src & activePlayersPieces) == src) {
	    	return (getValidMoves(src, mostRecent) & dst) == dst;
	    }
	    return false;
	}
	
	// TODO : MFD
	private long getAvailableCastles(long position) {
		long b1 = BoardConstants.RANKS[0] & BoardConstants.FILES[1];
		long c1 = BoardConstants.RANKS[0] & BoardConstants.FILES[2];
		long d1 = BoardConstants.RANKS[0] & BoardConstants.FILES[3];
		long e1 = BoardConstants.RANKS[0] & BoardConstants.FILES[4];
		long f1 = BoardConstants.RANKS[0] & BoardConstants.FILES[5];
		long g1 = BoardConstants.RANKS[0] & BoardConstants.FILES[6];
		long b8 = BoardConstants.RANKS[7] & BoardConstants.FILES[1];
		long c8 = BoardConstants.RANKS[7] & BoardConstants.FILES[2];
		long d8 = BoardConstants.RANKS[7] & BoardConstants.FILES[3];
		long e8 = BoardConstants.RANKS[7] & BoardConstants.FILES[4];
		long f8 = BoardConstants.RANKS[7] & BoardConstants.FILES[5];
		long g8 = BoardConstants.RANKS[7] & BoardConstants.FILES[6];
		long moves = 0L;
		
		if (!PieceUtils.isBlack(position, pieces) && isEmpty(b1|c1|d1) && a1CastleAllowed) {
            if (!DefenseUtils.isDefendedByColor(c1|d1|e1, pieces, PieceColor.WHITE)) {
				moves |= c1;
			}
		}
		if (!PieceUtils.isBlack(position, pieces) && isEmpty(f1|g1) && h1CastleAllowed) {
			if (!DefenseUtils.isDefendedByColor(e1|f1|g1, pieces, PieceColor.WHITE)) {
				moves |= g1;
			}
		}
		if (PieceUtils.isBlack(position, pieces)  && isEmpty(b8|c8|d8) && a8CastleAllowed) {
			if (!DefenseUtils.isDefendedByColor(c8|d8|e8, pieces, PieceColor.WHITE)) {
				moves |= c8;
			}
		}
		if (PieceUtils.isBlack(position, pieces)  && isEmpty(f8|g8) && h8CastleAllowed) {
			if (!DefenseUtils.isDefendedByColor(e8|f8|g8, pieces, PieceColor.WHITE)) {
				moves |= g8;
			}
		}
		return moves;
	}
	
	/**
	 * Determines whether the squares represented by a long are empty or not
	 * 
	 * @param boardSquares
	 * @return
	 */
	private boolean isEmpty(long boardSquares) {
		return (getAllPieces() & boardSquares) == 0L; 
	}
	
	// TODO : MFD
	public long getValidMoves(long src, Turn mostRecent) {
		String pieceCode = PieceUtils.getPieceCode(src, pieces);
		if (pieceCode != null) {
			PieceType type = PieceUtils.getPieceType(src, pieces);
			if (PieceType.KING.equals(type)) {
				if (GameUtils.isInCheck(src, pieces)) {
					return MoveUtils.getValidMoves(src, pieces, mostRecent);
				}
				return MoveUtils.getValidMoves(src, pieces, mostRecent) | getAvailableCastles(src);
			}
			else {
				if (GameUtils.isInCheck(src, pieces)) {
					return MoveUtils.getCheckBlockingMoves(src, pieces);
				}
				return MoveUtils.getValidMoves(src, pieces, mostRecent);
			}
		}
		return 0L;
	}
	
	/**
	 * Returns a deep copy of the Map<String, Long>
	 * inside here which contains pieces.
	 * 
	 * @return pieces
	 */
	public Map<String, Long> getPieces() {
		Map<String, Long> deepCopy = new HashMap<>();
		for (String key : pieces.keySet()) {
			deepCopy.put(key, pieces.get(key));
		}
		return deepCopy;
	}

	public String getPieceCodeAtPosition(long position) {
		return PieceUtils.getPieceCode(position, pieces);
	}
}
