package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;

import networking.Client2;
import player.EasyAIPlayer;
import player.HardAIPlayer;
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
		this.history = gameDriver.getHistory();
		this.scoreToGet = gameDriver.getScoreToGet();
		changeCurrentState(gameDriver.getCurrentState());
		this.currentGameNum = gameDriver.getCurrentGameNum();
	}

	public void saveGame() {
		if (currentState.isGameOver()) {
			JOptionPane.showMessageDialog(null, "Game has ended", "Game has ended", JOptionPane.ERROR_MESSAGE);
		} else {
			SaveManager s = new SaveManager();
			s.save(this);
		}
	}

	public void updateStats(Player winner, Player loser) {
		StatsObject stats;
		StatsManager m = new StatsManager();
		stats = m.getStatsObject();
		if (stats == null) {
			stats = new StatsObject();
		}
		if (winner.getScore() >= scoreToGet) {
			stats.addToScores(winner, loser, true);
		} else {
			stats.addToScores(winner, loser, false);
		}
		m.saveStats(stats);
		System.out.println("Winner: " + winner.getPlayerName());
		System.out.println("loser: " + loser.getPlayerName());
		if(loser instanceof Client2){
			loser.gameOver();
		}
//		winner.gameOver();
	}

	public boolean incrementScoreAtEndOfGame(Player winner) {
		Piece pieceThatWon = currentState.getPreviousMove().pieceMoved();
		winner.incrementScore(pieceThatWon.getPieceType().getPointValue());
		
		if (winner.getScore() >= scoreToGet) {
			this.tellAll(winner.getPlayerName() + " has won the game! In " + currentGameNum + " round(s)!");
			return true;
		}
		return false;
	}

	public void changeCurrentState(State currentState) {
		this.currentState = new State(currentState, currentState.getBoard());
		this.tellAll(currentState);
		if (currentState.getStartingPosition() != null) {
			this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
		}
	}

	public void undo() {
		if (history.size() >= 2) {
			State stateToCheck = history.pop();
			String playerToMoveName = stateToCheck.getPlayerToMove().getPlayerName();
			while (playerToMoveName.equals("Easy AI") || playerToMoveName.equals("Hard AI")) {
				stateToCheck = history.pop();
				playerToMoveName = stateToCheck.getPlayerToMove().getPlayerName();
			}
			changeCurrentState(stateToCheck);
		}
	}

	public int nextRound() {
		System.out.println("next round?");
		int optionChosen = -2;
		currentGameNum++;
		history = new Stack<>();
		Player playerToMove = currentState.getPlayerToMove();
		Board newBoard = currentState.getBoard();
		if (currentState.getBoard().isRandom()) {
			newBoard.setRandomBoardColours();
		}
		if (playerToMove.getPlayerTeam().equals("TeamWhite")) {
			while(optionChosen == -2){
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				optionChosen = nextRoundSetUp(currentState.getPlayerBlack(), currentState.getPlayerWhite());
			}
			currentState.getPlayerBlack().otherPersonOption(optionChosen);
			playerToMove = currentState.getPlayerBlack();
			
		} else {
			while(optionChosen == -2){
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				optionChosen = nextRoundSetUp(currentState.getPlayerWhite(), currentState.getPlayerBlack());
			}
			currentState.getPlayerWhite().otherPersonOption(optionChosen);
			playerToMove = currentState.getPlayerWhite();
			
		}
		if (optionChosen == 0) {
			currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(), playerToMove,
					newBoard, true);
		} else if (optionChosen == 1) {
			currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(), playerToMove,
					newBoard, false);
		}
		playerToMove.setGoingFirst(true);
		currentState.setFirstMove(true);
		this.tellAll(currentState);
		playGame();
		return optionChosen;
	}

	protected int nextRoundSetUp(Player firstToMove, Player secondToMove) {
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
		if (PlayerToMove.isAI()) {
			this.tellAll(false);
		} else {
			this.tellAll(true);
		}
		if(PlayerToMove instanceof HardAIPlayer || PlayerToMove instanceof EasyAIPlayer){
			Thread newThread = new Thread(PlayerToMove);
			newThread.start();
		}
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
			return false;
		} else {
			playTurnSound(currentState.isSumoPush());
			history.add(currentState);
			currentState = state;
			this.tellAll(currentState);
			return true;
		}
	}

	public boolean playTurn(Position placeClicked) {
		if (currentState.wasWinningMove()) {
			boolean gameOver;
			Player winner;
			Player loser;
			if (currentState.getPlayerToMove().equals(currentState.getPlayerWhite())) {
				winner = currentState.getPlayerWhite();
				loser = currentState.getPlayerBlack();
			} else {
				loser = currentState.getPlayerWhite();
				winner = currentState.getPlayerBlack();
			}
			gameOver = incrementScoreAtEndOfGame(winner);
			updateStats(winner, loser);
			if (!gameOver) {
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
				Board board = currentState.getBoard();
				currentState.setColourToMove(board.getColourName(board.findColor(posToMove)));
				if (nextTurn(++numOfNoGoes)) {
					currentState.getPlayerToMove().incrementScore(1);
					this.tellAll(currentState);
					this.tellAll(currentState.getPlayerToMove());
					return true;
				}
			}
		} else {
			this.tellAll(posToMove);
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
							currentState.getPlayerToMove().TurnEnded(null);
						}
					}
				} else if (tryToMove((Position) arg)) {
					Player temp = currentState.getPlayerToMove();
					if (playTurn((Position) arg)) {
						return;
					}
					if (!temp.equals(currentState.getPlayerToMove())){
						currentState.getPlayerToMove().TurnEnded(null);
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

	public Stack<State> getHistory() {
		return history;
	}

	public void playTurnSound(boolean sumo) {
		GeneralSettingsManager manager = new GeneralSettingsManager();
		GeneralSettings settings = manager.getGeneralSettings();
		if (settings.isSoundOn()) {
			try {
				AudioInputStream inputStream;
				if (sumo) {
					inputStream = AudioSystem.getAudioInputStream(new File("sumo.wav"));
				} else {
					inputStream = AudioSystem.getAudioInputStream(new File("pipe.wav"));
				}
				Clip clip = AudioSystem.getClip();
				clip.open(inputStream);
				FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				floatControl.setValue(settings.getSoundVolume());
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
