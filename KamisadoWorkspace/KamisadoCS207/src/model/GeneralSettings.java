package model;

import java.awt.Color;
import java.io.Serializable;

public class GeneralSettings implements Serializable {
	private boolean musicOn = false;
	private int volume = 0;
	private String pieceImageStyle = "PieceStyleOne";
	private Color brown = new Color(148, 104, 39);
	private Color green = new Color(108, 216, 68);
	private Color red = new Color(249, 69, 24);
	private Color yellow = new Color(245, 245, 26);
	private Color pink = new Color(239, 86, 208);
	private Color cyan = new Color(95, 207, 235);
	private Color blue = new Color(14, 104, 243);
	private Color orange = new Color(250, 190, 50);

	public void setMusicOn(boolean musicOn) {
		this.musicOn = musicOn;
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public void setPieceImageStyle(String pieceImageStyle) {
		this.pieceImageStyle = pieceImageStyle;
	}

	public String getPieceImageStyle() {
		return pieceImageStyle;
	}

	public Color getColour(String colour) {
		String colourT = colour.toLowerCase();
		if (colourT.equals("blue"))
			return blue;
		if (colourT.equals("brown"))
			return brown;
		if (colourT.equals("green"))
			return green;
		if (colourT.equals("red"))
			return red;
		if (colourT.equals("yellow"))
			return yellow;
		if (colourT.equals("pink"))
			return pink;
		if (colourT.equals("cyan"))
			return cyan;
		if (colourT.equals("orange"))
			return orange;
		return null;
	}

	public void setColour(String colour, int red, int green, int blue) {
		String colourT = colour.toLowerCase();
		if (colourT.equals("blue"))
			this.blue = new Color(red, green, blue);
		if (colourT.equals("brown"))
			this.brown = new Color(red, green, blue);
		if (colourT.equals("green"))
			this.green = new Color(red, green, blue);
		if (colourT.equals("red"))
			this.red = new Color(red, green, blue);
		if (colourT.equals("yellow"))
			this.yellow = new Color(red, green, blue);
		if (colourT.equals("pink"))
			this.pink = new Color(red, green, blue);
		if (colourT.equals("cyan"))
			this.cyan = new Color(red, green, blue);
		if (colourT.equals("orange"))
			this.orange = new Color(red, green, blue);
	}

	public int getColoursRed(String colour) {
		return getColour(colour).getRed();
	}

	public int getColoursGreen(String colour) {
		return getColour(colour).getGreen();
	}

	public int getColoursBlue(String colour) {
		return getColour(colour).getBlue();
	}

}
