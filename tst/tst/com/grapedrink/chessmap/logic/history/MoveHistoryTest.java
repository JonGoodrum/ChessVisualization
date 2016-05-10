package tst.com.grapedrink.chessmap.logic.history;

import static org.junit.Assert.*;

import org.junit.Test;

import com.grapedrink.chessmap.logic.history.MoveHistory;
import com.grapedrink.chessmap.logic.history.Turn;

public class MoveHistoryTest {

	private MoveHistory getRuyLopez() {
		MoveHistory ruyLopez = new MoveHistory();
		Turn t1 = new Turn("e2", "e4");
		Turn t2 = new Turn("e7", "e5");
		Turn t3 = new Turn("g1", "f3");
		Turn t4 = new Turn("b8", "c6");
		Turn t5 = new Turn("f1", "b5");
		ruyLopez.addMove(t1);
		ruyLopez.addMove(t2);
		ruyLopez.addMove(t3);
		ruyLopez.addMove(t4);
		ruyLopez.addMove(t5);
		return ruyLopez;
	}
	
	@Test
	public void test_addMove() throws IllegalAccessException {
		MoveHistory game = getRuyLopez();
		game.getPrev();
		game.getPrev();
		game.getPrev();
		assertTrue(game.hasNext());
		assertFalse(game.isBlacksTurn());
		game.addMove(new Turn("a2", "a4"));
		assertEquals(3, game.getMoveCount());
		assertFalse(game.hasNext());
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
		assertEquals("e4", game.getPrev().getDst());
		try {
			game.getPrev();
			fail("MoveHistory.getPrev() failed to throw an IndexOutOfBoundsException.");
		}
		catch (IndexOutOfBoundsException e) {
		}
	}
	
	@Test
	public void test_getNext() throws IndexOutOfBoundsException {
		MoveHistory game = getRuyLopez();
		game.getPrev();
		game.getPrev();
		game.getNext();
		assertEquals("b5", game.getNext().getDst());
		try {
			game.getNext();
			fail("MoveHistory.getNext() failed to throw an IndexOutOfBoundsException.");
		}
		catch (IndexOutOfBoundsException e) {
		}
	}
	
	@Test
	public void test_hasNext() {
		MoveHistory game = getRuyLopez();
		assertFalse(game.hasNext());
		game.getPrev();
		assertTrue(game.hasNext());
	}
		
	@Test
	public void test_hasPrev() {
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
