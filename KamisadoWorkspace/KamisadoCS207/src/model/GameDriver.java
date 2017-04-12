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
	public int scoreToGet;
	public int currentGameNum = 1;



	public GameDriver(Player playerWhite, Player playerBlack, Player playerToStart, int gameLength, boolean random) {
		this.history = new Stack<>();
		this.currentState = new State(playerWhite, playerBlack, playerToStart, random);
		currentState.setFirstMove(true);
		this.scoreToGet = gameLength;
	}

	public GameDriver(GameDriver gameDriver) {
		this.history = new Stack<>();
		this.scoreToGet = gameDriver.getScoreToGet();
		changeCurrentState(gameDriver.getCurrentState());
		this.currentGameNum = gameDriver.getCurrentGameNum();
	}

	public void saveGame() {
		if(history.empty() || currentState.isGameOver()){
			JOptionPane.showMessageDialog(null, "Game has not started / is ended", "Game has not started / is ended",
                    JOptionPane.ERROR_MESSAGE);
		}else{
			SaveManager s = new SaveManager();
			s.save(this);
		}
	}

	public boolean gameEnds(Player winner, Player looser) {
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
		System.out.println("calling gameEnds");
		return incrementScoreAtEndOfGame(winner);
	}
	
	public boolean incrementScoreAtEndOfGame(Player winner){
		PieceObject pieceThatWon = currentState.getPreviousMove().pieceMoved();
		winner.incrementScore(pieceThatWon.getPieceType().getPointValue());
		if(winner.getScore() >= scoreToGet){
			this.tellAll(winner.getPlayerName() + " has won the game! In " + currentGameNum + " round(s)!");
			return true;
		}
		return false;
	}

	public void changeCurrentState(State currentState) {
		this.currentState = currentState;
		this.tellAll(currentState);
		this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
	}

	public void undo() {
		if (history.size() >= 2) {
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
		Player playerToMove = currentState.getPlayerToMove();
		Board newBoard = currentState.getBoard();
		if (currentState.getBoard().isRandom()) {
			newBoard.setRandomBoardColours();
		}
		if (playerToMove.getPlayerTeam().equals("White")) {
			optionChosen = nextRoundSetUp(currentState.getPlayerBlack(), currentState.getPlayerWhite());
			playerToMove = currentState.getPlayerBlack();
		} else {
			optionChosen = nextRoundSetUp(currentState.getPlayerWhite(), currentState.getPlayerBlack());
			playerToMove = currentState.getPlayerWhite();
		}
		if(optionChosen == 0){
			currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(),playerToMove, newBoard, true);
		}else if(optionChosen == 1){
			currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(),playerToMove, newBoard, false);
		}
		playerToMove.setGoingFirst(true);
		currentState.setFirstMove(true);
		this.tellAll(currentState);
		playGame();
		return optionChosen;
	}
	
	private int nextRoundSetUp(Player firstToMove, Player secondToMove){
		firstToMove.setToFirstMove(true);
		secondToMove.setToFirstMove(false);
		return secondToMove.fillHomeRow();
	}

	public void reset() {
		boolean valid = false;
		while (!history.empty()) {
			currentState = history.pop();
			valid = true;
		}
		if (valid) {
			this.tellAll(currentState);
			this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
		}
	}

	public void playGame() {
		generateMove();
	}

	public void generateMove() {
		Player PlayerToMove = currentState.getPlayerToMove();
		PlayerToMove.getMove(currentState);
		if(PlayerToMove.isAI()){
			this.tellAll(false);
		}else{
			this.tellAll(true);
		}
		Thread newThread = new Thread(PlayerToMove);
		newThread.start();
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
			this.tellAll(currentState);
			return true;
		}
	}

	public boolean playTurn(Position placeClicked) {
		if (currentState.wasWinningMove()) {
			boolean gameOver;
			if (currentState.getPlayerToMove().equals(currentState.getPlayerWhite())) {							
				gameOver = gameEnds(currentState.getPlayerWhite(), currentState.getPlayerBlack());
			} else {
				gameOver = gameEnds(currentState.getPlayerBlack(), currentState.getPlayerWhite());
			}
			if(!gameOver){
				System.out.println("saying player has won round");
				Player winningPlayer = currentState.getPlayerToMove();
				this.tellAll(winningPlayer);
			}
			currentState.setGameOver(true);
			this.tellAll(currentState);
			return true;
		} else {
			return nextTurn(0);
		}
	}

	public boolean nextTurn(int numOfNoGoes) {
		Position posToMove = currentState.calcPieceToMove();
		ArrayList<Position> movesCanMake = currentState.calcValidMoves(posToMove);
		if (movesCanMake.isEmpty()) {
			if (numOfNoGoes >= 6) {
				return true;
			} else {
                currentState.setColourToMove(currentState.getBoard().findColor(posToMove));
				if(nextTurn(++numOfNoGoes)){
					currentState.getPlayerToMove().incrementScore(1);
					this.tellAll(currentState);
					this.tellAll(currentState.getPlayerToMove());
				}
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
	public int getScoreToGet() {
		return scoreToGet;
	}

	public int getCurrentGameNum() {
		return currentGameNum;
	}
}
