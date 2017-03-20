package view;

import java.awt.ScrollPane;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class LoadGamePanel extends JPanel {

	private JButton btnLoadSelected;
	private JLabel lblSavedGames;
	private JSeparator separator;
	private ScrollPane scrollPane;
	/**
	 * Create the panel.
	 */
	public LoadGamePanel() {
		this.setLayout(null);
		this.setFocusable(false);
		
		
		btnLoadSelected = new JButton("Load Selected");
		btnLoadSelected.setBounds(301, 253, 118, 23);
		add(btnLoadSelected);

		lblSavedGames = new JLabel("Saved Games:");
		lblSavedGames.setBounds(163, 11, 74, 14);
		add(lblSavedGames);

		separator = new JSeparator();
		separator.setBounds(104, 39, 172, 14);
		add(separator);

		scrollPane = new ScrollPane();
		scrollPane.setBounds(31, 55, 387, 192);
		add(scrollPane);

	}
}
