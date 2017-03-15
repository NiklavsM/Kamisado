package player;

import java.io.Serializable;

import model.MyObservable;
import model.State;

public abstract class Player implements MyObservable, Serializable{

    private String playerTeam;
    private int homeRow;
    private boolean goingFirst;

    public Player(String playerTeam, boolean goingFirst){
        this.playerTeam = playerTeam;
        if(playerTeam.equals("White")){
            homeRow = 0;
        }else{
            homeRow = 7;
        }
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
}
