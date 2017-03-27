package model;

import java.io.Serializable;
import java.util.ArrayList;

import player.Player;

public class StatsObject implements Serializable {
	public ArrayList<Stat> playerScores;
	
public StatsObject(){
	playerScores = new ArrayList<Stat>();
}
	
	public void addToScores(Player winner, Player looser){
		boolean winnerFound = false;
		boolean looserFound = false;
		for(Stat stat : playerScores){
			if(stat.getName().equals(winner.getPlayerName())){
				stat.addWins();
				winnerFound = true;
				System.out.println("HERE");
			}else if(stat.getName().equals(looser.getPlayerName())){
				stat.addLoses();
				looserFound = true;
				System.out.println("HERE2");
			}			
		}
		if(!winnerFound){
			Stat stat = new Stat(winner.getPlayerName());
			stat.addWins();
			playerScores.add(stat);	
			System.out.println("HERE3");
		}
		if(!looserFound){
			Stat stat = new Stat(looser.getPlayerName());
			stat.addLoses();
			playerScores.add(stat);	
			System.out.println("HERE5");
		}
		
	}
	public void printStatsTest(){
		for(Stat stat : playerScores){
			System.out.println("Name: " + stat.getName());
			System.out.println("Wins: " + stat.getWins());
			System.out.println("Losses: " + stat.getLoses());
		}
	}
	public String getStats(){
		String stats = null;
		for(Stat stat : playerScores){
			if(stats==null){
			stats = "Name: " + stat.getName() + " Wins: "+  stat.getWins() + " Losses: " + stat.getLoses() +"\n";
			}else{
				stats = stats + "Name: " + stat.getName() + " Wins: "+  stat.getWins() + " Losses: " + stat.getLoses() +"\n";
			}
			
		}
		return stats;
	}
}

