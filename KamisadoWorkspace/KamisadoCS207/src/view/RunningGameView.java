package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;
import model.Board;
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
		
		
		gameBoard = new GUIBoardView(newController);
		inGameOptions = new InGameOptions(newController);

		setUpTeamLabels(whiteName, blackName);
		setUpTimer();

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
		gameBoard.displaySelectable(state.getValidMoves());
		if(black.isAI() || white.isAI()){
			inGameOptions.showUndo(true);
		}else{
			inGameOptions.showUndo(false);
		}
	}

	public void setUpTeamLabels(String whiteName, String blackName){
		teamLabel = new JPanel();
		teamWhite = new JLabel(whiteName);
		teamWhite.setBackground(Color.BLACK);
		teamWhite.setForeground(Color.WHITE);
		teamWhite.setFont(new Font("Garamond", Font.BOLD, 15));
		teamWhite.setOpaque(true);
		teamBlack = new JLabel(blackName);
		teamBlack.setBackground(Color.BLACK);
		teamBlack.setForeground(Color.WHITE);
		teamBlack.setFont(new Font("Garamond", Font.BOLD, 15));
		teamBlack.setOpaque(true);
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
			label.setBounds(50, 250, 1000, 50);
			label.setBackground(Color.BLACK);
			label.setFont(new Font("Garamond", Font.BOLD | Font.ITALIC , 27));
			label.setForeground(Color.WHITE);
			label.setVisible(true);
			label.setOpaque(true);
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

	public void setGlassPane(Component glassPane) {
		this.glassPane = (JPanel) glassPane;
		gameBoard.setGlassPane(glassPane);
	}
	public void setUpTimer(){
		if(timer !=null){
			this.remove(timer);
		}
		timer = new GameTimer();
		timer.setVisible(true);
		timer.setFocusable(false);
		this.add(timer, BorderLayout.NORTH);
	}
}
