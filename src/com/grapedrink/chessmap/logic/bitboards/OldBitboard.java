package com.grapedrink.chessmap.logic.bitboards;

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

    public static void main(String... args) {
  //      initialize();
//        move("e2", "e4");
    	
    	long afile = 0x8080808080808080L;
    	
    	//printLong(afile >>> i);
        
        for(int i=0; i<8; ++i) {
            System.out.println(afile >>> i);
        }
        
        //int num = '1'-48;
        //System.out.println(num);
    }
}
