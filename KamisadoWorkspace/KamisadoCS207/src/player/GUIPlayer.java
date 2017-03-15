package player;

import java.io.Serializable;

import controller.Controller;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import model.State;

public class GUIPlayer extends Player implements MyObserver, Serializable{

    
        private Controller controller;
        private Position buttonClicked;
	
	public GUIPlayer(String playerTeam,String playerName, boolean goingFist, Controller controller){
		super(playerTeam,playerName, goingFist);
                this.controller = controller;
                buttonClicked = new Position(0,0);
	}
	
        @Override
	public void getMove(State state){
	}
        
        public void setPosition(Position positionClicked){
            buttonClicked = positionClicked;
        }

    @Override
    public void update(MyObservable o, Object arg) {
        System.out.println("update Player");
        if(arg instanceof Position){
            buttonClicked = (Position) arg;
        }
    }

}
