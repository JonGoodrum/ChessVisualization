package com.grapedrink.chessmap.logic.utils;

import java.util.Map;
import java.util.Set;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;
import com.grapedrink.chessmap.logic.bitboards.PieceColor;

public class DefenseUtils {

	private DefenseUtils(){}

	/**
	 * Returns all the squares defended by the team at position.
	 * Returns 0L if there is no piece at position.
	 * 
	 * @param position
	 * @param pieces
	 * @return defended positions
	 */
	public static long getDefendedSquaresFriendlyTeam(long position, Map<String, Long> pieces) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		Set<Long> playerPieces = PieceUtils.getIndividualPieces(position, pieces);
		long attacks = 0L;
		for (Long pos : playerPieces) {
			attacks |= getDefendedSquares(pos, pieces);
		}
		return attacks;
	}
	
	/**
	 * Returns all the squares defended by the team that's not at position.
	 * Returns 0L if no piece is at position.
	 * 
	 * @param position
	 * @param pieces
	 * @return
	 */
	public static long getDefendedSquaresEnemyTeam(long position, Map<String, Long> pieces){
		switch (PieceUtils.getPieceColor(position, pieces)) {
		case BLACK:
			return getDefendedSquaresForColor(pieces, PieceColor.WHITE);
		case WHITE:
			return getDefendedSquaresForColor(pieces, PieceColor.BLACK);
		default:
			return 0L;
		}
	}
	
	
	/**
	 * Returns all the squares currently defended by this piece.
	 * 
	 * For kings and knights, this is the set of empty board moves.
	 * 
	 * For sliding pieces, this is the set of all available slides
	 * and treats friendly pieces as enemies (aka capturable).
	 *  
	 * For pawns, this doesn't include en passant, but it does
	 * show the left/right squares currently under attack.
	 * 
	 * This set does not remove inaccessible moves from pinned pieces
	 * 
	 * @param position
	 * @param allPieces
	 * @param pieceCode
	 * @return
	 */
	public static long getDefendedSquares(long position, Map<String, Long> pieces) {
		long allPieces = PieceUtils.getAllPieces(pieces);
		switch (PieceUtils.getPieceType(position, pieces)) {
		case BISHOP:
			return getBishopDefense(position, allPieces);
		case KNIGHT:
			return getKnightDefense(position);
		case KING:
			return getKingDefense(position);
		case PAWN:
			return getPawnDefense(position, PieceUtils.getPawnDirection(position, pieces));
		case QUEEN:
			return getQueenDefense(position, allPieces);
		case ROOK:
			return getRookDefense(position, allPieces);
		default:
			return 0L;
		}
	}
	
	/**
	 * Returns all the squares potentially defended by a bishop
	 * at this position.  This treats all pieces as capturable.
	 * 
	 * @param position
	 * @param allPieces
	 * @return
	 */
	public static long getBishopDefense(long position, long allPieces) {
		return getDefendedRay(position, allPieces, 1)
				| getDefendedRay(position, allPieces, 3)
				| getDefendedRay(position, allPieces, 5)
				| getDefendedRay(position, allPieces, 7);
	}
	
	/**
	 * Returns all the squares potentially defended by a queen
	 * at this position.  This treats all pieces as capturable.
	 * 
	 * @param position
	 * @param allPieces
	 * @return
	 */
	public static long getQueenDefense(long position, long allPieces) {
		return getRookDefense(position, allPieces) | getBishopDefense(position, allPieces);
	}
	
	/**
	 * Returns all the squares potentially defended by a rook
	 * at this position.  This treats all pieces as capturable.
	 * 
	 * @param position
	 * @param allPieces
	 * @return
	 */
	public static long getRookDefense(long position, long allPieces) {
		return getDefendedRay(position, allPieces, 0)
				| getDefendedRay(position, allPieces, 2)
				| getDefendedRay(position, allPieces, 4)
				| getDefendedRay(position, allPieces, 6);
	}
	
	/**
	 * Returns all the squares potentially defended by a king
	 * at this position.  This treats all pieces as capturable.
	 * 
	 * @param position
	 * @param allPieces
	 * @return
	 */
	public static long getKingDefense(long position) {
		return BoardUtils.getAdjacentSquares(position);
	}
	
	/**
	 * Returns all the squares potentially defended by a knight
	 * at this position.  This treats all pieces as capturable.
	 * 
	 * @param position
	 * @param allPieces
	 * @return
	 */
	public static long getKnightDefense(long position) {
		return BoardUtils.getKnightSquares(position);
	}
	
	/**
	 * Returns all the squares potentially defended by a pawn
	 * at this position.  This treats all pieces as capturable.
	 * 
	 * @param position
	 * @param allPieces
	 * @return
	 */
	public static long getPawnDefense(long position, int direction) {
		long leftRightNeighbors = BoardUtils.getRank(position) & BoardUtils.getAdjacentSquares(position);
		return direction == 0 ? (leftRightNeighbors << 8) : (leftRightNeighbors >>> 8);
	}
	
	/**
	 * Returns all the squares defended by a ray traveling in direction
	 * at this position.  This treats all pieces as capturable.
	 * 
	 * @param position
	 * @param allPieces
	 * @return
	 */
	private static long getDefendedRay(long position, long allPieces, int direction) {
		long ray = 0L;
		long iterator = position;
		while (BoardUtils.hasNeighboringSquare(iterator, direction)) {
			iterator = BoardUtils.getNeighboringSquare(iterator, direction);
			ray |= iterator;
			if ((iterator & allPieces) == iterator) {
				return ray;
			}
		}
		return ray;
	}

	/**
	 * Returns all squares defended by color.
	 * 
	 * @param pieces
	 * @param color
	 * @return
	 */
	public static long getDefendedSquaresForColor(Map<String, Long> pieces, PieceColor color) {
		switch (color) {
		case BLACK:
			return getDefendedSquaresFriendlyTeam(pieces.get("bK"), pieces);
		case WHITE:
			return getDefendedSquaresFriendlyTeam(pieces.get("wK"), pieces);
		default:
			return 0L;
		}
	}

	/**
	 * Returns true if all the squares represented
	 * by the long are defended by color
	 * 
	 * @param squares
	 * @param pieces
	 * @param color
	 * @return
	 */
	public static boolean isDefendedByColor(long squares, Map<String, Long> pieces, PieceColor color) {
		return squares == (squares & getDefendedSquaresForColor(pieces, color));
	}

}
