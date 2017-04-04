package model;

import java.io.Serializable;

public enum PieceType implements Serializable{

	Standard("Standard", 7, 0, 0),
	Sumo("Sumo", 5, 1, 1),
	DoubleSumo("Double", 3, 2, 3),
	TripleSumo("Triple", 1, 3, 7),
	QuadrupleSumo("Quadruple", 0, 0, 15);
	
	private final String value;
	private final int maxMovement;
	private final int piecesItCanMove;
	private final int pointValue;
	
	private PieceType(String value, int maxMovement, int piecesItCanMove, int pointValue){
		this.value = value;
		this.maxMovement = maxMovement;
		this.piecesItCanMove = piecesItCanMove;
		this.pointValue = pointValue;
	}
	
	@Override
	public String toString() {
		return value;
	}

	public int getMaxMovement() {
		return maxMovement;
	}

	public int getPiecesItCanMove() {
		return piecesItCanMove;
	}
	
	public int getPointValue(){
		return pointValue;
	}
}
