package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import model.StatsManager;
import model.StatsObject;

public class StatPanel extends JPanel {
	JLabel statsText;
	JTextField filter;
	JTable table;
	DefaultTableModel model;

	public StatPanel() {
		setLabelText("User stats");
		setTable();
		this.setLayout(null);
		this.setFocusable(false);
	}

	private void setLabelText(String text) {
		statsText = new JLabel();
		statsText.setText(text);
		statsText.setBounds(5, 5, 100, 20);
		statsText.setFont(new Font("sherif", Font.BOLD, 16));
		add(statsText);
	}

	private void setTable() {
		JScrollPane scrollPane = new JScrollPane();
		String[] columnNames = { "Player name", "Wins", "Losses" };

		StatsManager m = new StatsManager();
		StatsObject stats = m.getStatsObject();
		if (stats != null) {
			model = new DefaultTableModel();
			model.setDataVector(stats.getTableData(), columnNames);
			table = new JTable(model);
			table.setEnabled(false);
			table.setAutoCreateRowSorter(true);
			scrollPane.setBounds(20, 56, 600, 300);
			scrollPane.setViewportView(table);
			add(scrollPane);
		}

		JLabel filterLebel = new JLabel();
		filterLebel.setText("Filter:");
		filterLebel.setBounds(20, 360, 70, 20);
		filterLebel.setFont(new Font("sherif", Font.BOLD, 16));
		add(filterLebel);

		filter = new JTextField();
		filter.setBounds(90, 360, 100, 20);
		filter.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
			}

			public void keyReleased(KeyEvent e) {
				filterTable();
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		add(filter);
	}

	private void filterTable() {
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(filter.getText()));
	}
}