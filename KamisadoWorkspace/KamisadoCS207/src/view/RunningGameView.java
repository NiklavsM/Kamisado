package view;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Board;
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

	public RunningGameView(String whiteName, String blackName, Controller newController) {
		timer = new GameTimer();
		timer.setText("Press Tab To Navigate window");
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
		teamBlack.setText(state.getPlayerBlack().getPlayerName());
		teamWhite.setText(state.getPlayerWhite().getPlayerName());
		winnerLabel.setText("");
		gameBoard.redrawBoard(state.getBoard());
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
			switch ((String) arg) {
			case "White":
				winnerLabel.setText("White Wins!");
				//System.out.println("White Wins!");
				break;
			case "Black":
				winnerLabel.setText("Black Wins!");
				//System.out.println("Black Wins!");
				break;
			case "Draw":
				winnerLabel.setText("Draw!");
				//System.out.println("Draw!");
			}
			gameBoard.setButtonsEnabled(false);
		} else if (arg instanceof Board) {
			gameBoard.redrawBoard((Board) arg);
		}
	}

	public void displaycSelectable(ArrayList<Position> validMoves) {
		gameBoard.removeSelectable();
		gameBoard.displaySelectable(validMoves);
	}

	public void addObserver(Player player) {
		gameBoard.addObserver((MyObserver) player);
	}
}
