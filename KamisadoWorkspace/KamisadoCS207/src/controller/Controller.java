package controller;

import java.awt.EventQueue;
import java.io.Serializable;

import model.GameDriver;
import model.SpeedGameDriver;
import player.EasyAIPlayer;
import player.GUIPlayer;
import player.Player;
import view.RunningGameView;

public class Controller implements Serializable{

	private GameDriver game;
	private RunningGameView main;
	private boolean firstMovePlayed;
	private Player playerWhite;
	private Player playerBlack;

	public Controller() {
		initialisePlayers();
		firstMovePlayed = false;
		main = new RunningGameView(this);
		main.setVisible(true);
		main.addObserver(playerWhite);
		main.addObserver(playerBlack);
		//playSinglePlayer(true);
		playTwoPlayer();

	}

	public void initialisePlayers() {
		playerWhite = new GUIPlayer("White", false, this);
		playerBlack = new GUIPlayer("Black", true, this);
	}

	public void refreshIcons() {

	}

	public void playSinglePlayer(boolean userToMoveFirst) {

		if (userToMoveFirst) {
			playerBlack = new EasyAIPlayer("Black", false);
			game = new GameDriver(playerWhite, playerBlack, main, playerWhite);
		} else {
			playerBlack = new EasyAIPlayer("Black", true);
			game = new GameDriver(playerWhite, playerBlack, main, playerBlack);
		}
		playerBlack.addObserver(game);
		main.getGameBoard().addObserver(game);
		game.playGame();

	}

	public void playTwoPlayer() {
		game = new SpeedGameDriver(playerWhite, playerBlack, main, playerWhite, 10);
		main.getGameBoard().addObserver(game);
		game.addObserver(main.getGameTimer());
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
