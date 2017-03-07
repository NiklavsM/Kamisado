package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;

public class State extends Observable{

	private final int boardUpperLimit = 7;
	private final int boardLowerLimit = 0;
	private Board currentBoard;
	private ArrayList<Position> validMoves; 
	private Piece[][] pieces;
	private Position startingPosition;
	private Color colourToMove;
	
	public State(){
		currentBoard = new Board();
		pieces = currentBoard.getPieces();
		colourToMove = Color.BLACK;
		validMoves = new ArrayList<>();
	}
	
	public State(Board board){
		validMoves = new ArrayList<>();
		currentBoard = board;
		pieces = currentBoard.getPieces();
	}
	
	public boolean legal(Position position){	
			if(validMoves.contains(position)){
				return true;
			}
			return false;
	}
	
	public void calcValidMoves(boolean playerWhite, Position position){
		validMoves.clear();
		//System.out.println("got to calc Moves");
		startingPosition = position;
		int startx = position.getX();
		int starty = position.getY();
		//System.out.println("chose player" + playerWhite);
		if(playerWhite){
			//System.out.println("chose player");
			optionsLeftDown(startx, starty);
			optionsMiddleDown(startx, starty);
			optionsRightDown(startx, starty);
		}else{
			optionsLeftUp(startx, starty);
			optionsMiddleUp(startx, starty);
			optionsRightUp(startx, starty);
			//System.out.println("chose black");
			
		}
		for(Position pos:validMoves){
			System.out.println("Pos : " + pos.getX() + ":" + pos.getY());
		}
		this.setChanged();
		this.notifyObservers(validMoves);
	}
	
	private void optionsLeftUp(int x, int y) {
	//	System.out.println("1");
		int numberOfOptionsLeft = x;
		options(numberOfOptionsLeft, x, y, -1, -1);
	}

	private void optionsMiddleUp(int x, int y) {
//		int numOfOptionsMiddle = y - 1;
//		options(numOfOptionsMiddle, x, y, 0, -1);
		System.out.println("got here middle up");
		y--;
		for (; y >= boardLowerLimit; y--) {
			if (!isSuccessfulValidMove(x, y)) {
				break;
			}
		}
	}

	private void optionsRightUp(int x, int y) {
//		int numOfOptionsRight = 7 - x;
//		options(numOfOptionsRight, x, y, 1, 1);
		x++;
		for (; x <= 7; x++) {
			y--;
			if (x >= boardLowerLimit && y >= boardLowerLimit) {
				if (!isSuccessfulValidMove(x, y)) {
					break;
				}
			}
		}
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
//		int numOfOptionsMiddle = (7-y)-1;
//		options(numOfOptionsMiddle, x, y, 0, 1);
		
		y++;
		for (; y <= boardUpperLimit; y++) {
			if (!isSuccessfulValidMove(x, y)) {
				break;
			}
		}
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
	
	private boolean isSuccessfulValidMove(int x, int y){
		if (pieces[x][y] == null) {
			//System.out.println("should add");
			validMoves.add(new Position(x, y));
			return true;
		}
		return false;
	}
	
	public State make(Position endPosition){
		if(legal(endPosition)){
			colourToMove = currentBoard.findColor(endPosition);
			return new State(currentBoard.make(startingPosition, endPosition));
		}
		return null;
	}
	

	public Board getBoard() {
		return currentBoard;
	}

	
}
