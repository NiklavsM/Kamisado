package player;

import model.Move;
import model.MyObservable;
import model.State;

public class HardAIPlayer extends Player implements MyObservable{

	TreeNode moveTree;
	
    public HardAIPlayer(String playerTeam,String playerName, boolean goingFist) {
        super(playerTeam,playerName, goingFist);
    }
    @Override
    public void getMove(State state){
    	moveTree = new TreeNode(5, state, 0);
    	Move move = moveTree.getBestChild();
    	move.print();
    	tellAll(move.getEndPos());
    }
    
    
}
