package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Stack;

import javax.swing.Timer;

import player.Player;

public class SpeedGameDriver extends GameDriver implements MyObserver, MyObservable, Serializable {

	private Timer timer;
	private int timerLimit;
	private int time;
	private boolean firstMove = true;
	private boolean gameOver = false;

	public SpeedGameDriver(Player white, Player black, Player playerToStart, int timerLimit) {
		super(white, black, playerToStart);
		this.timerLimit = timerLimit;
		currentState.setTime(timerLimit);
	}
	public SpeedGameDriver(State currentState) {
		super(currentState);
	}

	public void onTimeOut() {
		gameOver = true;
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
		time = timerLimit;
		currentState.setTime(timerLimit);
		tellAll(currentState.getTime());
		if (timer != null) {
			timer.stop();
		}

		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentState.setTime(currentState.getTime()-1);
				tellAll(currentState.getTime());
				if (currentState.getTime() <= 0) {
					onTimeOut();
				}
			}
		});
		timer.start();
	}
	public void undo() {
		if (!history.empty()) {
			turnBegin();
			gameOver = false;
			currentState = history.pop();
			this.tellAll(currentState.getBoard());
			this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));

		}
	}
	public void reset() {
		boolean valid = false;
		while (!history.empty()) {
			currentState = history.pop();
			valid = true;
		}
		if(valid){
			turnBegin();
			gameOver = false;
			firstMove = true;
			this.tellAll(currentState.getBoard());
			this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
		}
	}
	public void saveGame() {
		timer.stop();
		currentState.setTime(time);
		SaveManager s = new SaveManager();
		s.save(currentState);
		timer.start();
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if (arg instanceof Position) {
			if (!gameOver) {
				if (firstMove) {
					if (!playerFirstMove((Position) arg)) {
						if (tryToMove((Position) arg)) {
							firstMove = false;
							nextTurn(0);
							turnBegin();
						}
					}
				} else if (tryToMove((Position) arg)) {
					if (playTurn((Position) arg)) {
						timer.stop();
						gameOver = true;
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
