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
import model.TimerInfo;
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
	private Clip clip;

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
			if (isEasyAI) {
				playerBlack = new EasyAIPlayer("Black", blackName, false);
			} else {
				playerBlack = new HardAIPlayer("Black", blackName, false);
			}
			playerWhite = new GUIPlayer("White", whiteName, true, this);
			playSpeedGame(isSpeedGame, gameLength, timerTime, randomBoard);
			playerBlack.addObserver(game);
		} else {
			if (isEasyAI) {
				playerWhite = new EasyAIPlayer("White", whiteName, true);
			} else {
				playerWhite = new HardAIPlayer("White", whiteName, true);
			}
			playerBlack = new GUIPlayer("Black", blackName, false, this);
			playSpeedGame(isSpeedGame, gameLength, timerTime, randomBoard);
			playerWhite.addObserver(game);
		}
		finishGameSetup();
	}

	public void playTwoPlayer(boolean isSpeedGame, String whiteName, String blackName, int timerTime, int gameLength,
			boolean randomBoard) {
		initialisePlayers(whiteName, blackName);
		playSpeedGame(isSpeedGame, gameLength, timerTime, randomBoard);
		finishGameSetup();
	}

	private void playSpeedGame(boolean isSpeedGame, int gameLength, int timerTime, boolean randomBoard) {
		if (isSpeedGame) {
			game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, gameLength, timerTime, randomBoard);
			game.addObserver(main.getGameTimer());
			game.tellAll(new TimerInfo(timerTime, timerTime));
		} else {
			game = new GameDriver(playerWhite, playerBlack, playerWhite, gameLength, randomBoard);
		}
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
				game.addObserver(main.getGameTimer());
				game.tellAll(((SpeedGameDriver) game).getTimerInfo());
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
		menuFrame.ShowPanel("Game View");
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
					musicOn();
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
				Controller cont = new Controller();
			}
		});
	}

	public MenuFrame getMenuFrame() {
		return menuFrame;
	}

	public void musicOn() throws Exception {
		AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("backgroundmusic.wav"));
		if (clip != null) {
			clip.stop();
		}
		clip = AudioSystem.getClip();
		clip.open(inputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void musicOff() {
		if (clip != null) {
			clip.stop();
		}
	}

	public Player getPlayerWhite() {
		return playerWhite;
	}

	public Player getPlayerBlack() {
		return playerBlack;
	}
}
