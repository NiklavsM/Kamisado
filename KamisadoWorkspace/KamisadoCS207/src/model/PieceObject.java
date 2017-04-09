package model;

import java.io.Serializable;



public class PieceObject implements Serializable {


	private Piece piece;
	private PieceType pieceType;

	public PieceObject(Piece piece, PieceType pieceType) {
		this.piece = piece;
		this.pieceType = pieceType;
	}
	
	public PieceObject(PieceObject pieceObject){
		this.piece = pieceObject.getPiece();
		this.pieceType = pieceObject.getPieceType();
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public PieceType getPieceType() {
		return pieceType;
	}

	public void setPieceType(PieceType pieceType) {
		this.pieceType = pieceType;
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
