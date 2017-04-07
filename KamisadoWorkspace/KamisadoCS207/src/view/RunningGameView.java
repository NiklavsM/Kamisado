package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Board;
import model.GameDriver;
import model.Move;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import model.State;
import player.Player;

public class RunningGameView extends JPanel implements MyObserver {

	private JPanel teamLabel;
	private GUIBoardView gameBoard;
	private InGameOptions inGameOptions;
	private GameTimer timer;
	private JLabel winnerLabel;
	private JLabel teamWhite;
	private JLabel teamBlack;
	private JPanel glassPane;

	public RunningGameView(String whiteName, String blackName, Controller newController) {
		timer = new GameTimer();
		timer.setVisible(true);
		timer.setFocusable(false);
		
		gameBoard = new GUIBoardView(newController);
		inGameOptions = new InGameOptions(newController);

		setUpTeamLabels(whiteName, blackName);

		this.setLayout(new BorderLayout());
		this.add(timer, BorderLayout.NORTH);
		this.add(teamLabel, BorderLayout.EAST);
		this.add(inGameOptions, BorderLayout.SOUTH);
		this.add(gameBoard, BorderLayout.CENTER);
		this.setBounds(100, 100, 522, 482);
	}
	
	public void displayGame(State state){
		Player black = state.getPlayerBlack();
		Player white = state.getPlayerWhite();
		teamBlack.setText(black.getPlayerName());
		teamWhite.setText(white.getPlayerName());
		winnerLabel.setText("");
		gameBoard.redrawBoard(state.getBoard());
		if(black.isAI() || white.isAI()){
			inGameOptions.showUndo(true);
		}else{
			inGameOptions.showUndo(false);
		}
	}

	public void setUpTeamLabels(String whiteName, String blackName){
		teamLabel = new JPanel();
		teamWhite = new JLabel(whiteName);
		teamBlack = new JLabel(blackName);
		teamLabel.setLayout(new BorderLayout());
		teamLabel.add(teamBlack, BorderLayout.NORTH);
		winnerLabel = new JLabel("");
		teamLabel.add(winnerLabel, BorderLayout.CENTER);
		teamLabel.add(teamWhite, BorderLayout.SOUTH);
		teamLabel.setFocusable(false);
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
			gameBoard.displaySelectable((ArrayList<Position>) arg);
		}else if (arg instanceof String) {
			JLabel label = new JLabel();
			glassPane.removeAll();
			switch ((String) arg) {
			case "White":
				label.setText("White wins this round!");
				inGameOptions.displayContinue(true);
				break;
			case "Black":
				label.setText("Black wins this round!");
				inGameOptions.displayContinue(true);
				break;
			case "Draw":
				label.setText("Draw!");
				inGameOptions.displayContinue(true);
				break;
			default:
				label.setText((String) arg);
				inGameOptions.displayContinue(false);
			}
			label.setBounds(50, 50, 1000, 500);
			label.setBackground(Color.white);
			label.setVisible(true);
			
			glassPane.add(label);
			glassPane.repaint();
			//gameBoard.setButtonsEnabled(false);
		} else if (arg instanceof Board) {
			gameBoard.redrawBoard((Board) arg);
		}
	}

	public void displaycSelectable(ArrayList<Position> validMoves) {
		gameBoard.removeSelectable();
		gameBoard.displaySelectable(validMoves);
	}

	public void setWinnerLabel(String message){
		winnerLabel.setText(message);
	}
	public void setTimerLabel(String message){
		timer.setText(message);
	}

	public void setGlassPane(Component glassPane) {
		this.glassPane = (JPanel) glassPane;
		gameBoard.setGlassPane(glassPane);
	}
}
