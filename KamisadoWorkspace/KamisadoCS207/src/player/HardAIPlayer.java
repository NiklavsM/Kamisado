package player;

import java.util.HashMap;
import java.util.Random;

import model.Move;
import model.MyObservable;
import model.Piece;
import model.Position;
import model.State;

public class HardAIPlayer extends Player implements MyObservable{

	private int timesVisited = 0;
	private HashMap<Position, Position> bestOpeningMoves;
	private Position firstMove;
	
    public HardAIPlayer(String playerTeam,String playerName, boolean goingFist) {
        super(playerTeam,playerName, goingFist, true);
        bestOpeningMoves = new HashMap<>();
        initialiseBestMoves();
    }
    private void initialiseBestMoves() {
    	Random rnd = new Random();
        int num = rnd.nextInt(2)+3;
        firstMove = new Position(num,0);
		bestOpeningMoves.put(firstMove, new Position(num,4));
	}
	@Override
    public void getMove(State state){
    	if(getisFirst()){
            if(timesVisited == 0){
                timesVisited++;
                tellAll(firstMove);
                return;
            }else if(timesVisited == 1){
            	 timesVisited++;
            	 tellAll(new Position(bestOpeningMoves.get(firstMove)));
                 return;
            }
        }
    	if(this.getisFirst()){
    		TreeNode moveTree = new TreeNode(5, state, 1);
        	Move move = moveTree.getWorstChild();
        	move.print();
        	tellAll(move.getEndPos());
    	}else{
    		TreeNode moveTree = new TreeNode(5, state, 0);
        	Move move = moveTree.getBestChild();
        	move.print();
        	tellAll(move.getEndPos());
    	}
    }
	@Override
	public int fillHomeRow() {
		Random rnd = new Random();
        int num = rnd.nextInt(2);
		return num;
	}
	
	public void resetFirstMove(){
		initialiseBestMoves();
		timesVisited = 0;
	}
}
