package model;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public final class Board implements Serializable {
	private static final Color or = new Color(250, 190, 50);// orange
	private static final Color bl = new Color(14, 104, 243);// blue
	private static final Color br = new Color(148, 104, 39);// Brown
	private static final Color r = new Color(249, 69, 24);// red
	private static final Color c = new Color(95, 207, 235);// cyan
	private static final Color gr = new Color(108, 216, 68);// green
	private static final Color y = new Color(245, 245, 26);// yellow
	private static final Color p = new Color(239, 86, 208);// pink
	private Piece[][] pieces;
	private static Color[][] boardColours;
	private static Color[] defaultColours;// could take take dfault colors from file
	private static final int boardSize = 8;
	private Color colourToMove;

	public Board() {
		defaultColours = new Color[] { br, gr, r, y, p, c, bl, or };
		pieces = new Piece[boardSize][boardSize];
		// setDefaultBoardColours();
		setRandomBoardColours();
		initialisePiecePositions();
	}

	public Board(Board other) {
		// if(other.a != null) {
		// this.a = new A(other.a);
		// }
		if (other.pieces != null) {
			this.pieces = new Piece[boardSize][boardSize];
			for (int index = 0; index < boardSize; index++) {
				for (int j = 0; j < boardSize; j++) {
					if (other.pieces[index][j] != null) {
						this.pieces[index][j] = Piece.valueOf(other.pieces[index][j].toString());
					} else {
						this.pieces[index][j] = null;
					}

				}

			}
		}
	}

	// public Board(Board board) {
	// this(board.getPieces());
	// }

	private void setDefaultBoardColours() {
		setBoardColors(defaultColours);
	}

	private void setRandomBoardColours() {
		Color[] colorsTemp = new Color[] { br, gr, r, y, p, c, bl, or };
		Random rnd = new Random();
		for (int i = 0; i < colorsTemp.length; i++) {
			int index = rnd.nextInt(colorsTemp.length);
			Color temp = colorsTemp[index];
			colorsTemp[index] = colorsTemp[i];
			colorsTemp[i] = temp;
		}
		setBoardColors(colorsTemp);
		shiftColums();
	}
	
	public void shiftColums(){
		Random rnd = new Random();
		for (int i = 0;i<8;i++){
			int index = rnd.nextInt(8);
			for(int k = 0;k<8;k++){
				Color temp = boardColours[index][k];
				boardColours[index][k] = boardColours[i][k];
				boardColours[i][k] = temp;			
			}
		}
	}

	private void setBoardColors(Color[] colors) {
		int plusThree = 1;
		int plusFive = 6;
		boardColours = new Color[8][8];
		for (int i = 0; i < 8; i++, plusThree = plusThree + 3, plusFive = plusFive + 5) {
			boardColours[i][i] = colors[0];
			boardColours[i][7 - i] = colors[7];
			boardColours[i][((7 - i) + 4) % 8] = colors[3];
			boardColours[i][(i + 4) % 8] = colors[4];
			boardColours[i][plusThree % 8] = colors[5];
			boardColours[i][(plusThree + 4) % 8] = colors[1];
			boardColours[i][(plusFive) % 8] = colors[2];
			boardColours[i][(plusFive + 4) % 8] = colors[6];
		}
	}

	private void initialisePiecePositions() {

		pieces[0][7] = Piece.TeamBlackOrange;
		pieces[0][7].setColour(or);
		pieces[1][7] = Piece.TeamBlackBlue;
		pieces[1][7].setColour(bl);
		pieces[2][7] = Piece.TeamBlackCyan;
		pieces[2][7].setColour(c);
		pieces[3][7] = Piece.TeamBlackPink;
		pieces[3][7].setColour(p);
		pieces[4][7] = Piece.TeamBlackYellow;
		pieces[4][7].setColour(y);
		pieces[5][7] = Piece.TeamBlackRed;
		pieces[5][7].setColour(r);
		pieces[6][7] = Piece.TeamBlackGreen;
		pieces[6][7].setColour(gr);
		pieces[7][7] = Piece.TeamBlackBrown;
		pieces[7][7].setColour(br);

		pieces[7][0] = Piece.TeamWhiteOrange;
		pieces[7][0].setColour(or);
		pieces[6][0] = Piece.TeamWhiteBlue;
		pieces[6][0].setColour(bl);
		pieces[5][0] = Piece.TeamWhiteCyan;
		pieces[5][0].setColour(c);
		pieces[4][0] = Piece.TeamWhitePink;
		pieces[4][0].setColour(p);
		pieces[3][0] = Piece.TeamWhiteYellow;
		pieces[3][0].setColour(y);
		pieces[2][0] = Piece.TeamWhiteRed;
		pieces[2][0].setColour(r);
		pieces[1][0] = Piece.TeamWhiteGreen;
		pieces[1][0].setColour(gr);
		pieces[0][0] = Piece.TeamWhiteBrown;
		pieces[0][0].setColour(br);

	}
	
	public void fillBoardLeft(String team, Piece[][] oldPieces){
		int teamHomeBase;
		int homeRowCounter = 0;
		if(team.equals("White")){
			teamHomeBase = 0;
			for(int y = 0; y < 8; y++){
				for(int x = 0; x < 8; x++){
					if(oldPieces[x][y] != null){
						if(oldPieces[x][y].getTeam().equals(team)){
							pieces[homeRowCounter][teamHomeBase] = oldPieces[x][y];
							homeRowCounter++;
						}
					}
				}
			}
		}else{
			teamHomeBase = 7;
			for(int y = 7; y >= 0; y--){
				for(int x = 0; x < 8; x++){
					if(oldPieces[x][y] != null){
						if(oldPieces[x][y].getTeam().equals(team)){
							pieces[homeRowCounter][teamHomeBase] = oldPieces[x][y];
							homeRowCounter++;
						}
					}
				}
			}
		}
	}

	public void fillBoardRight(String team, Piece[][] oldPieces){
		int teamHomeBase;
		int homeRowCounter = 7;
		if(team.equals("White")){
			teamHomeBase = 0;
			for(int y = 0; y < 8; y++){
				for(int x = 7; x >= 0; x--){
					if(oldPieces[x][y] != null){
						if(oldPieces[x][y].getTeam().equals(team)){
							pieces[homeRowCounter][teamHomeBase] = oldPieces[x][y];
							homeRowCounter--;
						}
					}
				}
			}
		}else{
			teamHomeBase = 7;
			for(int y = 7; y >= 0; y--){
				for(int x = 7; x >= 0; x--){
					if(oldPieces[x][y] != null){
						if(oldPieces[x][y].getTeam().equals(team)){
							pieces[homeRowCounter][teamHomeBase] = oldPieces[x][y];
							homeRowCounter--;
						}
					}
				}
			}
		}
	}
	
	public Board make(Position startPosition, Position endPosition) {
		Board freshBoard = new Board(this);
		freshBoard.move(startPosition, endPosition);
		return freshBoard;
	}

	public void move(Position startPosition, Position endPosition) {
		PieceType pieceType = pieces[startPosition.getX()][startPosition.getY()].getPieceType();
		int endx = endPosition.getX();
		int endy = endPosition.getY();
		colourToMove = boardColours[endx][endy];
		if(pieces[endx][endy] != null){
			boolean pushingUp = startPosition.getY() < endy;
			sumoPush(pushingUp, endPosition, pieceType.getPiecesItCanMove());
		}
		pieces[endx][endy] = removePiece(startPosition.getX(), startPosition.getY());
		if(gameOver(endy)){
			pieces[endx][endy].promotePiece();
		}
	}
	
	private void sumoPush(boolean pushingUp, Position endPosition, int numOfPiecesCanMove){
		int endx = endPosition.getX();
		int endy = endPosition.getY();
		int numOfPiecesActuallyPushing = 0;
		for(int i = 0; i < numOfPiecesCanMove; i++){
			if(pushingUp){
				if(pieces[endx][endy+i] == null){
					break;
				}
			}else{
				if(pieces[endx][endy-i] == null){
					break;
				}
			}
			numOfPiecesActuallyPushing++;
		}
		if(pushingUp){
			colourToMove = boardColours[endx][endy+numOfPiecesActuallyPushing];
		}else{
			colourToMove = boardColours[endx][endy-numOfPiecesActuallyPushing];
		}
		for(int i = numOfPiecesActuallyPushing; i > 0; i--){
			if(pushingUp){
				System.out.println("placing X:" + endx + " Y:" + (endy+i-1) + " to X:" + endx + " Y:" + (endy+i));
				pieces[endx][endy+i] = removePiece(endx,endy+i-1);
			}else{
				pieces[endx][endy-i] = removePiece(endx,endy-i+1);
			}
		}
		
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

	public Color getColourToMove() {
		return colourToMove;
	}
	
	
}
