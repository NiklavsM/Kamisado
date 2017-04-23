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

	public void save(GameDriver gameDriver) {

		File saveFolder = new File("Saved Games");
		if(!saveFolder.exists()){
			saveFolder.mkdir();
		}
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(saveFolder);
		int fileAdded = fileChooser.showSaveDialog(null);
		if (fileAdded == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter fileCreater = new FileWriter(fileChooser.getSelectedFile() + ".bin");
				fileCreater.close();
				ObjectOutputStream stateObj = new ObjectOutputStream(
						new FileOutputStream(fileChooser.getSelectedFile().getPath() + ".bin"));
				stateObj.writeObject(gameDriver);
				stateObj.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public GameDriver load() {
		GameDriver gameDriver = null;
		File saveFolder = new File("Saved Games");
		if(!saveFolder.exists()){
			saveFolder.mkdir();
		}
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(saveFolder);
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			saveFolder = fileChooser.getSelectedFile();
			try {
				ObjectInputStream newStateO = new ObjectInputStream(new FileInputStream(saveFolder.getPath()));
				gameDriver = (GameDriver) newStateO.readObject();
				newStateO.close();
				return gameDriver;
			} catch (FileNotFoundException e) {
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
		return null;
	}

}
