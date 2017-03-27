package view;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.MatchReport;
import model.StatsObject;

public class StatPanel extends JPanel {

	
	private JComboBox comboBox;
	private JTextArea txtrStats;
	private JLabel lblPlayerName;
	/**
	 * Create the panel.
	 */
	public StatPanel() {
		this.setLayout(null);
		this.setFocusable(false);
		
		lblPlayerName = new JLabel("Player Name:");
		lblPlayerName.setBounds(210, 7, 64, 14);
		add(lblPlayerName);
		
		comboBox = new JComboBox();
		comboBox.setBounds(210, 25, 124, 20);
		add(comboBox);
		
		txtrStats = new JTextArea();
		txtrStats.setBounds(7, 56, 433, 233);
		//txtrStats.setText("Stats");
		add(txtrStats);
		MatchReport m = new MatchReport();
		StatsObject stats = m.getStats();
		txtrStats.append(stats.getStats());

	}

}