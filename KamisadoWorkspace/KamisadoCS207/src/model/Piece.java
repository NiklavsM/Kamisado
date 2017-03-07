package model;

public enum Piece {

	TeamBlackOrange("TeamBlack-Orange"),
	TeamBlackBlue("TeamBlack-Blue"),
	TeamBlackCyan("TeamBlack-Cyan"),
	TeamBlackPink("TeamBlack-Pink"),
	TeamBlackYellow("TeamBlack-Yellow"),
	TeamBlackRed("TeamBlack-Red"),
	TeamBlackGreen("TeamBlack-Green"),
	TeamBlackBrown("TeamBlack-Brown"),
	
	TeamWhiteOrange("TeamWhite-Orange"),
	TeamWhiteBlue("TeamWhite-Blue"),
	TeamWhiteCyan("TeamWhite-Cyan"),
	TeamWhitePink("TeamWhite-Pink"),
	TeamWhiteYellow("TeamWhite-Yellow"),
	TeamWhiteRed("TeamWhite-Red"),
	TeamWhiteGreen("TeamWhite-Green"),
	TeamWhiteBrown("TeamWhite-Brown");

	private String value;
	
	
	private Piece(String value){
		this.value = value;
	}
	
	
	@Override
	public String toString() {
		
		return value;
	}
	
	
	
}
