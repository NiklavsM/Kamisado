package player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import model.Board;
import model.Move;
import model.MyObservable;
import model.Position;
import model.State;

public class EasyAIPlayer extends Player implements MyObservable{

    private int timesVisited = 0;
    public EasyAIPlayer(String playerTeam, boolean goingFist){
        super(playerTeam, goingFist);
    }
    
    @Override
    public void getMove(State state){
        Random rnd = new Random();
        int num = rnd.nextInt(6)+1;
        if(getisFirst()){
            if(timesVisited == 0){
                timesVisited++;
                tellAll(new Position(num,getHomeRow()));
                return;
            }
        }
        ArrayList<Position> validMoves = state.getValidMoves();
        for(Position pos:validMoves){
            if(pos.getY() == 7- getHomeRow()){
                tellAll(pos);
                return;
            }
        }
        
        num = rnd.nextInt(validMoves.size());
        tellAll(validMoves.get(num));
    }
}
