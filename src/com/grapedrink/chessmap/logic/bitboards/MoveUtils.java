package com.grapedrink.chessmap.logic.bitboards;

import com.grapedrink.chessmap.logic.history.Turn;

public class MoveUtils {

	private MoveUtils() {}
	
	/* does not include castling */
	public static long getValidMoves(long position, long myPieces, long allPieces, long pawnStructure, String pieceCode, Turn mostRecent) {
		int direction = pieceCode.charAt(0) == 'w' ? 0 : 4;
		PieceType type = PieceType.get(pieceCode);
		if (PieceType.PAWN.equals(type)) {
			return getPawnMoves(position, allPieces, myPieces, direction) | getEnPassant(position, pawnStructure, direction, mostRecent);
		}
		else {
			return getDefendedSquares(position, allPieces, pieceCode) & ~myPieces;
		}
	}
	
	private static long getEnPassant(long position, long pawnStructure, int direction, Turn mostRecent) {
		if (mostRecent == null) {
			return 0L;
		}
		long lastSrc = BitboardUtils.getPositionAsLong(mostRecent.getSrc());
		long lastDst = BitboardUtils.getPositionAsLong(mostRecent.getDst());
		long enPassantRank = direction == 0 ? BitboardUtils.RANKS[4] : BitboardUtils.RANKS[3];
		
		boolean wereOnSameRank = ((position | lastDst) & enPassantRank) == (position | lastDst);
		boolean wereBothPawns = (lastDst | position) == ((lastDst | position) & (pawnStructure));
		boolean rankShiftIsTwo = (((lastSrc >>> 16) | (lastSrc << 16)) & lastDst) == lastDst;
		boolean fileIsAdjacent = (BitboardUtils.getAdjacentSquares(position) & lastDst) == lastDst;
		
		if (wereOnSameRank && wereBothPawns && rankShiftIsTwo && fileIsAdjacent) {
			return direction == 0 ? lastDst << 8 : lastDst >>> 8;
		}
		return 0L;
	}
	
	/* Returns all pawn moves, not including en passant */
	private static long getPawnMoves(long position, long allPieces, long myPieces, int direction) {
		long moveset = 0L;
		long nextSquare = BitboardUtils.getNeighbor(position, direction);
		if ((nextSquare & allPieces) == 0L) {
			moveset |= nextSquare;
			long staringRank = direction == 0 ? BitboardUtils.RANKS[1] : BitboardUtils.RANKS[6];
			boolean pawnOnStartingRank = (staringRank & position) == position;
			boolean doubleJumpSquareIsFree = (BitboardUtils.getNeighbor(nextSquare, direction) & allPieces) == 0L;
			if (pawnOnStartingRank && doubleJumpSquareIsFree) {
				moveset |= BitboardUtils.getNeighbor(nextSquare, direction);
			}
		}
		
		long enemyPieces = allPieces ^ myPieces;
		long leftRight = BitboardUtils.getRank(position) & ((position << 1) | (position >>> 1));
		long myAttacks = direction == 0 ? (leftRight << 8) : (leftRight >>> 8);
		return moveset | (myAttacks & enemyPieces);
	}
	
	/**
	 * Returns all the squares currently defended by this piece.
	 * For a pawn, this doesnt include en passant, but it does
	 * show the left/right forward squares currently under attack.
	 * 
	 * For all other pieces, this function returns their available slides,
	 * and treats pieces of the same color as enemies (aka capturable)
	 * 
	 * @param position
	 * @param allPieces
	 * @param pieceCode
	 * @return
	 */
	public static long getDefendedSquares(long position, long allPieces, String pieceCode) {
		PieceType type = PieceType.get(pieceCode);
		int direction = pieceCode.charAt(0) == 'w' ? 0 : 4;
		switch (type) {
		case BISHOP:
			return MoveUtils.getBishopDefense(position, allPieces);
		case KNIGHT:
			return MoveUtils.getKnightDefense(position);
		case KING:
			return MoveUtils.getKingDefense(position);
		case PAWN:
			return MoveUtils.getPawnDefense(position, direction);
		case QUEEN:
			return MoveUtils.getBishopDefense(position, allPieces) | MoveUtils.getRookDefense(position, allPieces);
		case ROOK:
			return MoveUtils.getRookDefense(position, allPieces);
		default:
			return 0L;
		}
	}
	
	public static long getBishopDefense(long position, long allPieces) {
		return getRayDefense(position, allPieces, 1);
	}
	
	public static long getRookDefense(long position, long allPieces) {
		return getRayDefense(position, allPieces, 0);
	}

	public static long getKingDefense(long position) {
		return BitboardUtils.getAdjacentSquares(position);
	}

	public static long getKnightDefense(long position) {
		return BitboardUtils.getKnightSquares(position);
	}
	
	public static long getPawnDefense(long position, int direction) {
		long leftRightNeighbors = BitboardUtils.getRank(position) & BitboardUtils.getAdjacentSquares(position);
		return direction == 0 ? (leftRightNeighbors << 8) : (leftRightNeighbors >>> 8);
	}
	
	private static long getRayDefense(long position, long allPieces, int rayIndex) {
		long moveset = 0L;
		long iterator;
		for (int i=rayIndex; i<8; i+=2) {
			iterator = position;
			while (BitboardUtils.hasNeighbor(iterator, i)) {
				iterator = BitboardUtils.getNeighbor(iterator, i);
				moveset |= iterator;
				if ((iterator & allPieces) == iterator) {
					break;
				}
			}
		}
		return moveset;
	}
}
