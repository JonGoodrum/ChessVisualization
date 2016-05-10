package com.grapedrink.chessmap.logic.bitboards;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.grapedrink.chessmap.logic.history.Turn;

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
		pieces = new HashMap<>();
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
		String source = BitboardUtils.getPositionAsString(src);
		String destination = BitboardUtils.getPositionAsString(dst);
		boolean playerIsBlack = (src & getBlackPieces()) == src;
		long previousEnemyPieces = playerIsBlack ? getWhitePieces() : getBlackPieces(); 
		long currentPieces;
		Turn turn = new Turn(source, destination);
		
		for (String pieceCode : pieces.keySet()) {
			currentPieces = pieces.get(pieceCode);
			if ((currentPieces & dst) == dst) {
				turn.addDiff(pieceCode, currentPieces);
				pieces.put(pieceCode, currentPieces ^ dst);
			}
			if ((currentPieces & src) == src) {
				turn.addDiff(pieceCode, currentPieces);
				pieces.put(pieceCode, (currentPieces ^ src) | dst);
			}
		}
		long currentEnemyPieces = playerIsBlack ? getWhitePieces() : getBlackPieces();
		handleEnPassant(previousEnemyPieces, currentEnemyPieces, src, dst, turn);
		handleCastling(src, dst, turn);
		disableCastlingIfNecessary(src, turn);
		disableCastlingIfNecessary(dst, turn);
		setPromotion(dst);
		
		/*
		 * TODO : turn this into a user prompt, where they can pick any BNRQ piece
		 */
		if (isPromotion()) {
			promotePawn(playerIsBlack ? "bQ" : "wQ", turn);
		}
		
		return turn;
	}
	
	public void setMove(Turn turn) {
		for (String pieceCode : turn.getDiffs().keySet()) {
			setPieceCode(pieceCode, turn.getDiffs().get(pieceCode));
		}
		a1CastleAllowed = turn.getA1();
		a8CastleAllowed = turn.getA8();
		h1CastleAllowed = turn.getH1();
		h8CastleAllowed = turn.getH8();
	}
	
	private void handleCastling(long src, long dst, Turn turn) {
		boolean playerIsBlack = dst == (dst & getBlackPieces());
		long castlingRank = playerIsBlack ? BitboardUtils.RANKS[7] : BitboardUtils.RANKS[0];
		boolean isKing = playerIsBlack ? (dst == (pieces.get("bK") & dst)) : (dst == (pieces.get("wK") & dst));
		boolean kingWasOnE = isKing && (castlingRank & BitboardUtils.FILES[4]) == src;
		boolean pieceMovedToC = (castlingRank & BitboardUtils.FILES[2]) == dst;
		boolean pieceMovedToG = (castlingRank & BitboardUtils.FILES[6]) == dst;

		if (playerIsBlack && kingWasOnE && pieceMovedToC) {
        	long a8d8 = BitboardUtils.RANKS[7] & (BitboardUtils.FILES[0] | BitboardUtils.FILES[3]);
        	turn.addDiff("bR", pieces.get("bR"));
        	pieces.put("bR", pieces.get("bR") ^ a8d8);
			a8CastleAllowed = false;
			h8CastleAllowed = false;
		}
        else if (playerIsBlack && kingWasOnE && pieceMovedToG) {
        	long f8h8 = BitboardUtils.RANKS[7] & (BitboardUtils.FILES[5] | BitboardUtils.FILES[7]);
        	turn.addDiff("bR", pieces.get("bR"));
        	pieces.put("bR", pieces.get("bR") ^ f8h8);
			a8CastleAllowed = false;
			h8CastleAllowed = false;			
		}
        else if (!playerIsBlack && kingWasOnE && pieceMovedToC) {
        	long a1d1 = BitboardUtils.RANKS[0] & (BitboardUtils.FILES[0] | BitboardUtils.FILES[3]);
        	turn.addDiff("wR", pieces.get("wR"));
        	pieces.put("wR", pieces.get("wR") ^ a1d1);
			a1CastleAllowed = false;
			h1CastleAllowed = false;			
		}
        else if (!playerIsBlack && kingWasOnE && pieceMovedToG) {
        	long f1d1 = BitboardUtils.RANKS[0] & (BitboardUtils.FILES[5] | BitboardUtils.FILES[7]);
        	turn.addDiff("wR", pieces.get("wR"));
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
			turn.addDiff(enemyPawnType, oldPawnStructure);
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

	private void disableCastlingIfNecessary(long position, Turn turn) {
		long a1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[0];
		long a8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[0];
		long h1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[7];
		long h8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[7];
		long e1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[4];
		long e8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[4];
		
        if (position == a1) {
        	turn.setA1();
        	a1CastleAllowed = false;
		}
		else if (position == a8) {
			turn.setA8();
			a8CastleAllowed = false;
		}
		else if (position == h1) {
			turn.setH1();
			h1CastleAllowed = false;
		}
		else if (position == h8) {
			turn.setH8();
			h8CastleAllowed = false;
		}
		else if (position == e1) {
			turn.setA1();
			turn.setH1();
			a1CastleAllowed = false;
			h1CastleAllowed = false;
		}
		else if (position == e8) {
			turn.setA8();
			turn.setH8();
			a8CastleAllowed = false;
			h8CastleAllowed = false;
		}
	}
	
	public boolean isPromotion() {
		return isPromotion;
	}
	
	private void setPromotion(long dst) {
		String pieceCode = getPieceCode(dst);
		PieceColor player = PieceColor.get(pieceCode);
		boolean isPawn = PieceType.PAWN.equals(PieceType.get(pieceCode));
		boolean isLastRank = PieceColor.BLACK.equals(player) ? ((dst & BitboardUtils.RANKS[0]) == dst) : ((dst & BitboardUtils.RANKS[7]) == dst);
		isPromotion = isPawn && isLastRank;
	}

	public void promotePawn(String pieceCode, Turn turn) {
		char color = pieceCode.charAt(0);
		long rank = color == 'b' ? BitboardUtils.RANKS[0] : BitboardUtils.RANKS[7];
		String pawnCode = String.format("%sP", color);
		if (isPromotion) {
			long promotion = rank & pieces.get(String.format("%sP", color));
			turn.addDiff(pawnCode, pieces.get(pawnCode));
			turn.addDiff(pieceCode, pieces.get(pieceCode));
			pieces.put(pawnCode, pieces.get(pawnCode) ^ promotion);
			pieces.put(pieceCode, pieces.get(pieceCode) | promotion);
		}
		isPromotion = false;
	}
	
	public PieceColor getPieceColor(long position) throws IllegalArgumentException {
		return PieceColor.get(getPieceCode(position).charAt(0));
	}
	
	public PieceType getPieceType(long position) throws IllegalArgumentException {
		return PieceType.get(getPieceCode(position).charAt(1));
	}
	
	public String getPieceCode(long position) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		for (String pieceCode : pieces.keySet()) {
			if ((pieces.get(pieceCode) & position) == position) {
				return pieceCode;
			}
		}
		return null;
	}

	public void setPieceCode(String pieceCode, long positions) {
		pieces.put(pieceCode, positions);
	}
	
	public boolean isValidMove(long src, long dst, Turn mostRecent, boolean isBlacksTurn) {
	    long activePlayersPieces = isBlacksTurn ? getBlackPieces() : getWhitePieces();
	    if ((src & activePlayersPieces) == src) {
	    	return (getValidMoves(src, mostRecent) & dst) == dst;
	    }
	    return false;
	}
	
	private long getAvailableCastles(long position) {
		long b1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[1];
		long c1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[2];
		long d1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[3];
		long e1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[4];
		long f1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[5];
		long g1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[6];
		long b8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[1];
		long c8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[2];
		long d8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[3];
		long e8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[4];
		long f8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[5];
		long g8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[6];
		long moves = 0L;
		if (!isBlack(position) && isEmpty(b1|c1|d1) && a1CastleAllowed) {
			if (!isUnderAttack(c1, PieceColor.BLACK) && !isUnderAttack(d1, PieceColor.BLACK) && !isUnderAttack(e1, PieceColor.BLACK)) {
				moves |= c1;
			}
		}
		if (!isBlack(position) && isEmpty(f1|g1) && h1CastleAllowed) {
			if (!isUnderAttack(e1, PieceColor.BLACK) && !isUnderAttack(f1, PieceColor.BLACK) && !isUnderAttack(g1, PieceColor.BLACK)) {
				moves |= g1;
			}
		}
		if (isBlack(position)  && isEmpty(b8|c8|d8) && a8CastleAllowed) {
			if (!isUnderAttack(c8, PieceColor.WHITE) && !isUnderAttack(d8, PieceColor.WHITE) && !isUnderAttack(e8, PieceColor.WHITE)) {
				moves |= c8;
			}
		}
		if (isBlack(position)  && isEmpty(f8|g8) && h8CastleAllowed) {
			if (!isUnderAttack(e8, PieceColor.WHITE) && !isUnderAttack(f8, PieceColor.WHITE) && !isUnderAttack(g8, PieceColor.WHITE)) {
				moves |= g8;
			}
		}
		return moves;
	}
	
	private boolean isUnderAttack(long position, PieceColor attacker) {
		InputValidation.validatePosition(position);
		return position == (position & getAllAttacks(attacker));
	}
	
	private long getAllAttacks(PieceColor attacker) {
		Set<Long> playerPieces = getIndividualPieces(attacker);
		long attacks = 0L;
		for (Long position : playerPieces) {
			attacks |= MoveUtils.getDefendedSquares(position, getAllPieces(), getPieceCode(position));
		}
		return attacks;
	}
	
	private boolean isEmpty(long boardSquares) {
		return (getAllPieces() & boardSquares) == 0L; 
	}
	
	private Set<Long> getIndividualPieces(PieceColor color) {
		Set<Long> individualPieces = new HashSet<>();
		long currentPosition;
		long myPieces = PieceColor.BLACK.equals(color) ? getBlackPieces() : getWhitePieces();
		for (int i=0; i<64; ++i) {
			currentPosition = (1L << i);
			if ((currentPosition & myPieces) != 0L) {
				individualPieces.add(currentPosition);
			}
		}
		return individualPieces;
	}
	
	public long getValidMoves(long src, Turn mostRecent) {
		String pieceCode = getPieceCode(src);
		if (pieceCode != null) {
			PieceType type = getPieceType(src);
			long myPieces = isBlack(src) ? getBlackPieces() : getWhitePieces();
			long allPieces = getBlackPieces() | getWhitePieces();
			long pawnStructure = pieces.get("bP") | pieces.get("wP");
			if (PieceType.KING.equals(type)) {
				return MoveUtils.getValidMoves(src, myPieces, allPieces, pawnStructure, pieceCode, mostRecent) | getAvailableCastles(src);
			}
			else {
				return MoveUtils.getValidMoves(src, myPieces, allPieces, pawnStructure, pieceCode, mostRecent);
			}
		}
		return 0L;
		
	}

	private boolean isBlack(long position) {
		return PieceColor.get(getPieceCode(position).charAt(0)).equals(PieceColor.BLACK);
	}
}
