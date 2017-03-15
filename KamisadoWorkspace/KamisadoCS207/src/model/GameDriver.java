package model;

import java.io.Serializable;
import java.util.ArrayList;

import player.Player;

public class GameDriver implements MyObservable, MyObserver, Serializable {

	private ArrayList<State> history;
	public State currentState;
	private boolean firstMove = true;
	private boolean gameOver = false;

	// private SaveManager saveManager;
	// private MatchReport matchReport;

	public GameDriver(Player playerWhite, Player playerBlack, Player playerToStart) {
		// this.playerWhite = playerWhite;
		// this.playerBlack = playerBlack;
		// PlayerToMove = playerToStart;
		this.history = new ArrayList<>();
		this.currentState = new State(playerWhite, playerBlack, playerToStart);
		// this.saveManager = new SaveManager();
	}

	public GameDriver(Player playerWhite, Player playerBlack, ArrayList<State> history, State currentState,
			Player playerToStart) {
		// this.playerWhite = playerWhite;
		// this.playerBlack = playerBlack;
		// PlayerToMove = playerToStart;
		this.history = history;
		this.currentState = currentState;
		// this.saveManager = new SaveManager();
	}
	public void save(){
        System.out.println("OPINAAS");
        SaveManeger s = new SaveManeger();
        s.save(currentState);
    }
    public void load(){
        System.out.println("OPINAAS2");
        SaveManeger s = new SaveManeger();
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
		System.out.println("asking player for move");
		if (placeClicked.getY() == PlayerToMove.getHomeRow()) {
			currentState.calcValidMoves(placeClicked);
			System.out.println("printing validMoves");
			for (Position pos : currentState.getValidMoves()) {
				System.out.println("X: " + pos.getX() + " Y: " + pos.getY());
			}
			// playerFirstMove();
			this.tellAll(currentState.getValidMoves());
			return false;
		} else {
			return actionOnClick(placeClicked);
		}
	}

	private boolean actionOnClick(Position placeClicked) {
		State state = currentState.make(placeClicked);
		if (state == null) {
			System.out.println("is not valid move X: " + placeClicked.getX() + " Y: " + placeClicked.getY());
			return false;
		} else {
			history.add(currentState);
			currentState = state;
			this.tellAll(currentState.getPreviousMove());
			return true;
		}
		// return true;
	}

	public boolean playTurn(Position placeClicked) {
		if (currentState.isGameOver()) {
			Player winningPlayer = currentState.getPlayerToMove();
			this.tellAll(winningPlayer.getPlayerTeam());
			return true;
		} else {
			return nextTurn(0);
		}
		// Position placeClicked = new Position(x,y);
		// actionOnClick(placeClicked);
	}

	public boolean tryToMove(Position placeClicked) {
		if (actionOnClick(placeClicked)) {
			return true;
		} else
			return false;
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
	//
	// @Override
	// public void update(MyObservable o, Object arg) {
	// if(o instanceof GUIBoardView){
	//
	// }
	// }

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
				} else {
					if (tryToMove((Position) arg)) {
						if (playTurn((Position) arg)) {
							gameOver = true;
						}
					}
				}
				generateMove();
			}

		}
	}
}
