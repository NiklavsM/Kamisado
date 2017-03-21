package controller;

import java.awt.EventQueue;
import java.io.Serializable;

import model.GameDriver;
import model.SaveManager;
import model.SpeedGameDriver;
import model.State;
import player.EasyAIPlayer;
import player.GUIPlayer;
import player.HardAIPlayer;
import player.Player;
import view.MenuFrame;
import view.RunningGameView;

public class Controller implements Serializable {

	private GameDriver game;
	private RunningGameView main;
	MenuFrame menuFrame;
	private Player playerWhite;
	private Player playerBlack;

	public Controller() {
		main = new RunningGameView("DefaultWhite", "DefaultBlack", this);
		menuFrame = new MenuFrame(this, main);
		menuFrame.setVisible(true);

	}

	public void initialisePlayers(String whiteName, String blackName) {
		playerWhite = new GUIPlayer("White", whiteName, true, this);
		playerBlack = new GUIPlayer("Black", blackName, false, this);
	}

	public void playSinglePlayer(boolean userToMoveFirst, boolean isSpeedGame, boolean isEasyAI, String whiteName,
			String blackName, int timerTime) {
		main.getGameBoard().removeObserver(game);
		if(game != null){
			game.removeObserver(main);
			game.removeObserver(main.getGameTimer());
			game.setState(null);
			game = null;
		}
//		game = null;
//		System.gc();
		
		if (userToMoveFirst) {
			playerWhite = new GUIPlayer("White", whiteName, true, this);

			if (isEasyAI) {
				playerBlack = new EasyAIPlayer("Black", blackName, false);
			} else {
				playerBlack = new HardAIPlayer("Black", blackName, false);
			}
			if (isSpeedGame) {
				game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, timerTime);
			} else {
				game = new GameDriver(playerWhite, playerBlack, playerWhite);
			}

			playerBlack.addObserver(game);
		} else {
			if (isEasyAI) {
				playerWhite = new EasyAIPlayer("White", whiteName, true);
			} else {
				playerWhite = new HardAIPlayer("White", whiteName, true);
			}
			playerBlack = new GUIPlayer("Black", blackName, false, this);
			if (isSpeedGame) {
				game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, timerTime);
			} else {
				game = new GameDriver(playerWhite, playerBlack, playerWhite);
			}
			playerWhite.addObserver(game);

		}
		if(game.getCurrentState() == null){
			System.out.println("state is null");
		}
		main.getGameBoard().addObserver(game);
		main.displayGame(game.getCurrentState());
		

		game.addObserver(main.getGameTimer());
		game.addObserver(main);

		menuFrame.ShowGameViewPanel();
		// main.addObserver(playerWhite);
		// main.addObserver(playerBlack);
		game.playGame();
	}

	public void playTwoPlayer(boolean isSpeedGame, String whiteName, String blackName, int timerTime) {
		main.getGameBoard().removeObserver(game);
		if(game != null){
			game.removeObserver(main);
			game.removeObserver(main.getGameTimer());
			game.setState(null);
			game = null;
		}
//		game = null;
//		System.gc();
		
		initialisePlayers(whiteName, blackName);
		if (isSpeedGame) {
			game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, timerTime);
		} else {
			game = new GameDriver(playerWhite, playerBlack, playerWhite);
		}
		main.getGameBoard().addObserver(game);
		main.displayGame(game.getCurrentState());
		
		
		game.addObserver(main.getGameTimer());
		game.addObserver(main);
		menuFrame.ShowGameViewPanel();
		// playerWhite.addObserver(main);
		// playerBlack.addObserver(main);
		game.playGame();

	}

	public GameDriver getGame() {
		return game;
	}

	public void loadGame() {
		SaveManager s = new SaveManager();
		State stateToLoad = s.load();
		if (stateToLoad != null) {
			if (game == null) {
				if (stateToLoad.getTime() > 0) {
					game = new SpeedGameDriver(stateToLoad);
				} else {
					game = new GameDriver(stateToLoad);
				}
				main.displayGame(game.getCurrentState());
				main.getGameBoard().addObserver(game);
				game.addObserver(main.getGameTimer());
				game.addObserver(main);
				game.changeCurrentState(stateToLoad);
				game.playGame();
			} else {
				game.changeCurrentState(stateToLoad);

			}
		}
		menuFrame.ShowGameViewPanel();

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
}
