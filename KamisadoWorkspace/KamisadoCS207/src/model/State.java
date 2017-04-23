package model;

import java.io.Serializable;
import java.util.ArrayList;

import player.Player;

public final class State implements Serializable {

	private static final long serialVersionUID = 1L;
	private final int boardUpperLimit = 7;
	private final int boardLowerLimit = 0;

	private Board currentBoard;
	private ArrayList<Position> validMoves;
	private Piece[][] pieces;
	private Position startingPosition;
	private String colourToMove;
	private Move previousMove;
	private Player playerWhite;
	private Player playerBlack;
	private Player PlayerToMove;
	private Position pieceToMove;
	private boolean firstMove = false;
	private boolean gameOver = false;
	private boolean sumoPush = false;

	public State(Player playerWhite, Player playerBlack, Player playerToMove, Boolean random) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		this.PlayerToMove = playerToMove;
		currentBoard = new Board(random);
		pieces = currentBoard.getPieces();
		validMoves = new ArrayList<>();
		previousMove = null;
		pieceToMove = null;
		startingPosition = null;
	}

	public State(State state, Board board) {
		this.startingPosition = state.startingPosition;
		this.colourToMove = state.colourToMove;
		this.currentBoard = new Board(board);
		this.previousMove = state.previousMove;
		this.pieces = currentBoard.getPieces();
		this.playerWhite = state.playerWhite;
		this.playerBlack = state.playerBlack;
		this.PlayerToMove = state.PlayerToMove;
		this.pieceToMove = state.pieceToMove;
		this.firstMove = state.firstMove;
		if (state.validMoves != null) {
			this.validMoves = new ArrayList<Position>();
			for (int index = 0; index < state.validMoves.size(); index++) {
				if (state.validMoves.get(index) != null) {
					this.validMoves.add(new Position(state.validMoves.get(index)));
				}
			}
		}
	}

	public State(Player playerWhite, Player playerBlack, Player playerToMove, Board previousBoard, boolean b) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		this.PlayerToMove = playerToMove;
		this.currentBoard = new Board(previousBoard);
		this.pieces = currentBoard.getPieces();
		if (playerToMove.equals(playerWhite)) {
			currentBoard.fillHomeRow(playerBlack.getPlayerTeam(), b);
			currentBoard.fillHomeRow(playerWhite.getPlayerTeam(), !b);
		} else {
			currentBoard.fillHomeRow(playerWhite.getPlayerTeam(), b);
			currentBoard.fillHomeRow(playerBlack.getPlayerTeam(), !b);
		}
		validMoves = new ArrayList<>();
		previousMove = null;
		pieceToMove = null;
		startingPosition = null;
	}

	public Position calcPieceToMove() {
		flipPlayerToMove();
		for (int i = 0; i <= boardUpperLimit; i++) {
			for (int j = 0; j <= boardUpperLimit; j++) {
				if (pieces[i][j] != null) {
					if (pieces[i][j].getPieceName().equals(colourToMove)
							&& pieces[i][j].getTeam().equals(PlayerToMove.getPlayerTeam())) {
						pieceToMove = new Position(i, j);
						return pieceToMove;
					}
				}
			}
		}
		return null;
	}

	public ArrayList<Position> calcValidMoves(Position position) {
		validMoves.clear();

		startingPosition = position;
		int startx = position.getX();
		int starty = position.getY();
		PieceType pieceToMoveType = pieces[startx][starty].getPieceType();
		int distanceCanMove = pieceToMoveType.getMaxMovement();
		if (PlayerToMove.getPlayerTeam().equals("TeamWhite")) {
			options(startx, startx, starty, -1, 1, distanceCanMove);// left up
			options((7 - starty), startx, starty, 0, 1, distanceCanMove);// middle up
			options((7 - startx), startx, starty, 1, 1, distanceCanMove);// right up

			if (!legal(new Position(startx, starty + 1))) {
				checkSumoPush(startx, starty, true);
			}
		} else {
			options(startx, startx, starty, -1, -1, distanceCanMove);// left down
			options(starty, startx, starty, 0, -1, distanceCanMove);// middle down
			options((7 - startx), startx, starty, 1, -1, distanceCanMove);// right down

			if (!legal(new Position(startx, starty - 1))) {
				checkSumoPush(startx, starty, false);
			}
		}
		return validMoves;
	}

	private void options(int options, int x, int y, int incrementx, int incrementy, int numSpacesCanMove) {
		if (options > numSpacesCanMove) {
			options = numSpacesCanMove;
		}
		for (int j = 0; j < options; j++) {
			x = x + incrementx;
			y = y + incrementy;
			if ((x >= boardLowerLimit && x <= boardUpperLimit) && (y >= boardLowerLimit && y <= boardUpperLimit)) {
				if (pieces[x][y] == null) {
					validMoves.add(new Position(x, y));
				} else {
					break;
				}
			}
		}
	}

	private void checkSumoPush(int posX, int posY, boolean checkingUp) {
		PieceType pieceType = pieces[posX][posY].getPieceType();
		if (!pieceType.equals(PieceType.Standard)) {
			if (sumoLoop(posX, posY, pieceType.getPiecesItCanMove(), checkingUp)) {
				int increment = -1;
				if (checkingUp) {
					increment = 1;
				}
				validMoves.add(new Position(posX, posY + increment));
			}
		}
	}

	private boolean sumoLoop(int x, int y, int piecesCanMove, boolean checkingUp) {
		int increment = -1;
		if (checkingUp) {
			increment = 1;
		}
		int tempVal;
		for (int i = 1; i <= piecesCanMove + 1; i++) {
			tempVal = y + (i * increment);
			if (tempVal > boardUpperLimit || tempVal < boardLowerLimit) {
				return false;
			} else if (pieces[x][tempVal] == null) {
				return true;
			} else if (pieces[x][tempVal].getTeam().equals(pieces[x][y].getTeam())) {
				return false;
			} else if (pieces[x][y].getPieceType().compareTo(pieces[x][tempVal].getPieceType()) > 0) {
				continue;
			} else {
				return false;
			}
		}
		return false;
	}

	public State make(Position endPosition) {
		if (legal(endPosition)) {
			setSumoPush(false);
			if (pieces[endPosition.getX()][endPosition.getY()] != null) {
				setSumoPush(true);
			}
			Board newBoard = new Board(currentBoard);
			newBoard.move(startingPosition, endPosition);
			if (newBoard != null) {
				colourToMove = newBoard.getColourToMove();
				State newState = new State(this, newBoard);
				newState.setPreviousMove(new Move(startingPosition, endPosition,
						newBoard.findPieceAtLoc(endPosition.getX(), endPosition.getY())));
				if (isSumoPush()) {
					newState.flipPlayerToMove();
				}
				return newState;
			}
		}
		return null;
	}

	private boolean legal(Position position) {
		for (Position pos : validMoves) {
			if (pos.getX() == position.getX() && pos.getY() == position.getY()) {
				return true;
			}
		}
		return false;
	}

//	public Piece findPiece(Position position) {
//		return pieces[position.getX()][position.getY()];
//	}

	public void flipPlayerToMove() {
		if (PlayerToMove.equals(playerWhite)) {
			PlayerToMove = playerBlack;
		} else {
			PlayerToMove = playerWhite;
		}
	}

	public Board getBoard() {
		return currentBoard;
	}

	public ArrayList<Position> getValidMoves() {
		return validMoves;
	}

	public Move getPreviousMove() {
		return previousMove;
	}

	private void setPreviousMove(Move previousMove) {
		this.previousMove = previousMove;
	}

	public boolean wasWinningMove() {
		return currentBoard.gameOver(previousMove.getEndPos().getY());
	}

//	public String getColourToMove() {
//		return colourToMove;
//	}

	public Player getPlayerToMove() {
		return PlayerToMove;
	}

//	public Board getCurrentBoard() {
//		return currentBoard;
//	}

	public Piece[][] getPieces() {
		return pieces;
	}

	public Position getStartingPosition() {
		return startingPosition;
	}

	public Player getPlayerWhite() {
		return playerWhite;
	}

	public Player getPlayerBlack() {
		return playerBlack;
	}

	public void setColourToMove(String colour) {
		colourToMove = colour;
	}

//	public void setValidMoves(ArrayList<Position> arrayList) {
//		validMoves = arrayList;
//	}

	public Position getPieceToMove() {
		return pieceToMove;
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isSumoPush() {
		return sumoPush;
	}

	public void setSumoPush(boolean sumoPush) {
		this.sumoPush = sumoPush;
	}
}