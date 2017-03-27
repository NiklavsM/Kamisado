package controller;

import java.awt.EventQueue;
import java.io.Serializable;

import javax.swing.JOptionPane;

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
		if (userToMoveFirst) {
			playerWhite = new GUIPlayer("White", whiteName, true, this);

			if (isEasyAI) {
				playerBlack = new EasyAIPlayer("Black", blackName, false);
			} else {
				playerBlack = new HardAIPlayer("Black", blackName, false);
			}
			if (isSpeedGame) {
				game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, timerTime);
				game.addObserver(main.getGameTimer());
				game.tellAll(timerTime);
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
				game.addObserver(main.getGameTimer());
				game.tellAll(timerTime);
			} else {
				game = new GameDriver(playerWhite, playerBlack, playerWhite);
			}
			playerWhite.addObserver(game);
		}
		main.getGameBoard().addObserver(game);
		main.displayGame(game.getCurrentState());
		game.addObserver(main);
		menuFrame.ShowGameViewPanel();
		game.playGame();
		JOptionPane.showMessageDialog(null,"1. Press Tab to start moving the selected tile 2. Highlighted tiles indicates the valid moves", "Instructions", JOptionPane.INFORMATION_MESSAGE);
	}

	public void playTwoPlayer(boolean isSpeedGame, String whiteName, String blackName, int timerTime) {
		initialisePlayers(whiteName, blackName);
		if (isSpeedGame) {
			game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, timerTime);
			game.addObserver(main.getGameTimer());
			game.tellAll(timerTime);
		} else {
			game = new GameDriver(playerWhite, playerBlack, playerWhite);
		}
		main.getGameBoard().addObserver(game);
		main.displayGame(game.getCurrentState());
		game.addObserver(main);
		menuFrame.ShowGameViewPanel();
		game.playGame();
		JOptionPane.showMessageDialog(null,"1. Press Tab to start moving the selected tile 2. Highlighted tiles indicates the valid moves", "Instructions", JOptionPane.INFORMATION_MESSAGE);
	}

	public GameDriver getGame() {
		return game;
	}

	public void killGame() {
		if (game != null) {
			main.getGameBoard().removeObserver(game);
			game.removeObserver(main);
			game.removeObserver(main.getGameTimer());
			game.setState(null);
			game = null;
		}
	}

	public boolean loadGame() {
		SaveManager s = new SaveManager();
		State stateToLoad = s.load();
		if (stateToLoad != null) {
			if (stateToLoad.getTime() > 0) {
				game = new SpeedGameDriver(stateToLoad);
				game.addObserver(main.getGameTimer());
				game.tellAll(stateToLoad.getTime());
			} else {
				game = new GameDriver(stateToLoad);
			}

			if (stateToLoad.getPlayerBlack().isAI()) {
				stateToLoad.getPlayerBlack().addObserver(game);
			} else if (stateToLoad.getPlayerWhite().isAI()) {
				stateToLoad.getPlayerWhite().addObserver(game);
			}

			main.getGameBoard().addObserver(game);
			main.displayGame(game.getCurrentState());
			game.addObserver(main);
			game.changeCurrentState(stateToLoad);
			game.playGame();
			menuFrame.ShowGameViewPanel();
			JOptionPane.showMessageDialog(null,"1. Press Tab to start moving the selected tile 2. Highlighted tiles indicates the valid moves", "Instructions", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		return false;
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