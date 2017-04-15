package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
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
	JCheckBox styleOne;
	JCheckBox styleTwo;
	ButtonGroup pieceTypes;
	BufferedImage styleOneImage;
	BufferedImage styleTwoImage;
	JLabel styleOneImageLabel;
	JLabel styleTwoImageLabel;
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
		
		try{
			styleOneImage = ImageIO.read(getClass().getResource("/pieceStyleOne/TeamWhiteBlue.png"));
		}catch (IOException e){
			e.printStackTrace();
		}
		styleOneImageLabel = new JLabel(new ImageIcon(styleOneImage));
		styleOneImageLabel.setBounds(300, 100, 50, 50);
		add(styleOneImageLabel);
		
		try{
			styleTwoImage = ImageIO.read(getClass().getResource("/pieceStyleTwo/TeamWhiteBlue.png"));
		}catch (IOException e){
			e.printStackTrace();
		}
		styleTwoImageLabel = new JLabel(new ImageIcon(styleTwoImage));
		styleTwoImageLabel.setBounds(380, 100, 50, 50);
		add(styleTwoImageLabel);
		
		styleOne = new JCheckBox("Style One");
		styleOne.setBounds(300, 150, 80, 20);
		styleTwo = new JCheckBox("Style Two");
		styleTwo.setBounds(380, 150, 80, 20);
		if(settings.getPieceImageStyle().equals("pieceStyleOne")){
			styleOne.setSelected(true);
		}else if(settings.getPieceImageStyle().equals("pieceStyleTwo")){
			styleTwo.setSelected(true);
		}
		
		pieceTypes = new ButtonGroup();
		pieceTypes.add(styleOne);
		pieceTypes.add(styleTwo);
		add(styleOne);
		add(styleTwo);

		apply = new JButton("Apply");
		apply.setBounds(50, 150, 80, 20);
		apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setMusicOn(soundOn.isSelected());
				settings.setVolume(volume.getValue());
				if(styleOne.isSelected()){
					settings.setPieceImageStyle("pieceStyleOne");
				}else if(styleTwo.isSelected()){
					settings.setPieceImageStyle("pieceStyleTwo");
				}
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
