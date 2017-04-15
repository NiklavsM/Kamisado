package model;

import java.awt.Color;
import java.io.Serializable;

public class GeneralSettings implements Serializable {
	private boolean musicOn = false;
	private int volume = 0;
	private String pieceImageStyle = "PieceStyleOne";

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

}
