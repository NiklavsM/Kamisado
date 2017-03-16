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

	public RunningGameView(String whiteName, String blackName, Controller newController) {
		timer = new GameTimer();
		timer.setText("  ");
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

	public void setUpTeamLabels(String whiteName, String blackName){
		teamLabel = new JPanel();
		teamLabel.setLayout(new BorderLayout());
		teamLabel.add(new JLabel(blackName), BorderLayout.NORTH);
		winnerLabel = new JLabel("");
		teamLabel.add(winnerLabel, BorderLayout.CENTER);
		teamLabel.add(new JLabel(whiteName), BorderLayout.SOUTH);
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
		} 
//			else if (arg instanceof Move) {
//			System.out.println("got Move");
//			gameBoard.removeSelectable();
//
//			gameBoard.pieceMoved(((Move) arg).getStartPos(), ((Move) arg).getEndPos());
//		} 
		else if (arg instanceof String) {
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
			gameBoard.disableButtons();
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
