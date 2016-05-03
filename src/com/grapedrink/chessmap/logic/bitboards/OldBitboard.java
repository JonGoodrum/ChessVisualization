package com.grapedrink.chessmap.logic.bitboards;

import java.util.HashMap;
import java.util.Map;

public class OldBitboard {

    // possible single-bit values are 2^n, 0 <= n <= 63.
    // They are all represented by either a 1, 2, 4, or 8
    // if it's the only piece on it's halfrank.
    
    /*
     * To shift a rank: {a1 >>> fileOffset}
     *     ex: {a1 >>> 7} === h1
     *     
     * To shift a file: {a1 << 8*fileOffset}
     *     ex: {a1 << 32} === a5
     */
    private static long a1    = 0x0000000000000080L;
    
    /*
     * Ranks 2-8 === {rank1 << 8*(rank-1)}
     * ex: seven = rank1 << 48
     */
    private static long rank1 = 0x00000000000000FFL;
    
    private static long universe  = 0xFFFFFFFFFFFFFFFFL;
    private static long nullverse = 0x0000000000000000L;
    
    /*
     * Files b-h === {afile >>> (fileOffset)}
     * ex: h-file = afile >>> 7
     */
    private static long afile = 0x8080808080808080L;

    private static long getBoard() {
        return wpcs() | bpcs();
    }
    
    private static long wpcs() {
        return wB() | wK() | wN() | wP() | wQ() | wR();
    }
    
    private static long bpcs() {
        return bB() | bK() | bN() | bP() | bQ() | bR();
    }
    
    private static void initialize() {
        pieces[0] = 0x2400000000000000L;
        pieces[1] = 0x0800000000000000L;
        pieces[2] = 0x4200000000000000L;
        pieces[3] = 0x00FF000000000000L;
        pieces[4] = 0x1000000000000000L;
        pieces[5] = 0x8100000000000000L;
        pieces[6] = 0x0000000000000024L;
        pieces[7] = 0x0000000000000008L;
        pieces[8] = 0x0000000000000042L;
        pieces[9] = 0x000000000000FF00L;
        pieces[10] = 0x0000000000000010L;
        pieces[11] = 0x0000000000000081L;
    }

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
	
	
    private static long bB() { return pieces[0];}
    private static long bK() { return pieces[1];}
    private static long bN() { return pieces[2];}
    private static long bP() { return pieces[3];}
    private static long bQ() { return pieces[4];}
    private static long bR() { return pieces[5];}
    private static long wB() { return pieces[6];}
    private static long wK() { return pieces[7];}
    private static long wN() { return pieces[8];}
    private static long wP() { return pieces[9];}
    private static long wQ() { return pieces[10];}
    private static long wR() { return pieces[11];}
    
    private static long[] pieces = {0,0,0,0,0,0,0,0,0,0,0,0};
    
    private static long getSquare(String square) {
        int fileOffset = square.charAt(0) - 97;
        int rankOffset = (square.charAt(1) - 49) * 8;
        return a1 << rankOffset >>> fileOffset;
    }
    
    private static void move(String source, String destination) {
        long src = getSquare(source);
        long dst = getSquare(destination);
        

        for (int i=0; i<pieces.length; ++i) {
            // find bitboard containing src
            if ((src & pieces[i]) != nullverse) {
                // remove src, add dst
                pieces[i] ^= src;
                
                // remove dst from everything
                for (long k : pieces) {
                    pieces[i] &= ~dst;
                }
                
                //add dst to this
                pieces[i] |= dst;
                break;
            }
        }
        
        
    }
    
    private static String longToByteString(long value) {
        String replacement = "•";
        return String.format("%64s", Long.toBinaryString(value)).replace(" ","0").replace("0", replacement);
    }

    private static void printLong(long value) {
        StringBuilder board = new StringBuilder();
        String byteString = longToByteString(value);
        
        for (int i = 0; i < 64; ++i) {
            board.append(byteString.charAt(i));
            if (i%8==7) {
                board.append("\n");
            }
        }
        System.out.println(board.toString());
    }

	private static long getRank(long position) {
		long currentRank;
    	for (int rank=1; rank <=8; ++rank) {
    		currentRank = RANKS_AND_FILES.get(""+rank);
    		if ((currentRank & position) == position) {
    			return currentRank;
    		}
    	}
		return 0L;
	}

	private static long getFile(long position) {
		long currentFile;
    	for (char file='a'; file <= 'h'; ++file) {
    		currentFile = RANKS_AND_FILES.get(file);
    		if ((currentFile & position) == position) {
    			return currentFile;
    		}
    	}
		return 0L;
	}
	
	private static long getNeighboringSquares(long position) {
		long myRank = getRank(position);
		long myRow = ((position << 1) & myRank) | position | ((position >> 1) & myRank);
		return position ^ ((myRow << 8) | myRow | (myRow >>> 8));
	}

	private static long UP_DIAGONAL = 0x0102040810204080L;
	private static long DOWN_DIAGONAL = 0x8040201008040201L;

	private static long FILLED_DOWN_DIAGONAL = 0x0080C0E0F0F8FCFEL;

	private static long FILLED_UP_DIAGONAL   = 0x000103070F1F3F7FL;

	private static long getUpDiagonal(long position) {
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

	private static long getDownDiagonal(long position) {
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
	
	private static long getPawnMoves(String pieceCode, long position, long opponents) {
		char playerTurn = pieceCode.charAt(0);
		long moveset;
		long rank;
		long attacks;
		switch (playerTurn) {
		case 'w':
			moveset = position << 8;
			if ((position & 65280L) == position) {
				moveset |= position << 16;
			}
			rank = getRank(position);
			attacks = (((position << 1) & rank) | ((position >>> 1) & rank)) << 8;
			return moveset | (attacks & opponents);
		case 'b':
			moveset = position >>> 8;
			if((position & 0xFF000000000000L) == position) {
				moveset |= position >>> 16;
			}
			rank = getRank(position);
			attacks = (((position << 1) & rank) | ((position >>> 1) & rank)) >>> 8;
			return moveset | (attacks & opponents);
		default:
			return 0L;
		}
	}
	
	private static long getKnightMoves(long position) {
		long rank = getRank(position);
		long oneaway = ((position << 1) & rank) | ((position >>> 1) & rank);
		long twoaway = ((position << 2) & rank) | ((position >>> 2) & rank);
		return (oneaway << 16) | (oneaway >>> 16) | (twoaway << 8) | (twoaway >>> 8);
	}
	
    public static void main(String... args) {
    	
    	String lolz = "h1";
    	long eight = 8L;
    	//eight <<= 4L;
    	
    	printLong(2251799813685248L);
    }
}
