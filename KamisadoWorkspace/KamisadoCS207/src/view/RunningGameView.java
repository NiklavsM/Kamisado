package view;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Move;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import player.Player;

public class RunningGameView extends JFrame implements MyObserver, KeyListener {

	private JPanel contentPane;
	private GUIBoardView gameBoard;
	private Controller controller;
	private GameTimer timer;

	public RunningGameView(Controller newController) {
		timer = new GameTimer();
		controller = newController;
		gameBoard = new GUIBoardView(newController);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 522, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(timer, BorderLayout.SOUTH);
		contentPane.add(gameBoard, BorderLayout.CENTER);

		setContentPane(contentPane);
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
}
