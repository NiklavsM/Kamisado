package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import controller.Controller;
import model.GeneralSettings;
import model.GeneralSettingsManager;

public class GeneralSettingsPanel extends JPanel {
	JLabel soundLabel;
	JCheckBox soundOn;
	JSlider volume;
	JButton apply;
	GeneralSettingsManager manager;
	GeneralSettings settings;

	public GeneralSettingsPanel(Controller controller) {
		this.setLayout(null);
		this.setFocusable(false);

		manager = new GeneralSettingsManager();
		settings = manager.getGeneralSettings();

		soundLabel = new JLabel("Sound");
		soundLabel.setText("Sound");
		soundLabel.setBounds(50, 50, 50, 50);
		add(soundLabel);

		soundOn = new JCheckBox("On");
		soundOn.setSelected(settings.isMusicOn());
		soundOn.setBounds(50, 100, 50, 50);
		add(soundOn);
		
		volume = new JSlider(-80, 6, settings.getVolume());
		volume.setBounds(100, 100, 100, 50);
		add(volume);

		apply = new JButton("Apply");
		apply.setBounds(50, 150, 80, 20);
		apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setMusicOn(soundOn.isSelected());
				settings.setVolume(volume.getValue());
				manager.saveGeneralSettings(settings);
				try {
					controller.applySettings();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		add(apply);

	}

}
