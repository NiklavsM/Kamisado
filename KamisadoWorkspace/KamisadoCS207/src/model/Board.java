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
	private PieceObject[][] pieces;
	private static Color[][] boardColours;
	private static Color[] defaultColours;// could take take dfault colors from file
	private static final int boardSize = 8;
	private Color colourToMove;

	public Board() {
		defaultColours = new Color[] { br, gr, r, y, p, c, bl, or };
		pieces = new PieceObject[boardSize][boardSize];
		setDefaultBoardColours();
		//setRandomBoardColours();
		initialisePiecePositions();
	}

	public Board(Board other) {
		if (other.pieces != null) {
			this.pieces = new PieceObject[boardSize][boardSize];
			for (int index = 0; index < boardSize; index++) {
				for (int j = 0; j < boardSize; j++) {
					if (other.pieces[index][j] != null) {
						this.pieces[index][j] = new PieceObject(other.pieces[index][j]);
					} else {
						this.pieces[index][j] = null;
					}

				}
			}
		}
	}

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

		pieces[0][7] = new PieceObject(Piece.TeamBlackOrange, PieceType.Standard);
		pieces[0][7].getPiece().setColour(or);
		pieces[1][7] = new PieceObject(Piece.TeamBlackBlue, PieceType.Standard);
		pieces[1][7].getPiece().setColour(bl);
		pieces[2][7] = new PieceObject(Piece.TeamBlackCyan, PieceType.Standard);
		pieces[2][7].getPiece().setColour(c);
		pieces[3][7] = new PieceObject(Piece.TeamBlackPink, PieceType.Standard);
		pieces[3][7].getPiece().setColour(p);
		pieces[4][7] = new PieceObject(Piece.TeamBlackYellow, PieceType.Standard);
		pieces[4][7].getPiece().setColour(y);
		pieces[5][7] = new PieceObject(Piece.TeamBlackRed, PieceType.Standard);
		pieces[5][7].getPiece().setColour(r);
		pieces[6][7] = new PieceObject(Piece.TeamBlackGreen, PieceType.Standard);
		pieces[6][7].getPiece().setColour(gr);
		pieces[7][7] = new PieceObject(Piece.TeamBlackBrown, PieceType.Standard);
		pieces[7][7].getPiece().setColour(br);

		pieces[7][0] = new PieceObject(Piece.TeamWhiteOrange, PieceType.Standard);
		pieces[7][0].getPiece().setColour(or);
		pieces[6][0] = new PieceObject(Piece.TeamWhiteBlue, PieceType.Standard);
		pieces[6][0].getPiece().setColour(bl);
		pieces[5][0] = new PieceObject(Piece.TeamWhiteCyan, PieceType.Standard);
		pieces[5][0].getPiece().setColour(c);
		pieces[4][0] = new PieceObject(Piece.TeamWhitePink, PieceType.Standard);
		pieces[4][0].getPiece().setColour(p);
		pieces[3][0] = new PieceObject(Piece.TeamWhiteYellow, PieceType.Standard);
		pieces[3][0].getPiece().setColour(y);
		pieces[2][0] = new PieceObject(Piece.TeamWhiteRed, PieceType.Standard);
		pieces[2][0].getPiece().setColour(r);
		pieces[1][0] = new PieceObject(Piece.TeamWhiteGreen, PieceType.Standard);
		pieces[1][0].getPiece().setColour(gr);
		pieces[0][0] = new PieceObject(Piece.TeamWhiteBrown, PieceType.Standard);
		pieces[0][0].getPiece().setColour(br);

	}
	
	public void fillHomeRow(String team, boolean fillLeft){
		int teamHomeBase;
		int homeRowCounter = 7;
		int increment = -1;
		int permx = 7;
		if(fillLeft){
			increment = 1;
			homeRowCounter = 0;
			permx = 0;
		}
		int x = permx;
		if(team.equals("White")){
			teamHomeBase = 0;
			for(int y = 0; y < 8; y++){
				x = permx;
				for(int j = 0; j < 8; j++){
					if(pieces[x][y] != null){
						if(pieces[x][y].getPiece().getTeam().equals(team)){
							if(homeRowCounter == x && teamHomeBase == y){
								homeRowCounter += increment;
							}else{
								pieces[homeRowCounter][teamHomeBase] = pieces[x][y];
								pieces[x][y] = null;
								System.out.println("moving x:" + x + " y:" + y + " to x:" + homeRowCounter + " y:" + teamHomeBase);
								homeRowCounter += increment;
							}
						}
					}
					x += increment;
				}
			}
		}else{
			teamHomeBase = 7;
			for(int y = 7; y >= 0; y--){
				x = permx;
				for(int j = 0; j < 8; j++){
					if(pieces[x][y] != null){
						if(pieces[x][y].getPiece().getTeam().equals(team)){
							if(homeRowCounter == x && teamHomeBase == y){
								homeRowCounter += increment;
							}else{
								pieces[homeRowCounter][teamHomeBase] = pieces[x][y];
								pieces[x][y] = null;
								homeRowCounter += increment;
							}
						}
					}
					x += increment;
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

	private PieceObject removePiece(int x, int y) {
		PieceObject pieceToRemove = pieces[x][y];
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
		if(pieces[x][y] != null){
			return pieces[x][y].getPiece();
		}
		return null;
	}

	public PieceObject[][] getPieces() {
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
