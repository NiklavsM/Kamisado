package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import model.MyObservable;
import model.MyObserver;
import model.TimerInfo;

public class GameTimer extends JPanel implements MyObserver {

	private static final long serialVersionUID = 1L;
	private JLabel time;
	private JProgressBar timeProgressBar;

	public GameTimer() {
		time = new JLabel();
		time.setVisible(true);
		time.setText(" ");
		time.setFont(new Font("sherif", Font.BOLD, 18));
		this.add(time);
	}
	
	@Override
	  protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	    ImageIcon woodImage = new ImageIcon(getClass().getResource("/images/backgroundwood.png"));
      g.drawImage(woodImage.getImage(), 0, 0, null);
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if (arg instanceof TimerInfo) {
			int timeLimit = ((TimerInfo) arg).getTimerLimit();
			int timeLeft = ((TimerInfo) arg).getTimeLeft();
			if (timeProgressBar == null) {
				timeProgressBar = new JProgressBar(0, timeLimit) {

					private static final long serialVersionUID = 1L;

					@Override
					public String getString() {
						return super.getString() + (getValue());
					}

				};
				timeProgressBar.setForeground(Color.blue);
				timeProgressBar.setString("Remaining time: ");
				timeProgressBar.setStringPainted(true);
				timeProgressBar.setValue(timeLeft);
				timeProgressBar.setFont(new Font("sherif", Font.BOLD, 14));
				this.add(timeProgressBar);
			} else {
				timeProgressBar.setValue(timeLeft);

			}
		}

	}

}
