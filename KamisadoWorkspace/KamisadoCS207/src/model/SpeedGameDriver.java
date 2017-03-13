package model;

import java.util.Timer;

import player.Player;

public class SpeedGameDriver extends GameDriver implements MyObserver {

	private Timer timer;
	private int timerLimit;

	public SpeedGameDriver(Player white, Player black, MyObserver observerToState, Player playerToStart,
			int timerLimit) {
		super(white, black, observerToState, playerToStart);
		this.timerLimit = timerLimit;
		timer = new Timer();
	}

	public void onTimeOut() {

	}

	@Override
	public void update(MyObservable o, Object arg) {
	}
}
