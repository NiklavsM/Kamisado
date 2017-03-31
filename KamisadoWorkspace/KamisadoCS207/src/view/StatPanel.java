package view;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.StatsManager;
import model.StatsObject;

public class StatPanel extends JPanel {

	public StatPanel() {
		JScrollPane scrollPane = new JScrollPane();
		JLabel statsText = new JLabel();
		statsText.setText("User stats");
		statsText.setBounds(5, 5, 100, 20);
		statsText.setFont(new Font("sherif", Font.BOLD, 16));
		add(statsText);
		String[] columnNames = { "Player name", "Wins", "Losses" };

		StatsManager m = new StatsManager();
		StatsObject stats = m.getStatsObject();
		if (stats != null) {
			JTable table = new JTable(stats.getTableData(), columnNames);
			table.setEnabled(false);
			table.setAutoCreateRowSorter(true);
			scrollPane.setBounds(20, 56, 600, 300);
			scrollPane.setViewportView(table);
			add(scrollPane);
		}
		this.setLayout(null);
		this.setFocusable(false);
	}
}