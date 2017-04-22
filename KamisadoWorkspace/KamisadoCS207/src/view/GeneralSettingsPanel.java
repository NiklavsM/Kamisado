package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

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
import model.MyColour;

public class GeneralSettingsPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JCheckBox musicOn;
	private JCheckBox soundOn;
	private JSlider soundVolume;
	private JSlider musicVolume;
	private JButton apply;
	private JCheckBox styleOne;
	private JCheckBox styleTwo;
	private ButtonGroup pieceTypes;
	private BufferedImage styleOneImage;
	private BufferedImage styleTwoImage;
	private JLabel styleOneImageLabel;
	private JLabel styleTwoImageLabel;
	private JComboBox<String> colourPicker;
	private JLabel colourPreview;
	private JSlider redSlider;
	private JSlider greenSlider;
	private JSlider blueSlider;
	private GeneralSettingsManager manager;
	private GeneralSettings settings;
	private Controller controller;
	private String fontStyle = "Sitka Text";
	transient private ImageMerger merger = new ImageMerger();

	public GeneralSettingsPanel(Controller controller) {
		this.controller = controller;
		this.setLayout(null);
		this.setFocusable(false);
		manager = new GeneralSettingsManager();
		settings = manager.getGeneralSettings();
		
		JLabel title = new JLabel("Game settings");
		title.setBounds(300,100,220,40);
		title.setFont(new Font(fontStyle, Font.BOLD, 28));
		add(title);

		soundOptions();
		pieceTypeChooser();
		boardColourChooser();
		initializeApplyButton();
		setUpGraphics();
	}

	public void soundOptions() {
		JLabel soundLabel = new JLabel("Sound");
		soundLabel.setText("Sound");
		soundLabel.setBounds(250, 150, 100, 30);
		soundLabel.setFont(new Font(fontStyle, Font.BOLD, 18));
		add(soundLabel);

		musicOn = new JCheckBox("Music On");
		musicOn.setSelected(settings.isMusicOn());
		musicOn.setBounds(250, 200, 80, 20);
		musicOn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				musicVolume.setEnabled(musicOn.isSelected());

			}
		});
		add(musicOn);

		musicVolume = new JSlider(-80, 6, settings.getMusicVolume());
		musicVolume.setBounds(330, 200, 100, 20);
		musicVolume.setEnabled(settings.isMusicOn());
		add(musicVolume);

		soundOn = new JCheckBox("Sound On");
		soundOn.setSelected(settings.isSoundOn());
		soundOn.setBounds(250, 230, 80, 20);
		soundOn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				soundVolume.setEnabled(soundOn.isSelected());

			}
		});
		add(soundOn);

		soundVolume = new JSlider(-80, 6, settings.getSoundVolume());
		soundVolume.setBounds(330, 230, 100, 20);
		soundVolume.setEnabled(settings.isSoundOn());
		add(soundVolume);
	}



	public void boardColourChooser() {
		
		JLabel colorsStyle = new JLabel("Colours & Style");
		colorsStyle.setBounds(250, 270, 160, 30);
		colorsStyle.setFont(new Font(fontStyle, Font.BOLD, 18));
		add(colorsStyle);

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

		colourPicker = new JComboBox<String>(
				new String[] { "Blue", "Brown", "Green", "Red", "Yellow", "Pink", "Cyan", "Orange" });
		colourPicker.setBounds(250, 300, 80, 30);
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
		
		JLabel redLabel = new JLabel("Red");
		redLabel.setBounds(210, 350, 40, 20);
		add(redLabel);
		redSlider = new JSlider(0, 255, settings.getColoursRed(colourPicker.getSelectedItem().toString()));
		redSlider.setBounds(250, 350, 120, 20);
		redSlider.addChangeListener(sliderChangeListener);
		add(redSlider);
		
		JLabel greenLabel = new JLabel("Green");
		greenLabel.setBounds(210, 380, 40, 20);
		add(greenLabel);
		greenSlider = new JSlider(0, 255, settings.getColoursGreen(colourPicker.getSelectedItem().toString()));
		greenSlider.setBounds(250, 380, 120, 20);
		greenSlider.addChangeListener(sliderChangeListener);
		add(greenSlider);
			
		JLabel blueLabel = new JLabel("Blue");
		blueLabel.setBounds(210, 410, 40, 20);
		add(blueLabel);
		blueSlider = new JSlider(0, 255, settings.getColoursBlue(colourPicker.getSelectedItem().toString()));
		blueSlider.setBounds(250, 410, 120, 20);
		blueSlider.addChangeListener(sliderChangeListener);
		add(blueSlider);

		colourPreview = new JLabel();
		colourPreview.setBounds(380, 350, 80, 80);
		colourPreview.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
		colourPreview.setOpaque(true);
		add(colourPreview);

		JButton resetDefaultColours = new JButton("Reset Colours");
		resetDefaultColours.setBounds(360, 300, 120, 20);
		resetDefaultColours.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setDefaultColors();
				settings.setStaticColors();
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
	
	public void pieceTypeChooser() {
		styleOneImage = merger.mergeRegularStyle(MyColour.valueOf("blue").getColour(), "TeamWhite", "Standard");

		styleOneImageLabel = new JLabel(new ImageIcon(styleOneImage));
		styleOneImageLabel.setBounds(500, 350, 50, 50);
		add(styleOneImageLabel);

		styleTwoImage = merger.mergeAlternateStyle(MyColour.valueOf("blue").getColour(), "TeamWhite", "Standard");

		styleTwoImageLabel = new JLabel(new ImageIcon(styleTwoImage));
		styleTwoImageLabel.setBounds(580, 350, 50, 50);
		add(styleTwoImageLabel);

		styleOne = new JCheckBox("Style One");
		styleOne.setBounds(500, 400, 80, 20);
		styleOne.setSelected(settings.getPieceImageStyle().equals("pieceStyleOne"));

		styleTwo = new JCheckBox("Style Two");
		styleTwo.setBounds(580, 400, 80, 20);
		styleTwo.setSelected(settings.getPieceImageStyle().equals("pieceStyleTwo"));

		pieceTypes = new ButtonGroup();
		pieceTypes.add(styleOne);
		pieceTypes.add(styleTwo);
		add(styleOne);
		add(styleTwo);
	}

	public void initializeApplyButton() {
		apply = new JButton("Apply");
		apply.setBounds(510, 500, 120, 40);
		apply.setBackground(new Color(239, 155, 0));
		apply.setFont(new Font(fontStyle, Font.BOLD, 24));
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
	
	private void setUpGraphics(){
		JLabel logo = new JLabel();
		logo.setBounds(260, 0, 500, 100);
		ImageIcon homeImage = new ImageIcon(getClass().getResource("/images/logo.png"));
		logo.setIcon(homeImage);
		add(logo);
		JLabel dragonLeft = new JLabel();
		dragonLeft.setBounds(-10, 0, 300, 650);
		homeImage = new ImageIcon(getClass().getResource("/images/dragonleft.png"));
		dragonLeft.setIcon(homeImage);
		add(dragonLeft);
		JLabel dragonRight = new JLabel();
		dragonRight.setBounds(555, 0, 300, 650);
		homeImage = new ImageIcon(getClass().getResource("/images/dragonright.png"));
		dragonRight.setIcon(homeImage);
		add(dragonRight);
	}
}
