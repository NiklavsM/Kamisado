package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import model.MyObservable;
import model.MyObserver;

public class GameTimer extends JLabel implements MyObserver {

	public GameTimer() {
		this.setText("Time remaining: ");
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if(arg instanceof Integer){
			this.setText("Time remaining: " + Integer.toString((int) arg));
		}
		
	}

}
