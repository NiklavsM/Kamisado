package model;

import java.awt.Color;
import java.io.Serializable;

public class Piece implements Serializable {

	private String team;
	private PieceType pieceType;
	private Color colour;

	public Piece(String team,PieceType pieceType, Color color) {
		this.team = team;
		this.pieceType = pieceType;
		this.colour = color;
	}
	
	public Piece(Piece piece){
		this.team = piece.getTeam();
		this.pieceType = piece.getPieceType();
		this.colour = piece.getColour();
	}

	public PieceType getPieceType() {
		return pieceType;
	}

	public void setPieceType(PieceType pieceType) {
		this.pieceType = pieceType;
	}
	
	public String getTeam() {
		return team;
	}
	
	public Color getColour() {
		return colour;
	}
	
	public void promotePiece(){
		switch(pieceType){
			case Standard: pieceType = PieceType.Sumo;
							break;
			case Sumo: pieceType = PieceType.DoubleSumo;
							break;
			case DoubleSumo: pieceType = PieceType.TripleSumo;
							break;
			case TripleSumo: pieceType = PieceType.QuadrupleSumo;
							break;
			case QuadrupleSumo: //can't promote, do nothing
		}
	}

}
