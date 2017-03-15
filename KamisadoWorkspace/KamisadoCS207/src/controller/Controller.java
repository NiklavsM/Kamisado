package controller;

import java.awt.EventQueue;
import java.io.Serializable;

import model.GameDriver;
import model.SpeedGameDriver;
import player.EasyAIPlayer;
import player.GUIPlayer;
import player.HardAIPlayer;
import player.Player;
import view.MenuFrame;
import view.RunningGameView;

public class Controller implements Serializable{

	private GameDriver game;
	private RunningGameView main;
	MenuFrame menuFrame;
	private boolean firstMovePlayed;
	private Player playerWhite;
	private Player playerBlack;

	public Controller() {
		
		firstMovePlayed = false;
		menuFrame = new MenuFrame(this);
		menuFrame.setVisible(true);
		main = menuFrame.getRunningGameView();
		//main.setVisible(true);
		main.addObserver(playerWhite);
		main.addObserver(playerBlack);
		

	}

	public void initialisePlayers(String whiteName, String blackName) {
		playerWhite = new GUIPlayer("White",whiteName, false, this);
		playerBlack = new GUIPlayer("Black",blackName, true, this);
	}

	public void playSinglePlayer(boolean userToMoveFirst,boolean isSpeedGame,boolean isEasyAI, String whiteName, String blackName) {
		if(userToMoveFirst){
			playerWhite = new GUIPlayer("White",whiteName, true, this);
			
			if(isEasyAI){
				playerBlack = new EasyAIPlayer("Black",blackName, false);
			}else{
				playerBlack = new HardAIPlayer("Black",blackName, false);
			}
			if (isSpeedGame) {
				game = new SpeedGameDriver(playerWhite, playerBlack, main, playerWhite, 10);
			} else {
				game = new GameDriver(playerWhite, playerBlack, main, playerWhite);
			}
			playerBlack.addObserver(game);
		}else{
			if(isEasyAI){
				playerWhite = new EasyAIPlayer("White",whiteName, true);
			}else{
				playerWhite = new HardAIPlayer("White",whiteName, true);
			}
			playerBlack = new GUIPlayer("Black",blackName, false, this);
			if (isSpeedGame) {
				game = new SpeedGameDriver(playerWhite, playerBlack, main, playerWhite,10);
			} else {
				game = new GameDriver(playerWhite, playerBlack, main, playerWhite);
			}
			playerWhite.addObserver(game);
		}
		main.getGameBoard().addObserver(game);
		menuFrame.addPanel(main);
		game.playGame();
	}

	public void playTwoPlayer(boolean isSpeedGame, String whiteName, String blackName) {
		initialisePlayers(whiteName,blackName);
		if(isSpeedGame){
			game = new SpeedGameDriver(playerWhite, playerBlack, main, playerWhite, 10);
		}else{
			game = new GameDriver(playerWhite, playerBlack, main, playerWhite);
		}
		main.getGameBoard().addObserver(game);
		game.addObserver(main.getGameTimer());
		menuFrame.addPanel(main);
		game.playGame();

	}
	public GameDriver getGame() {
		return game;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Controller cont = new Controller();
				} catch (Exception e) {
				}
			}
		});
	}

	// public void displayValidMoves(ArrayList<Position> validMoves) {
	// main.displaycSelectable(validMoves);
	// }

	// public Position waitForClick() {
	// System.out.println("waiting for click from main");
	// return main.waitForClick();
	// }
}
