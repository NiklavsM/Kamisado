package player;

import java.util.ArrayList;
import java.util.Random;

import model.MyObservable;
import model.Position;
import model.State;

public class EasyAIPlayer extends Player{

	private static final long serialVersionUID = 1L;
	private int timesVisited = 0;
    private State workingState;
    public EasyAIPlayer(String playerTeam, String playerName, boolean goingFist){
        super(playerTeam,playerName, goingFist, true);
    }
    
    @Override
    public void getMove(State state){
    	workingState = state;
    }
    
    @Override
	public int fillHomeRow() {
		Random rnd = new Random();
        int num = rnd.nextInt(2);
		return num;
	}
    
    public void setToFirstMove(boolean isGoingFirst){
    	System.out.println("reseting");
		timesVisited = 0;
		this.setGoingFirst(isGoingFirst);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 Random rnd = new Random();
        int num = rnd.nextInt(6)+1;
        if(getisFirst()){
            if(timesVisited == 0){
                timesVisited++;
                tellAll(new Position(num,getHomeRow()));
                return;
            }
        }
        ArrayList<Position> validMoves = workingState.getValidMoves();
        for(Position pos:validMoves){
            if(pos.getY() == 7- getHomeRow()){
                tellAll(pos);
                return;
            }
        }
        
        num = rnd.nextInt(validMoves.size());
        tellAll(validMoves.get(num));
	}

	@Override
	public void update(MyObservable o, Object arg) {
		
	}
}
