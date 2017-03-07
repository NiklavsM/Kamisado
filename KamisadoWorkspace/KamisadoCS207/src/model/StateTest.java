package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class StateTest {

	@Test
	public void test() {
		State state = new State();
		state.setPieceToMove(Piece.PlayerWhiteGreen);
		state.calcValidMoves(true, 1, 7);
		
	}

}
