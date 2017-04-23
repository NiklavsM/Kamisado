package model;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;

public final class Board implements Serializable {

	private static final long serialVersionUID = 1L;
	private Piece[][] pieces;
	private MyColour[][] boardColours = new MyColour[boardSize][boardSize];
	private static MyColour[] defaultColours = { MyColour.brown, MyColour.green, MyColour.red, MyColour.yellow,
			MyColour.pink, MyColour.cyan, MyColour.blue, MyColour.orange };

	private static final int boardSize = 8;
	private String colourToMove;
	private boolean isRandom;

	public Board(boolean random) {
		pieces = new Piece[boardSize][boardSize];
		isRandom = random;
		if (random) {
			setRandomBoardColours();
		} else {
			setDefaultBoardColours();
		}
		initialisePiecePositions();
	}

	public Board(Board other) {
		isRandom = other.isRandom();
		if (other.pieces != null) {
			this.pieces = new Piece[boardSize][boardSize];
			for (int index = 0; index < boardSize; index++) {
				for (int j = 0; j < boardSize; j++) {
					boardColours[index][j] = other.getBoardColours()[index][j];
					if (other.pieces[index][j] != null) {
						this.pieces[index][j] = new Piece(other.findPieceAtLoc(index, j));
					} else {
						this.pieces[index][j] = null;
					}
				}
			}
		}
	}

	public String getColourName(Color colour) {
		if (colour.equals(MyColour.orange.getColour()))
			return "orange";
		if (colour.equals(MyColour.blue.getColour()))
			return "blue";
		if (colour.equals(MyColour.brown.getColour()))
			return "brown";
		if (colour.equals(MyColour.red.getColour()))
			return "red";
		if (colour.equals(MyColour.cyan.getColour()))
			return "cyan";
		if (colour.equals(MyColour.green.getColour()))
			return "green";
		if (colour.equals(MyColour.yellow.getColour()))
			return "yellow";
		if (colour.equals(MyColour.pink.getColour()))
			return "pink";
		return null;
	}

	public boolean isRandom() {
		return isRandom;
	}

	private void setDefaultBoardColours() {
		setBoardColors(defaultColours);
	}

	public void setRandomBoardColours() {
		MyColour[] colorsTemp = { MyColour.brown, MyColour.green, MyColour.red, MyColour.yellow, MyColour.pink,
				MyColour.cyan, MyColour.blue, MyColour.orange };
		Random rnd = new Random();
		for (int i = 0; i < colorsTemp.length; i++) {
			int index = rnd.nextInt(colorsTemp.length);
			MyColour temp = colorsTemp[index];
			colorsTemp[index] = colorsTemp[i];
			colorsTemp[i] = temp;
		}
		setBoardColors(colorsTemp);
		shiftColums();
	}

	public void shiftColums() {
		Random rnd = new Random();
		for (int i = 0; i < boardSize; i++) {
			int index = rnd.nextInt(boardSize);
			for (int k = 0; k < boardSize; k++) {
				MyColour temp = boardColours[index][k];
				boardColours[index][k] = boardColours[i][k];
				boardColours[i][k] = temp;
			}
		}
	}

	private void setBoardColors(MyColour[] colorsTemp) {
		int plusOne = 0;
		int plusThree = 1;
		int plusFive = 6;
		for (; plusOne < boardSize; plusOne++, plusThree = plusThree + 3, plusFive = plusFive + 5) {
			boardColours[plusOne][plusOne] = colorsTemp[0];
			boardColours[plusOne][(plusThree + 4) % 8] = colorsTemp[1];
			boardColours[plusOne][(plusFive) % 8] = colorsTemp[2];
			boardColours[plusOne][((7 - plusOne) + 4) % 8] = colorsTemp[3];
			boardColours[plusOne][(plusOne + 4) % 8] = colorsTemp[4];
			boardColours[plusOne][plusThree % 8] = colorsTemp[5];
			boardColours[plusOne][(plusFive + 4) % 8] = colorsTemp[6];
			boardColours[plusOne][7 - plusOne] = colorsTemp[7];
		}
	}

	private void initialisePiecePositions() {
		for (int i = 0; i < boardSize; i++) {
			pieces[i][7] = new Piece("TeamBlack", PieceType.Standard, boardColours[i][7].toString());
			pieces[i][0] = new Piece("TeamWhite", PieceType.Standard, boardColours[i][0].toString());
		}
	}

	public void fillHomeRow(String team, boolean fillLeft) {
		int teamHomeBase;
		int homeRowCounter = 7;
		int increment = -1;
		int permx = 7;
		if (fillLeft) {
			increment = 1;
			homeRowCounter = 0;
			permx = 0;
		}
		int x = permx;
		if (team.equals("TeamWhite")) {
			teamHomeBase = 0;
			for (int y = 0; y < 8; y++) {
				x = permx;
				homeRowCounter = fillHomeRowHelper(homeRowCounter, increment, x, y, team, teamHomeBase);
			}
		} else {
			teamHomeBase = 7;
			for (int y = 7; y >= 0; y--) {
				x = permx;
				homeRowCounter = fillHomeRowHelper(homeRowCounter, increment, x, y, team, teamHomeBase);
			}
		}
	}

	private int fillHomeRowHelper(int homeRowCounter, int increment, int x, int y, String team, int teamHomeBase) {
		for (int j = 0; j < boardSize; j++) {
			if (pieces[x][y] != null) {
				if (pieces[x][y].getTeam().equals(team)) {
					if (homeRowCounter == x && teamHomeBase == y) {
						homeRowCounter += increment;
					} else {
						pieces[homeRowCounter][teamHomeBase] = pieces[x][y];
						pieces[x][y] = null;
						homeRowCounter += increment;
					}
				}
			}
			x += increment;
		}
		return homeRowCounter;
	}

	// private Board make(Position startPosition, Position endPosition) {
	// Board freshBoard = new Board(this);
	// freshBoard.move(startPosition, endPosition);
	// return freshBoard;
	// }

	public void move(Position startPosition, Position endPosition) {
		PieceType pieceType = pieces[startPosition.getX()][startPosition.getY()].getPieceType();
		int endx = endPosition.getX();
		int endy = endPosition.getY();
		colourToMove = boardColours[endx][endy].toString();
		if (pieces[endx][endy] != null) {
			boolean pushingUp = startPosition.getY() < endy;
			sumoPush(pushingUp, endPosition, pieceType.getPiecesItCanMove());
		}
		pieces[endx][endy] = removePiece(startPosition.getX(), startPosition.getY());
		if (gameOver(endy)) {
			pieces[endx][endy].promotePiece();
		}
	}

	private void sumoPush(boolean pushingUp, Position endPosition, int numOfPiecesCanMove) {
		int endx = endPosition.getX();
		int endy = endPosition.getY();
		int numOfPiecesActuallyPushing = 0;
		for (int i = 0; i < numOfPiecesCanMove; i++) {
			if (pushingUp) {
				if (pieces[endx][endy + i] == null) {
					break;
				}
			} else {
				if (pieces[endx][endy - i] == null) {
					break;
				}
			}
			numOfPiecesActuallyPushing++;
		}
		if (pushingUp) {
			colourToMove = boardColours[endx][endy + numOfPiecesActuallyPushing].toString();
		} else {
			colourToMove = boardColours[endx][endy - numOfPiecesActuallyPushing].toString();
		}
		for (int i = numOfPiecesActuallyPushing; i > 0; i--) {
			if (pushingUp) {
				pieces[endx][endy + i] = removePiece(endx, endy + i - 1);
			} else {
				pieces[endx][endy - i] = removePiece(endx, endy - i + 1);
			}
		}
	}

	private Piece removePiece(int x, int y) {
		Piece pieceToRemove = pieces[x][y];
		pieces[x][y] = null;
		return pieceToRemove;
	}

	// private Position findPiecePos(Piece piece) {
	// Position foundPos = null;
	// for (int i = 0; i < boardSize; i++) {
	// for (int j = 0; j < boardSize; j++) {
	// if (pieces[i][j].equals(piece)) {
	// foundPos = new Position(i, j);
	// break;
	// }
	// }
	// }
	// return foundPos;
	// }

	public boolean gameOver(int y) {
		return y == 0 || y == 7;
	}

	public Color findColor(Position position) {
		return boardColours[position.getX()][position.getY()].getColour();
	}

	public Piece findPieceAtLoc(int x, int y) {
		if (pieces[x][y] != null) {
			return pieces[x][y];
		}
		return null;
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	private MyColour[][] getBoardColours() {
		return boardColours;
	}

	public String getColourToMove() {
		return colourToMove;
	}

}
