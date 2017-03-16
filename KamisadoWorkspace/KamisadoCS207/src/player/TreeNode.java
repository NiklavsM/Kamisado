package player;

import java.awt.Color;
import java.util.ArrayList;

import model.Move;
import model.Position;
import model.State;

public class TreeNode {

	private ArrayList<TreeNode> children;
	private ArrayList<Move> moves;
	private Move move;
	private State boardState;
	private Position location;
	private int player;
	private int depth;
	private ArrayList<Position> validMoves;

	public TreeNode(int depth, State boardState, int player, Position pieceToMove) {
		children = new ArrayList<>();
		this.boardState = new State(boardState, boardState.getBoard());
		location = new Position(-2, -2);
		moves = new ArrayList<>();
		this.player = player;
		this.move = boardState.getPreviousMove();
		move.setScore(0);
		this.depth = depth;

		this.boardState.calcValidMoves(pieceToMove);
		validMoves = this.boardState.getValidMoves();
		calcScore();
	}

	public TreeNode(int depth, State boardState, Move locationMovedTo, int player,
			ArrayList<Move> moves) {
		this.move = locationMovedTo;
		children = new ArrayList<>();
		this.boardState = new State(boardState, boardState.getBoard());
		location = move.getEndPos();
		this.player = player;

		this.moves = new ArrayList<>();
		for (int i = 0; i < moves.size(); i++) {
			this.moves.add(new Move(moves.get(i)));
		}
		this.boardState.calcValidMoves(this.boardState.calcPieceToMove());
		validMoves = this.boardState.getValidMoves();

		this.depth = depth;
		calcScore();
	}

	public Move generateChildren() {

		TreeNode childNode = null;
		if (depth == 0) {
			return move;
		} else if ((player == 0 && location.getY() == 7) || (player == 1 && location.getY() == 0)) {
			return move;
		} else if (validMoves.size() > 0) {
			for (Position pos : validMoves) {
				State state = boardState.make(pos);

				Move move = state.getPreviousMove();
				moves.add(move);

				if (pos.getY() <= 7 && pos.getY() >= 0) {
					childNode = new TreeNode((depth - 1), new State(state, state.getBoard()), move, (1 - player),
							moves);
					children.add(childNode);
					moves.remove(moves.size() - 1);
				}
				
				
			}
			ArrayList<Move> bestMoves = new ArrayList<>();
			for (int i = 0; i < children.size(); i++) {
				bestMoves.add(i, children.get(i).generateChildren());
			}
			if(player == 0){
				move.setScore(maxScore(bestMoves));
			}else{
				move.setScore(minScore(bestMoves));
			}
		} else {
			State board = new State(boardState, boardState.getBoard());

			Position pos = board.getPieceToMove();
			Move thisMove = new Move(pos, pos, board.findPiece(pos));
			moves.add(thisMove);
			board.flipPlayerToMove();
			if (pos.getY() <= 7 && pos.getY() >= 0) {
				childNode = new TreeNode(depth - 1, new State(board, board.getBoard()), thisMove, (1 - player),
						moves);
				children.add(childNode);
			}
			move.setScore(childNode.generateChildren().getScore());
		}
		if(player == 0){
			return maxMove(children);
		}else{
			return minMove(children);
		}
	}
	public void calcScore() {
		int score = move.getScore();
		int locationY = move.getEndPos().getY();
		int startY = move.getStartPos().getY();
		if (locationY == 7) {
			score = 1000000000;
			move.setScore(score);
			return;
		} else if (locationY == 0) {
			score = -1000000000;
			move.setScore(score);
			return;
		}
		if (player  == 0) {
			if (locationY != -2) {
				score -= validMoves.size() * 10;
				if (validMoves.size() == 0) {
					score += 1000000;
				}
				score += locationY * 5;
				for (Position position : validMoves) {
					if (position.getY() == 0) {
						score -= 200000;
					}
				}
			}
		} else {
			if (locationY != -2) {
				score += validMoves.size() * 10;
				if (validMoves.size() == 0) {
					score -= 10000;
				}
				score -= (7-locationY) * 5;
				for (Position position : validMoves) {
					if (position.getY() == 7) {
						score += 200000;
					}
				}
			}
		}
		move.setScore(score);
	}

	public int getChildrenSize() {
		return children.size();
	}

	public ArrayList<TreeNode> getChildren() {
		return children;
	}

	public int getScore() {
		return move.getScore();
	}

	public Move getMove() {
		return move;
	}
	
	public Move maxMove(ArrayList<TreeNode> children){
		Move currentBest = children.get(0).getMove();
		for(TreeNode node : children){
			if(node.getMove().getScore() > currentBest.getScore()){
				currentBest = node.getMove();
			}
		}
		return currentBest;
	}
	
	public Move minMove(ArrayList<TreeNode> children){
		Move currentBest = children.get(0).getMove();
		for(TreeNode node : children){
			if(node.getMove().getScore() < currentBest.getScore()){
				currentBest = node.getMove();
			}
		}
		return currentBest;
	}
	
	public int maxScore(ArrayList<Move> children){
		int currentBest = 0;
		for(int i = 0; i < children.size();i++){
			if(children.get(i).getScore() > currentBest){
				currentBest = i;
			}
		}
		return currentBest;
	}
	
	public int minScore(ArrayList<Move> children){
		int currentBest = 0;
		for(int i = 0; i < children.size();i++){
			if(children.get(i).getScore() < currentBest){
				currentBest = i;
			}
		}
		return currentBest;
	}

}
