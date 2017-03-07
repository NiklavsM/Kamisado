package model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import player.Player;

public class GameDriver extends Observable{

	private Player playerWhite;
	private Player playerBlack;
	private boolean WhiteToMove;
	
	private ArrayList<State> history;
	private State currentState;
	
//	private SaveManager saveManager;
//	private MatchReport matchReport;
	
	public GameDriver(Player playerWhite, Player playerBlack, Observer observerToState) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		WhiteToMove = true;
		this.history = new ArrayList<>();
		this.currentState = new State();
		currentState.addObserver(observerToState);
		//this.saveManager = new SaveManager();
		play();
	}

	public GameDriver(Player playerWhite, Player playerBlack, ArrayList<State> history, State currentState) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		WhiteToMove = true;
		this.history = history;
		this.currentState = currentState;
		//this.saveManager = new SaveManager();
		play();
	}

	public void play(){
		//playTurn();
		
	}
	
	public boolean playerFirstMove(int x, int y){
		Position placeCLicked = new Position(x,y);
		if(placeCLicked.getY() == 0){
			currentState.calcValidMoves(WhiteToMove, placeCLicked);
			return false;
		}else{
			State state = currentState.make(placeCLicked);
			if(state == null){
				return false;
			}else{
				history.add(currentState);
				currentState = state;
				return true;
			}
		}
	}
	
	public void playTurn(){
		nextTurn();
		Position placeToMove;
		boolean acceptedMove = true;
		while(!acceptedMove){
			if(WhiteToMove){
				placeToMove = playerWhite.getMove(currentState.getBoard());
			}else{
				placeToMove = playerBlack.getMove(currentState.getBoard());
			}
			
//			Board board = currentState.make(placeToMove);
//			if(board != null){
//				history.add(currentState);
//				currentState = new State(board);
//				acceptedMove = true;
//			}
		}
		
	}
	
	public void nextTurn(){
		if(WhiteToMove){
			WhiteToMove = false;
		}else{
			WhiteToMove = true;
		}
	}
	
}
