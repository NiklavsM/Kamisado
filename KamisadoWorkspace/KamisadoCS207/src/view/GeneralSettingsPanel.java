package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Controller;
import model.GeneralSettings;
import model.GeneralSettingsManager;

public class GeneralSettingsPanel extends JPanel {
	JLabel soundLabel;
	JCheckBox musicOn;
	JCheckBox soundOn;
	JSlider soundVolume;
	JSlider musicVolume;
	JButton apply;
	JCheckBox styleOne;
	JCheckBox styleTwo;
	ButtonGroup pieceTypes;
	BufferedImage styleOneImage;
	BufferedImage styleTwoImage;
	JLabel styleOneImageLabel;
	JLabel styleTwoImageLabel;
	JComboBox colourPicker;
	JLabel colourPreview;
	JSlider redSlider;
	JSlider greenSlider;
	JSlider blueSlider;
	GeneralSettingsManager manager;
	GeneralSettings settings;
	JButton resetDefaultColours;
	Controller controller;
	
	ImageMerger merger = new ImageMerger();

	public GeneralSettingsPanel(Controller controller) {
		this.controller = controller;
		this.setLayout(null);
		this.setFocusable(false);
		manager = new GeneralSettingsManager();
		settings = manager.getGeneralSettings();

		soundOptions();
		pieceTypeChooser();
		boardColourChooser();
		initializeApplyButton();
	}

	public void soundOptions() {
		soundLabel = new JLabel("Sound");
		soundLabel.setText("Sound");
		soundLabel.setBounds(50, 50, 50, 50);
		add(soundLabel);

		musicOn = new JCheckBox("Music On");
		musicOn.setSelected(settings.isMusicOn());
		musicOn.setBounds(50, 100, 80, 20);
		musicOn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				musicVolume.setEnabled(musicOn.isSelected());
				
			}
		});
		add(musicOn);

		musicVolume = new JSlider(-80, 6, settings.getMusicVolume());
		musicVolume.setBounds(130, 100, 100, 20);
		musicVolume.setEnabled(settings.isMusicOn());
		add(musicVolume);
		
		soundOn = new JCheckBox("Sound On");
		soundOn.setSelected(settings.isSoundOn());
		soundOn.setBounds(50, 130, 80, 20);
		soundOn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				soundVolume.setEnabled(soundOn.isSelected());
				
			}
		});
		add(soundOn);

		soundVolume = new JSlider(-80, 6, settings.getSoundVolume());
		soundVolume.setBounds(130, 130, 100, 20);
		soundVolume.setEnabled(settings.isSoundOn());
		add(soundVolume);
	}

	public void pieceTypeChooser() {
		styleOneImage = merger.mergeRegularStyle(settings.getColour("blue"), "TeamWhite", "Standard");
		
		styleOneImageLabel = new JLabel(new ImageIcon(styleOneImage));
		styleOneImageLabel.setBounds(300, 100, 50, 50);
		add(styleOneImageLabel);

		styleTwoImage = merger.mergeAlternateStyle(settings.getColour("blue"), "TeamWhite", "Standard");
		
		styleTwoImageLabel = new JLabel(new ImageIcon(styleTwoImage));
		styleTwoImageLabel.setBounds(380, 100, 50, 50);
		add(styleTwoImageLabel);

		styleOne = new JCheckBox("Style One");
		styleOne.setBounds(300, 150, 80, 20);
		styleOne.setSelected(settings.getPieceImageStyle().equals("pieceStyleOne"));
		
		styleTwo = new JCheckBox("Style Two");
		styleTwo.setBounds(380, 150, 80, 20);
		styleTwo.setSelected(settings.getPieceImageStyle().equals("pieceStyleTwo"));

		pieceTypes = new ButtonGroup();
		pieceTypes.add(styleOne);
		pieceTypes.add(styleTwo);
		add(styleOne);
		add(styleTwo);
	}

	public void boardColourChooser() {

		ChangeListener sliderChangeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				colourPreview
						.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
				
				styleOneImage = merger.mergeRegularStyle(colourPreview.getBackground(), "TeamWhite", "Standard");
				styleTwoImage = merger.mergeAlternateStyle(colourPreview.getBackground(), "TeamWhite", "Standard");
				styleOneImageLabel.setIcon(new ImageIcon(styleOneImage));
				styleTwoImageLabel.setIcon(new ImageIcon(styleTwoImage));
			}
		};
		
		colourPicker = new JComboBox<Object>(
				new Object[] { "Blue", "Brown", "Green", "Red", "Yellow", "Pink", "Cyan", "Orange" });
		colourPicker.setBounds(50, 250, 80, 30);
		colourPicker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				redSlider.setValue(settings.getColoursRed(colourPicker.getSelectedItem().toString()));
				greenSlider.setValue(settings.getColoursGreen(colourPicker.getSelectedItem().toString()));
				blueSlider.setValue(settings.getColoursBlue(colourPicker.getSelectedItem().toString()));
				
				styleOneImage = merger.mergeRegularStyle(colourPreview.getBackground(), "TeamWhite", "Standard");
				styleTwoImage = merger.mergeAlternateStyle(colourPreview.getBackground(), "TeamWhite", "Standard");
				styleOneImageLabel.setIcon(new ImageIcon(styleOneImage));
				styleTwoImageLabel.setIcon(new ImageIcon(styleTwoImage));
			}
		});
		add(colourPicker);

		redSlider = new JSlider(0, 255, settings.getColoursRed(colourPicker.getSelectedItem().toString()));
		redSlider.setBounds(50, 300, 120, 20);
		redSlider.addChangeListener(sliderChangeListener);
		add(redSlider);

		greenSlider = new JSlider(0, 255, settings.getColoursGreen(colourPicker.getSelectedItem().toString()));
		greenSlider.setBounds(50, 330, 120, 20);
		greenSlider.addChangeListener(sliderChangeListener);
		add(greenSlider);

		blueSlider = new JSlider(0, 255, settings.getColoursBlue(colourPicker.getSelectedItem().toString()));
		blueSlider.setBounds(50, 360, 120, 20);
		blueSlider.addChangeListener(sliderChangeListener);
		add(blueSlider);

		colourPreview = new JLabel();
		colourPreview.setBounds(180, 300, 80, 80);
		colourPreview.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
		colourPreview.setOpaque(true);
		add(colourPreview);
		
		resetDefaultColours = new JButton("Reset Colours");
		resetDefaultColours.setBounds(180, 250, 120, 20);
		resetDefaultColours.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setDefaultColors();
				redSlider.setValue(settings.getColoursRed(colourPicker.getSelectedItem().toString()));
				greenSlider.setValue(settings.getColoursGreen(colourPicker.getSelectedItem().toString()));
				blueSlider.setValue(settings.getColoursBlue(colourPicker.getSelectedItem().toString()));
				styleOneImage = merger.mergeRegularStyle(colourPreview.getBackground(), "TeamWhite", "Standard");
				styleTwoImage = merger.mergeAlternateStyle(colourPreview.getBackground(), "TeamWhite", "Standard");
				styleOneImageLabel.setIcon(new ImageIcon(styleOneImage));
				styleTwoImageLabel.setIcon(new ImageIcon(styleTwoImage));
			}
		});
		add(resetDefaultColours);
	}

	public void initializeApplyButton() {
		apply = new JButton("Apply");
		apply.setBounds(500, 500, 80, 20);
		apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setMusicOn(musicOn.isSelected());
				settings.setMusicVolume(musicVolume.getValue());
				settings.setSoundOn(soundOn.isSelected());
				settings.setSoundVolume(soundVolume.getValue());
				settings.setColour(colourPicker.getSelectedItem().toString(), redSlider.getValue(),
						greenSlider.getValue(), blueSlider.getValue());
				if (styleOne.isSelected()) {
					settings.setPieceImageStyle("pieceStyleOne");
				} else if (styleTwo.isSelected()) {
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
