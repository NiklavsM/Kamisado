package model;

import java.awt.Color;
import java.io.Serializable;

public enum Piece implements Serializable {

	TeamBlackOrange("TeamBlackOrange","Black"),
	TeamBlackBlue("TeamBlackBlue","Black"),
	TeamBlackCyan("TeamBlackCyan", "Black"),
	TeamBlackPink("TeamBlackPink","Black"),
	TeamBlackYellow("TeamBlackYellow", "Black"),
	TeamBlackRed("TeamBlackRed","Black"),
	TeamBlackGreen("TeamBlackGreen", "Black"),
	TeamBlackBrown("TeamBlackBrown",  "Black"),
	
	TeamWhiteOrange("TeamWhiteOrange", "White"),
	TeamWhiteBlue("TeamWhiteBlue","White"),
	TeamWhiteCyan("TeamWhiteCyan", "White"),
	TeamWhitePink("TeamWhitePink",  "White"),
	TeamWhiteYellow("TeamWhiteYellow",   "White"),
	TeamWhiteRed("TeamWhiteRed", "White"),
	TeamWhiteGreen("TeamWhiteGreen", "White"),
	TeamWhiteBrown("TeamWhiteBrown",  "White");

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
