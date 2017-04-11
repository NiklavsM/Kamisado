package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
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

	private final Icon SELECTED = new ImageIcon(getClass().getResource("/images/Selected.png"));
	private JPanel teamLabel;
	private GUIBoardView gameBoard;
	private InGameOptions inGameOptions;
	private GameTimer timer;
	private JLabel winnerLabel;
	private JLabel teamWhite;
	private JLabel teamBlack;
	private JPanel glassPane;
	private Controller controller;//Discuss

	public RunningGameView(String whiteName, String blackName, Controller newController) {
		
		this.controller = newController;
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
		teamBlack.setText(black.getPlayerName() + " : " + black.getScore());
		teamWhite.setText(white.getPlayerName() + " : " + white.getScore());
		winnerLabel.setText("");
		gameBoard.redrawBoard(state.getBoard());
		displaySelectable(state.getValidMoves());
		if(black.isAI() || white.isAI()){
			inGameOptions.showUndo(true);
		}else{
			inGameOptions.showUndo(false);
		}
	}

	public void setUpTeamLabels(String whiteName, String blackName){
		teamLabel = new JPanel();
		teamWhite = new JLabel(whiteName + " : 0");
		teamWhite.setBackground(Color.BLACK);
		teamWhite.setForeground(Color.WHITE);
		teamWhite.setFont(new Font("Garamond", Font.BOLD, 15));
		teamWhite.setOpaque(true);
		teamBlack = new JLabel(blackName + " : 0");
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
			displaySelectable((ArrayList<Position>) arg);
		}else if(arg instanceof Player){
			JLabel label = new JLabel();
			glassPane.removeAll();
			Player player = ((Player) arg);
			String displayMessage = player.getPlayerName() + " wins this round!";
			label.setText(displayMessage);
			addTextToGlassPane(label);
			inGameOptions.showUndo(false);
			inGameOptions.displayContinue(true);
		}else if (arg instanceof String) {
			inGameOptions.showUndo(false);
			JLabel label = new JLabel();
			glassPane.removeAll();
			label.setText((String) arg);
			inGameOptions.displayContinue(false);
			addTextToGlassPane(label);
		} else if(arg instanceof Board){
			if (controller.getPlayerBlack().isAI() || controller.getPlayerWhite().isAI()) {
				inGameOptions.showUndo(true);
			}
			gameBoard.redrawBoard((Board)arg);
		}else if (arg instanceof State) {
			State state = (State)arg;	
			updateTeamScores(state.getPlayerWhite(), state.getPlayerBlack());
		}else if(arg instanceof Boolean){
			if(!(Boolean)arg){
				glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}else{
				glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			gameBoard.setButtonsClickable((Boolean)arg);
		}
	}
	
	private void updateTeamScores(Player playerWhite, Player playerBlack){
		teamWhite.setText(playerWhite.getPlayerName() + " : " + playerWhite.getScore());
		teamBlack.setText(playerBlack.getPlayerName() + " : " + playerBlack.getScore());
	}

	public void displaycSelectable(ArrayList<Position> validMoves) {
		gameBoard.removeSelectable();
		displaySelectable(validMoves);
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
	
	private void addTextToGlassPane(JLabel label){
		label.setBounds(50, 250, 1000, 50);
		label.setBackground(Color.BLACK);
		label.setFont(new Font("Garamond", Font.BOLD | Font.ITALIC , 27));
		label.setForeground(Color.WHITE);
		label.setVisible(true);
		label.setOpaque(true);
		glassPane.add(label);
		glassPane.repaint();
	}
	
	public void displaySelectable(ArrayList<Position> positions){
		gameBoard.removeSelectable();
		Double gameBoardPosX = gameBoard.getBounds().getMinX();
		Double gameBoardPosY = gameBoard.getBounds().getMinY();
		for (Position pos : positions) {
			//buttons[pos.getX()][pos.getY()].setIcon(SELECTED);
			JLabel label = new JLabel();
			label.setIcon(SELECTED);
			label.setBounds(((pos.getX()) * 70) + gameBoardPosX.intValue() + 5, ((7 - (pos.getY())) * 70) + gameBoardPosY.intValue() + 30, 70, 70);
			glassPane.add(label);
			glassPane.repaint();
		}
	}
}
