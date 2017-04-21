package model;

import java.awt.Color;
import java.io.Serializable;

public class GeneralSettings implements Serializable {
	private boolean musicOn = false;
	private boolean soundOn = false;
	private int musicVolume = -20;
	private int soundVolume = -20;
	private String pieceImageStyle = "pieceStyleOne";
	private Color brown;
	private Color green;
	private Color red;
	private Color yellow;
	private Color pink;
	private Color cyan;
	private Color blue;
	private Color orange;

	public GeneralSettings() {
		setDefaultColors();
	}

	public void setMusicOn(boolean musicOn) {
		this.musicOn = musicOn;
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public int getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume;
	}

	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}

	public int getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(int soundVolume) {
		this.soundVolume = soundVolume;
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
		Color colourToSet = new Color(red, green, blue);
		if (colourT.equals("blue"))
			this.blue = colourToSet;
		else if (colourT.equals("brown"))
			this.brown = colourToSet;
		else if (colourT.equals("green"))
			this.green = colourToSet;
		else if (colourT.equals("red"))
			this.red = colourToSet;
		else if (colourT.equals("yellow"))
			this.yellow = colourToSet;
		else if (colourT.equals("pink"))
			this.pink = colourToSet;
		else if (colourT.equals("cyan"))
			this.cyan = colourToSet;
		else if (colourT.equals("orange"))
			this.orange = colourToSet;
		MyColour.valueOf(colourT).setColour(colourToSet);

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

	public void setDefaultColors() {
		brown = new Color(148, 104, 39);
		MyColour.valueOf("brown").setColour(brown);
		green = new Color(108, 216, 68);
		MyColour.valueOf("green").setColour(green);
		red = new Color(249, 69, 24);
		MyColour.valueOf("red").setColour(red);
		yellow = new Color(245, 245, 26);
		MyColour.valueOf("yellow").setColour(yellow);
		pink = new Color(239, 86, 208);
		MyColour.valueOf("pink").setColour(pink);
		cyan = new Color(95, 207, 235);
		MyColour.valueOf("cyan").setColour(cyan);
		blue = new Color(14, 104, 243);
		MyColour.valueOf("blue").setColour(blue);
		orange = new Color(250, 190, 50);
		MyColour.valueOf("orange").setColour(orange);

	}

	public String getColorName(Color colour) {
		if (colour.equals(blue))
			return "blue";
		if (colour.equals(brown))
			return "brown";
		if (colour.equals(green))
			return "green";
		if (colour.equals(red))
			return "red";
		if (colour.equals(yellow))
			return "yellow";
		if (colour.equals(pink))
			return "pink";
		if (colour.equals(cyan))
			return "cyan";
		if (colour.equals(orange))
			return "orange";
		return null;
	}
}
