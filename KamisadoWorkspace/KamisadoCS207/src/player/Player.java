package player;

import java.io.Serializable;
import java.util.ArrayList;

import model.MyObservable;
import model.MyObserver;
import model.State;

public abstract class Player implements MyObservable, Serializable{

	private String playerName;
	private String playerTeam;
    private int homeRow;
    private boolean goingFirst;
    ArrayList<MyObserver> observers = new ArrayList<>();

    public Player(String playerTeam, String playerName, boolean goingFirst){
        this.playerTeam = playerTeam;
        if(playerTeam.equals("White")){
            homeRow = 0;
        }else{
            homeRow = 7;
        }
        this.playerName = playerName;
        this.goingFirst = goingFirst;
    }

    public void getMove(State state){
    }

    public void interupt(){

    }
    
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
}
