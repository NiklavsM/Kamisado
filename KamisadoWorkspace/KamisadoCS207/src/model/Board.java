package model;

import java.awt.Color;
import java.util.ArrayList;

public class Board {

	private Piece[][] pieces;
	private Color[][] boardColours;
	private ArrayList<Position> validMoves;
	private final int boardSize = 8;

	
	public Board(){
		pieces = new Piece[8][8];
		setBoardColours();
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

		boardColours = new Color[][] { { or, bl, c, p, y, r, gr, br }, { r, or, p, gr, bl, y, br, c },
				{ gr, p, or, r, c, br, y, bl }, { p, c, bl, or, br, gr, r, y }, { y, r, gr, br, or, bl, c, p },
				{ bl, y, br, c, r, or, p, gr }, { c, br, y, bl, gr, p, or, r }, { br, gr, r, y, p, c, bl, or } };
	}
	
	public void initialisePiecePositions(){
		pieces[0][0] = Piece.PlayerBlackOrange;
		pieces[1][0] = Piece.PlayerBlackBlue;
		pieces[2][0] = Piece.PlayerBlackCyan;
		pieces[3][0] = Piece.PlayerBlackPink;
		pieces[4][0] = Piece.PlayerBlackCyan;
		pieces[5][0] = Piece.PlayerBlackCyan;
		pieces[6][0] = Piece.PlayerBlackCyan;
		pieces[7][0] = Piece.PlayerBlackCyan;
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

	private boolean gameOver(int y){
		if(y == 0 || y == 7){
			return true;
		}
		return false;
	}

	public Piece[][] getPieces() {
		return pieces;
	}
}
