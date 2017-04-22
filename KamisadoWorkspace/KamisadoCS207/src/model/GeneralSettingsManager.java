package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JOptionPane;

public class GeneralSettingsManager implements Serializable {

	private static final long serialVersionUID = 1L;

	public GeneralSettings getGeneralSettings() {
		GeneralSettings generalSettings = null;
		File file = new File("settings.bin");
		if (!file.exists()) {
			FileWriter fileCreater;
			try {
				fileCreater = new FileWriter(file);
				fileCreater.close();
				saveGeneralSettings(new GeneralSettings());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			ObjectInputStream newStateO = new ObjectInputStream(new FileInputStream(file.getPath()));
			generalSettings = (GeneralSettings) newStateO.readObject();
			newStateO.close();
			return generalSettings;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException s) {
			// s.printStackTrace();
			// Thats fine(it has reached the end of file)
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Class not right", "Class not right", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void saveGeneralSettings(GeneralSettings generalSettings) {
		File file = new File("settings.bin");
		try {
			FileWriter fileCreater = new FileWriter(file);
			fileCreater.close();
			ObjectOutputStream stateObj = new ObjectOutputStream(new FileOutputStream(file));
			stateObj.writeObject(generalSettings);
			stateObj.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
