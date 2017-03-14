package player;

import java.util.ArrayList;

import model.Position;
import model.State;

public class TreeNode {

	private ArrayList<TreeNode> children;
	private State boardState;	
	private int score;
	private Position location;
	private int player;
	
	public TreeNode(int depth, int score, State boardState, Position locationMovedTo, int player) {
		this.score = score;
		children = new ArrayList<>();
		this.boardState = boardState;
		location = locationMovedTo;
		//if(calcScore(location)){
		generateChildren(depth);
		//}
	}


	public void generateChildren(int depth){
		ArrayList<Position> validMovesOfPieceToMove = boardState.getValidMoves();
		TreeNode childNode = null;
		if(depth == 0){
			//stop
		}else if(validMovesOfPieceToMove.size() > 0){
			for(Position location:validMovesOfPieceToMove){
				State board = null;
				try {
					board = (State) boardState.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("created child");
				System.out.println("movingTo: X: " + location.getX() + " Y: " + location.getY());
				board.make(location);
				
				if(location.getY() <= 6 && location.getY() >= 1){
					childNode = new TreeNode((depth - 1), score, board, location, (1-player));
					children.add(childNode);	
				}else{
					System.out.println("Winner: " + player);
				}
			}
		}
		else{
			System.out.println("created only child");
			State board = null;
			try {
				board = (State) boardState.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			board.make(endPosition);
			if(location.getY() <= 6 && location.getY() >= 1){
				childNode = new TreeNode(depth - 1, score, board, new Position(-1,-1), (1-player));
				children.add(childNode);		
			}else{
				System.out.println("Winner: " + player);
			}
		}
		
	}
	
	public boolean calcScore(Position location){
		ArrayList<Position> validMoves;
		if(location.getY() == -1){
			return true;
		}else{
			validMoves = boardState.calcValidMoves(location);
		}
		if(location.getY() == 7){
			score = 1000;
			return false;
		}
		else if(location.getY() == 0){
			score = -1000;
			return false;
		}
		if(player == 1){
			if(location.getY() != -2){
				score += validMoves.size() * 10;
				score += (location.getY() % 7) * 5;
				for(Position position:validMoves){
					if(position.getY() == 7){
						score += 20;
					}
				}
			}
		}else{
			if(location.getY() != -2){
				score -= validMoves.size() * 10;
				score -= (location.getY() % 8) * 5;
				for(Position position:validMoves){
					if(position.getY() == 0){
						score -= 20;
					}
				}
			}
		}
		return true;
		
	}
		
}
