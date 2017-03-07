package view;

import java.util.Observer;

public interface StateView extends Observer{

	BoardView child;
	State state;
	
}
