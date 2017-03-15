package model;

public class Move{

	
	private Position startPos;
	private Position endPos;
	private Piece pieceMoved;
	
	public Move(Position startPos, Position endPos, Piece pieceMoved){
            this.startPos = startPos;
            this.endPos = endPos;
            this.pieceMoved = pieceMoved;
	}
	
	public Move(Move move) {
		this.startPos = move.startPos;
		this.endPos = move.endPos;
		this.pieceMoved = move.pieceMoved;
	}

	public String pieceMoved(){
		return pieceMoved.toString();
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
	
	public void print(){
		if(pieceMoved != null){
			System.out.println(pieceMoved.toString() + 
					" From: X: " + startPos.getX() + " Y: " + startPos.getY() 
					+ " To: X: " + endPos.getX() + " Y: " + endPos.getY());
		}else{
			System.out.println("pieceNull?");
		}
		
	}
	
}
