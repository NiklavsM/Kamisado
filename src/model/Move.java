package model;

import java.io.Serializable;

public class Move implements Serializable {

	private static final long serialVersionUID = 1L;
	private Position startPos;
	private Position endPos;
	private Piece pieceMoved;
	private int score;

	public Move(Position startPos, Position endPos, Piece pieceMoved) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.pieceMoved = pieceMoved;
	}

	public Move(Move move) {
		this.startPos = move.startPos;
		this.endPos = move.endPos;
		this.pieceMoved = move.pieceMoved;
		this.score = move.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public Piece pieceMoved() {
		return pieceMoved;
	}

	public Position getStartPos() {
		return startPos;
	}

	public void setStartPos(Position startPos) {
		this.startPos = startPos;
	}

	public Position getEndPos() {
		return endPos;
	}

	public void setEndPos(Position endPos) {
		this.endPos = endPos;
	}

	@Override
	public String toString() {
		return pieceMoved.getTeam() + " '" + pieceMoved.getPieceColour() + "' " + " [" + startPos.toString() + "] -> ["
				+ endPos.toString() + "]";
	}
}
