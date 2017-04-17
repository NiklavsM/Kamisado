package view;

import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class NewGameOptionPanel extends JPanel {
	
	
	private JButton btnPlay = new JButton("Play");
	private JLabel gameLogo = new JLabel();
	private JLabel lblGameType = new JLabel("Game Type:");
	private JLabel lblAiDifficulty = new JLabel("AI Difficulty:");
	private JLabel aiStart = new JLabel("AI Colour");
	private JTextField txtBlackName = new JTextField("Player Black");
	private JTextField txtWhiteName = new JTextField("Player White");
	private JCheckBox chckbxSpeedMode = new JCheckBox("Speed Mode?");
	private JCheckBox chckbxRandomBoard = new JCheckBox("Random Board Colours?");
	private JCheckBox chckbxAiToStart = new JCheckBox("AI To Start?");
	private JLabel white = new JLabel("Player White");
	private JLabel black = new JLabel("Player Black");
	private JRadioButton rdbtnEasy = new JRadioButton("Easy AI");
	private JRadioButton rdbtnHard = new JRadioButton("Hard AI");
	private JRadioButton rdbtnSingleplayer = new JRadioButton("Single Player");
	private JRadioButton rdbtnTwoPlayer = new JRadioButton("Two Player");
	private JRadioButton whiteAiPlayer = new JRadioButton();
	private JRadioButton blackAiPlayer = new JRadioButton();
	private JTextField timerTime = new JTextField("20");
	private JLabel timeLabel = new JLabel("Seconds: ");
	private JTextField AiSelectedField = new JTextField();
	private ButtonGroup gameType = new ButtonGroup();
	private ButtonGroup aiStartCol = new ButtonGroup();
	private ButtonGroup aiDiff = new ButtonGroup();
	private JComboBox<Integer> gameLength = new JComboBox<>();

	public NewGameOptionPanel(){
		setBorder(new EmptyBorder(5, 5, 5, 5));
		MigLayout layout = new MigLayout(
				"", // Layout Constraints
				"200[][]200[][]200", // Column constraints
				"20[][][][][]30[][][][]"); // Row constraints
		setLayout(layout);
		
		UIManager.put("Button.focus", Color.red);
		UIManager.put("RadioButton.focus", Color.red);
		UIManager.put("CheckBox.focus", Color.red);
		
		ImageIcon homeImage = new ImageIcon(getClass().getResource("/images/logo.png"));
		gameLogo.setIcon(homeImage);
		this.add(gameLogo, "dock north");
		
		setUpGameTypeOptions();
		
	}

	private void setUpGameTypeOptions() {
		this.add(lblGameType, "span 2");
		this.add(lblAiDifficulty, "span 2");
		this.add(new JLabel(), "wrap");
		this.add(rdbtnSingleplayer, "span 2");
		this.add(rdbtnEasy, "span 2");
		this.add(new JLabel(), "wrap");
		this.add(rdbtnTwoPlayer, "span 2");
		this.add(rdbtnHard, "span 2");
		this.add(new JLabel(), "wrap");
		this.add(new JLabel("Round Length"), "span 2");
		this.add(new JLabel("AI To Start?"));
		this.add(chckbxAiToStart);
		this.add(new JLabel(), "wrap");
		this.add(new JLabel("First To:"));
		this.add(gameLength, "wrap");
		
		this.add(new JLabel("More Options"), "wrap");
		this.add(chckbxRandomBoard, "span 2");
		this.add(black);
		this.add(txtBlackName, "wrap");
		this.add(chckbxSpeedMode, "span 2");
		this.add(white);
		this.add(txtWhiteName, "wrap");
		this.add(timeLabel);
		this.add(timerTime);
		this.add(btnPlay);
	}
}
