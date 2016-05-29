package com.grapedrink.chessmap.logic.utils;

import java.util.HashMap;
import java.util.Map;

import com.grapedrink.chessmap.logic.bitboards.PieceColor;
import com.grapedrink.chessmap.logic.bitboards.PieceType;
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
			moves = DefenseUtils.getDefendedSquares(position, pieces) & ~myPieces;
		}
		long friendlyKing = PieceUtils.getFriendlyKing(position, pieces);
		System.out.println("isPinned: " + isPinned(position, pieces));
		return isPinned(position, pieces) ? moves & BoardUtils.getConnectingRay(position, friendlyKing) : moves;
	}
	
	private static long getKingMoves(long position, Map<String, Long> pieces) {
		long enemyKing = PieceUtils.getEnemyKing(position, pieces);
		long nonAttackedSquares = ~(DefenseUtils.getDefendedSquares(enemyKing, pieces));
		return BoardUtils.getAdjacentSquares(position) & ~PieceUtils.getFriendlyPieces(position, pieces) & nonAttackedSquares;
	}
	
	public static boolean isPinned(long position, Map<String, Long> pieces) {
		long myKing = PieceUtils.getFriendlyKing(position, pieces);
		
		boolean pieceIsKing = position == myKing;
		boolean notInLineWithKing = (BoardUtils.getRaystar(position) & myKing) == 0L;
		if (pieceIsKing || notInLineWithKing) {
			return false;
		}
		
		long iterator = myKing;
		long allPieces = PieceUtils.getAllPieces(pieces);
		int direction = getConnectingDirection(myKing, position);
		while ((iterator = BoardUtils.getNeighboringSquare(iterator, direction)) != position) {
			if ((iterator & allPieces) == iterator) {
				return false;
			}
		}
		
		PieceColor enemyColor = PieceUtils.isBlack(position, pieces) ? PieceColor.WHITE : PieceColor.BLACK;
		long attackersThatCanPin = PieceUtils.getSlidingPieces(pieces, enemyColor);
		while (BoardUtils.hasNeighboringSquare(iterator, direction)) {
			iterator = BoardUtils.getNeighboringSquare(iterator, direction);
			if ((iterator & attackersThatCanPin) == iterator) {
				return (DefenseUtils.getDefendedSquares(iterator, pieces) & position) != 0L;
			}
		}
		System.out.println("exit 4");
		return false;
	}
	
	/* assumes pieces are connected */
	private static int getConnectingDirection(long src, long dst) {
		if ((BoardUtils.getRank(src) & dst) == dst) {
			return src > dst ? 2 : 6;
		}
		else if ((BoardUtils.getFile(src) & dst) == dst) {
			return src < dst ? 0 : 4;
		}
		else if ((BoardUtils.getDiagonal(src) & dst) == dst) {
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
		long lastSrc = ConvertUtils.getPositionAsLong(mostRecent.getSrc());
		long lastDst = ConvertUtils.getPositionAsLong(mostRecent.getDst());
		long enPassantRank = direction == 0 ? BoardConstants.RANKS[4] : BoardConstants.RANKS[3];
		
		boolean wereOnSameRank = ((position | lastDst) & enPassantRank) == (position | lastDst);
		boolean wereBothPawns = (lastDst | position) == ((lastDst | position) & (pawnStructure));
		boolean rankShiftIsTwo = (((lastSrc >>> 16) | (lastSrc << 16)) & lastDst) == lastDst;
		boolean fileIsAdjacent = (BoardUtils.getAdjacentSquares(position) & lastDst) == lastDst;
		
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
		
		long nextSquare = BoardUtils.getNeighboringSquare(position, direction);
		if ((nextSquare & allPieces) == 0L) {
			moveset |= nextSquare;
			long staringRank = direction == 0 ? BoardConstants.RANKS[1] : BoardConstants.RANKS[6];
			boolean pawnOnStartingRank = (staringRank & position) == position;
			boolean doubleJumpSquareIsFree = (BoardUtils.getNeighboringSquare(nextSquare, direction) & allPieces) == 0L;
			if (pawnOnStartingRank && doubleJumpSquareIsFree) {
				moveset |= BoardUtils.getNeighboringSquare(nextSquare, direction);
			}
		}
		
		long leftRight = BoardUtils.getRank(position) & ((position << 1) | (position >>> 1));
		long myAttacks = direction == 0 ? (leftRight << 8) : (leftRight >>> 8);
		long enemyPieces = PieceUtils.getEnemyPieces(position, pieces);
		return moveset | (myAttacks & enemyPieces);
	}

	public static long getCheckBlockingMoves(long position, final Map<String, Long> pieces) {
		long moves = 0L;
		Map<String, Long> copyOfPieces = new HashMap<>();
		for (String key : pieces.keySet()) {
			copyOfPieces.put(key, pieces.get(key));
		}
		String myPieceCode = PieceUtils.getPieceCode(position, copyOfPieces);
		final long myPieceTypeWithoutMe = pieces.get(myPieceCode) ^ position;
		
		long removedEnemy;
		boolean enemyWasRemoved;
		String enemyName = null;
		
		for (long potentialMove : ConvertUtils.getPositionsAsLongs(getValidMoves(position, copyOfPieces, null))) {
			
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
			
			if (!GameUtils.isInCheck(potentialMove, copyOfPieces)) {
				moves |= potentialMove;
			}
			if (enemyWasRemoved) {
				copyOfPieces.put(enemyName, copyOfPieces.get(enemyName) | removedEnemy);
			}
		}
		return moves;
	}
	
	
}
