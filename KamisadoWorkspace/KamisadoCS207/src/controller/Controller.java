package controller;

import java.awt.EventQueue;
import java.io.Serializable;

import javax.swing.JOptionPane;

import model.GameDriver;
import model.GeneralSettings;
import model.GeneralSettingsManager;
import model.Move;
import model.MusicPlayer;
import model.SaveManager;
import model.SpeedGameDriver;
import model.TimerInfo;
import player.EasyAIPlayer;
import player.GUIPlayer;
import player.HardAIPlayer;
import player.Player;
import player.TreeNode;
import view.MenuFrame;
import view.RunningGameView;

public class Controller implements Serializable {

	private GameDriver game;
	private RunningGameView main;
	private MenuFrame menuFrame;
	private Player playerWhite;
	private Player playerBlack;
	transient private MusicPlayer musicPlayer;
	transient private GeneralSettingsManager manager;

	public Controller() {
		main = new RunningGameView("DefaultWhite", "DefaultBlack", this);
		menuFrame = new MenuFrame(this, main);
		menuFrame.setVisible(true);
		main.setGlassPane(menuFrame.getGlassPane());
		musicPlayer = new MusicPlayer();
		applySettings();
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
			playGame(isSpeedGame, gameLength, timerTime, randomBoard);
			playerBlack.addObserver(game);
		} else {
			if (isEasyAI) {
				playerWhite = new EasyAIPlayer("White", whiteName, true);
			} else {
				playerWhite = new HardAIPlayer("White", whiteName, true);
			}
			playerBlack = new GUIPlayer("Black", blackName, false, this);
			playGame(isSpeedGame, gameLength, timerTime, randomBoard);
			playerWhite.addObserver(game);
		}
		finishGameSetup();
	}

	public void playTwoPlayer(boolean isSpeedGame, String whiteName, String blackName, int timerTime, int gameLength,
			boolean randomBoard) {
		initialisePlayers(whiteName, blackName);
		playGame(isSpeedGame, gameLength, timerTime, randomBoard);
		finishGameSetup();
	}

	private void playGame(boolean isSpeedGame, int gameLength, int timerTime, boolean randomBoard) {
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
			game.changeCurrentState(gameDriver.getCurrentState());
			finishGameSetup();
			
			// main.displayGame(game.getCurrentState());
			// main.displaycSelectable(gameDriver.getCurrentState().getValidMoves());
			return true;
		}
		return false;
	}

	private void finishGameSetup() {
		main.getGameBoard().addObserver(game);
		menuFrame.ShowPanel("Game View");
		main.displayGame(game);
		game.addObserver(main);

		game.playGame();
		JOptionPane.showMessageDialog(null,
				"1. Press Tab to start moving the selected tile 2. Highlighted tiles indicates the valid moves",
				"Instructions", JOptionPane.INFORMATION_MESSAGE);
	}

	public void applySettings() {
		manager = new GeneralSettingsManager();
		GeneralSettings settings = manager.getGeneralSettings();
		if (settings != null) {
			if (settings.isMusicOn()) {
					try {
						musicPlayer.musicOn();
						musicPlayer.setVolume(settings.getVolume());
					} catch (Exception e) {
						e.printStackTrace();
					}
			}else musicPlayer.musicOff();
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
	


	public Player getPlayerWhite() {
		return playerWhite;
	}

	public Player getPlayerBlack() {
		return playerBlack;
	}

	public void rematch() {
		playerWhite.setScore(0);
		playerBlack.setScore(0);
		if(game instanceof SpeedGameDriver){
			game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, game.getScoreToGet(), ((SpeedGameDriver) game).getTimerInfo().getTimerLimit(), game.getCurrentState().getBoard().isRandom());
			game.addObserver(main.getGameTimer());
			game.tellAll(((SpeedGameDriver) game).getTimerInfo());
		}else{
			game = new GameDriver(playerWhite, playerBlack, playerWhite, game.getScoreToGet(), game.getCurrentState().getBoard().isRandom());
		}
		if (game.getCurrentState().getPlayerBlack().isAI()) {
			game.getCurrentState().getPlayerBlack().addObserver(game);
		} else if (game.getCurrentState().getPlayerWhite().isAI()) {
			game.getCurrentState().getPlayerWhite().addObserver(game);
		}
		playerWhite.setToFirstMove(true);
		playerBlack.setToFirstMove(false);
		finishGameSetup();
	}

	public void showHint() {
		if(!game.getCurrentState().isFirstMove() && !game.getCurrentState().isGameOver()){
			Player playerToMove = game.getCurrentState().getPlayerToMove();
			if(!playerToMove.isAI()){
				if(playerToMove.getHomeRow() == 0){
					TreeNode moveTree = new TreeNode(5, game.getCurrentState(), 1);
					Move move = moveTree.getBestOrWorstsChild(false);
					move.print();
					main.showHint(move.getEndPos());
				}else {
					TreeNode moveTree = new TreeNode(5, game.getCurrentState(), 0);
					Move move = moveTree.getBestOrWorstsChild(true);
					move.print();
					main.showHint(move.getEndPos());
				}
			}
		}
	}
}
