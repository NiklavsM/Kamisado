package view;

import java.awt.Font;

import javax.swing.JLabel;

import model.MyObservable;
import model.MyObserver;

public class GameTimer extends JLabel implements MyObserver {

	public GameTimer() {
		this.setVisible(false);
		this.setText(" ");
		this.setFont(new Font("sherif", Font.BOLD, 16));
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if(arg instanceof Integer){
			System.out.println("Time: "+ Integer.toString((int) arg));
			this.setVisible(true);
			this.setText("Time remaining: " + Integer.toString((int) arg));
		}
		
	}

}
