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
		//this.score = 0;
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

	}

	public TreeNode(int depth, State boardState, Move locationMovedTo, int player,
			ArrayList<Move> moves) {
		//this.score = score;
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

	public void generateChildren() {

		TreeNode childNode = null;
		if (depth == 0) {

		} else if ((player == 0 && location.getY() == 7) || (player == 1 && location.getY() == 0)) {
			System.out.println();
			System.out.println("Winner: " + player + " X: " + location.getX() + " Y:" + location.getY());
			System.out.println();
			for (Move moved : moves) {
				moved.print();
			}
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
		}
		for (TreeNode child : children) {
			child.generateChildren();
		}
	}

	public boolean calcScore() {
		int score = move.getScore();
		if (location.getY() == -1) {
			return true;
		}
		if (location.getY() == 7) {
			score = 100000;
			move.setScore(score);
			return false;
		} else if (location.getY() == 0) {
			score = -100000;
			move.setScore(score);
			return false;
		}
		if (player == 1) {
			if (location.getY() != -2) {
				score += validMoves.size() * 10;
				if (validMoves.size() == 0) {
					score -= 10;
				}
				score += (location.getY() % 7) * 5;
				for (Position position : validMoves) {
					if (position.getY() == 7) {
						score += 20;
					}
				}
			}
		} else {
			if (location.getY() != -2) {
				score -= validMoves.size() * 10;
				if (validMoves.size() == 0) {
					score += 10;
				}
				score -= (location.getY() % 8) * 5;
				for (Position position : validMoves) {
					if (position.getY() == 0) {
						score -= 20;
					}
				}
			}
		}
		move.setScore(score);
		return true;

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

}
