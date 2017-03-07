package model;

import java.util.Timer;

public class SpeedGameDriver extends GameDriver{
	
	private Timer timer;
	
	public SpeedGameDriver(){
		super(playerBlack, playerBlack, history, currentState, saveManager);
	}
	
	public void onTimeOut(){
		
	}
	
	public void setTimeLimit(){
		
	}

}
