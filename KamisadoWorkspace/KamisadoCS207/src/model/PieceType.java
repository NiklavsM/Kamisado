package model;

import java.io.Serializable;

public enum PieceType implements Serializable{

	Standard("Standard", 7, 0),
	Sumo("Sumo", 5, 1),
	DoubleSumo("Double", 3, 2),
	TripleSumo("Triple", 1, 3),
	QuadrupleSumo("Quadruple", 0, 0);
	
	private final String value;
	private final int maxMovement;
	private final int piecesItCanMove;
	
	private PieceType(String value, int maxMovement, int piecesItCanMove){
		this.value = value;
		this.maxMovement = maxMovement;
		this.piecesItCanMove = piecesItCanMove;
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
}
