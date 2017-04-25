package model;

import java.io.Serializable;

public class Stat implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private Integer roundsWon;
	private Integer roundsLost;
	private Integer gamesWon;
	private Integer gamesLost;

	public Stat(String name) {
		this.name = name;
		roundsWon = new Integer(0);
		roundsLost = new Integer(0);
		gamesWon = new Integer(0);
		gamesLost = new Integer(0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRoundsWon() {
		return roundsWon;
	}

	public void addRoundsWon() {
		roundsWon++;
	}

	public Integer getRoundsLost() {
		return roundsLost;
	}

	public void addRoundsLost() {
		roundsLost++;
	}

	public Integer getGamesWon() {
		return gamesWon;
	}

	public void addGamesWon() {
		gamesWon++;
	}

	public Integer getGamesLost() {
		return gamesLost;
	}

	public void addGamesLost() {
		gamesLost++;
	}

	public float roundsWinLosRatio() {
		if (roundsWon == 0 && roundsLost == 0) {
			return 0;
		} else
			return (float) roundsWon / (roundsWon + roundsLost) * 100;
	}

	public float gamesWinLosRatio() {
		if (gamesWon == 0 && gamesLost == 0) {
			return 0;
		}
		return (float) gamesWon / (gamesWon + gamesLost) * 100;
	}
}
