package tst.com.grapedrink.chessmap.logic.bitboards;

import static org.junit.Assert.*;

import org.junit.Test;

import com.grapedrink.chessmap.logic.utils.BoardConstants;
import com.grapedrink.chessmap.logic.utils.BoardUtils;
import com.grapedrink.chessmap.logic.utils.ConvertUtils;

public class BitboardUtilsTest {
	
	private static final long a1Long = 0x0000000000000080L;
	private static final long a8Long = 0x8000000000000000L;
	private static final long h1Long = 0x0000000000000001L;
	private static final long h8Long = 0x0100000000000000L;
	private static final String a1String = "a1";
	private static final String a8String = "a8";
	private static final String h1String = "h1";
	private static final String h8String = "h8";
	
	@Test
	public void test_getPositionAsLong() {
		assertEquals(a1Long, ConvertUtils.getPositionAsLong(a1String));
		assertEquals(a8Long, ConvertUtils.getPositionAsLong(a8String));
		assertEquals(h1Long, ConvertUtils.getPositionAsLong(h1String));
		assertEquals(h8Long, ConvertUtils.getPositionAsLong(h8String));
		try {
			ConvertUtils.getPositionAsLong(null);
			fail("Exception not caught with invalid input");
		}
		catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void test_getPositionAsString() {
		assertEquals(a1String, ConvertUtils.getPositionAsString(a1Long));
		assertEquals(a8String, ConvertUtils.getPositionAsString(a8Long));
		assertEquals(h1String, ConvertUtils.getPositionAsString(h1Long));
		assertEquals(h8String, ConvertUtils.getPositionAsString(h8Long));
		try {
			ConvertUtils.getPositionAsString(11L);
			fail("Exception not caught with invalid input");
		}
		catch (IllegalArgumentException e) {
		}
	}

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
}
