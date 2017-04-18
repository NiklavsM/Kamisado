package player;

import java.awt.Color;
import java.util.ArrayList;

import model.Move;
import model.Piece;
import model.Position;
import model.State;

public class TreeNode {

	private ArrayList<TreeNode> children;
	private ArrayList<Move> moves;
	private Move previousMove;
	private State boardState;
	private Position posOfPreviousMove;
	private int playerToMove;
	private int depth;
	private ArrayList<Position> validMovesForThisPlayer;
	private int alpha;
	private int beta;

	public TreeNode(int depth, State boardState, int playerToMove) {
		this.children = new ArrayList<>();
		this.boardState = new State(boardState, boardState.getBoard());

		this.moves = new ArrayList<>();
		this.playerToMove = playerToMove;
		this.previousMove = new Move(boardState.getPreviousMove());
		this.moves.add(previousMove);
		this.previousMove.setScore(5);
		this.posOfPreviousMove = previousMove.getEndPos();
		this.depth = depth;
		this.boardState.calcValidMoves(this.boardState.getPieceToMove());
		this.validMovesForThisPlayer = this.boardState.getValidMoves();
		this.alpha = -1000000000;
		this.beta = 1000000000;
	}

	public TreeNode(int depth, State boardState, int playerToMove, ArrayList<Move> moves) {
		this.previousMove = new Move(boardState.getPreviousMove());
		this.children = new ArrayList<>();
		this.boardState = new State(boardState, boardState.getBoard());
		this.posOfPreviousMove = previousMove.getEndPos();
		this.playerToMove = playerToMove;

		this.moves = new ArrayList<>();
		for (int i = 0; i < moves.size(); i++) {
			this.moves.add(new Move(moves.get(i)));
		}
		this.validMovesForThisPlayer = this.boardState.calcValidMoves(this.boardState.calcPieceToMove());
		this.alpha = -1000000000;
		this.beta = 1000000000;
		this.depth = depth;
	}

	public Move getBestOrWorstsChild(boolean maxBest) {
		this.generateChildren();
		if (children.size() == 0) {
			return previousMove;
		}
		if(maxBest){
			return maxMove();
		}else{
			return minMove();
		}
	}

	public void generateChildren() {
		int v; 
		TreeNode childNode = null;
		if (depth == 0) {
			calcScore();
			return;
		} else if (playerToMove == 0 && posOfPreviousMove.getY() == 7) {
			// player has won
			previousMove.setScore(-1000000000);
			return;
		} else if (playerToMove == 1 && posOfPreviousMove.getY() == 0) {
			previousMove.setScore(1000000000);
			return;
		}
		if (validMovesForThisPlayer.size() > 0) {
			if (playerToMove == 0) {
				v = -1000000000;
			} else {
				v = 1000000000;
			}
			for (Position pos : validMovesForThisPlayer) {
				State state = boardState.make(pos);
				moves.add(state.getPreviousMove());
				childNode = new TreeNode((depth - 1), new State(state, state.getBoard()), (1 - playerToMove), moves);
				children.add(childNode);
				childNode.generateChildren();
				moves.remove(moves.size() - 1);
				if (playerToMove == 0) {
					if(childNode.getChildrenSize() > 0){
						v = max(v, childNode.getScore());
					}
					alpha = max(alpha, v);
					if(beta <= alpha){
						break;
					}
				}else{
					if(childNode.getChildrenSize() > 0){
						v = min(v, childNode.getScore());
					}
					beta = min(beta, v);
					if(beta <= alpha){
						break;
					}
				}
			}
			if (playerToMove == 0) {
				previousMove.setScore(maxScore()-1);
			} else {
				previousMove.setScore(minScore()+1);
			}
		} else {
			// if player misses a go
			State state = new State(boardState, boardState.getBoard());
			Position pos = state.getPieceToMove();
			moves.add(new Move(pos, pos, state.findPiece(pos)));
			state.flipPlayerToMove();
			childNode = new TreeNode(depth - 1, new State(state, state.getBoard()),(1 - playerToMove), moves);
			children.add(childNode);
			childNode.generateChildren();
			previousMove.setScore(childNode.getScore());
		}
	}

	public void calcScore() {
		int score = 0;
		if (playerToMove == 0) {
			if (validMovesForThisPlayer.size() == 0) {
				score += 400;
			} else {
				score -= validMovesForThisPlayer.size() * 20;
				for (Position position : validMovesForThisPlayer) {
					if (position.getY() == 0) {
						score -= 500;
					}
				}
			}

		} else {
			if (validMovesForThisPlayer.size() == 0) {
				score -= 400;
			} else {
				score += validMovesForThisPlayer.size() * 20;
				for (Position position : validMovesForThisPlayer) {
					if (position.getY() == 7) {
						score += 500;
					}
				}
			}
		}
		previousMove.setScore(score);
	}
	
	public int getChildrenSize() {
		return children.size();
	}

	public ArrayList<TreeNode> getChildren() {
		return children;
	}

	public int getScore() {
		return previousMove.getScore();
	}

	public Move getMove() {
		return previousMove;
	}

	public Move maxMove() {
		Move currentBest = children.get(0).getMove();
		for (TreeNode node : children) {
			if (node.getMove().getScore() > currentBest.getScore()) {
				currentBest = node.getMove();
			}
		}
		return currentBest;
	}

	public Move minMove() {
		Move currentBest = children.get(0).getMove();
		for (TreeNode node : children) {
			if (node.getMove().getScore() < currentBest.getScore()) {
				currentBest = node.getMove();
			}
		}
		return currentBest;
	}

	public int maxScore() {
		int currentBest = this.children.get(0).getScore();
		for (int i = 1; i < this.children.size(); i++) {
			if (this.children.get(i).getScore() >= currentBest) {
				currentBest = this.children.get(i).getScore();
			}
		}
		return currentBest;
	}

	public int minScore() {
		int currentBest = this.children.get(0).getScore();
		for (int i = 1; i < this.children.size(); i++) {
			if (this.children.get(i).getScore() <= currentBest) {
				currentBest = this.children.get(i).getScore();
			}
		}
		return currentBest;
	}
	
	public int max(int a, int b){
		if(a > b){
			return a;
		}
		return b;
	}
	
	public int min(int a, int b){
		if(a < b){
			return a;
		}
		return b;
	}

}
