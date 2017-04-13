package model;

import java.io.Serializable;
import java.util.ArrayList;

import player.Player;

public class StatsObject implements Serializable {
	public ArrayList<Stat> playerScores;

	public StatsObject() {
		playerScores = new ArrayList<Stat>();
	}

	public void addToScores(Player winner, Player looser, boolean gameEnds) {
		boolean winnerFound = false;
		boolean looserFound = false;
		for (Stat stat : playerScores) {
			if (stat.getName().equals(winner.getPlayerName())) {
				stat.addRoundsWon();
				if (gameEnds) {
					stat.addGamesWon();
				}
				winnerFound = true;
			} else if (stat.getName().equals(looser.getPlayerName())) {
				stat.addRoundsLost();
				if (gameEnds) {
					stat.addGamesLost();
				}
				looserFound = true;
			}
		}
		if (!winnerFound) {
			Stat stat = new Stat(winner.getPlayerName());
			stat.addRoundsWon();
			if (gameEnds) {
				stat.addGamesWon();
			}
			playerScores.add(stat);
		}
		if (!looserFound) {
			Stat stat = new Stat(looser.getPlayerName());
			stat.addRoundsLost();
			if (gameEnds) {
				stat.addGamesLost();
			}
			playerScores.add(stat);
		}

	}

	public Object[][] getTableData() {
		Object stats[][] = new String[playerScores.size()][5];

		int i = 0;
		for (Stat stat : playerScores) {
			stats[i][0] = stat.getName();
			stats[i][1] = Integer.toString(stat.getRoundsWon());// fix
			stats[i][2] = Integer.toString(stat.getRoundsLost());
			stats[i][3] = Integer.toString(stat.getGamesWon());
			stats[i][4] = Integer.toString(stat.getGamesLost());
			i++;
		}
		return stats;
	}
}
