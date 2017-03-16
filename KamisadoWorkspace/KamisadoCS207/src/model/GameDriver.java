package model;

import java.io.Serializable;
import java.util.ArrayList;

import player.Player;

public class GameDriver implements MyObservable, MyObserver, Serializable {

	private ArrayList<State> history;
	public State currentState;
	private boolean firstMove = true;
	private boolean gameOver = false;

	public GameDriver(Player playerWhite, Player playerBlack, Player playerToStart) {
		this.history = new ArrayList<>();
		this.currentState = new State(playerWhite, playerBlack, playerToStart);
	}
//	public GameDriver(Player playerWhite, Player playerBlack, ArrayList<State> history, State currentState,
//			Player playerToStart) {
//		this.history = history;
//		this.currentState = currentState;
//	}
	public void saveGame(){
        System.out.println("OPINAAS");
        SaveManager s = new SaveManager();
        s.save(currentState);
    }
    public void loadGame(){
        System.out.println("OPINAAS2");
        SaveManager s = new SaveManager();
        currentState = s.load();
        System.out.println("turn"+ currentState.getPlayerToMove().getPlayerTeam());
        this.tellAll(currentState.getValidMoves());
        //this.tellAll(currentState.getPreviousMove());
        Position posToMove = currentState.calcPieceToMove();
        ArrayList<Position> movesCanMake = currentState.calcValidMoves(posToMove);
        this.tellAll(currentState.getBoard());
        this.tellAll(movesCanMake);
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
			return false;
		} else {
			return tryToMove(placeClicked);
		}
	}

	private boolean tryToMove(Position placeClicked) {
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
		if(!tryToMove(placeClicked)){
			return false;
		}else if (currentState.isGameOver()) {
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
						nextTurn(0);
						firstMove = false;
					}
				} else if (playTurn((Position) arg)) {
					gameOver = true;
				}
				generateMove();
			}
		}
	}
}
