package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import player.Player;

public class SpeedGameDriver extends GameDriver implements MyObserver, MyObservable {

	private Timer timer;
	private int timerLimit;
	private int time;
	private boolean firstMove = true;
	private boolean gameOver = false;

	public SpeedGameDriver(Player white, Player black, Player playerToStart,
			int timerLimit) {
		super(white, black, playerToStart);
		this.timerLimit = timerLimit;
		System.out.println("SEIT");
	}

	public void onTimeOut() {
		gameOver = true;
		Player winningPlayer;
		if(currentState.getPlayerToMove().equals(currentState.getPlayerWhite())){
			winningPlayer = currentState.getPlayerBlack();
		}else{
			winningPlayer = currentState.getPlayerWhite();
		}		
		this.tellAll(winningPlayer.getPlayerTeam());
		timer.stop();
		System.out.println("GameOver");
	}

	public void turnBegin() {
		time = timerLimit;
		tellAll(time);
		if (timer != null) {
			timer.stop();
		}

		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("time" + time);
				tellAll(--time);
				System.out.println("time" + time);
				if (time <= 0) {
					onTimeOut();
				}
			}
		});
		timer.start();
	}

	public void update(MyObservable o, Object arg) {
		if (arg instanceof Position) {
			System.out.println("update gamedriver");
			if (!gameOver) {
				if (firstMove) {
					turnBegin();
					if (playerFirstMove((Position) arg)) {
						nextTurn(0);
						firstMove = false;
					}
				} else {
					if (tryToMove((Position) arg)) {
						if (playTurn((Position) arg)) {
							timer.stop();
							gameOver = true;
						} else {
							turnBegin();
						}
					}

				}
				generateMove();
			}

		}
	}
}
