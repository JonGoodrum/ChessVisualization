package tst.com.grapedrink.chessmap.logic.bitboards;

import static org.junit.Assert.*;

import org.junit.Test;

import com.grapedrink.chessmap.logic.utils.BoardConstants;
import com.grapedrink.chessmap.logic.utils.BoardUtils;

public class BoardUtilsTest {
	
	private static final long a1Long = 0x0000000000000080L;
	private static final long a8Long = 0x8000000000000000L;
	private static final long h1Long = 0x0000000000000001L;
	private static final long h8Long = 0x0100000000000000L;

	@Test
	public void test_getRank() {
		assertEquals(BoardConstants.RANKS[0], BoardUtils.getRank(a1Long));
		assertEquals(BoardConstants.RANKS[0], BoardUtils.getRank(h1Long));
		assertEquals(BoardConstants.RANKS[7], BoardUtils.getRank(a8Long));
		assertEquals(BoardConstants.RANKS[7], BoardUtils.getRank(h8Long));
	}
	
	@Test
	public void test_getFile() {
		assertEquals(BoardConstants.FILES[0], BoardUtils.getFile(a1Long));
		assertEquals(BoardConstants.FILES[0], BoardUtils.getFile(a8Long));
		assertEquals(BoardConstants.FILES[7], BoardUtils.getFile(h1Long));
		assertEquals(BoardConstants.FILES[7], BoardUtils.getFile(h8Long));
	}
	
	@Test
	public void test_hasNeighboringSquare() {
		// verify that non-border squares have all 8 neighbors
		long position;
		for (int square=0; square<55; ++square) {
			position = 1L << square;
			if (((position) & BoardConstants.BORDER) == 0L) {
				for (int direction=0; direction<8; ++direction) {
				    assertTrue(BoardUtils.hasNeighboringSquare(position, direction));
				}
			}
		}
		
		// verify that
	}
	
}
