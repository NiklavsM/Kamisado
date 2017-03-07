package model;

import java.util.ArrayList;

public class State {

	private final int boardUpperLimit = 7;
	private final int boardLowerLimit = 0;
	private Board currentBoard;
	private ArrayList<Position> validMoves; 
	private Piece[][] pieces;
	private Piece pieceToMove;
	
	public State(){
		currentBoard = new Board();
		pieces = currentBoard.getPieces();
	}
	
	public boolean legal(Position position){	
			if(validMoves.contains(position)){
				return true;
			}
			return false;
	}
	
	public void calcValidMoves(boolean playerWhite, int startx, int starty){
		
		if(playerWhite){
			optionsLeftUp(startx, starty);
			optionsMiddleUp(startx, starty);
			optionsRightUp(startx, starty);
		}else{
			optionsLeftDown(startx, starty);
			optionsMiddleDown(startx, starty);
			optionsRightDown(startx, starty);
		}
	}
	
	private void optionsLeftUp(int x, int y) {
		int numberOfOptionsLeft = x;
		options(numberOfOptionsLeft, x, y, -1, -1);
	}

	private void optionsMiddleUp(int x, int y) {
		int numOfOptionsMiddle = y - 1;
		options(numOfOptionsMiddle, x, y, 0, -1);
//		y--;
//		for (; y >= boardLowerLimit; y--) {
//			if (!isSuccesfulValidMove(x, y)) {
//				break;
//			}
//		}
	}

	private void optionsRightUp(int x, int y) {
		int numOfOptionsRight = 7 - x;
		options(numOfOptionsRight, x, y, 1, 1);
//		x++;
//		for (; x <= 7; x++) {
//			y--;
//			if (x >= boardLowerLimit && y >= boardLowerLimit) {
//				if (!isSuccesfulValidMove(x, y)) {
//					break;
//				}
//			}
//		}
	}
	
	private void options(int options, int x, int y, int incrementx, int incrementy){
		for(int j = 0; j < options; j++){
			x = x + incrementx;
			y = y + incrementy;
			if ((x >= boardLowerLimit && x <= boardUpperLimit) && (y >= boardUpperLimit && y <= boardUpperLimit)) {
				if (pieces[x][y] == null) {
					validMoves.add(new Position(x, y));
					break;
				}
			}
		}
	}
	
	private void optionsLeftDown(int x, int y) {
		int numOfOptionsLeft = x;
		
		options(numOfOptionsLeft, x, y, -1, 1);
//		int p = 7-x;
//		for (int i = 0; i < p; i++) {
//			x--;
//			y++;
//			if (x >= boardLowerLimit && y <= boardUpperLimit) {
//				if (!isSuccesfulValidMove(x, y)) {
//					break;
//				}
//			}
//		}
	}

	private void optionsMiddleDown(int x, int y) {
		int numOfOptionsMiddle = (7-y)-1;
		options(numOfOptionsMiddle, x, y, 0, 1);
		
//		y++;
//		for (; y <= boardUpperLimit; y++) {
//			if (!isSuccesfulValidMove(x, y)) {
//				break;
//			}
//		}
	}

	private void optionsRightDown(int x, int y) {
		int numOfOptionsRight = 7-x;
		options(numOfOptionsRight, x, y, 1, 1);
		
//		x++;
//		for (; x <= boardUpperLimit; x++) {
//			y++;
//			if (x >= boardLowerLimit && y <= boardUpperLimit) {
//				if (!isSuccesfulValidMove(x, y)) {
//					break;
//				}
//			}
//		}
	}
	
	public Board make(Position startPosition, Position endPosition){
		calcValidMoves(true, startPosition.getX(), startPosition.getY());
		if(legal(endPosition)){
			return currentBoard.make(startPosition, endPosition);
		}
		return null;
	}
	
	public Piece getPieceToMove() {
		return pieceToMove;
	}

	public void setPieceToMove(Piece pieceToMove) {
		this.pieceToMove = pieceToMove;
	}

	public Board getBoard() {
		return currentBoard;
	}

	
}
