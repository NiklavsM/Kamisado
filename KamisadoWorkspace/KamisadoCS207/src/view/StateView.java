package view;

import java.util.Observer;

import model.GameDriver;
import model.State;

public interface StateView extends Observer{

	State state = new State();
	
	public void buttonClicked();
	
}
