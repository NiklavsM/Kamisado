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
    	moveTree = new TreeNode(6, state, 0, state.getPieceToMove());
    	Move move = moveTree.generateChildren();
    	move.print();
    	System.out.println(move.getScore());
    	tellAll(move.getEndPos());
    }
}
