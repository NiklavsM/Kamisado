package player;

import java.util.ArrayList;

import model.Board;
import model.Move;
import model.Position;
import model.State;

public class TreeNode {

	private ArrayList<TreeNode> children;
	private Move previousMove;
	private State boardState;
	private int playerToMove;
	private int depth;
	private ArrayList<Position> validMovesForThisPlayer;

	public TreeNode(int depth, State boardState, int playerToMove) {
		this.children = new ArrayList<>();
		this.playerToMove = playerToMove;
		this.boardState = new State(boardState, boardState.getBoard());

		this.previousMove = new Move(boardState.getPreviousMove());
		this.previousMove.setScore(5);
		this.depth = depth;
		if (depth == 4) {
			this.boardState.calcValidMoves(this.boardState.getPieceToMove());
		} else {
			this.boardState.calcValidMoves(this.boardState.calcPieceToMove());
		}
		this.validMovesForThisPlayer = this.boardState.getValidMoves();
	}

	public Move getBestOrWorstsChild(boolean maxBest) {
		this.generateChildren();
		if (children.size() == 0) {
			return previousMove;
		}
		if (maxBest) {
			return maxMove();
		} else {
			return minMove();
		}
	}

	private void generateChildren() {
		int y = previousMove.getEndPos().getY();
		TreeNode childNode = null;
		if (depth == 0) {
			calcScore();
			return;
		}
		if (playerToMove == 0 && y == 0) {
			previousMove.setScore(-1000000000);
			return;
		}
		if (playerToMove == 1 && y == 7) {
			previousMove.setScore(1000000000);
			return;
		}
		if (validMovesForThisPlayer.size() > 0) {
			for (Position pos : validMovesForThisPlayer) {
				State state = boardState.make(pos);
				if (state.isSumoPush()) {
					childNode = new TreeNode((depth - 1), state, (playerToMove));
				} else {
					childNode = new TreeNode((depth - 1), state, (1 - playerToMove));
				}
				children.add(childNode);
				childNode.generateChildren();
			}
			if (playerToMove == 0) {
				previousMove.setScore(maxScore() - 1);
			} else {
				previousMove.setScore(minScore() + 1);
			}
		} else {
			Board board = boardState.getBoard();
			boardState.setColourToMove(board.getColourName(board.findColor(boardState.getStartingPosition())));
			childNode = new TreeNode(depth - 1, boardState, (1 - playerToMove));
			children.add(childNode);
			childNode.generateChildren();
			previousMove.setScore(childNode.getScore());			
		}
	}

	private void calcScore() {
		int score = 0;
		if (playerToMove == 1) {
			if (validMovesForThisPlayer.isEmpty()) {
				score += 1000;
			} else {
				score -= validMovesForThisPlayer.size() * 15;
				for (Position position : validMovesForThisPlayer) {
					if (position.getY() == 0) {
						score -= 800;
					}
				}
			}

		} else {
			if (validMovesForThisPlayer.isEmpty()) {
				score -= 1000;
			} else {
				score += validMovesForThisPlayer.size() * 15;
				for (Position position : validMovesForThisPlayer) {
					if (position.getY() == 7) {
						score += 800;
					}
				}
			}
		}
		previousMove.setScore(score);
	}

	private int getScore() {
		return previousMove.getScore();
	}

	private Move getMove() {
		return previousMove;
	}

	private Move maxMove() {
		Move currentBest = children.get(0).getMove();
		for (TreeNode node : children) {
			if (node.getMove().getScore() > currentBest.getScore()) {
				currentBest = node.getMove();
			}
		}
		return currentBest;
	}

	private Move minMove() {
		Move currentBest = children.get(0).getMove();
		for (TreeNode node : children) {
			if (node.getMove().getScore() < currentBest.getScore()) {
				currentBest = node.getMove();
			}
		}
		return currentBest;
	}

	private int maxScore() {
		int currentBest = this.children.get(0).getScore();
		for (int i = 1; i < this.children.size(); i++) {
			if (this.children.get(i).getScore() > currentBest) {
				currentBest = this.children.get(i).getScore();
			}
		}
		return currentBest;
	}

	private int minScore() {
		int currentBest = this.children.get(0).getScore();
		for (int i = 1; i < this.children.size(); i++) {
			if (this.children.get(i).getScore() < currentBest) {
				currentBest = this.children.get(i).getScore();
			}
		}
		return currentBest;
	}

}
