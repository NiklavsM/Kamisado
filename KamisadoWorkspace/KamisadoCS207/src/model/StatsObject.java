package model;

import java.io.Serializable;
import java.util.ArrayList;

import player.Player;

public class StatsObject implements Serializable {
	public ArrayList<Stat> playerScores;

	public StatsObject() {
		playerScores = new ArrayList<Stat>();
	}

	public void addToScores(Player winner, Player looser) {
		boolean winnerFound = false;
		boolean looserFound = false;
		for (Stat stat : playerScores) {
			if (stat.getName().equals(winner.getPlayerName())) {
				stat.addWins();
				winnerFound = true;
			} else if (stat.getName().equals(looser.getPlayerName())) {
				stat.addLoses();
				looserFound = true;
			}
		}
		if (!winnerFound) {
			Stat stat = new Stat(winner.getPlayerName());
			stat.addWins();
			playerScores.add(stat);
		}
		if (!looserFound) {
			Stat stat = new Stat(looser.getPlayerName());
			stat.addLoses();
			playerScores.add(stat);
		}

	}

	public Object[][] getTableData() {
		Object stats[][] = new String[playerScores.size()][3];

		int i = 0;
		for (Stat stat : playerScores) {
			stats[i][0] = stat.getName();
			stats[i][1] = Integer.toString(stat.getWins());//fix
			stats[i][2] = Integer.toString(stat.getLoses());
			i++;
		}
		return stats;
	}
}
