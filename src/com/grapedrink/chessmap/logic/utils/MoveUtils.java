package com.grapedrink.chessmap.logic.utils;

import java.util.HashMap;
import java.util.Map;

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
			return getValidKingMoves(position, pieces);
		}
		else if (PieceType.PAWN.equals(type)) {
			moves = (getPawnMovesWithoutEnPassant(position, pieces) | getEnPassant(position, pieces, mostRecent));
		}
		else {
			moves = DefenseUtils.getDefendedSquares(position, pieces) & ~myPieces;
		}
		long friendlyKing = PieceUtils.getFriendlyKing(position, pieces);
		return GameUtils.isPinned(position, pieces) ? moves & BoardUtils.getConnectingRay(position, friendlyKing) : moves;
	}
	
	private static long getValidKingMoves(long position, Map<String, Long> pieces) {
		return BoardUtils.getAdjacentSquares(position)
				& ~DefenseUtils.getDefendedSquaresEnemyTeam(position, pieces)
		        & ~PieceUtils.getFriendlyPieces(position, pieces);
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

	private static long getPawnMovesWithoutEnPassant(long position, Map<String, Long> pieces) {
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
