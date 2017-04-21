package view;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
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
	private JLabel statsText;
	private JTextField filter;
	private JTable table;
	private DefaultTableModel model;
	private String fontStyle = "Rockwell";

	public StatPanel() {
		setLabelText("User stats");
		setTable();
		this.setLayout(null);
		this.setFocusable(false);
		setUpGraphics();
	}

	private void setLabelText(String text) {
		statsText = new JLabel();
		statsText.setText(text);
		statsText.setBounds(330, 100, 200, 40);
		statsText.setFont(new Font(fontStyle, Font.BOLD, 28));
		add(statsText);
	}

	private void setTable() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFocusable(true);

		String[] columnNames = { "Player name", "RoundsWon", "RoundsLost", "Rounds Won %", "GamesWon", "GamesLost",
				"Games Won %" };

		StatsManager m = new StatsManager();
		StatsObject stats = m.getStatsObject();
		if (stats != null) {
			model = new DefaultTableModel(stats.getTableData(), columnNames) {

				public Class getColumnClass(int c) {
					return getValueAt(0, c).getClass();
				}
			};
			table = new JTable(model);
			table.setEnabled(false);
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
			table.setRowSorter(sorter);
			scrollPane.setBounds(20, 150, 750, 300);
			scrollPane.setViewportView(table);
			add(scrollPane);
		}

		JLabel filterLebel = new JLabel();
		filterLebel.setText("Filter:");
		filterLebel.setBounds(70, 460, 70, 20);
		filterLebel.setFont(new Font(fontStyle, Font.BOLD, 20));
		add(filterLebel);

		filter = new JTextField();
		filter.setFocusable(true);
		filter.setBounds(150, 460, 120, 20);
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
		TableRowSorter<DefaultTableModel> rowFilter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(rowFilter);
		rowFilter.setRowFilter(RowFilter.regexFilter(filter.getText()));
	}

	private void setUpGraphics() {
		JLabel logo = new JLabel();
		logo.setBounds(260, 0, 500, 100);
		ImageIcon homeImage = new ImageIcon(getClass().getResource("/images/logo.png"));
		logo.setIcon(homeImage);
		add(logo);
		JLabel dragonLeftCorner = new JLabel();
		dragonLeftCorner.setBounds(-80, -100, 460, 310);
		ImageIcon dragonLeftCornerImage = new ImageIcon(getClass().getResource("/images/dragonleftcorner.png"));
		dragonLeftCorner.setIcon(dragonLeftCornerImage);
		add(dragonLeftCorner);
		JLabel dragonRightCorner = new JLabel();
		dragonRightCorner.setBounds(500, -100, 460, 310);
		ImageIcon dragonRightCornerImage = new ImageIcon(getClass().getResource("/images/dragonrightcorner.png"));
		dragonRightCorner.setIcon(dragonRightCornerImage);
		add(dragonRightCorner);
		JLabel dragonHorizontal = new JLabel();
		dragonHorizontal.setBounds(-40, 445, 820, 240);
		ImageIcon dragonHorizontalImage = new ImageIcon(getClass().getResource("/images/dragonhorizontal.png"));
		dragonHorizontal.setIcon(dragonHorizontalImage);
		add(dragonHorizontal);
	}
}