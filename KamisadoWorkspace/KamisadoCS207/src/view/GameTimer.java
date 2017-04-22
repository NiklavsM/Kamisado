package view;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import model.MyObservable;
import model.MyObserver;
import model.TimerInfo;

public class GameTimer extends JPanel implements MyObserver {

	private static final long serialVersionUID = 1L;
	JLabel time;
	JProgressBar timeProgressBar;
	public GameTimer() {
		time = new JLabel();
		time.setVisible(true);
		time.setText(" ");
		time.setFont(new Font("sherif", Font.BOLD, 18));
		this.add(time);
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if(arg instanceof TimerInfo){
			int timeLimit = ((TimerInfo) arg).getTimerLimit();
			int timeLeft = ((TimerInfo) arg).getTimeLeft();
			if(timeProgressBar == null){
				timeProgressBar = new JProgressBar(0,timeLimit){

					private static final long serialVersionUID = 1L;

					@Override
			        public String getString() {
			            return super.getString() + (getValue());
			        }

			    };
				timeProgressBar.setString("Remaining time: ");
				timeProgressBar.setStringPainted(true);
				timeProgressBar.setValue(timeLeft);
				timeProgressBar.setFont(new Font("sherif", Font.BOLD, 14));
				this.add(timeProgressBar);
			}else{
				timeProgressBar.setValue(timeLeft);

			}
		}
		
	}

}
