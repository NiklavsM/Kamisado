package controller;

import java.awt.EventQueue;

import model.GameDriver;
import player.EasyAIPlayer;
import player.GUIPlayer;
import view.RunningGameView;

public class Controller {

	private GameDriver game;
	private RunningGameView main;
	
	public Controller(){
		main = new RunningGameView(this);
		main.setVisible(true);
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
		game = new GameDriver(new GUIPlayer(), new EasyAIPlayer(), main);
	}
	
	public void playTwoPlayer(){
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Controller cont = new Controller();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
