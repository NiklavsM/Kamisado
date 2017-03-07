package controller;

import model.GameDriver;
import player.EasyAIPlayer;
import player.GUIPlayer;
import view.RunningGameView;

public class Controller {

	private GameDriver game;
	private RunningGameView main;
	
	public Controller(){
		main = new RunningGameView(this);
		playSinglePlayer();
	}
	
	public void buttonClicked(int x, int y){ 
		if(game.playerFirstMove(x, y)){
			//game.playGame();
		}
	}
	
	public void refreshIcons(){
		
	}
	
	public void playSinglePlayer(){
		game = new GameDriver(new GUIPlayer(), new EasyAIPlayer(), main.getGameBoard());
	}
	
	public void playTwoPlayer(){
		
	}
}
