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

	public SpeedGameDriver(Player white, Player black, MyObserver observerToState, Player playerToStart,
			int timerLimit) {
		super(white, black, observerToState, playerToStart);
		this.timerLimit = timerLimit;
		System.out.println("SEIT");
	}

	public void onTimeOut() {
		gameOver = true;
		timer.stop();
		System.out.println("GameOver");
	}

	public void turnBegin() {
		time = timerLimit;
		if (timer != null) {
			timer.stop();
		}

		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("time" + time);
				tellAll(time);
				if (--time < 0) {
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
					if (playerFirstMove((Position) arg)) {
						nextTurn(0);
						firstMove = false;
						turnBegin();
					}
				} else {
					if (playTurn((Position) arg)) {
						gameOver = true;
					}
					turnBegin();
				}
				generateMove();
			}

		}
	}
}
