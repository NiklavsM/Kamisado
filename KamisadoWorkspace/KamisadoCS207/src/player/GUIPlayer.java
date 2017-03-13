package player;

import java.awt.Color;
import java.util.ArrayList;

import controller.Controller;
import model.Board;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import model.State;

public class GUIPlayer extends Player implements MyObserver{

    
        private Controller controller;
        private Position buttonClicked;
	
	public GUIPlayer(String playerTeam, boolean goingFist, Controller controller){
		super(playerTeam, goingFist);
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
