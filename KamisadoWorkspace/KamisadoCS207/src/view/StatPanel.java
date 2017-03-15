package view;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class StatPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public StatPanel() {
		setLayout(null);
		this.setFocusable(false);
		JLabel lblPlayerName = new JLabel("Player Name:");
		lblPlayerName.setBounds(210, 7, 64, 14);
		add(lblPlayerName);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(210, 25, 124, 20);
		add(comboBox);
		
		JTextArea txtrStats = new JTextArea();
		txtrStats.setBounds(7, 56, 433, 233);
		txtrStats.setText("Stats");
		add(txtrStats);

	}

}