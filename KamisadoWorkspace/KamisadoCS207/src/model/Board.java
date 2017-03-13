package model;

import java.awt.Color;

public class Board {
	private final Color or = new Color(250, 190, 50);// orange
	private final Color bl = new Color(14, 104, 243);// blue
	private final Color br = new Color(148, 104, 39);// Brown
	private final Color r = new Color(249, 69, 24);// red
	private final Color c = new Color(95, 207, 235);// cyan
	private final Color gr = new Color(108, 216, 68);// green
	private final Color y = new Color(245, 245, 26);// yellow
	private final Color p = new Color(239, 86, 208);// pink
	private Piece[][] pieces;
	private Color[][] boardColours;
	private final int boardSize = 8;

	public Board() {
		pieces = new Piece[boardSize][boardSize];
		setBoardColours();
		initialisePiecePositions();
	}

	public Board(Board board) {
		this.pieces = board.pieces;
		this.boardColours = board.boardColours;
	}

	private void setBoardColours() {

		boardColours = new Color[][] { { br, c, bl, y, p, gr, r, or }, { gr, br, y, r, c, p, or, bl },
				{ r, y, br, gr, bl, or, p, c }, { y, bl, c, br, or, r, gr, p }, { p, gr, r, or, br, c, bl, y },
				{ c, p, or, bl, gr, br, y, r }, { bl, or, p, c, r, y, br, gr }, { or, r, gr, p, y, bl, c, br } };
	}

	private void initialisePiecePositions() {
		pieces[0][7] = Piece.TeamBlackOrange;

		pieces[1][7] = Piece.TeamBlackBlue;
		pieces[2][7] = Piece.TeamBlackCyan;
		pieces[3][7] = Piece.TeamBlackPink;
		pieces[4][7] = Piece.TeamBlackYellow;
		pieces[5][7] = Piece.TeamBlackRed;
		pieces[6][7] = Piece.TeamBlackGreen;
		pieces[7][7] = Piece.TeamBlackBrown;

		pieces[7][0] = Piece.TeamWhiteOrange;
		pieces[6][0] = Piece.TeamWhiteBlue;
		pieces[5][0] = Piece.TeamWhiteCyan;
		pieces[4][0] = Piece.TeamWhitePink;
		pieces[3][0] = Piece.TeamWhiteYellow;
		pieces[2][0] = Piece.TeamWhiteRed;
		pieces[1][0] = Piece.TeamWhiteGreen;
		pieces[0][0] = Piece.TeamWhiteBrown;

		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 8; i++) {
				if (pieces[i][j * 7] != null) {
					pieces[i][j * 7].setColour(boardColours[i][j * 7]);
				}
			}
		}
	}

	public Board make(Position startPosition, Position endPosition) {
		Board freshBoard = new Board(this);
		freshBoard.move(startPosition, endPosition);
		return freshBoard;
	}

	private void move(Position startPosition, Position endPosition) {
		int endx = endPosition.getX();
		int endy = endPosition.getY();
		pieces[endx][endy] = removePiece(startPosition.getX(), startPosition.getY());
	}

	private Piece removePiece(int x, int y) {
		Piece pieceToRemove = pieces[x][y];
		pieces[x][y] = null;
		return pieceToRemove;
	}

	public Position findPiecePos(Piece piece) {
		Position foundPos = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieces[i][j].equals(piece)) {
					foundPos = new Position(i, j);
					break;
				}
			}
		}
		return foundPos;
	}

	public boolean gameOver(int y) {
		return y == 0 || y == 7;
	}

	public Color findColor(Position position) {
		return boardColours[position.getX()][position.getY()];
	}

	public Position findColorPos(String teamColor, Color pieceColor) {
		return findPiecePos(Piece.valueOf("Team" + teamColor + "-" + pieceColor(pieceColor)));
	}

	// public Position findPiece(Piece piece){
	// for(int i = 0; i < 8; i++){
	// for(int j = 0; j < 8; j++){
	// if(pieces[i][j].equals(piece)){
	// return new Position(i,j);
	// }
	// }
	// }
	// return null;
	// }

	public Piece findPieceAtLoc(int x, int y) {
		return pieces[x][y];
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	public Color[][] getBoardColours() {
		return boardColours;
	}

	private String pieceColor(Color pieceColor) {
		if (pieceColor.equals(or)) {
			return "Orange";
		} else if (pieceColor.equals(bl)) {
			return "Blue";
		} else if (pieceColor.equals(br)) {
			return "Brown";
		} else if (pieceColor.equals(r)) {
			return "Red";
		} else if (pieceColor.equals(c)) {
			return "Cyan";
		} else if (pieceColor.equals(gr)) {
			return "Green";
		} else if (pieceColor.equals(y)) {
			return "Yellow";
		} else if (pieceColor.equals(p)) {
			return "Pink";
		} else {
			return "";
		}
	}
}
