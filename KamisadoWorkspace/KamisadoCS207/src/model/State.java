package model;

import java.awt.Color;
import java.util.ArrayList;
import player.Player;

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
	
	public Position calcPieceToMove(String playerTeamName){
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(pieces[i][j] != null){
                        if(pieces[i][j].getColour().equals(colourToMove) && pieces[i][j].getTeam().equals(playerTeamName)){
                            colourToMove = currentBoard.findColor(new Position(i,j));
                            return new Position(i,j);
                        }
                    }
                }
            }
            return null;
	}
	
	public ArrayList<Position> calcValidMoves(String playerWhite, Position position){
            validMoves.clear();
            startingPosition = position;
            int startx = position.getX();
            int starty = position.getY();
            if(playerWhite.equals("White")){
                optionsLeftUp(startx, starty);
                optionsMiddleUp(startx, starty);
                optionsRightUp(startx, starty);
            }else{	
                optionsLeftDown(startx, starty);
                optionsMiddleDown(startx, starty);
                optionsRightDown(startx, starty);	
            }
            return validMoves;
	}
	
	private void optionsLeftDown(int x, int y) {
		int numberOfOptionsLeft = x;
		options(numberOfOptionsLeft, x, y, -1, -1);
	}

	private void optionsMiddleDown(int x, int y) {
		int numOfOptionsMiddle = y;
		options(numOfOptionsMiddle, x, y, 0, -1);
	}

	private void optionsRightDown(int x, int y) {
		int numOfOptionsRight = 7 - x;
		options(numOfOptionsRight, x, y, 1, -1);
	}
	
	private void options(int options, int x, int y, int incrementx, int incrementy){
		for(int j = 0; j < options; j++){
			x = x + incrementx;
			y = y + incrementy;
			if ((x >= boardLowerLimit && x <= boardUpperLimit) && (y >= boardLowerLimit && y <= boardUpperLimit)) {
				if (pieces[x][y] == null) {
					validMoves.add(new Position(x, y));
				}else{
					break;
				}
			}
		}
	}
	
	private void optionsLeftUp(int x, int y) {
            int numOfOptionsLeft = x;
            options(numOfOptionsLeft, x, y, -1, 1);
	}

	private void optionsMiddleUp(int x, int y) {
            int numOfOptionsMiddle = (7-y);
            options(numOfOptionsMiddle, x, y, 0, 1);
	}

	private void optionsRightUp(int x, int y) {
            int numOfOptionsRight = 7-x;
            options(numOfOptionsRight, x, y, 1, 1);
	}
	
	public State make(Position endPosition){
            if(legal(endPosition)){
                colourToMove = currentBoard.findColor(endPosition);

                previousMove = new Move(startingPosition, endPosition);

                Board newBoard = currentBoard.make(startingPosition, endPosition);
                if(newBoard == null){
                    return null;
                }
                State newState = new State(this, newBoard);
                return newState;
            }
            return null;
	}
        
	private boolean legal(Position position){	
            for(Position pos:validMoves){
                if(pos.getX() == position.getX() && pos.getY() == position.getY()){
                    return true;
                }
            }
            return false;
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
        
        public boolean isGameOver(){
            return currentBoard.gameOver(previousMove.getEndPos().getY());
        }
        
        public Color getColourToMove(){
            return colourToMove;
        }
        
        public Position findPositionOfColour(String teamColor, Color colourToMove){
            return currentBoard.findColorPos(teamColor, colourToMove);
        }
}
