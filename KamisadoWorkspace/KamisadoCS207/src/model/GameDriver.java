package model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import player.Player;

public class GameDriver extends Observable{

	private Player playerWhite;
	private Player playerBlack;
	
	private ArrayList<State> history;
	private State currentState;
	
	private SaveManager saveManager;
	private MatchReport matchReport;
	
	public GameDriver(Player playerWhite, Player playerBlack,
			SaveManager saveManager, MatchReport matchReport) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		this.history = new ArrayList<>();
		this.currentState = new State();
		this.saveManager = new SaveManager();
	}

	public GameDriver(Player playerWhite, Player playerBlack, ArrayList<State> history, State currentState,
			SaveManager saveManager) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		this.history = history;
		this.currentState = currentState;
		this.saveManager = saveManager;
	}

	public void play(){
		playerWhite.getMove(currentState.getBoard());
		
		
	}
	
	public void nextTurn(){
		
	}
	
}
