package model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import player.GUIPlayer;

import player.Player;
import view.GUIBoardView;

public class GameDriver implements MyObservable, MyObserver{
	private Player playerWhite;
	private Player playerBlack;
	private Player PlayerToMove;
	
	private ArrayList<State> history;
	private State currentState;
        private boolean firstMove = true;
        private boolean gameOver = false;
	
//	private SaveManager saveManager;
//	private MatchReport matchReport;
	
	public GameDriver(Player playerWhite, Player playerBlack, MyObserver observerToState, Player playerToStart) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		PlayerToMove = playerToStart;
		this.history = new ArrayList<>();
		this.currentState = new State();
		this.addObserver(observerToState);
		//this.saveManager = new SaveManager();
	}

	public GameDriver(Player playerWhite, Player playerBlack, ArrayList<State> history, State currentState, Player playerToStart) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		PlayerToMove = playerToStart;
		this.history = history;
		this.currentState = currentState;
		//this.saveManager = new SaveManager();
	}
        
        public void playGame(){
           generateMove();
        }
        
        private void generateMove(){
            PlayerToMove.getMove(currentState);
        }
	
	private boolean playerFirstMove(Position placeClicked){
                System.out.println("asking player for move");
                if(placeClicked.getY() == PlayerToMove.getHomeRow()){
			currentState.calcValidMoves(PlayerToMove.getPlayerTeam(), placeClicked);
                        
                        //playerFirstMove();
			this.tellAll(currentState.getValidMoves());
			return false;
		}else{
			return actionOnClick(placeClicked);
		}
	}
	
	private boolean actionOnClick(Position placeClicked){
		State state = currentState.make(placeClicked);
		if(state == null){
			System.out.println("is not valid move X: " + placeClicked.getX() + " Y: " + placeClicked.getY());
			return false;
		}else{
			history.add(currentState);
			currentState = state;
			this.tellAll(currentState.getPreviousMove());
			return true;
		}
            //return true;
	}
	
	private boolean playTurn(Position placeClicked){
            if(!actionOnClick(placeClicked)){
                return false;
            }else if(currentState.isGameOver()){
                this.tellAll(PlayerToMove.getPlayerTeam());
                return true;
            }else{
                return nextTurn(0);
            }
//		Position placeClicked = new Position(x,y);
//		actionOnClick(placeClicked);
	}
	
	private boolean nextTurn(int numOfNoGoes){
		if(PlayerToMove.equals(playerWhite)){
			PlayerToMove = playerBlack;
		}else{
			PlayerToMove = playerWhite;
		}
		Position posToMove = currentState.calcPieceToMove(PlayerToMove.getPlayerTeam());
		ArrayList<Position> movesCanMake = currentState.calcValidMoves(PlayerToMove.getPlayerTeam(), posToMove);
		if(movesCanMake.isEmpty()){
                        if(numOfNoGoes >= 3){
                           this.tellAll("Draw"); 
                           return true;
                        }else{
                            nextTurn(++numOfNoGoes);
                        }
		}else{
			this.tellAll(movesCanMake);
		}
                return false;
	}
//
//    @Override
//    public void update(MyObservable o, Object arg) {
//        if(o instanceof GUIBoardView){
//            
//        }
//    }

    @Override
    public void update(MyObservable o, Object arg) {
        if(arg instanceof Position){
            System.out.println("update gamedriver");
            if(!gameOver){
                 if(firstMove){
                    if(playerFirstMove((Position)arg)){
                        nextTurn(0);
                        firstMove = false;
                    }
                }else{
                    if(playTurn((Position)arg)){
                        gameOver = true;
                    }
                } 
                generateMove();
            }
           
        
        }
    }
}
