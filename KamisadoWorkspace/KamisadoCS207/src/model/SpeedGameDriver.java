package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import player.Player;

public class SpeedGameDriver extends GameDriver implements MyObserver, MyObservable, Serializable {

	private TimerInfo timerInfo;
	private Timer timer;
	private boolean timeOut = false;

	public SpeedGameDriver(Player white, Player black, Player playerToStart, int gameLength, int timerLimit,
			boolean randomBoard) {
		super(white, black, playerToStart, gameLength, randomBoard);
		timerInfo = new TimerInfo(timerLimit,timerLimit);
		createTimer();
	}

	public SpeedGameDriver(SpeedGameDriver gameDriver) {
		super(gameDriver);
		timerInfo = gameDriver.getTimerInfo();
		createTimer();
	}

	public void createTimer() {
		tellAll(timerInfo);
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (timerInfo != null) {
					timerInfo.setTimeLeft(timerInfo.getTimeLeft() - 1);;
					tellAll(timerInfo);
					if (timerInfo.getTimeLeft() <= 0) {
						onTimeOut();
					}
				}
			}
		});
		timer.start();
	}

	public void onTimeOut() {
		timeOut = true;
		if (currentState != null) {
			currentState.setGameOver(true);
			Player winner;
			Player loser;
			if (currentState.getPlayerToMove().equals(currentState.getPlayerWhite())) {
				winner = currentState.getPlayerBlack();
				loser = currentState.getPlayerWhite();
			} else {
				winner = currentState.getPlayerWhite();
				loser = currentState.getPlayerBlack();
			}
			this.tellAll(winner);
			incrementScoreAtEndOfGame(winner);
			updateStats(winner, loser);
			this.tellAll(currentState);
			timer.stop();
		}
	}

	public void turnBegin() {
		timerInfo.setTimeLeft(timerInfo.getTimerLimit());
		tellAll(timerInfo);
		timer.restart();
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
			turnBegin();
		}
	}

	public void reset() {
		boolean valid = false;
		while (!history.empty()) {
			currentState = history.pop();
			valid = true;
		}
		if (valid) {
			turnBegin();
			this.tellAll(currentState);
			this.tellAll(currentState.calcValidMoves(currentState.getStartingPosition()));
		}
	}

	public void saveGame() {
		if (history.empty() || currentState.isGameOver()) {
			JOptionPane.showMessageDialog(null, "Game has not started / is ended", "Game has not started / is ended",
					JOptionPane.ERROR_MESSAGE);
		} else {
			timer.stop();
			SaveManager s = new SaveManager();
			s.save(this);
			timer.start();
		}
	}

	public int nextRound() { // FIX
		timeOut = false;
		int optionChosen = 0;
		currentGameNum++;
		history = new Stack<>();
		Player playerToMove = currentState.getPlayerToMove();
		if (playerToMove.equals(currentState.getPlayerBlack())) {
			if (timerInfo.getTimeLeft() == 0) {
				optionChosen = nextRoundSetUp(currentState.getPlayerBlack(), currentState.getPlayerWhite());
			} else {
				playerToMove = currentState.getPlayerWhite();
				optionChosen = nextRoundSetUp(currentState.getPlayerWhite(), currentState.getPlayerBlack());
			}

		} else {
			if (timerInfo.getTimeLeft() == 0) {
				optionChosen = nextRoundSetUp(currentState.getPlayerWhite(), currentState.getPlayerBlack());
			} else {
				playerToMove = currentState.getPlayerBlack();
				optionChosen = nextRoundSetUp(currentState.getPlayerBlack(), currentState.getPlayerWhite());
			}
		}
		Board newBoard = new Board(currentState.getBoard());
		if (currentState.getBoard().isRandom()) {
			newBoard.setRandomBoardColours();
		}
		if (optionChosen == 0) {
			this.currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(), playerToMove,
					newBoard, true);
		} else if (optionChosen == 1) {
			this.currentState = new State(currentState.getPlayerWhite(), currentState.getPlayerBlack(), playerToMove,
					newBoard, false);
		}
		currentState.setFirstMove(true);
		this.tellAll(currentState);
		tellAll(timerInfo);
		playGame();
		turnBegin();
		return optionChosen;
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
							turnBegin();
						}
					}
				} else if (tryToMove((Position) arg)) {
					if (playTurn((Position) arg)) {
						timer.stop();
						return;
					} else {
						turnBegin();
					}
				}
				generateMove();
			}
		}
	}

	@Override
	public boolean incrementScoreAtEndOfGame(Player winner) {
		if (!timeOut) {
			PieceObject pieceThatWon = currentState.getPreviousMove().pieceMoved();
			winner.incrementScore(pieceThatWon.getPieceType().getPointValue());
		} else {
			winner.incrementScore(1);
		}
		if (winner.getScore() >= scoreToGet) {
			this.tellAll(winner.getPlayerName() + " has won the game! In " + currentGameNum + " round(s)!");
			return true;
		}
		return false;
	}

	public TimerInfo getTimerInfo() {
		return timerInfo;
	}
	
}
