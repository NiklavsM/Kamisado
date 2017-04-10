package controller;

import java.awt.EventQueue;
import java.io.File;
import java.io.Serializable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import model.GameDriver;
import model.GeneralSettings;
import model.GeneralSettingsManager;
import model.SaveManager;
import model.SpeedGameDriver;
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
	private static Clip clip;

	public Controller() {
		applySettings();
		main = new RunningGameView("DefaultWhite", "DefaultBlack", this);
		menuFrame = new MenuFrame(this, main);
		menuFrame.setVisible(true);
		main.setGlassPane(menuFrame.getGlassPane());
	}

	public void initialisePlayers(String whiteName, String blackName) {
		playerWhite = new GUIPlayer("White", whiteName, true, this);
		playerBlack = new GUIPlayer("Black", blackName, false, this);
	}

	public void playSinglePlayer(boolean userToMoveFirst, boolean isSpeedGame, boolean isEasyAI, String whiteName,
			String blackName, int timerTime, int gameLength, boolean randomBoard) {
		if (userToMoveFirst) {
			playerWhite = new GUIPlayer("White", whiteName, true, this);
			if (isEasyAI) {
				playerBlack = new EasyAIPlayer("Black", blackName, false);
			} else {
				playerBlack = new HardAIPlayer("Black", blackName, false);
			}
			if (isSpeedGame) {
				game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, gameLength, timerTime, randomBoard);
				main.setTimerMax(timerTime);
				game.addObserver(main.getGameTimer());
				game.tellAll(timerTime);
			} else {
				game = new GameDriver(playerWhite, playerBlack, playerWhite, gameLength, randomBoard);
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
				game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, gameLength, timerTime, randomBoard);
				main.setTimerMax(timerTime);
				game.addObserver(main.getGameTimer());
				game.tellAll(timerTime);
			} else {
				game = new GameDriver(playerWhite, playerBlack, playerWhite, gameLength, randomBoard);
			}
			playerWhite.addObserver(game);
		}
		finishGameSetup();
	}

	public void playTwoPlayer(boolean isSpeedGame, String whiteName, String blackName, int timerTime, int gameLength,
			boolean randomBoard) {
		initialisePlayers(whiteName, blackName);
		if (isSpeedGame) {
			game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, gameLength, timerTime, randomBoard);
			main.setTimerMax(timerTime);
			game.addObserver(main.getGameTimer());
			game.tellAll(timerTime);
		} else {
			game = new GameDriver(playerWhite, playerBlack, playerWhite, gameLength, randomBoard);
		}
		finishGameSetup();
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
		GameDriver gameDriver = s.load();
		if (gameDriver != null) {
			if (gameDriver instanceof SpeedGameDriver) {
				game = new SpeedGameDriver((SpeedGameDriver) gameDriver);
				main.setTimerMax(((SpeedGameDriver) game).getTimerLimit());
				game.addObserver(main.getGameTimer());
				game.tellAll(gameDriver.getCurrentState().getTime());
			} else {
				game = new GameDriver(gameDriver);
			}

			if (gameDriver.getCurrentState().getPlayerBlack().isAI()) {
				gameDriver.getCurrentState().getPlayerBlack().addObserver(game);
			} else if (gameDriver.getCurrentState().getPlayerWhite().isAI()) {
				gameDriver.getCurrentState().getPlayerWhite().addObserver(game);
			}

			finishGameSetup();
			game.changeCurrentState(gameDriver.getCurrentState());
			// main.displayGame(game.getCurrentState());
			// main.displaycSelectable(gameDriver.getCurrentState().getValidMoves());
			return true;
		}
		return false;
	}

	private void finishGameSetup() {
		main.getGameBoard().addObserver(game);
		menuFrame.ShowGameViewPanel();
		main.displayGame(game.getCurrentState());
		game.addObserver(main);
		
		game.playGame();
		JOptionPane.showMessageDialog(null,
				"1. Press Tab to start moving the selected tile 2. Highlighted tiles indicates the valid moves",
				"Instructions", JOptionPane.INFORMATION_MESSAGE);
	}

	private void applySettings() {
		GeneralSettingsManager manager = new GeneralSettingsManager();
		GeneralSettings settings = manager.getGeneralSettings();
		if (settings != null) {
			if (settings.isMusicOn()) {
				try {
					music();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {// fix put in separate class
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Controller cont = new Controller();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuFrame getMenuFrame() {
		return menuFrame;
	}

	public static void music() throws Exception {
		AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("backgroundmusic.wav"));
		clip = AudioSystem.getClip();
		clip.open(inputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}
