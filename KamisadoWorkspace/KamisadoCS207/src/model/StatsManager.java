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

public class StatsManager {

	public StatsObject getStatsObject() {
		StatsObject stats = null;
		File file = new File("stats.bin");// needs fixing
		if (!file.exists()) {
			FileWriter fileCreater;
			try {
				fileCreater = new FileWriter(file);
				fileCreater.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			ObjectInputStream newStateO = new ObjectInputStream(new FileInputStream(file.getPath()));
			stats = (StatsObject) newStateO.readObject();
			newStateO.close();
			return stats;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException s) {
			//s.printStackTrace();
			// Thats fine(it has reached the end of file)
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Class not right", "Class not right", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void saveStats(StatsObject stats) {
		File file = new File("stats.bin");// needs fixing
		try {
			FileWriter fileCreater = new FileWriter(file);
			fileCreater.close();
			ObjectOutputStream stateObj = new ObjectOutputStream(new FileOutputStream(file));
			stateObj.writeObject(stats);
			stateObj.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
