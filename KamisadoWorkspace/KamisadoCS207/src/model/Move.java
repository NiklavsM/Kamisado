package model;

public class Move {

	
	private Position startPos;
	private Position endPos;
	
	public Move(Position startPos, Position endPos){
		this.startPos = startPos;
		this.endPos = endPos;
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
	
	
}
