package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MatchReport {

	public StatsObject getStats() {
		StatsObject stats = null;
		File file = new File("stats.bin");// needs fixing
			try {
				ObjectInputStream newStateO = new ObjectInputStream(new FileInputStream(file.getPath()));
				stats = (StatsObject) newStateO.readObject();
				newStateO.close();
				return stats;
			} catch (FileNotFoundException e) {
				FileWriter fileCreater;
				try {
					fileCreater = new FileWriter(file);
					fileCreater.close();
					getStats();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "File not found", "File not found", JOptionPane.ERROR_MESSAGE);
				return null;
			} catch (IOException s) {
				JOptionPane.showMessageDialog(null, "File not right IO", "File not right IO",
						JOptionPane.ERROR_MESSAGE);
				return null;
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Class not right", "Class not right", JOptionPane.ERROR_MESSAGE);
				return null;
			}
	}

	public void saveStats(StatsObject stats) {
		File file = new File("stats.bin");// needs fixing
		try {
			FileWriter fileCreater = new FileWriter(file);
			ObjectOutputStream stateObj = new ObjectOutputStream(new FileOutputStream(file));
			stateObj.writeObject(stats);
			stateObj.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
