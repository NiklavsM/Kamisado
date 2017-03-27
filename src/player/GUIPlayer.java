package player;

import java.io.Serializable;

import controller.Controller;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import model.State;

public class GUIPlayer extends Player implements MyObservable, Serializable {

	private Controller controller;
	private Position buttonClicked;

	public GUIPlayer(String playerTeam, String playerName, boolean goingFist, Controller controller) {
		super(playerTeam, playerName, goingFist, false);
		this.controller = controller;
		buttonClicked = new Position(0, 0);
	}

	@Override
	public void getMove(State state) {
	}

	public void setPosition(Position positionClicked) {
		buttonClicked = positionClicked;
	}
}