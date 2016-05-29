package com.grapedrink.chessmap.logic.utils;

public class BoardConstants {

	private BoardConstants() {}
	
	public static final long UNIVERSE    = 0xFFFFFFFFFFFFFFFFL;
	public static final long BORDER      = 0xFF818181818181FFL;
	public static final long L_BORDER    = 0x8080808080808080L;
	public static final long R_BORDER    = 0x0101010101010101L;
	public static final long T_BORDER    = 0xFF00000000000000L;
	public static final long B_BORDER    = 0x00000000000000FFL;
	public static final long BL_BORDER   = 0x80808080808080FFL;
	public static final long BR_BORDER   = 0x01010101010101FFL;
	public static final long TL_BORDER   = 0xFF80808080808080L;
	public static final long TR_BORDER   = 0xFF01010101010101L;
	
	/**
	 * The upward diagonals, arranged from left to right.
	 * DIAGONALS[0] passes through a8,
	 * DIAGONALS[8] passes through a1 and h8,
	 * and DIAGONALS[15] passes through h1.
	 */
	public static final long[] DIAGONALS = {
		0x8000000000000000L,
		0x4080000000000000L,
		0x2040800000000000L,
		0x1020408000000000L,
		0x0810204080000000L,
		0x0408804020100000L,
		0x0204081020408000L,
		0x0102040810204080L,
		0x0001020408102040L,
		0x0000010204081020L,
		0x0000000102040810L,
		0x0000000001020408L,
		0x0000000000010204L,
		0x0000000000000102L,
		0x0000000000000001L,
	};
	
	/**
	 * The downward anti-diagonals, arranged from left to right.
	 * ANTI_DIAGONALS[0] passes through a1,
	 * ANTI_DIAGONALS[8] passes through a8 and h1,
	 * and ANTI_DIAGONALS[15] passes through h8.
	 */
	public static final long[] ANTI_DIAGONALS = {
		0x0000000000000080L,
		0x0000000000008040L,
		0x0000000000804020L,
		0x0000000080402010L,
		0x0000008040201008L,
		0x0000804020100804L,
		0x0080402010080402L,
		0x8040201008040201L,
		0x0040201008040201L,
		0x0000201008040201L,
		0x0000001008040201L,
		0x0000000008040201L,
		0x0000000000040201L,
		0x0000000000000201L,
		0x0000000000000001L,
	};
	
	/**
	 * The ranks, with
	 * RANKS[0] representing rank 1
	 * and RANKS[7] representing rank 8.
	 */
	public static final long[] RANKS = {
		0x00000000000000FFL,
		0x000000000000FF00L,
		0x0000000000FF0000L,
		0x00000000FF000000L,
		0x000000FF00000000L,
		0x0000FF0000000000L,
		0x00FF000000000000L,
		0xFF00000000000000L,
	};
	
	/**
	 * The files, with
	 * FILES[0] representing the a-file
	 * and FILES[7] representing the h-file.
	 */
	public static final long[] FILES = {
		0x8080808080808080L,
		0x4040404040404040L,
		0x2020202020202020L,
		0x1010101010101010L,
		0x0808080808080808L,
		0x0404040404040404L,
		0x0202020202020202L,
		0x0101010101010101L,
	};
}
