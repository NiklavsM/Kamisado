package player;

import java.io.Serializable;
import java.util.ArrayList;

import model.MyObservable;
import model.MyObserver;
import model.State;

public abstract class Player implements MyObservable,MyObserver, Serializable, Runnable{

	private String playerName;
	private String playerTeam;
    private int homeRow;
    private boolean goingFirst;
    private boolean isAI;
    ArrayList<MyObserver> observers = new ArrayList<>();
    private int score = 0;

    public Player(String playerTeam, String playerName, boolean goingFirst, boolean isAI){
        this.playerTeam = playerTeam;
        if(playerTeam.equals("TeamWhite")){
            homeRow = 0;
        }else{
            homeRow = 7;
        }
        this.playerName = playerName;
        this.goingFirst = goingFirst;
        this.isAI = isAI;
    }

    
    
    public void setGoingFirst(boolean goingFirst) {
		this.goingFirst = goingFirst;
	}

	public abstract void getMove(State state);

//    public void interupt(){
//
//    }
    
    public boolean getisFirst(){
        return goingFirst;
    }
    
    public String getPlayerTeam(){
        return playerTeam;
    }

    public int getHomeRow() {
        return homeRow;
    }
    public String getPlayerName() {
		return playerName;
	}
    
    @Override
	public void tellAll(Object arg) {
		for(MyObserver obs : observers){
			obs.update(this, arg);
		}
	}

	@Override
	public void addObserver(MyObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(MyObserver o) {
		if(observers.contains(o)){
			observers.remove(o);
		}
	}

	@Override
	public ArrayList<MyObserver> getObservers() {
		return observers;
	}

	public boolean isAI() {
		return isAI;
	}
	
	public void incrementScore(int increment){
		score += increment;
	}
	
	public int getScore(){
		return score;
	}
	
	public abstract int fillHomeRow();
	
	public abstract void setToFirstMove(boolean isGoingFirst);

	public void setScore(int i) {
		score = i;
	}
	public void wasValidMove() {
	}
	public void setName(String playerName2) {
		playerName = playerName2;
	}
	
}
