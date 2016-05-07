package tst.com.grapedrink.chessmap.logic.bitboards;

import static org.junit.Assert.*;

import org.junit.Test;

import com.grapedrink.chessmap.logic.bitboards.BitboardUtils;

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
		assertEquals(a1Long, BitboardUtils.getPositionAsLong(a1String));
		assertEquals(a8Long, BitboardUtils.getPositionAsLong(a8String));
		assertEquals(h1Long, BitboardUtils.getPositionAsLong(h1String));
		assertEquals(h8Long, BitboardUtils.getPositionAsLong(h8String));
		try {
			BitboardUtils.getPositionAsLong(null);
			fail("Exception not caught with invalid input");
		}
		catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void test_getPositionAsString() {
		assertEquals(a1String, BitboardUtils.getPositionAsString(a1Long));
		assertEquals(a8String, BitboardUtils.getPositionAsString(a8Long));
		assertEquals(h1String, BitboardUtils.getPositionAsString(h1Long));
		assertEquals(h8String, BitboardUtils.getPositionAsString(h8Long));
		try {
			BitboardUtils.getPositionAsString(11L);
			fail("Exception not caught with invalid input");
		}
		catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void test_getRank() {
		assertEquals(BitboardUtils.RANKS[0], BitboardUtils.getRank(a1Long));
		assertEquals(BitboardUtils.RANKS[0], BitboardUtils.getRank(h1Long));
		assertEquals(BitboardUtils.RANKS[7], BitboardUtils.getRank(a8Long));
		assertEquals(BitboardUtils.RANKS[7], BitboardUtils.getRank(h8Long));
	}
	
	@Test
	public void test_getFile() {
		assertEquals(BitboardUtils.FILES[0], BitboardUtils.getFile(a1Long));
		assertEquals(BitboardUtils.FILES[0], BitboardUtils.getFile(a8Long));
		assertEquals(BitboardUtils.FILES[7], BitboardUtils.getFile(h1Long));
		assertEquals(BitboardUtils.FILES[7], BitboardUtils.getFile(h8Long));
	}
}
