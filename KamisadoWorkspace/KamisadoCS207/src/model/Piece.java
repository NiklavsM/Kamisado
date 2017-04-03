package model;

import java.awt.Color;
import java.io.Serializable;

public enum Piece implements Serializable {

	TeamBlackOrange("TeamBlackOrange","Black", PieceType.Standard),
	TeamBlackBlue("TeamBlackBlue","Black",PieceType.Standard),
	TeamBlackCyan("TeamBlackCyan", "Black",PieceType.Standard),
	TeamBlackPink("TeamBlackPink","Black",PieceType.Standard),
	TeamBlackYellow("TeamBlackYellow", "Black",PieceType.Standard),
	TeamBlackRed("TeamBlackRed","Black",PieceType.Standard),
	TeamBlackGreen("TeamBlackGreen", "Black",PieceType.Standard),
	TeamBlackBrown("TeamBlackBrown",  "Black",PieceType.Standard),
	
	TeamWhiteOrange("TeamWhiteOrange", "White",PieceType.Standard),
	TeamWhiteBlue("TeamWhiteBlue","White",PieceType.Standard),
	TeamWhiteCyan("TeamWhiteCyan", "White",PieceType.Standard),
	TeamWhitePink("TeamWhitePink",  "White",PieceType.Standard),
	TeamWhiteYellow("TeamWhiteYellow",   "White",PieceType.Standard),
	TeamWhiteRed("TeamWhiteRed", "White",PieceType.Standard),
	TeamWhiteGreen("TeamWhiteGreen", "White",PieceType.Standard),
	TeamWhiteBrown("TeamWhiteBrown",  "White",PieceType.Standard);

	private final String value;
	private Color colour;
	private final String team;
	private PieceType pieceType;
	
	private Piece(String value, String team, PieceType pieceType){
		this.value = value;
		this.colour = null;
		this.team = team;
		this.pieceType = pieceType;
	}
	
	
	public Color getColour() {
		return colour;
	}
	
	public void setColour(Color colour){
		this.colour = colour;
	}


	public String getTeam() {
		return team;
	}

	public void setPieceType(PieceType pieceType){
		this.pieceType = pieceType;
	}
	
	public PieceType getPieceType(){
		return this.pieceType;
	}

	@Override
	public String toString() {
		
		return value;
	}
	
	
	
}
