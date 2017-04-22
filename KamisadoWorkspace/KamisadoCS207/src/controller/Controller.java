package controller;

import java.io.Serializable;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.GameDriver;
import model.GeneralSettings;
import model.GeneralSettingsManager;
import model.Move;
import model.MusicPlayer;
import model.SaveManager;
import model.SpeedGameDriver;
import model.TimerInfo;
import networking.Client;
import networking.Server;
import player.EasyAIPlayer;
import player.GUIPlayer;
import player.HardAIPlayer;
import player.Player;
import player.TreeNode;
import view.MenuFrame;
import view.RunningGameView;

public class Controller implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameDriver game;
	private RunningGameView main;
	transient private MenuFrame menuFrame;
	private Player playerWhite;
	private Player playerBlack;
	transient private MusicPlayer musicPlayer;
	// transient private GeneralSettingsManager manager;
	/// transient private GeneralSettings settings;
	transient private boolean networkGame = false;
	transient private Client client;

	public Controller() {
		menuFrame = new MenuFrame(this);
		menuFrame.setVisible(true);
		musicPlayer = new MusicPlayer();
		applySettings();
	}

	public void initialisePlayers(String whiteName, String blackName) {
		playerWhite = new GUIPlayer("TeamWhite", whiteName, true, this);
		playerBlack = new GUIPlayer("TeamBlack", blackName, false, this);
	}

	public void playSinglePlayer(boolean userToMoveFirst, boolean isSpeedGame, boolean isEasyAI, String whiteName,
			String blackName, int timerTime, int gameLength, boolean randomBoard) {
		networkGame = false;
		if (userToMoveFirst) {
			if (isEasyAI) {
				playerBlack = new EasyAIPlayer("TeamBlack", blackName, false);
			} else {
				playerBlack = new HardAIPlayer("TeamBlack", blackName, false);
			}
			playerWhite = new GUIPlayer("TeamWhite", whiteName, true, this);
			playGame(isSpeedGame, gameLength, timerTime, randomBoard);
			playerBlack.addObserver(game);
		} else {
			if (isEasyAI) {
				playerWhite = new EasyAIPlayer("TeamWhite", whiteName, true);
			} else {
				playerWhite = new HardAIPlayer("TeamWhite", whiteName, true);
			}
			playerBlack = new GUIPlayer("TeamBlack", blackName, false, this);
			playGame(isSpeedGame, gameLength, timerTime, randomBoard);
			playerWhite.addObserver(game);
		}
		finishGameSetup();
		System.out.println(getMenuFrame().getFocusOwner().getClass());
		System.out.println(getMenuFrame().getFocusOwner());
	}

	public void playTwoPlayer(boolean isSpeedGame, String whiteName, String blackName, int timerTime, int gameLength,
			boolean randomBoard) {
		networkGame = false;
		initialisePlayers(whiteName, blackName);
		playGame(isSpeedGame, gameLength, timerTime, randomBoard);
		finishGameSetup();
	}

	public boolean playNetwork(boolean hosting, boolean isSpeedGame, String whiteName, String blackName, int timerTime,
			int gameLength) {
		GeneralSettingsManager manager = new GeneralSettingsManager();
		GeneralSettings settings = manager.getGeneralSettings();
		int gameL;
		networkGame = true;
		if (hosting) {

			// Thread hostingThread = new Thread(new Runnable(){
			//
			// @Override
			// public void run() {
			// JOptionPane pane = new JOptionPane("Waiting for opponent...",
			// JOptionPane.CANCEL_OPTION);
			// pane.createDialog("waiting...");
			// final JDialog dialog = pane.createDialog("Board Sent");
			// pane.setVisible(true);
			// dialog.setVisible(true);
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// dialog.setVisible(false);
			// pane.setVisible(false);
			// }
			//
			// });
			// hostingThread.start();
			Server host = new Server(gameLength);
			Thread hostThread = new Thread(host);
			hostThread.start();

			playerBlack = new GUIPlayer("TeamBlack", blackName, false, this);
			client = new Client("TeamWhite", whiteName, blackName, true, true, this, "localhost");
			client.tryConnect();
			playerWhite = client;
			Thread newThread = new Thread(client);
			newThread.start();

			gameL = gameLength;

		} else {
			Thread joining = new Thread(new Runnable() {
				@Override
				public void run() {
					// JOptionPane.showMessageDialog(null, "connecting");
					JOptionPane pane = new JOptionPane("Connecting...", JOptionPane.INFORMATION_MESSAGE);
					final JDialog dialog = pane.createDialog("Board Sent");
					dialog.setVisible(true);

					dialog.setVisible(false);

					return;
				}
			});
			joining.start();
			playerWhite = new GUIPlayer("TeamWhite", whiteName, true, this);
			String ip = JOptionPane.showInputDialog("Please enter hosts IP", settings.getRecentIP());
			settings.setRecentIP(ip);
			manager.saveGeneralSettings(settings);
			client = new Client("TeamBlack", blackName, whiteName, false, false, this, ip);
			if (!client.tryConnect()) {
				return false;
			}
			playerBlack = client;
			Thread newThread = new Thread(client);
			newThread.start();
			gameL = client.getGameLengthFromServer();

		}
		playGame(false, gameL, timerTime, false);

		main.getGameBoard().addObserver(playerBlack);
		main.getGameBoard().addObserver(playerWhite);
		playerBlack.addObserver(game);
		playerWhite.addObserver(game);
		System.out.println("finishing game setup");
		finishGameSetup();
		return true;
	}

	private void playGame(boolean isSpeedGame, int gameLength, int timerTime, boolean randomBoard) {
		setUpRunningGameView();
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
			setUpRunningGameView();
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

			return true;
		}
		return false;
	}

	private void finishGameSetup() {
		main.getGameBoard().addObserver(game);
		main.displayGame(game);
		game.addObserver(main);
		game.playGame();
	}

	public void applySettings() {
		GeneralSettingsManager manager = new GeneralSettingsManager();
		GeneralSettings settings = manager.getGeneralSettings();
		if (settings != null) {
			if (settings.isMusicOn()) {
				try {
					musicPlayer.musicOn();
					musicPlayer.setVolume(settings.getMusicVolume());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				musicPlayer.musicOff();
		}
	}

	public MenuFrame getMenuFrame() {
		return menuFrame;
	}

	public void setUpRunningGameView() {
		main = new RunningGameView(this);
		main.setGlassPane(menuFrame.getGlassPane());
		menuFrame.addPanel(main, "Game View");
		menuFrame.ShowPanel("Game View");
	}

	public Player getPlayerWhite() {
		return playerWhite;
	}

	public Player getPlayerBlack() {
		return playerBlack;
	}

	public void rematchNonNetwork() {
		System.out.println("REMATCHING");
		playerWhite = game.getCurrentState().getPlayerWhite();
		playerBlack = game.getCurrentState().getPlayerBlack();
		playerWhite.setScore(0);
		playerBlack.setScore(0);
		if (game instanceof SpeedGameDriver) {
			game = new SpeedGameDriver(playerWhite, playerBlack, playerWhite, game.getScoreToGet(),
					((SpeedGameDriver) game).getTimerInfo().getTimerLimit(),
					game.getCurrentState().getBoard().isRandom());
			game.addObserver(main.getGameTimer());
			game.tellAll(((SpeedGameDriver) game).getTimerInfo());
		} else {
			game = new GameDriver(playerWhite, playerBlack, playerWhite, game.getScoreToGet(),
					game.getCurrentState().getBoard().isRandom());
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

	public void rematch() {
		if (networkGame) {
			client.askToRematch();
			main.getInGameOptions().displayRematch(true);
		} else {
			rematchNonNetwork();
		}
	}

	public void showHint() {
		if (!game.getCurrentState().isFirstMove() && !game.getCurrentState().isGameOver()) {
			Player playerToMove = game.getCurrentState().getPlayerToMove();
			if (!playerToMove.isAI()) {
				if (playerToMove.getHomeRow() == 0) {
					TreeNode moveTree = new TreeNode(5, game.getCurrentState(), 1);
					Move move = moveTree.getBestOrWorstsChild(false);
					main.showHint(move.getEndPos());
				} else {
					TreeNode moveTree = new TreeNode(5, game.getCurrentState(), 0);
					Move move = moveTree.getBestOrWorstsChild(true);
					main.showHint(move.getEndPos());
				}
			}
		}
	}

	public int continueGame() {
		if (networkGame) {
			client.askToContinue();
			return -1;
		} else {
			return game.nextRound();
		}
	}

	public void networkContinue() {
		main.getInGameOptions().displayContinue(false);
		if (game.nextRound() >= 0) {
		}
	}

	public void networkRematch() {
		rematchNonNetwork();
		main.getInGameOptions().displayRematch(false);
	}

	public boolean isNetworking() {
		return networkGame;
	}

	public void quit() {
		menuFrame.ShowPanel("New Game");
		if (networkGame) {
			client.disconnected();
		}

	}
}
