package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import player.Player;

public class GameDriver implements MyObservable, MyObserver, Serializable {

	private Stack<State> history;
	public State currentState;
	private boolean firstMove = true;
	private boolean gameOver = false;

	public GameDriver(Player playerWhite, Player playerBlack, Player playerToStart) {
		// this.playerWhite = playerWhite;
		// this.playerBlack = playerBlack;
		// PlayerToMove = playerToStart;
		this.history = new Stack<>();
		this.currentState = new State(playerWhite, playerBlack, playerToStart);
		// this.saveManager = new SaveManager();
	}

	public GameDriver(Player playerWhite, Player playerBlack, Stack<State> history, State currentState,
			Player playerToStart) {
		// this.playerWhite = playerWhite;
		// this.playerBlack = playerBlack;
		// PlayerToMove = playerToStart;
		this.history = history;
		this.currentState = currentState;
		// this.saveManager = new SaveManager();
	}

	public void saveGame() {
		System.out.println("OPINAAS");
		SaveManager s = new SaveManager();
		s.save(currentState);
	}

	public void loadGame() {
		System.out.println("OPINAAS2");
		SaveManager s = new SaveManager();
		currentState = s.load();
		System.out.println("turn" + currentState.getPlayerToMove().getPlayerTeam());
		//this.tellAll(currentState.getValidMoves());
		//this.tellAll(currentState.getPreviousMove());
		//Position posToMove = currentState.calcPieceToMove();
		ArrayList<Position> movesCanMake = currentState.calcValidMoves(currentState.getStartingPosition());
		this.tellAll(currentState.getBoard());
		this.tellAll(movesCanMake);
	}

	public void undo() {
		if (!history.empty()) {
			currentState = history.pop();
			ArrayList<Position> movesCanMake = currentState.calcValidMoves(currentState.getStartingPosition());
			this.tellAll(currentState.getBoard());
			this.tellAll(movesCanMake);

		}
	}

	public void playGame() {
		generateMove();
	}

	public void generateMove() {
		Player PlayerToMove = currentState.getPlayerToMove();
		PlayerToMove.getMove(currentState);
	}

	public boolean playerFirstMove(Position placeClicked) {
		Player PlayerToMove = currentState.getPlayerToMove();
		System.out.println("asking player for first move");
		if (placeClicked.getY() == PlayerToMove.getHomeRow()) {
			currentState.calcValidMoves(placeClicked);
//			for (Position pos : currentState.getValidMoves()) {
//				System.out.println("X: " + pos.getX() + " Y: " + pos.getY());
//			}
			this.tellAll(currentState.getValidMoves());
			return true;
		}
		return false;
//			else {
//			return tryToMove(placeClicked);
//		}
	}

	public boolean tryToMove(Position placeClicked) {
		State state = currentState.make(placeClicked);
		if (state == null) {
			System.out.println("is not valid move X: " + placeClicked.getX() + " Y: " + placeClicked.getY());
			return false;
		} else {
			history.add(currentState);
			currentState = state;
			this.tellAll(currentState.getBoard());
			return true;
		}
	}

	public boolean playTurn(Position placeClicked) {
		if (currentState.isGameOver()) {
			Player winningPlayer = currentState.getPlayerToMove();
			this.tellAll(winningPlayer.getPlayerTeam());
			return true;
		} else {
			return nextTurn(0);
		}
	}

//	public boolean tryToMove(Position placeClicked) {
//		if (actionOnClick(placeClicked)) {
//			return true;
//		} else
//			return false;
//	}

	public boolean nextTurn(int numOfNoGoes) {
		Position posToMove = currentState.calcPieceToMove();
		ArrayList<Position> movesCanMake = currentState.calcValidMoves(posToMove);
		if (movesCanMake.isEmpty()) {
			if (numOfNoGoes >= 3) {
				this.tellAll("Draw");
				return true;
			} else {
				nextTurn(++numOfNoGoes);
			}
		} else {
			this.tellAll(movesCanMake);
		}
		return false;
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if (arg instanceof Position) {
			System.out.println("update gamedriver");
			if (!gameOver) {
				if (firstMove) {
					if (playerFirstMove((Position) arg)) {
						
					}else if(tryToMove((Position) arg)){
						firstMove = false;
						nextTurn(0);
					}
				} else if(tryToMove((Position) arg)){
					if (playTurn((Position) arg)) {
						gameOver = true;
					}
				}
				generateMove();
			}
		}
	}
}
