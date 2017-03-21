package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import player.Player;

public class GameDriver implements MyObservable, MyObserver, Serializable {

	public Stack<State> history;
	public State currentState;
	ArrayList<MyObserver> observers = new ArrayList<MyObserver>();

	public GameDriver(Player playerWhite, Player playerBlack, Player playerToStart) {
		this.history = new Stack<>();
		this.currentState = new State(playerWhite, playerBlack, playerToStart);
		currentState.setFirstMove(true);
	}

	public GameDriver(State currentState) {
		this.history = new Stack<>();
		this.currentState = currentState;
	}

	public void saveGame() {
		SaveManager s = new SaveManager();
		s.save(currentState);
	}

	public void changeCurrentState(State currentState) {
		this.currentState = currentState;
		this.tellAll(currentState.getBoard());
		this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
	}

	public void undo() {
		if (!history.empty()) {
			currentState = history.pop();
			this.tellAll(currentState.getBoard());
			this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
		}
	}

	public void reset() {
		boolean valid = false;
		while (!history.empty()) {
			currentState = history.pop();
			valid = true;
		}
		if (valid) {
			this.tellAll(currentState.getBoard());
			this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
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
		if (placeClicked.getY() == PlayerToMove.getHomeRow()) {
			currentState.calcValidMoves(placeClicked);
			this.tellAll(currentState.getValidMoves());
			return true;
		}
		return false;
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
		if (currentState.wasWinningMove()) {
			Player winningPlayer = currentState.getPlayerToMove();
			this.tellAll(winningPlayer.getPlayerTeam());
			return true;
		} else {
			return nextTurn(0);
		}
	}

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

	public State getCurrentState() {
		return currentState;
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if (arg instanceof Position) {
			if (!currentState.isGameOver()) {
				if (currentState.isFirstMove()) {
					if (!playerFirstMove((Position) arg)) {
						if (tryToMove((Position) arg)) {
							currentState.setFirstMove(false);
							nextTurn(0);
						}
					}
				} else if (tryToMove((Position) arg)) {
					if (playTurn((Position) arg)) {
						currentState.setGameOver(true);
						return;
					}
				}
				generateMove();
			}
		}
	}

	public void setState(State state) {
		currentState = state;
	}
	
	@Override
	public void tellAll(Object arg) {
		for(MyObserver obs : observers){
			obs.update(this, arg);
		}
	}

	@Override
	public void addObserver(MyObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(MyObserver o) {
		if(observers.contains(o)){
			observers.remove(o);
		}
	}

	@Override
	public ArrayList<MyObserver> getObservers() {
		return observers;
	}
}
