package view;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import model.MyObservable;
import model.MyObserver;

public class GameTimer extends JProgressBar implements MyObserver {

	public GameTimer() {
		this.setVisible(false);
		this.setValue(0);
		this.setString("");
		this.setStringPainted(true);
		this.setFont(new Font("sherif", Font.BOLD, 16));
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if(arg instanceof Integer){
			this.setValue(this.getMaximum() - (int)arg);
			//System.out.println("Time: "+ Integer.toString((int) arg));
			this.setVisible(true);
			this.setString("Time remaining: " + Integer.toString((int) arg));
			this.setStringPainted(true);
		}
	}
	
	
}
