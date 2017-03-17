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
		this.boardState.calcValidMoves(this.boardState.calcPieceToMove());
		this.validMovesForThisPlayer = this.boardState.getValidMoves();

		this.depth = depth;

	}

	public Move getBestChild() {
		this.generateChildren();
		if(children.size() == 0){
			return previousMove;
		}
		return maxMove(children);
	}

	public void generateChildren() {

		// System.out.println(move.getScore());
		TreeNode childNode = null;
		if (depth == 0) {
			calcScore();
			return;
		} else if (playerToMove == 0 && posOfPreviousMove.getY() == 7) {
			// player has won
			previousMove.setScore(-1000000000);
			return;
		}else if(playerToMove == 1 && posOfPreviousMove.getY() == 0){
			previousMove.setScore(1000000000);
			return;
		}
		if (validMovesForThisPlayer.size() > 0) {
			// has valid position(s) to move to
			for (Position pos : validMovesForThisPlayer) {
				State state = boardState.make(pos);
				moves.add(state.getPreviousMove());
				childNode = new TreeNode((depth - 1), new State(state, state.getBoard()), (1 - playerToMove), moves);
				children.add(childNode);
				childNode.generateChildren();
				moves.remove(moves.size() - 1);
			}
			if (playerToMove == 0) {
				previousMove.setScore(maxScore(children)-1);
			} else {
				previousMove.setScore(minScore(children)+1);
			}
		} else {
			// if player misses a go
			State board = new State(boardState, boardState.getBoard());
			Position pos = board.getPieceToMove();
			moves.add(new Move(pos, pos, board.findPiece(pos)));
			board.flipPlayerToMove();
			childNode = new TreeNode(depth - 1, new State(board, board.getBoard()), (1 - playerToMove), moves);
			children.add(childNode);
			childNode.generateChildren();
			previousMove.setScore(childNode.getScore());
		}
	}

	public void calcScore() {
		int score = 0;
		if (playerToMove == 0) {
			if (validMovesForThisPlayer.size() == 0) {
				score += 200;
			}else{
				score -= validMovesForThisPlayer.size() * 10;
				for (Position position : validMovesForThisPlayer) {
					if (position.getY() == 0) {
						score -= 200;
					}
				}
			}	
			
			
		} else {
			if (validMovesForThisPlayer.size() == 0) {
				score -= 200;
			}else{
				score += validMovesForThisPlayer.size() * 10;
				for (Position position : validMovesForThisPlayer) {
					if (position.getY() == 7) {
						score += 200;
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

	public Move maxMove(ArrayList<TreeNode> children) {
		Move currentBest = children.get(0).getMove();
		for (TreeNode node : children) {
			if (node.getMove().getScore() > currentBest.getScore()) {
				currentBest = node.getMove();
			}
		}
		return currentBest;
	}

	public Move minMove(ArrayList<TreeNode> children) {
		Move currentBest = children.get(0).getMove();
		for (TreeNode node : children) {
			if (node.getMove().getScore() < currentBest.getScore()) {
				currentBest = node.getMove();
			}
		}
		return currentBest;
	}

	public int maxScore(ArrayList<TreeNode> children) {
		int currentBest = children.get(0).getScore();
		for (int i = 1; i < children.size(); i++) {
			if (children.get(i).getScore() >= currentBest) {
				currentBest = children.get(i).getScore();
			}
		}
		return currentBest;
	}

	public int minScore(ArrayList<TreeNode> children) {
		int currentBest = children.get(0).getScore();
		for (int i = 1; i < children.size(); i++) {
			if (children.get(i).getScore() <= currentBest) {
				currentBest = children.get(i).getScore();
			}
		}
		return currentBest;
	}

}
