package com.grapedrink.chessmap.logic.bitboards;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.grapedrink.chessmap.logic.history.Turn;

public class PieceUtils {

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
	
	public PieceUtils() {
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
		handleEnPassant(previousEnemyPieces, currentEnemyPieces, src, dst);
		handleCastling(src, dst);
		disableCastlingIfNecessary(src);
		disableCastlingIfNecessary(dst);
		setPromotion(dst);
		
		/*
		 * TODO : turn this into a user prompt, where they can pick any BNRQ piece
		 */
		if (isPromotion()) {
			promotePawn(playerIsBlack ? "bQ" : "wQ");
		}
		
		return turn;
	}
	
	private void handleCastling(long src, long dst) {
		boolean playerIsBlack = dst == (dst & getBlackPieces());
		long castlingRank = playerIsBlack ? BitboardUtils.RANKS[7] : BitboardUtils.RANKS[0];
		boolean isKing = playerIsBlack ? (dst == (pieces.get("bK") & dst)) : (dst == (pieces.get("wK") & dst));
		boolean kingWasOnE = isKing && (castlingRank & BitboardUtils.FILES[4]) == src;
		boolean pieceMovedToC = (castlingRank & BitboardUtils.FILES[2]) == dst;
		boolean pieceMovedToG = (castlingRank & BitboardUtils.FILES[6]) == dst;

		if (playerIsBlack && kingWasOnE && pieceMovedToC) {
        	long a8d8 = BitboardUtils.RANKS[7] & (BitboardUtils.FILES[0] | BitboardUtils.FILES[3]);
        	pieces.put("bR", pieces.get("bR") ^ a8d8);
			a8CastleAllowed = false;
			h8CastleAllowed = false;
		}
        else if (playerIsBlack && kingWasOnE && pieceMovedToG) {
        	long f8h8 = BitboardUtils.RANKS[7] & (BitboardUtils.FILES[5] | BitboardUtils.FILES[7]);
        	pieces.put("bR", pieces.get("bR") ^ f8h8);
			a8CastleAllowed = false;
			h8CastleAllowed = false;			
		}
        else if (!playerIsBlack && kingWasOnE && pieceMovedToC) {
        	long a1d1 = BitboardUtils.RANKS[0] & (BitboardUtils.FILES[0] | BitboardUtils.FILES[3]);
        	pieces.put("wR", pieces.get("wR") ^ a1d1);
			a1CastleAllowed = false;
			h1CastleAllowed = false;			
		}
        else if (!playerIsBlack && kingWasOnE && pieceMovedToG) {
        	long f1d1 = BitboardUtils.RANKS[0] & (BitboardUtils.FILES[5] | BitboardUtils.FILES[7]);
        	pieces.put("wR", pieces.get("wR") ^ f1d1);
        	a1CastleAllowed = false;
			h1CastleAllowed = false;			
		}
	}
	
	private void handleEnPassant(long prevEnemy, long currEnemy, long src, long dst) {
		if (isEnPassant(prevEnemy, currEnemy, src, dst)) {
			String enemyPawnType = src > dst ? "wP" : "bP";
			long oldPawnStructure = pieces.get(enemyPawnType);
			long deadPawn = src > dst ? dst << 8 : dst >>> 8;
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

	private void disableCastlingIfNecessary(long position) {
		long a1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[0];
		long a8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[0];
		long h1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[7];
		long h8 = BitboardUtils.RANKS[7] & BitboardUtils.FILES[7];
		long e1 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[0];
		long e8 = BitboardUtils.RANKS[0] & BitboardUtils.FILES[0];
		
        if (position == a1) {
        	a1CastleAllowed = false;
		}
		else if (position == a8) {
			a8CastleAllowed = false;
		}
		else if (position == h1) {
			h1CastleAllowed = false;
		}
		else if (position == h8) {
			h8CastleAllowed = false;
		}
		else if (position == e1) {
			a1CastleAllowed = false;
			h1CastleAllowed = false;
		}
		else if (position == e8) {
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

	public void promotePawn(String pieceCode) {
		char color = pieceCode.charAt(0);
		long rank = color == 'b' ? BitboardUtils.RANKS[0] : BitboardUtils.RANKS[7];
		String pawnCode = String.format("%sP", color);
		if (isPromotion) {
			long promotion = rank & pieces.get(String.format("%sP", color));
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

	public long getValidMoves(long position, Turn mostRecent) {
		long moves = 0L;
		PieceColor color = PieceColor.get(getPieceCode(position));
		PieceType type = PieceType.get(getPieceCode(position));
		switch (type) {
		case BISHOP:
			moves = getValidBishopMoves(position, color);
			break;
		case KNIGHT:
			moves = getValidKnightMoves(position, color);
			break;
		case KING:
			moves = getValidKingMoves(position, color);
			moves |= getAvailableCastles(position, color);
			break;
		case PAWN:
			moves = getValidPawnMoves(position, color);
			moves |= getEnPassant(position, color, mostRecent);
			break;
		case QUEEN:
			moves = getValidRookMoves(position, color) | getValidBishopMoves(position, color);
			break;
		case ROOK:
			moves = getValidRookMoves(position, color);
			break;
		}
		getAllAttacks(color);
		return moves;
	}
	
	private long getAvailableCastles(long position, PieceColor color) {
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
		if (PieceColor.WHITE.equals(color) && isEmpty(b1|c1|d1) && a1CastleAllowed) {
			if (!isUnderAttack(c1, PieceColor.BLACK) && !isUnderAttack(d1, PieceColor.BLACK) && !isUnderAttack(e1, PieceColor.BLACK)) {
				return c1;
			}
		}
		else if (PieceColor.WHITE.equals(color) && isEmpty(f1|g1) && h1CastleAllowed) {
			if (!isUnderAttack(e1, PieceColor.BLACK) && !isUnderAttack(f1, PieceColor.BLACK) && !isUnderAttack(g1, PieceColor.BLACK)) {
				return g1;
			}
		}
		else if (PieceColor.BLACK.equals(color)  && isEmpty(b8|c8|d8) && a8CastleAllowed) {
			if (!isUnderAttack(c8, PieceColor.WHITE) && !isUnderAttack(d8, PieceColor.WHITE) && !isUnderAttack(e8, PieceColor.WHITE)) {
				return c8;
			}
		}
		else if (PieceColor.BLACK.equals(color)  && isEmpty(f8|g8) && h8CastleAllowed) {
			if (!isUnderAttack(e8, PieceColor.WHITE) && !isUnderAttack(f8, PieceColor.WHITE) && !isUnderAttack(g8, PieceColor.WHITE)) {
				return g8;
			}
		}
		return 0L;
	}
	
	private boolean isUnderAttack(long position, PieceColor attacker) {
		InputValidation.validatePosition(position);
		return position == (position & getAllAttacks(attacker));
	}
	
	private long getAllAttacks(PieceColor attacker) {
		Set<Long> playerPieces = getIndividualPieces(attacker);
		long attacks = 0L;
		for (Long position : playerPieces) {
			PieceType type = PieceType.get(getPieceCode(position));
			switch (type) {
			case BISHOP:
				attacks |= getValidBishopMoves(position, attacker);
				break;
			case KNIGHT:
				attacks |= getValidKnightMoves(position, attacker);
				break;
			case KING:
				attacks |= getValidKingMoves(position, attacker);
				break;
			case PAWN:
				attacks |= getSquaresDefendedByPawn(position, attacker);
				break;
			case QUEEN:
				attacks |= getValidRookMoves(position, attacker) | getValidBishopMoves(position, attacker);
				break;
			case ROOK:
				attacks |= getValidRookMoves(position, attacker);
				break;
			}
		}
		System.out.println("\n" + attacker.toString() + " attacks:\n\n");
		BitboardUtils.printBinaryBoard(attacks);
		return attacks;
	}
	
	private long getSquaresDefendedByPawn(long position, PieceColor color) {
		long myRank = BitboardUtils.getRank(position);
		long nextRank = PieceColor.BLACK.equals(color) ? (myRank >>> 8) : (myRank << 8);
		return BitboardUtils.getAdjacentSquares(position) & nextRank;
	}
	
	private boolean isEmpty(long squares) {
		return (getAllPieces() & squares) == 0L; 
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

	private long getEnPassant(long position, PieceColor color, Turn mostRecent) {
		if (mostRecent == null) {
			return 0L;
		}
		
		long lastSrc = BitboardUtils.getPositionAsLong(mostRecent.getSrc());
		long lastDst = BitboardUtils.getPositionAsLong(mostRecent.getDst());
		long pawnStructure = pieces.get("bP") | pieces.get("wP");
		long enPassantRank = PieceColor.BLACK.equals(color) ? BitboardUtils.RANKS[3] : BitboardUtils.RANKS[4];
		
		boolean wereOnSameRank = ((position | lastDst) & enPassantRank) == (position | lastDst);
		boolean wereBothPawns = (lastDst | position) == ((lastDst | position) & (pawnStructure));
		boolean rankShiftIsTwo = (((lastSrc >>> 16) | (lastSrc << 16)) & lastDst) == lastDst;
		boolean fileIsAdjacent = (BitboardUtils.getAdjacentSquares(position) & lastDst) == lastDst;
		
		if (wereOnSameRank && wereBothPawns && rankShiftIsTwo && fileIsAdjacent) {
			return PieceColor.BLACK.equals(color) ? lastDst >>> 8 : lastDst << 8;
		}
		return 0L;
	}
	
	private long getValidKnightMoves(long position, PieceColor color) {
		long myPieces = PieceColor.BLACK.equals(color) ? getBlackPieces() : getWhitePieces();
		return BitboardUtils.getKnightSquares(position) & ~myPieces;
	}
	
	private long getValidKingMoves(long position, PieceColor color) {
		long myPieces = PieceColor.BLACK.equals(color) ? getBlackPieces() : getWhitePieces();
		return BitboardUtils.getAdjacentSquares(position) & ~myPieces;
	}

	private long getValidBishopMoves(long position, PieceColor color) {
		long enemyPieces = PieceColor.BLACK.equals(color) ? getWhitePieces() : getBlackPieces();
		long allPieces = getAllPieces();
		long moveset = 0L;
		long iterator;
		for (int i=1; i<8; i+=2) {
			iterator = position;
			while (BitboardUtils.hasNeighbor(iterator, i)) {
				iterator = BitboardUtils.getNeighbor(iterator, i);
				if ((iterator & allPieces) == iterator) {
					if ((iterator & enemyPieces) == iterator) {
						moveset |= iterator;
					}
					break;
				}
				moveset |= iterator;
			}
		}
		return moveset;
	}

	private long getValidRookMoves(long position, PieceColor color) {
		long enemyPieces = PieceColor.BLACK.equals(color) ? getWhitePieces() : getBlackPieces();
		long allPieces = getAllPieces();
		long moveset = 0L;
		long iterator;
		for (int i=0; i<8; i+=2) {
			iterator = position;
			while (BitboardUtils.hasNeighbor(iterator, i)) {
				iterator = BitboardUtils.getNeighbor(iterator, i);
				if ((iterator & allPieces) == iterator) {
					if ((iterator & enemyPieces) == iterator) {
						moveset |= iterator;
					}
					break;
				}
				moveset |= iterator;
			}
		}
		return moveset;
	}
	
	private long getValidPawnAttacksWithoutEnPassant(long position, PieceColor color) {
		long enemyPieces = PieceColor.BLACK.equals(color) ? getWhitePieces() : getBlackPieces();
		long next;
		switch (color) {
		case BLACK:
			next = BitboardUtils.getNeighbor(position, 4);
			return (enemyPieces & (BitboardUtils.getRank(position>>>8) & ((next << 1) | (next >>> 1))));
		case WHITE:
			next = BitboardUtils.getNeighbor(position, 0);
			return (enemyPieces & (BitboardUtils.getRank(position<<8) & ((next << 1) | (next >>> 1))));
		}
		return 0L;
	}
	
	private long getValidPawnMoves(long position, PieceColor color) {
		long allPieces = getAllPieces();
		long moveset = 0L;
		long next;

		switch (color) {
		case BLACK:
			next = BitboardUtils.getNeighbor(position, 4);
			if ((next & allPieces) == 0L) {
				moveset |= next;
				if ((position & BitboardUtils.RANKS[6]) == position && (BitboardUtils.getNeighbor(next, 4) & allPieces) == 0L) {
					moveset |= BitboardUtils.getNeighbor(next, 4);
				}
			}
			return moveset | getValidPawnAttacksWithoutEnPassant(position, color);
		case WHITE:
			next = BitboardUtils.getNeighbor(position, 0);
			if ((next & allPieces) == 0L) {
				moveset |= next;
				if ((position & BitboardUtils.RANKS[1]) == position && (BitboardUtils.getNeighbor(next, 0) & allPieces) == 0L) {
					moveset |= BitboardUtils.getNeighbor(next, 0);
				}
			}
			return moveset | getValidPawnAttacksWithoutEnPassant(position, color);
		}
		return 0L;
	}

}
