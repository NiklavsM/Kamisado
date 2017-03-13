package model;

import java.awt.Color;

public enum Piece {

	TeamBlackOrange("TeamBlack-Orange","Black"),
	TeamBlackBlue("TeamBlack-Blue","Black"),
	TeamBlackCyan("TeamBlack-Cyan", "Black"),
	TeamBlackPink("TeamBlack-Pink","Black"),
	TeamBlackYellow("TeamBlack-Yellow", "Black"),
	TeamBlackRed("TeamBlack-Red","Black"),
	TeamBlackGreen("TeamBlack-Green", "Black"),
	TeamBlackBrown("TeamBlack-Brown",  "Black"),
	
	TeamWhiteOrange("TeamWhite-Orange", "White"),
	TeamWhiteBlue("TeamWhite-Blue","White"),
	TeamWhiteCyan("TeamWhite-Cyan", "White"),
	TeamWhitePink("TeamWhite-Pink",  "White"),
	TeamWhiteYellow("TeamWhite-Yellow",   "White"),
	TeamWhiteRed("TeamWhite-Red", "White"),
	TeamWhiteGreen("TeamWhite-Green", "White"),
	TeamWhiteBrown("TeamWhite-Brown",  "White");

	private final String value;
	private Color colour;
	private final String team;
	
	private Piece(String value, String team){
		this.value = value;
		this.colour = null;
		this.team = team;
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


	@Override
	public String toString() {
		
		return value;
	}
	
	
	
}
