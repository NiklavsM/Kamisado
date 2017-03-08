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
		this.addObserver(observerToState);
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
		Position placeClicked = new Position(x,y);
		if(placeClicked.getY() == 0){
			currentState.calcValidMoves(WhiteToMove, placeClicked);
			this.setChanged();
			this.notifyObservers(currentState.getValidMoves());
			return false;
		}else{
			return actionOnClick(placeClicked);
		}
	}
	
	public boolean actionOnClick(Position placeClicked){
		System.out.println("heard click");
		State state = currentState.make(placeClicked);
		System.out.println("made move at board and state level");
		if(state == null){
			System.out.println("is not valid move or games over X: " + placeClicked.getX() + " Y: " + placeClicked.getY());
			return false;
		}else{
			System.out.println("is valid move");
			history.add(currentState);
			currentState = state;
			this.setChanged();
			this.notifyObservers(currentState.getPreviousMove());
			nextTurn();
			return true;
		}
	}
	
	public void playTurn(int x, int y){
		Position placeClicked = new Position(x,y);
		actionOnClick(placeClicked);
	}
	
	public void nextTurn(){
		if(WhiteToMove){
			WhiteToMove = false;
		}else{
			WhiteToMove = true;
		}
		Position posToMove = currentState.calcPieceToMove(WhiteToMove);
		System.out.println("pos : " + posToMove);
		ArrayList<Position> movesCanMake = currentState.calcValidMoves(WhiteToMove, posToMove);
		if(movesCanMake.size() == 0){
			nextTurn();
		}else{
			this.setChanged();
			this.notifyObservers(movesCanMake);
		}
	}
}
