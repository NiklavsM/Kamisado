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

public class SaveManager {

	public void save(State state) {
		File file = new File("/home/me/Desktop");//needs fixing
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(file);
		int fileAdded = fileChooser.showSaveDialog(null);
		if (fileAdded == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter fileCreater = new FileWriter(fileChooser.getSelectedFile() + ".bin");
				ObjectOutputStream stateObj = new ObjectOutputStream(
						new FileOutputStream(fileChooser.getSelectedFile().getPath() + ".bin"));
				stateObj.writeObject(state);
				stateObj.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public State load() {
		State state = null;
		File file = new File("/home/me/Desktop");//needs fixing
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(file);
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				ObjectInputStream newStateO = new ObjectInputStream(new FileInputStream("/home/me/Desktop"));
				state = (State) newStateO.readObject();
				newStateO.close();
				return state;
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File not right", "File not right",
                        JOptionPane.ERROR_MESSAGE);
				return null;
			} catch (IOException s) {
				JOptionPane.showMessageDialog(null, "File not right", "File not right",
                        JOptionPane.ERROR_MESSAGE);
				return null;
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Class not right", "Class not right",
                        JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		return null;
	}

}
