package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

import player.Player;

public class GameDriver implements MyObservable, MyObserver, Serializable {

	public Stack<State> history;
	public State currentState;
	ArrayList<MyObserver> observers = new ArrayList<MyObserver>();
	private int scoreToGet;
	private int currentGameNum = 1;


	public GameDriver(Player playerWhite, Player playerBlack, Player playerToStart, int gameLength, boolean random) {
		this.history = new Stack<>();
		this.currentState = new State(playerWhite, playerBlack, playerToStart, random);
		currentState.setFirstMove(true);
		this.scoreToGet = gameLength;
	}

	public GameDriver(State currentState) {
		this.history = new Stack<>();
		this.currentState = currentState;
	}

	public void saveGame() {
		if(history.empty() || currentState.isGameOver()){
			JOptionPane.showMessageDialog(null, "Game has not started / is ended", "Game has not started / is ended",
                    JOptionPane.ERROR_MESSAGE);
		}else{
			SaveManager s = new SaveManager();
			s.save(currentState);
		}
	}

	public void gameEnds(Player winner, Player looser) {
		
		
		StatsObject stats;
		StatsManager m = new StatsManager();
		stats = m.getStatsObject();
		if (stats != null) {
			stats.addToScores(winner, looser);
		} else {
			stats = new StatsObject();
			stats.addToScores(winner, looser);
		}
		m.saveStats(stats);
		PieceObject pieceThatWon = currentState.getPreviousMove().pieceMoved();
		
		winner.incrementScore(pieceThatWon.getPieceType().getPointValue());
		if(winner.getScore() >= scoreToGet){
			this.tellAll(winner.getPlayerName() + " has won the game! In " + currentGameNum + " round(s)!");
		}
	}

	public void changeCurrentState(State currentState) {
		this.currentState = currentState;
		this.tellAll(currentState.getBoard());
		this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
	}

	public void undo() {
		if (history.size() >= 2) {
//			history.pop();
			State stateToCheck = history.pop();
			String playerToMoveName = stateToCheck.getPlayerToMove().getPlayerName();
			while(playerToMoveName.equals("Easy AI") || playerToMoveName.equals("Hard AI")){
				stateToCheck = history.pop();
				playerToMoveName = stateToCheck.getPlayerToMove().getPlayerName();
			}
			changeCurrentState(stateToCheck);
		}
	}
	
	public int nextRound(){
		int optionChosen = 0;
		currentGameNum++;
		history = new Stack<>();
		Player playerToMove = currentState.getPlayerWhite();
		String previousWinner = currentState.getPreviousMove().pieceMoved().getPiece().getTeam();
		Board previousBoard = currentState.getBoard();
		if(previousWinner.equals("White")){
			optionChosen = currentState.getPlayerWhite().fillHomeRow();
			playerToMove = currentState.getPlayerBlack();
		}
		else{
			optionChosen = currentState.getPlayerBlack().fillHomeRow();
		}
		if(optionChosen ==0){
			this.currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(),playerToMove, previousBoard, true);
		}else if(optionChosen == 1){
			this.currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(),playerToMove, previousBoard, false);
		}
		
		currentState.setFirstMove(true);
		this.tellAll(currentState.getBoard());
		return optionChosen;
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
			//JOptionPane.showMessageDialog(null,"Not a valid move!", "Not a valid move!", JOptionPane.INFORMATION_MESSAGE);
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
						if (currentState.getPlayerToMove().equals(currentState.getPlayerWhite())) {							
							gameEnds(currentState.getPlayerWhite(), currentState.getPlayerBlack());
						} else {
							gameEnds(currentState.getPlayerBlack(), currentState.getPlayerWhite());
						}
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
		for (MyObserver obs : observers) {
			obs.update(this, arg);
		}
	}

	@Override
	public void addObserver(MyObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(MyObserver o) {
		if (observers.contains(o)) {
			observers.remove(o);
		}
	}

	@Override
	public ArrayList<MyObserver> getObservers() {
		return observers;
	}
}
