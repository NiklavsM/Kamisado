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
	
	public void setPieceImageStyle(String pieceImageStyle){
		this.pieceImageStyle = pieceImageStyle;
	}
	
	public String getPieceImageStyle(){
		return pieceImageStyle;
	}
	
	public Color getBrown() {
		return brown;
	}

	public void setBrown(Color brown) {
		this.brown = brown;
	}

	public Color getGreen() {
		return green;
	}

	public void setGreen(Color green) {
		this.green = green;
	}

	public Color getRed() {
		return red;
	}

	public void setRed(Color red) {
		this.red = red;
	}

	public Color getYellow() {
		return yellow;
	}

	public void setYellow(Color yellow) {
		this.yellow = yellow;
	}

	public Color getPink() {
		return pink;
	}

	public void setPink(Color pink) {
		this.pink = pink;
	}

	public Color getCyan() {
		return cyan;
	}

	public void setCyan(Color cyan) {
		this.cyan = cyan;
	}

	public Color getBlue() {
		return blue;
	}

	public void setBlue(Color blue) {
		this.blue = blue;
	}

	public Color getOrange() {
		return orange;
	}

	public void setOrange(Color orange) {
		this.orange = orange;
	}

}
