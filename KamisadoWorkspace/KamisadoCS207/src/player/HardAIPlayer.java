package player;

import model.Move;
import model.State;

public class HardAIPlayer extends Player{

	TreeNode moveTree;
	
    public HardAIPlayer(String playerTeam,String playerName, boolean goingFist) {
        super(playerTeam,playerName, goingFist);
    }
    
    public void getMove(State state){
    	moveTree = new TreeNode(5, state, getHomeRow()%7, state.getPieceToMove());
    	moveTree.generateChildren();
    	Move alpha = new Move(null, null, null);
    	alpha.setScore(-10000000);
    	Move beta = new Move(null, null, null);
    	beta.setScore(100000000);
    	System.out.println(alphabeta(moveTree, 5,alpha, beta , 10000, true));
    	
    }
    
    public Move alphabeta(TreeNode node, int depth, Move a, Move b, boolean maximising){
    	Move bestMove = node.getMove();
    	if(depth == 0 || node.getChildrenSize() == 0){
    		return bestMove;
    	}
    	if(maximising){
    		Move tempMove = new Move(null, null, null);
    		tempMove.setScore(-1000000);
    		for(TreeNode child : node.getChildren()){
    			tempMove = max(tempMove, alphabeta(child, depth -1, a, b, false));
    			a = max(a,tempMove);
    			if(b <= a){
    				break;
    			}
    		}
    		return tempMove;
    	}else{
    		Move tempMove = new Move(null, null, null);
    		tempMove.setScore(100000000);
    		for(TreeNode child : node.getChildren()){
    			tempMove = min(tempMove, alphabeta(child, depth -1, a, b, true));
    			b = min(b,tempMove);
    			if(b <= a){
    				break;
    			}
    		}
    		return tempMove;
    	}
    }
    
    public Move max(Move val1, Move val2){
    	if(val1.getScore() > val2.getScore()){
    		return val1;
    	}else{
    		return val2;
    	}
    }
    public Move min(Move val1, Move val2){
    	if(val1.getScore() > val2.getScore()){
    		return val2;
    	}else{
    		return val1;
    	}
    }

}
