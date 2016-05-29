package com.grapedrink.chessmap.logic.utils;

import java.util.HashSet;
import java.util.Set;

import com.grapedrink.chessmap.logic.bitboards.InputValidation;

public class ConvertUtils {

	private ConvertUtils() {}

	/**
	 * Converts the String representation of a square,
	 * such as "c2", into a long, such as 8192L.
	 * 
	 * @param position position
	 * @return position as long
	 * @throws IllegalArgumentException
	 */
	public static long getPositionAsLong(String position) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		return BoardConstants.RANKS[position.charAt(1)-49] & BoardConstants.FILES[position.charAt(0)-97];
	}


	/**
	 * Converts the long representation of a square,
	 * such as 8192L, into a String, such as "c2".
	 * 
	 * @param position
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static String getPositionAsString(long position) throws IllegalArgumentException {
		InputValidation.validatePosition(position);
		int rank = 0;
		int file = 0;
		for (int i=0; i<BoardConstants.RANKS.length; ++i) {
			if ((BoardConstants.RANKS[i] & position) != 0L) {
				rank = i;
				break;
			}
		}
		for (int i=0; i<BoardConstants.FILES.length; ++i) {
			if ((BoardConstants.FILES[i] & position) != 0L) {
				file = i;
				break;
			}
		}
		return String.format("%s%d", (char)(file+97), rank+1); 
	}

	
	/**
	 * Converts a long into the squares it represents.
	 * For instance, 0xDL would return a Set containing
	 * "e1", "f1", and "h1".
	 * 
	 * @param positions
	 * @return
	 */
	public static Set<String> getPositionsAsStrings(long positions) {
		Set<String> set = new HashSet<>();
		long pos;
		for (int i=0; i<64; ++i) {
			pos = 1L << i;
			if ((positions & pos) == pos) {
				set.add(getPositionAsString(pos));
			}
		}
		return set;
	}
	
	
	/**
	 * Converts a long into a series of longs, one for each
	 * square represented by this long.  For instance, 0xDL
	 * would return a Set containing 0x8L, 0x4L and 0x1L
	 * 
	 * @param positions
	 * @return
	 */
	public static Set<Long> getPositionsAsLongs(long positions) {
		Set<Long> set = new HashSet<Long>();
		for (String pos : getPositionsAsStrings(positions)) {
			set.add(getPositionAsLong(pos));
		}
		return set;
	}
	
}
