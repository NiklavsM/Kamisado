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

public class RunningGameView extends JPanel implements MyObserver, KeyListener {

	private JPanel teamLabel;
	private GUIBoardView gameBoard;
	private InGameOptions inGameOptions;
	//private JPanel contentPane;
	private GameTimer timer;

	public RunningGameView(String whiteName, String blackName,Controller newController) {
		timer = new GameTimer();
		timer.setVisible(true);
		timer.setText("  ");
        gameBoard = new GUIBoardView(newController);
        inGameOptions = new InGameOptions(newController);
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 522, 482);
        teamLabel = new JPanel();
        teamLabel.setLayout(new BorderLayout());
		teamLabel.add(new JLabel(blackName), BorderLayout.NORTH);
		teamLabel.add(new JLabel(whiteName), BorderLayout.SOUTH);
        this.setLayout(new BorderLayout());
       // this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.add(timer, BorderLayout.NORTH);
        
		this.add(teamLabel, BorderLayout.EAST);
		this.add(inGameOptions, BorderLayout.SOUTH);
		this.add(gameBoard, BorderLayout.CENTER);
        //setContentPane(contentPane);
	}

	public GUIBoardView getGameBoard() {
		return gameBoard;
	}
	public GameTimer getGameTimer(){
		return timer;
	}

	@Override
	public void update(MyObservable o, Object arg) {
		// System.out.println("got here1");
		if (arg instanceof ArrayList<?>) {
			// System.out.println("have been updated");
			gameBoard.displaySelectable((ArrayList<Position>) arg);
		} else if (arg instanceof Move) {
			System.out.println("got Move");
			gameBoard.removeSelectable();
			// System.out.println("endx: " + ((Move) arg).getEndPos().getX() + "
			// endy: "+ ((Move) arg).getEndPos().getY());
			gameBoard.pieceMoved(((Move) arg).getStartPos(), ((Move) arg).getEndPos());
		} else if (arg instanceof String) {
			switch ((String) arg) {
			case "White":
				System.out.println("White Wins!");
				break;
			case "Black":
				System.out.println("Black Wins!");
				break;
			case "Draw":
				System.out.println("Draw!");
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

	// public Position waitForClick() {
	// System.out.println("waiting for click from gameBoard");
	// return gameBoard.respondToClick();
	// }

	public void addObserver(Player player) {
		gameBoard.addObserver((MyObserver) player);
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
//	public void changeCurrentState(State newState){
//		gameBoard.displayBoard(newState);
//	}
}
