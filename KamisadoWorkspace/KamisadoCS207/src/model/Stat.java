package model;

import java.io.Serializable;

public class Stat implements Serializable {
	private String name;
	private int wins;
	private int loses;

	public Stat(String name) {
		this.name = name;
		wins = 0;
		loses = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWins() {
		return wins;
	}

	public void addWins() {
		wins++;
	}

	public int getLoses() {
		return loses;
	}

	public void addLoses() {
		loses++;
	}
}
