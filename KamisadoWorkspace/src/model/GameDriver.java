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
	
	
	
	public GameDriver(){
		
	}
	
	public void play(){
		
	}
	
}
