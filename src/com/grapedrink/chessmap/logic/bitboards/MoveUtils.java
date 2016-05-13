package com.grapedrink.chessmap.logic.bitboards;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.grapedrink.chessmap.logic.history.Turn;

public class MoveUtils {

	private MoveUtils() {}
	
	/* does not include castling */
	public static long getValidMoves(long position, Map<String, Long> pieces, Turn mostRecent) {
		long myPieces = PieceUtils.getFriendlyPieces(position, pieces);
		PieceType type = PieceUtils.getPieceType(position, pieces);
		
		long moves;
		if (PieceType.KING.equals(type)) {
			return getKingMoves(position, pieces);
		}
		else if (PieceType.PAWN.equals(type)) {
			moves = (getPawnMoves(position, pieces) | getEnPassant(position, pieces, mostRecent));
		}
		else {
			moves = getMyDefendedSquares(position, pieces) & ~myPieces;
		}
		long friendlyKing = PieceUtils.getFriendlyKing(position, pieces);
		System.out.println("isPinned: " + isPinned(position, pieces));
		return isPinned(position, pieces) ? moves & BitboardUtils.getConnectingRay(position, friendlyKing) : moves;
	}
	
	/**
	 * Returns all the squares defended by the team at position.
	 * 
	 * @param position
	 * @param pieces
	 * @return
	 */
	public static long getFriendlyTeamDefendedSquares(long position, Map<String, Long> pieces) {
		Set<Long> playerPieces = PieceUtils.getIndividualPieces(position, pieces);
		long attacks = 0L;
		for (Long pos : playerPieces) {
			attacks |= getMyDefendedSquares(pos, pieces);
		}
		return attacks;
	}
	
	/**
	 * Returns all the squares defended by PieceColor color.
	 *  
	 * @param position
	 * @param pieces
	 * @param color
	 * @return
	 */
	public static long getAllDefendedSquares(Map<String, Long> pieces, PieceColor color) {
		long position = PieceColor.BLACK.equals(color) ? pieces.get("bK") : pieces.get("wK");
		return getFriendlyTeamDefendedSquares(position, pieces);
	}
	
	public static boolean isInCheck(long teammate, Map<String, Long> pieces) {
		long king = PieceUtils.getFriendlyKing(teammate, pieces);
		return king == (king & getEnemyTeamDefendedSquares(king, pieces));
	}

	/*
	public static boolean kingCannotEscapeCheck(long king, Map<String, Long> pieces) {
		long possibleMoves = getKingMoves(king, pieces) & ~getAllEnemyDefendedSquares(king, pieces);
	}
	*/
	
	public static long getEnemyTeamDefendedSquares(long friendlyPiece, Map<String, Long> pieces) {
		long position = PieceUtils.isBlack(friendlyPiece, pieces) ? pieces.get("wK") : pieces.get("bK");
		return getFriendlyTeamDefendedSquares(position, pieces);
	}

	private static long getKingMoves(long position, Map<String, Long> pieces) {
		long enemyKing = PieceUtils.getEnemyKing(position, pieces);
		long nonAttackedSquares = ~(getFriendlyTeamDefendedSquares(enemyKing, pieces));
		return BitboardUtils.getAdjacentSquares(position) & ~PieceUtils.getFriendlyPieces(position, pieces) & nonAttackedSquares;
	}
	
	public static boolean isPinned(long position, Map<String, Long> pieces) {
		long myKing = PieceUtils.getFriendlyKing(position, pieces);
		
		boolean pieceIsKing = position == myKing;
		boolean notInLineWithKing = (BitboardUtils.getRaystar(position) & myKing) == 0L;
		if (pieceIsKing || notInLineWithKing) {
			System.out.println("exit 1");
			return false;
		}
		
		long iterator = myKing;
		long allPieces = PieceUtils.getAllPieces(pieces);
		int direction = getConnectingDirection(myKing, position);
		while ((iterator = BitboardUtils.getNeighbor(iterator, direction)) != position) {
			if ((iterator & allPieces) == iterator) {
				System.out.println("exit 2");
				return false;
			}
		}
		
		PieceColor enemyColor = PieceUtils.isBlack(position, pieces) ? PieceColor.WHITE : PieceColor.BLACK;
		long attackersThatCanPin = PieceUtils.getSlidingPieces(pieces, enemyColor);
		while (BitboardUtils.hasNeighbor(iterator, direction)) {
			iterator = BitboardUtils.getNeighbor(iterator, direction);
			if ((iterator & attackersThatCanPin) == iterator) {
				System.out.println("exit 3");
				return (getMyDefendedSquares(iterator, pieces) & position) != 0L;
			}
		}
		System.out.println("exit 4");
		return false;
	}
	
	/* assumes pieces are connected */
	private static int getConnectingDirection(long src, long dst) {
		if ((BitboardUtils.getRank(src) & dst) == dst) {
			return src > dst ? 2 : 6;
		}
		else if ((BitboardUtils.getFile(src) & dst) == dst) {
			return src < dst ? 0 : 4;
		}
		else if ((BitboardUtils.getDiagonal(src) & dst) == dst) {
			return src < dst ? 1 : 5;
		}
		return src > dst ? 3 : 7;
	}
	
	private static long getEnPassant(long position, Map<String, Long> pieces, Turn mostRecent) {
		if (mostRecent == null || PieceUtils.isPieceCode(mostRecent.getSrc())) {
			return 0L;
		}
		int direction = PieceUtils.getPawnDirection(position, pieces);
		long pawnStructure = PieceUtils.getPawnStructure(pieces);
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
	private static long getPawnMoves(long position, Map<String, Long> pieces) {
		long allPieces = PieceUtils.getAllPieces(pieces);
		int direction = PieceUtils.getPawnDirection(position, pieces);
		
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
		
		long leftRight = BitboardUtils.getRank(position) & ((position << 1) | (position >>> 1));
		long myAttacks = direction == 0 ? (leftRight << 8) : (leftRight >>> 8);
		long enemyPieces = PieceUtils.getEnemyPieces(position, pieces);
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
	public static long getMyDefendedSquares(long position, Map<String, Long> pieces) {
		long allPieces = PieceUtils.getAllPieces(pieces);
		switch (PieceUtils.getPieceType(position, pieces)) {
		case BISHOP:
			return MoveUtils.getBishopDefense(position, allPieces);
		case KNIGHT:
			return MoveUtils.getKnightDefense(position);
		case KING:
			return MoveUtils.getKingDefense(position);
		case PAWN:
			return MoveUtils.getPawnDefense(position, PieceUtils.getPawnDirection(position, pieces));
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

	public static long getCheckBlockingMoves(long position, final Map<String, Long> pieces) {
		long moves = 0L;
		Map<String, Long> copyOfPieces = new HashMap<>();
		for (String key : pieces.keySet()) {
			copyOfPieces.put(key, pieces.get(key));
		}
		String myPieceCode = PieceUtils.getPieceCode(position, copyOfPieces);
		final long myPieceTypeWithoutMe = pieces.get(myPieceCode) ^ position;
		System.out.println("*******************************************");

		System.out.println("my board without me: ");
		BitboardUtils.printBinaryBoard(myPieceTypeWithoutMe);
		
		System.out.println("number of available moves (without check): " + BitboardUtils.getPositionsAsLongs(getValidMoves(position, copyOfPieces, null)).size());
		
		long removedEnemy;
		boolean enemyWasRemoved;
		String enemyName = null;
		
		for (long potentialMove : BitboardUtils.getPositionsAsLongs(getValidMoves(position, copyOfPieces, null))) {
			
			System.out.println("potential move:");
			BitboardUtils.printBinaryBoard(potentialMove);
			
            removedEnemy = 0L;
            enemyWasRemoved = false;
			
			// handle captures.  If I capture a piece it needs to be removed TODO
			for (String pc : copyOfPieces.keySet()) {
				if ((copyOfPieces.get(pc) & potentialMove) != 0L) {
					enemyWasRemoved = true;
					removedEnemy = potentialMove;
					enemyName = pc;
					copyOfPieces.put(enemyName, copyOfPieces.get(enemyName) ^ removedEnemy);
				}
			}
			copyOfPieces.put(myPieceCode, myPieceTypeWithoutMe | potentialMove);
			
			
			
			System.out.println("isInCheck here? " + isInCheck(potentialMove, copyOfPieces));
			if (!isInCheck(potentialMove, copyOfPieces)) {
				moves |= potentialMove;
			}
			if (enemyWasRemoved) {
				copyOfPieces.put(enemyName, copyOfPieces.get(enemyName) | removedEnemy);
			}
			
			System.out.println("*******************************************");
			
			
		}
		return moves;
	}
	
	
}
