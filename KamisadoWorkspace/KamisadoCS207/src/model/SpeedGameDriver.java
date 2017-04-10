package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import player.Player;

public class SpeedGameDriver extends GameDriver implements MyObserver, MyObservable, Serializable {

	private int timerLimit;
	private Timer timer;

	public SpeedGameDriver(Player white, Player black, Player playerToStart,int gameLength, int timerLimit, boolean randomBoard) {
		super(white, black, playerToStart, gameLength,randomBoard);
		this.timerLimit = timerLimit;
		currentState.setTime(timerLimit);
		createTimer();
	}

	public SpeedGameDriver(SpeedGameDriver gameDriver) {
		super(gameDriver);
		this.timerLimit = gameDriver.getTimerLimit();
		createTimer();
	}

	public void createTimer() {
		tellAll(timerLimit);
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentState != null) {
					currentState.setTime(currentState.getTime() - 1);
					tellAll(currentState.getTime());
					if (currentState.getTime() <= 0) {
						onTimeOut();
					}
				}
			}
		});
		timer.start();
	}

	public void onTimeOut() {
		currentState.setGameOver(true);
		Player winningPlayer;
		if (currentState.getPlayerToMove().equals(currentState.getPlayerWhite())) {
			winningPlayer = currentState.getPlayerBlack();
		} else {
			winningPlayer = currentState.getPlayerWhite();
		}
		this.tellAll(winningPlayer.getPlayerTeam());
		timer.stop();
	}

	public void turnBegin() {
		currentState.setTime(timerLimit);
		tellAll(timerLimit);
		timer.restart();
	}

	public void undo() {
		if (history.size() >= 2) {
			State stateToCheck = history.pop();
			String playerToMoveName = stateToCheck.getPlayerToMove().getPlayerName();
			while (playerToMoveName.equals("Easy AI") || playerToMoveName.equals("Hard AI")) {
				stateToCheck = history.pop();
				playerToMoveName = stateToCheck.getPlayerToMove().getPlayerName();
			}
			changeCurrentState(stateToCheck);
			turnBegin();
		}
	}

	public void reset() {
		boolean valid = false;
		while (!history.empty()) {
			currentState = history.pop();
			valid = true;
		}
		if (valid) {
			turnBegin();
			this.tellAll(currentState.getBoard());
			this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
		}
	}

	public void saveGame() {
		if (history.empty() || currentState.isGameOver()) {
			JOptionPane.showMessageDialog(null, "Game has not started / is ended", "Game has not started / is ended",
					JOptionPane.ERROR_MESSAGE);
		} else {
			timer.stop();
			SaveManager s = new SaveManager();
			s.save(this);
			timer.start();
		}
	}
	public int nextRound(){ //FIX
		currentState.getPlayerWhite().resetFirstMove();
		currentState.getPlayerBlack().resetFirstMove();
		int optionChosen = 0;
		currentGameNum++;
		history = new Stack<>();
		Player playerToMove = currentState.getPlayerWhite();
		String previousWinner = currentState.getPreviousMove().pieceMoved().getPiece().getTeam();
		Board newBoard = new Board(currentState.getBoard().isRandom());
		if(previousWinner.equals("White")){
			optionChosen = currentState.getPlayerWhite().fillHomeRow();
			playerToMove = currentState.getPlayerBlack();
		}
		else{
			optionChosen = currentState.getPlayerBlack().fillHomeRow();
		}
		if(optionChosen ==0){
			this.currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(),playerToMove, newBoard, true);
		}else if(optionChosen == 1){
			this.currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(),playerToMove, newBoard, false);
		}
		currentState.setFirstMove(true);
		this.tellAll(currentState.getBoard());
		tellAll(timerLimit);//FIX
		return optionChosen;
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if (arg instanceof Position) {
			if (!currentState.isGameOver()) {
				if (currentState.isFirstMove()) {
					if (!playerFirstMove((Position) arg)) {
						if (tryToMove((Position) arg)) {
							currentState.setFirstMove(false);
							nextTurn(0);
							turnBegin();
						}
					}
				} else if (tryToMove((Position) arg)) {
					if (playTurn((Position) arg)) {
						timer.stop();
						currentState.setGameOver(true);
						if (currentState.getPlayerToMove().equals(currentState.getPlayerWhite())) {							
							gameEnds(currentState.getPlayerWhite(), currentState.getPlayerBlack());
						} else {
							gameEnds(currentState.getPlayerBlack(), currentState.getPlayerWhite());
						}
						return;
					} else {
						turnBegin();
					}
				}
				generateMove();
			}
		}
	}

	public int getTimerLimit() {
		return timerLimit;
	}
}
