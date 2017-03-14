package view;

import java.awt.Font;

import javax.swing.JLabel;

import model.MyObservable;
import model.MyObserver;

public class GameTimer extends JLabel implements MyObserver {

	public GameTimer() {
		this.setText("Time remaining: ");
		this.setFont(new Font("sherif", Font.BOLD, 16));
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if(arg instanceof Integer){
			this.setText("Time remaining: " + Integer.toString((int) arg));
		}
		
	}

}
