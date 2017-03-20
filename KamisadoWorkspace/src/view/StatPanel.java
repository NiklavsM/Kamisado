package view;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;

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
		txtrStats.setText("Stats");
		add(txtrStats);

	}

}