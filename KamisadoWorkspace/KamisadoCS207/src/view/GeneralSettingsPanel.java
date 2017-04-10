package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.GeneralSettings;
import model.GeneralSettingsManager;

public class GeneralSettingsPanel extends JPanel {
	JLabel soundLabel;
	JCheckBox soundOn;
	JButton save;
	GeneralSettingsManager manager;
	GeneralSettings settings;

	public GeneralSettingsPanel() {
		manager = new GeneralSettingsManager();
		settings = manager.getGeneralSettings();
		soundLabel = new JLabel("Sound");
		soundLabel.setText("Sound");
		soundLabel.setBounds(50, 50, 50, 50);
		add(soundLabel);
		this.setLayout(null);
		this.setFocusable(false);

		soundOn = new JCheckBox("On");
		soundOn.setSelected(settings.isMusicOn());
		soundOn.setBounds(50, 100, 50, 50);
		add(soundOn);

		save = new JButton("Save");
		save.setBounds(50, 150, 80, 20);
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setMusicOn(soundOn.isSelected());
				manager.saveGeneralSettings(settings);
			}

		});
		add(save);

	}

}
