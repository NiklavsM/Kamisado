package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import player.Player;

public class SpeedGameDriver extends GameDriver implements MyObserver, MyObservable, Serializable {

	private int timerLimit;
	private Timer timer;
	private boolean timeOut = false;

	public SpeedGameDriver(Player white, Player black, Player playerToStart, int gameLength, int timerLimit,
			boolean randomBoard) {
		super(white, black, playerToStart, gameLength, randomBoard);
		this.timerLimit = timerLimit;
		currentState.setTime(timerLimit);
		createTimer();
	}

	public SpeedGameDriver(SpeedGameDriver gameDriver) {
		super(gameDriver);
		this.timerLimit = gameDriver.getTimerLimit();
		createTimer();
	}

	public void createTimer() {
		tellAll(timerLimit);
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentState != null) {
					currentState.setTime(currentState.getTime() - 1);
					tellAll(currentState.getTime());
					if (currentState.getTime() <= 0) {
						onTimeOut();
					}
				}
			}
		});
		timer.start();
	}

	public void onTimeOut() {
		timeOut = true;
		currentState.setGameOver(true);
		Player winningPlayer;
		if (currentState.getPlayerToMove().equals(currentState.getPlayerWhite())) {
			winningPlayer = currentState.getPlayerBlack();
		} else {
			winningPlayer = currentState.getPlayerWhite();
		}
		this.tellAll(winningPlayer);
		incrementScoreAtEndOfGame(winningPlayer);
		this.tellAll(currentState);
		timer.stop();
	}

	public void turnBegin() {
		currentState.setTime(timerLimit);
		tellAll(timerLimit);
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
			this.tellAll(currentState.getBoard());
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
		System.out.println(currentState.getPlayerToMove().getPlayerTeam());
		currentState.getPlayerWhite().setToFirstMove(true);
		currentState.getPlayerBlack().setToFirstMove(false);
		if (playerToMove.equals(currentState.getPlayerBlack())) {
			if (currentState.getTime() == 0) {
				currentState.getPlayerWhite().setToFirstMove(false);
				currentState.getPlayerBlack().setToFirstMove(true);
				optionChosen = currentState.getPlayerWhite().fillHomeRow();
			} else {
				playerToMove = currentState.getPlayerWhite();
				optionChosen = currentState.getPlayerBlack().fillHomeRow();
			}

		} else {
			if (currentState.getTime() == 0) {
				System.out.println("is first move");
				optionChosen = currentState.getPlayerBlack().fillHomeRow();
			} else {
				System.out.println("settings to black");
				playerToMove = currentState.getPlayerBlack();
				currentState.getPlayerWhite().setToFirstMove(false);
				currentState.getPlayerBlack().setToFirstMove(true);
				optionChosen = currentState.getPlayerWhite().fillHomeRow();
			}

		}
		System.out.println(playerToMove.getPlayerTeam());
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
		this.tellAll(currentState.getBoard());
		tellAll(timerLimit);// FIX
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
						currentState.setGameOver(true);
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

	public int getTimerLimit() {
		return timerLimit;
	}
}
