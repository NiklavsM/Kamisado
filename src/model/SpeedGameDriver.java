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

	public SpeedGameDriver(Player white, Player black, Player playerToStart, int timerLimit) {
		super(white, black, playerToStart);
		this.timerLimit = timerLimit;
		currentState.setTime(timerLimit);
		createTimer();
	}

	public SpeedGameDriver(State currentState) {
		super(currentState);
		this.timerLimit = currentState.getTimerLimit();
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
		tellAll(currentState.getTime());
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
			currentState.setTimerLimit(timerLimit);
			s.save(currentState);
			timer.start();
		}
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
						return;
					} else {
						turnBegin();
					}
				}
				generateMove();
			}
		}
	}
}
