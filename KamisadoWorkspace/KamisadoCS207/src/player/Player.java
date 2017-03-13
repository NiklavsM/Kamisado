package player;

import java.util.ArrayList;

import controller.Controller;
import model.Board;
import model.MyObservable;
import model.Position;
import model.State;

public abstract class Player implements MyObservable{

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
