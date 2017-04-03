package model;

import java.io.Serializable;

public enum PieceType implements Serializable{

	Standard("Standard"),
	Sumo("Sumo"),
	DoubleSumo("Double"),
	TripleSumo("Triple"),
	QuadrupleSumo("Quadruple");
	
	private final String value;
	
	
	private PieceType(String value){
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
