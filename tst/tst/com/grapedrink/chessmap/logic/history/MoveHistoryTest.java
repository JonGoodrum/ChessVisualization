package tst.com.grapedrink.chessmap.logic.history;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.grapedrink.chessmap.logic.history.MoveHistory;

public class MoveHistoryTest {

	private MoveHistory getRuyLopez() {
		MoveHistory ruyLopez = new MoveHistory();
		ruyLopez.addMove("e2", "e4");
		ruyLopez.addMove("e7", "e5");
		ruyLopez.addMove("g1", "f3");
		ruyLopez.addMove("b8", "c6");
		ruyLopez.addMove("f1", "b5");
		return ruyLopez;
	}
	
	@Test
	public void test_isBlacksTurn() throws IllegalAccessException {
		assertFalse(new MoveHistory().isBlacksTurn());
		MoveHistory game = getRuyLopez();
		assertTrue(game.isBlacksTurn());
		game.getPrev();
		game.getPrev();
		game.getPrev();
		assertFalse(game.isBlacksTurn());
	}
	
	@Test
	public void test_getMoveCount() throws IllegalAccessException {
		MoveHistory game = getRuyLopez();
		assertEquals(5, game.getMoveCount());
		game.getPrev();
		assertEquals(4, game.getMoveCount());
		game.getPrev();
		assertEquals(3, game.getMoveCount());
		game.getPrev();
		assertEquals(2, game.getMoveCount());
		game.getNext();
		assertEquals(3, game.getMoveCount());
		game.getNext();
		assertEquals(4, game.getMoveCount());
		game.getNext();
		assertEquals(5, game.getMoveCount());
	}
	
	@Test
	public void test_getPrev() throws IllegalAccessException {
		MoveHistory game = getRuyLopez();
		game.getPrev();
		game.getPrev();
		game.getPrev();
		game.getPrev();
		assertEquals("e4", game.getPrev().getValue());
		try {
			game.getPrev();
			fail("MoveHistory.getPrev() failed to throw an IllegalAccessException.");
		}
		catch (IllegalAccessException e) {
		}
	}
	
	@Test
	public void test_getNext() throws IllegalAccessException {
		MoveHistory game = getRuyLopez();
		game.getPrev();
		game.getPrev();
		game.getNext();
		assertEquals("b5", game.getNext().getValue());
		try {
			game.getNext();
			fail("MoveHistory.getNext() failed to throw an IllegalAccessException.");
		}
		catch (IllegalAccessException e) {
		}
	}
	
	@Test
	public void test_hasNext() throws IllegalAccessException {
		MoveHistory game = getRuyLopez();
		assertFalse(game.hasNext());
		game.getPrev();
		assertTrue(game.hasNext());
	}
		
	@Test
	public void test_hasPrev() throws IllegalAccessException {
		MoveHistory game = getRuyLopez();
		assertTrue(game.hasPrev());
		game.getPrev();
		game.getPrev();
		game.getPrev();
		game.getPrev();
		game.getPrev();
		assertFalse(game.hasPrev());
		assertFalse(new MoveHistory().hasPrev());
	}
}
