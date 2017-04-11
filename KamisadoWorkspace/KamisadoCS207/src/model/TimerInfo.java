package model;

import java.io.Serializable;

public class TimerInfo implements Serializable{
	private int timerLimit;
	private int timeLeft;
	
	public TimerInfo(int timerLimit, int timeLeft){
		this.timerLimit = timerLimit;
		this.timeLeft = timeLeft;
	}

	public int getTimerLimit() {
		return timerLimit;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimerLimit(int timerLimit) {
		this.timerLimit = timerLimit;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

}
