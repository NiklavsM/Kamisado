package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.ImageIcon;

public class State{

	private final int boardUpperLimit = 7;
	private final int boardLowerLimit = 0;
	private Board currentBoard;
	private ArrayList<Position> validMoves; 
	private Piece[][] pieces;
	private Position startingPosition;
	private Color colourToMove;
	private Move previousMove;
	
	public State(){
		currentBoard = new Board();
		pieces = currentBoard.getPieces();
		colourToMove = Color.BLACK;
		validMoves = new ArrayList<>();
		previousMove = null;
	}
	
	public State(Board board){
		validMoves = new ArrayList<>();
		currentBoard = board;
		pieces = currentBoard.getPieces();
	}
	
	public State(State state, Board board){
		this.validMoves = state.validMoves;
		this.startingPosition = state.startingPosition;
		this.colourToMove = state.colourToMove;
		this.currentBoard = board;
		this.previousMove = state.previousMove;
		this.pieces = state.pieces;		
	}
	
	public Position calcPieceToMove(boolean playerWhiteToMove){
		if(playerWhiteToMove){

			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(pieces[i][j] != null){
						if(pieces[i][j].getColour().equals(colourToMove) && pieces[i][j].getTeam().equals("White")){
							colourToMove = currentBoard.findColor(new Position(i,j));
							return new Position(i,j);
						}
					}
				}
			}
		}else{
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(pieces[i][j] != null){
						//System.out.println("colToMove: " + colourToMove.toString());
						if(pieces[i][j].getColour().toString().equals(colourToMove.toString())){
							//System.out.println("found: " + pieces[i][j].getColour().toString());
							if(pieces[i][j].getTeam().equals("Black")){
								colourToMove = currentBoard.findColor(new Position(i,j));
								return new Position(i,j);
							}
							
						}
					}
				}
			}
		}
		return null;
	}
	
	public boolean legal(Position position){	
			System.out.println("checking X: " + position.getX() + " Y: " + position.getY() );
			
			for(Position pos:validMoves){
				if(pos.getX() == position.getX() && pos.getY() == position.getY()){
					return true;
				}
			}
			return false;
//			if(validMoves.contains(position)){
//				return true;
//			}
//			return false;
	}
	
	public ArrayList<Position> calcValidMoves(boolean playerWhite, Position position){
		validMoves.clear();
		//System.out.println("got to calc Moves");
		startingPosition = position;
		int startx = position.getX();
		int starty = position.getY();
		//System.out.println("chose player" + playerWhite);
		if(playerWhite){
			//System.out.println("chose player");
			optionsLeftUp(startx, starty);
			optionsMiddleUp(startx, starty);
			optionsRightUp(startx, starty);
		}else{
			
			optionsLeftDown(startx, starty);
			optionsMiddleDown(startx, starty);
			optionsRightDown(startx, starty);
			
			//System.out.println("chose black");
			
		}
		return validMoves;
//		for(Position pos:validMoves){
//			System.out.println("Pos : " + pos.getX() + ":" + pos.getY());
//		}
	}
	
	private void optionsLeftDown(int x, int y) {
	//	System.out.println("1");
		int numberOfOptionsLeft = x;
		options(numberOfOptionsLeft, x, y, -1, -1);
	}

	private void optionsMiddleDown(int x, int y) {
		int numOfOptionsMiddle = y;
		options(numOfOptionsMiddle, x, y, 0, -1);
//		System.out.println("got here middle up");
//		y--;
//		for (; y >= boardLowerLimit; y--) {
//			if (!isSuccessfulValidMove(x, y)) {
//				break;
//			}
//		}
	}

	private void optionsRightDown(int x, int y) {
		int numOfOptionsRight = 7 - x;
		options(numOfOptionsRight, x, y, 1, -1);
//		x++;
//		for (; x <= 7; x++) {
//			y--;
//			if (x >= boardLowerLimit && y >= boardLowerLimit) {
//				if (!isSuccessfulValidMove(x, y)) {
//					break;
//				}
//			}
//		}
	}
	
	private void options(int options, int x, int y, int incrementx, int incrementy){
		for(int j = 0; j < options; j++){
			x = x + incrementx;
			y = y + incrementy;
			if ((x >= boardLowerLimit && x <= boardUpperLimit) && (y >= boardLowerLimit && y <= boardUpperLimit)) {
				if (pieces[x][y] == null) {
					//System.out.println("X: " + x + " Y: " + y);
					validMoves.add(new Position(x, y));
				}else{
					break;
				}
			}
		}
	}
	
	private void optionsLeftUp(int x, int y) {
		int numOfOptionsLeft = x;
		//System.out.println("Left: ");
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

	private void optionsMiddleUp(int x, int y) {
		int numOfOptionsMiddle = (7-y);
		//System.out.println("Middle: ");
		options(numOfOptionsMiddle, x, y, 0, 1);
		
//		y++;
//		for (; y <= boardUpperLimit; y++) {
//			if (!isSuccessfulValidMove(x, y)) {
//				break;
//			}
//		}
	}

	private void optionsRightUp(int x, int y) {
		int numOfOptionsRight = 7-x;
		//System.out.println("Right: ");
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
//	
//	private boolean isSuccessfulValidMove(int x, int y){
//		if (pieces[x][y] == null) {
//			//System.out.println("should add");
//			validMoves.add(new Position(x, y));
//			return true;
//		}
//		return false;
//	}
	
	public State make(Position endPosition){
		if(legal(endPosition)){
			colourToMove = currentBoard.findColor(endPosition);
			
			previousMove = new Move(startingPosition, new Position(endPosition.getX(), endPosition.getY()));
			
			Board newBoard = currentBoard.make(startingPosition, endPosition);
			if(newBoard == null){
				return null;
			}
			State newState = new State(this, newBoard);
			return newState;
		}
		return null;
	}
	

	public Board getBoard() {
		return currentBoard;
	}

	public Object getValidMoves() {
		return validMoves;
	}

	public Object getPreviousMove() {
		return previousMove;
	}

	
}
