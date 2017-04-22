package player;

import java.io.Serializable;

import javax.swing.JOptionPane;

import controller.Controller;
import model.MyObservable;
import model.State;

public class GUIPlayer extends Player implements MyObservable, Serializable {

	private static final long serialVersionUID = 1L;
	private boolean isDud = false;
	private int option = -2;

	public GUIPlayer(String playerTeam, String playerName, boolean goingFist, Controller controller) {
		super(playerTeam, playerName, goingFist, false);
	}
	
	public GUIPlayer(String playerTeam, String playerName, boolean goingFist, Controller controller, boolean isDud) {
		super(playerTeam, playerName, goingFist, false);
		this.isDud = isDud;
	}

	@Override
	public void getMove(State state) {
	}

	public int fillHomeRow() {
		if(isDud){
			System.out.println("returning " + option);
			return option;
		}else{
			Object[] options = { "Fill from the left", "Fill from the right" };
			return JOptionPane.showOptionDialog(null, this.getPlayerName() + ", Please select an option!",
					"Ready for next round!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
					options[1]);
		}
	}

	@Override
	public void setToFirstMove(boolean isGoingFirst) {
		//option = -2;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if(arg instanceof Integer){
			System.out.println("got num at GUI " + arg);
			option = (int)arg;
			System.out.println("option after = " + option);
		}
	}
	
	public void setOption(int i){
		option = i;
	}
}
