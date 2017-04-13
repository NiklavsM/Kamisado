package model;

import java.io.Serializable;

public class Stat implements Serializable {
	private String name;
	private int roundsWon;
	private int roundsLost;
	private int gamesWon;
	private int gamesLost;

	public Stat(String name) {
		this.name = name;
		roundsWon = 0;
		roundsLost = 0;
		gamesWon = 0;
		gamesLost = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRoundsWon() {
		return roundsWon;
	}

	public void addRoundsWon() {
		roundsWon++;
	}

	public int getRoundsLost() {
		return roundsLost;
	}

	public void addRoundsLost() {
		roundsLost++;
	}

	public int getGamesWon() {
		return gamesWon;
	}

	public void addGamesWon() {
		gamesWon++;
	}

	public int getGamesLost() {
		return gamesLost;
	}

	public void addGamesLost() {
		gamesLost++;
	}
}
