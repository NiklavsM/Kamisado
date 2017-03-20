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
		if (main == null) {
			main = new RunningGameView(whiteName, blackName, this);
		}
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
		main.displayGame(game.getCurrentState());
		main.getGameBoard().addObserver(game);

		game.addObserver(main.getGameTimer());
		menuFrame.ShowGameViewPanel();
		game.addObserver(main);
		// main.addObserver(playerWhite);
		// main.addObserver(playerBlack);
		game.playGame();
	}

	public void playTwoPlayer(boolean isSpeedGame, String whiteName, String blackName, int timerTime) {
		main.getGameBoard().removeObserver(game);
		initialisePlayers(whiteName, blackName);
		if (main == null) {
			main = new RunningGameView(whiteName, blackName, this);
		}
		if (isSpeedGame) {
			game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, timerTime);
		} else {
			game = new GameDriver(playerWhite, playerBlack, playerWhite);
		}
		main.displayGame(game.getCurrentState());
		main.getGameBoard().addObserver(game);
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
		if (game == null) {
			if (stateToLoad != null) {
				if (stateToLoad.getTime() > 0) {
					game = new SpeedGameDriver(stateToLoad);
				} else {
					game = new GameDriver(stateToLoad);
				}
				main.displayGame(game.getCurrentState());
				main.getGameBoard().addObserver(game);
				game.addObserver(main.getGameTimer());
				game.addObserver(main);
				menuFrame.ShowGameViewPanel();
				game.changeCurrentState(stateToLoad);
				game.playGame();
			}
		} else {
			game.changeCurrentState(stateToLoad);
		}

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
