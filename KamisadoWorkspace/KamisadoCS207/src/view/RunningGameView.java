package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

import controller.Controller;
import model.GameDriver;
import model.GeneralSettings;
import model.GeneralSettingsManager;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import model.State;
import player.Player;

public class RunningGameView extends JPanel implements MyObserver {

	private static final long serialVersionUID = 1L;
	private final Icon SELECTED = new ImageIcon(getClass().getResource("/images/Selected.png"));
	private JPanel teamLabel;
	private GUIBoardView gameBoard;
	private InGameOptions inGameOptions;
	private GameTimer timer;
	private JLabel teamWhite;
	private JLabel teamBlack;
	private JTextArea gameLog;
	private JPanel glassPane;
	private Controller controller;
	private JPanel gridViewGlassPane;
	private JLabel musicSwitch;
	private JLabel soundSwitch;
	private GeneralSettingsManager settingManager;
	private GeneralSettings settings;

	public RunningGameView(Controller newController) {
		this.controller = newController;
		gameBoard = new GUIBoardView(newController);
		inGameOptions = new InGameOptions(newController);
		inGameOptions.setPreferredSize(new Dimension(300, 50));
		setUpTeamLabels();
		setUpTimer();
		setUpSoundOptions();
		this.setFocusable(false);

		this.setLayout(new BorderLayout());
		this.add(timer, BorderLayout.NORTH);
		this.add(teamLabel, BorderLayout.EAST);
		this.add(inGameOptions, BorderLayout.SOUTH);
		this.add(gameBoard, BorderLayout.CENTER);
		this.setBounds(100, 100, 522, 482);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				gameBoard.requestFocusInWindow();
			}
		});
	}

	public void displayGame(GameDriver game) {
		State gameState = game.getCurrentState();
		Player black = gameState.getPlayerBlack();
		Player white = gameState.getPlayerWhite();
		teamBlack.setText(black.getPlayerName() + " : " + black.getScore());
		teamWhite.setText(white.getPlayerName() + " : " + white.getScore());
		gameLog.setText(null);
		gameLog.append("Round " + game.getCurrentGameNum() + ":" + "                   First to " + game.getScoreToGet()
				+ "\n");
		gameLog.setFocusable(false);
		gameBoard.redrawBoard(gameState.getBoard());
		displaySelectable(gameState.getValidMoves());
		setUpGridView();
		gridViewGlassPane.setVisible(true);
		gridViewGlassPane.setOpaque(false);
		inGameOptions.displayRematch(false);
		inGameOptions.displayContinue(false);
		//inGameOptions.showUndo(false);
		//inGameOptions.displayHint(false);
		//inGameOptions.displaySave(true);
		displayHintSaveUndo(false);
		if(!controller.isNetworking()){
			inGameOptions.displaySave(true);
		}
	}

	public void setUpTeamLabels() {
		gameLog = new JTextArea(1, 20);
		gameLog.setEditable(false);
		gameLog.setLineWrap(true);
		gameLog.append("Round 1:" + "\n");
		gameLog.setFocusable(false);
		JScrollPane scroll = new JScrollPane(gameLog);
		teamLabel = new JPanel();
		teamLabel.setFocusable(false);
		teamWhite = setUpLabel("");
		teamBlack = setUpLabel("");
		teamLabel.setLayout(new BorderLayout());
		teamLabel.add(teamBlack, BorderLayout.NORTH);
		teamLabel.add(scroll, BorderLayout.CENTER);
		teamLabel.add(teamWhite, BorderLayout.SOUTH);
	}

	private JLabel setUpLabel(String name) {
		JLabel label = new JLabel(name + " : 0");
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Garamond", Font.BOLD, 19));
		label.setOpaque(true);
		return label;
	}

	public GUIBoardView getGameBoard() {
		return gameBoard;
	}

	public GameTimer getGameTimer() {
		return timer;
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if (arg instanceof ArrayList<?>) {
			displaySelectable((ArrayList<Position>) arg);
		} else if (arg instanceof Player) {
			Player player = ((Player) arg);
			String displayMessage = player.getPlayerName() + " wins this round!";
			roundOrGameOver(displayMessage);
			inGameOptions.displayContinue(true);
		} else if (arg instanceof String) {
			roundOrGameOver((String) arg);
			inGameOptions.displayRematch(true);
		} else if (arg instanceof State) {
			State state = (State) arg;
			GameDriver gameDriver = (GameDriver) o;
			if (!state.isGameOver()) {
				if ((state.isFirstMove() && state.getPreviousMove() == null) || controller.isNetworking()) {
					displayHintSaveUndo(false);
					if(!controller.isNetworking()){
						inGameOptions.displaySave(true);
					}
				} else if ((state.getPlayerBlack().isAI() || state.getPlayerWhite().isAI())) {
					displayHintSaveUndo(true);
				}
				
				gameBoard.redrawBoard(state.getBoard());
			}
			if (state.getPreviousMove() != null) {
				addToGameLog(state.getPreviousMove().toString());
				gameBoard.showPreviousLocation(state.getPreviousMove().getStartPos());
			} else {
				gameLog.setText(null);
				gameLog.append("Round " + gameDriver.getCurrentGameNum() + ":" + "                   First to "
						+ gameDriver.getScoreToGet() + "\n");
			}
			updateTeamScores(state.getPlayerWhite(), state.getPlayerBlack());
		} else if (arg instanceof Boolean) {
			if (!(Boolean) arg) {
				glassPane.removeAll();
				ImageIcon gif = new ImageIcon(getClass().getResource("/images/AI Wait.gif"));
				JLabel label = new JLabel(gif);
				addTextToGlassPane(label);
				glassPane.repaint();
			}
			gameBoard.setButtonsClickable((Boolean) arg);
		} else if (arg instanceof Position) {
			gameBoard.showHint((Position) arg);
		}
	}
	
	public void displayHintSaveUndo(boolean b){
		inGameOptions.displayHint(b);
		inGameOptions.displaySave(b);
		inGameOptions.showUndo(b);
	}

	private void roundOrGameOver(String gameMessage) {
		displayHintSaveUndo(false);
		JLabel label = new JLabel();
		glassPane.removeAll();
		label.setText(gameMessage);
		addTextToGlassPane(label);
	}

	private void updateTeamScores(Player playerWhite, Player playerBlack) {
		teamWhite.setText(playerWhite.getPlayerName() + " : " + playerWhite.getScore());
		teamBlack.setText(playerBlack.getPlayerName() + " : " + playerBlack.getScore());
	}

	public void addToGameLog(String message) {
		try {
			boolean dupe = false;
			for (int i = 0; i < gameLog.getLineCount() - 1; i++) {
				int start = gameLog.getLineStartOffset(i);
				int end = gameLog.getLineEndOffset(i);
				gameLog.setSelectionStart(start);
				gameLog.setSelectionEnd(end);
				String currentLine = gameLog.getSelectedText().trim();
				if (currentLine.equals(message)) {
					gameLog.replaceRange("", end, gameLog.getLineEndOffset(gameLog.getLineCount() - 1));
					dupe = true;
					break;
				}
			}
			if (!dupe) {
				gameLog.append(message + "\n");
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void setGlassPane(Component glassPane) {
		this.glassPane = (JPanel) glassPane;
		gameBoard.setGlassPane(glassPane);
	}

	public void setUpTimer() {
		if (timer != null) {
			this.remove(timer);
		}
		timer = new GameTimer();
		timer.setVisible(true);
		timer.setFocusable(false);
		this.add(timer, BorderLayout.NORTH);
	}

	private void addTextToGlassPane(JLabel label) {
		label.setBounds(0, 250, 587, 50);
		label.setBackground(Color.BLACK);
		label.setFont(new Font("Garamond", Font.BOLD | Font.ITALIC, 27));
		label.setForeground(Color.WHITE);
		label.setVisible(true);
		label.setOpaque(true);
		glassPane.add(label);
		glassPane.repaint();
	}

	public void displaySelectable(ArrayList<Position> positions) {
		gameBoard.removeSelectable();
		Double gameBoardPosX = gameBoard.getBounds().getMinX();
		Double gameBoardPosY = gameBoard.getBounds().getMinY();
		for (Position pos : positions) {
			JLabel label = new JLabel();
			label.setIcon(SELECTED);
			label.setBounds(((pos.getX()) * 70) + gameBoardPosX.intValue() + 5,
					((7 - (pos.getY())) * 70) + gameBoardPosY.intValue() + 30, 70, 70);
			glassPane.add(label);
			glassPane.repaint();
		}
	}

	public void toggleGridView(boolean toggle) {
		if (toggle) {
			controller.getMenuFrame().setGlassPane(gridViewGlassPane);
			gridViewGlassPane.setVisible(true);
			gameBoard.setFocusable(false);
			gameBoard.setButtonsClickable(false);
		} else {
			controller.getMenuFrame().setGlassPane(glassPane);
			gameBoard.setButtonsClickable(true);
			gameBoard.setFocusable(true);
		}
		controller.getMenuFrame().getGlassPane().repaint();
	}

	public void setUpGridView() {
		gridViewGlassPane = new JPanel();
		gridViewGlassPane.setBounds(glassPane.getBounds());
		Double gameBoardPosX = gameBoard.getBounds().getMinX();
		Double gameBoardPosY = gameBoard.getBounds().getMinY();
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x <= 7; x++) {
				JLabel label = new JLabel();
				label.setText("[" + x + ":" + y + "]");
				label.setOpaque(true);
				label.setVisible(true);
				label.setBounds((x * 70) + gameBoardPosX.intValue() + 27,
						((7 - y) * 70) + gameBoardPosY.intValue() + 30, 25, 11);
				label.setPreferredSize(label.getSize());
				gridViewGlassPane.add(label);
				gridViewGlassPane.repaint();
			}
		}
		gridViewGlassPane.setLayout(null);
		gridViewGlassPane.setIgnoreRepaint(true);
	}

	public void showHint(Position endPos) {
		gameBoard.showHint(endPos);
	}

	private void setUpSoundOptions() {
		settingManager = new GeneralSettingsManager();
		settings = settingManager.getGeneralSettings();

		ImageIcon soundSwitchImage = null;
		if (settings.isSoundOn()) {
			soundSwitchImage = new ImageIcon(getClass().getResource("/images/soundon.png"));
		} else {
			soundSwitchImage = new ImageIcon(getClass().getResource("/images/soundoff.png"));
		}

		soundSwitch = new JLabel(soundSwitchImage);
		soundSwitch.setBounds(700, 603, 30, 30);
		soundSwitch.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				soundSwitchActivated();
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		add(soundSwitch);
		soundSwitch.setFocusable(false);
		this.requestFocus();
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK), "soundSwitch");
		this.getActionMap().put("soundSwitch", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				soundSwitchActivated();

			}
		});

		ImageIcon musicSwitchImage = null;
		if (settings.isMusicOn()) {
			musicSwitchImage = new ImageIcon(getClass().getResource("/images/musicon.png"));
		} else {
			musicSwitchImage = new ImageIcon(getClass().getResource("/images/musicoff.png"));
		}

		musicSwitch = new JLabel(musicSwitchImage);
		musicSwitch.setBounds(730, 603, 30, 30);
		musicSwitch.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				musicSwitchActivated();
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		add(musicSwitch);
		musicSwitch.setFocusable(false);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_DOWN_MASK), "musicSwitch");
		this.getActionMap().put("musicSwitch", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				musicSwitchActivated();
			}

		});
	}

	private void musicSwitchActivated() {
		if (settings.isMusicOn()) {
			musicSwitch.setIcon(new ImageIcon(getClass().getResource("/images/musicoff.png")));
			settings.setMusicOn(false);
		} else {
			musicSwitch.setIcon(new ImageIcon(getClass().getResource("/images/musicon.png")));
			controller.applySettings();
			settings.setMusicOn(true);
		}
		settingManager.saveGeneralSettings(settings);
		controller.applySettings();

	}

	public InGameOptions getInGameOptions() {
		return inGameOptions;
	}

	public void soundSwitchActivated() {
		if (settings.isSoundOn()) {
			soundSwitch.setIcon(new ImageIcon(getClass().getResource("/images/soundoff.png")));
			settings.setSoundOn(false);
		} else {
			soundSwitch.setIcon(new ImageIcon(getClass().getResource("/images/soundon.png")));
			settings.setSoundOn(true);
		}
		settingManager.saveGeneralSettings(settings);
	}

}
