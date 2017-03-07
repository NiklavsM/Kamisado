package model;

import java.awt.Color;
import java.util.ArrayList;

public class Board {

	private Piece[][] pieces;
	private Color[][] boardColours;
	private final int boardSize = 8;

	
	public Board(){
		pieces = new Piece[boardSize][boardSize];
		setBoardColours();
		initialisePiecePositions();
	}
	
	public void setBoardColours() {
		Color or = new Color(250, 190, 50);//orange
		Color bl = new Color(14, 104, 243);//blue
		Color br = new Color(148, 104, 39);//Brown
		Color r = new Color(249, 69, 24);//red
		Color c = new Color(95, 207, 235);//cyan
		Color gr = new Color(108, 216, 68);//green
		Color y = new Color(245, 245, 26);//yellow
		Color p = new Color(239, 86, 208);//pink

		boardColours = new Color[][] { 
			{ br, c, bl, y, p, gr, r, or },
			{ gr, br, y, r, c, p, or, bl },
			{ r, y, br, gr, bl, or, p, c }, 
			{ y, bl, c, br, or, r, gr, p },
			{ p, gr, r, or, br, c, bl, y }, 
			{ c, p, or, bl, gr, br, y, r },
			{ bl, or, p, c, r, y, br, gr },				
			{ or, r, gr, p, y, bl, c, br }
		    };

									   
	}
	
	public void initialisePiecePositions(){
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
	}
	
	public Board make(Position startPosition, Position endPosition){
		Board freshBoard = null;
		try {
			freshBoard = (Board) this.clone();
			freshBoard.move(startPosition, endPosition);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return freshBoard;
	}
	
	public boolean move(Position startPosition, Position endPosition){
		int endx = endPosition.getX();
		int endy = endPosition.getY();
		pieces[endx][endy] = removePiece(startPosition.getX(), startPosition.getY());
		return gameOver(endy);
	}
	
	private Piece removePiece(int x, int y){
		Piece pieceToRemove = pieces[x][y];
		pieces[x][y] = null;
		return pieceToRemove;
	}
	
	public Position findPiecePos(Piece piece){
		Position foundPos = null;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(pieces[i][j].equals(piece)){
					foundPos = new Position(i, j);
					break;
				}
			}
		}
		return foundPos;
	}

	private boolean gameOver(int y){
		if(y == 0 || y == 7){
			return true;
		}
		return false;
	}

	public Color findColor(Position position){
		return boardColours[7-position.getX()][position.getY()];
	}
	
	public Piece findPieceAtLoc(int x, int y){
		return pieces[x][7-y];
	}
	
	public Piece[][] getPieces() {
		return pieces;
	}

	public Color[][] getBoardColours() {
		return boardColours;
	}

	public void setBoardColours(Color[][] boardColours) {
		this.boardColours = boardColours;
	}
	
	
}
