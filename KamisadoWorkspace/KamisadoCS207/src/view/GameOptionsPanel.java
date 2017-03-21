package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Controller;

public class GameOptionsPanel extends JPanel {
	private JTextField txtEnterPName;
	private JTextField txtEnterPName_1;
	JCheckBox chckbxSpeedMode;
	JLabel white;
	JLabel black;
	JRadioButton rdbtnEasy;
	JRadioButton rdbtnHard;
	JRadioButton rdbtnSingleplayer;
	JRadioButton rdbtnTwoPlayer;
	JRadioButton whiteAiPlayer;
	JRadioButton blackAiPlayer;
	JTextField timerTime;
	JLabel timeLabel;
	JTextField AiSelectedField;
	ButtonGroup gameType = new ButtonGroup();
	ButtonGroup aiStartCol = new ButtonGroup();
	ButtonGroup aiDiff = new ButtonGroup();

	/**
	 * Create the panel.
	 */
	public GameOptionsPanel(Controller controller) {
		setLayout(null);
		Controller thisController = controller;
		initialiseComponents();
		setUpAIdifficulty();
		setUpGameTypeSelect();
		setUpSpeedMode();
		setUpPlayerTxtField();
		setUpAIColour();

		JSeparator separator = new JSeparator();
		separator.setBounds(32, 108, 168, 22);
		add(separator);

		JButton btnPlay = new JButton("Play");
		btnPlay.setBounds(354, 234, 89, 23);
		btnPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(txtEnterPName.getText().length() <= 12 && txtEnterPName_1.getText().length() <=12){
					if(chckbxSpeedMode.isSelected()){
						int time;
						try{
							time = Integer.parseInt(timerTime.getText());
							System.out.println(time);
						}catch(NumberFormatException exception){
							showDialog("Please Enter a Number From 5 To 20!");
							return;
						}
						if (time < 5 || time > 20) {
							showDialog("Please Enter a Number From 5 To 20!");
						} else if (rdbtnSingleplayer.isSelected()) {
							thisController.playSinglePlayer(blackAiPlayer.isSelected(), chckbxSpeedMode.isSelected(), rdbtnEasy.isSelected(),
									txtEnterPName_1.getText(), txtEnterPName.getText(), time);
						} else if (rdbtnTwoPlayer.isSelected()) {
							thisController.playTwoPlayer(chckbxSpeedMode.isSelected(), txtEnterPName_1.getText(),
									txtEnterPName.getText(), time);
						}
						
					}else{
						if (rdbtnSingleplayer.isSelected()) {
							thisController.playSinglePlayer(blackAiPlayer.isSelected(),chckbxSpeedMode.isSelected(),rdbtnEasy.isSelected(), txtEnterPName_1.getText(), txtEnterPName.getText(),0);
						} else if (rdbtnTwoPlayer.isSelected()) {
							thisController.playTwoPlayer(chckbxSpeedMode.isSelected(), txtEnterPName_1.getText(), txtEnterPName.getText(), 0);
						}
					}
				}else{
					showDialog("Player Names are too long, Please limit to 12 characters!");
				}
			}
		});
		add(btnPlay);
	}
	
	private void initialiseComponents(){
		rdbtnSingleplayer = new JRadioButton("Single Player");
		rdbtnTwoPlayer = new JRadioButton("Two Player");
		rdbtnEasy = new JRadioButton("Easy");
		rdbtnHard = new JRadioButton("Hard");
		chckbxSpeedMode = new JCheckBox("Speed Mode");
		timerTime = new JTextField("20");
		timeLabel = new JLabel("Seconds:");
		whiteAiPlayer = new JRadioButton();
		blackAiPlayer = new JRadioButton();
		black = new JLabel("Player Black");
		white = new JLabel("Player White");
		txtEnterPName = new JTextField();
		txtEnterPName_1 = new JTextField();
		AiSelectedField = txtEnterPName;
	}
	
	private void setUpGameTypeSelect(){
		
		
		
		JLabel lblGameType = new JLabel("Game Type");
		lblGameType.setBounds(48, 23, 81, 14);
		add(lblGameType);
		
		
		rdbtnSingleplayer.setBounds(48, 52, 109, 23);
		rdbtnSingleplayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnEasy.setEnabled(true);
				rdbtnHard.setEnabled(true);
				rdbtnEasy.setSelected(true);
				whiteAiPlayer.setEnabled(true);
				
				blackAiPlayer.setEnabled(true);
				blackAiPlayer.doClick();
				//blackAiPlayer.setSelected(true);
				AiSelectedField = txtEnterPName;
			}
		});
		add(rdbtnSingleplayer);

		
		rdbtnTwoPlayer.setBounds(48, 78, 109, 23);
		rdbtnTwoPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnEasy.setEnabled(false);
				rdbtnHard.setEnabled(false);
				aiDiff.clearSelection();
				whiteAiPlayer.setEnabled(false);
				blackAiPlayer.setEnabled(false);
				aiStartCol.clearSelection();
				AiSelectedField.setText("Player Two");
				AiSelectedField.setEditable(true);
			}
		});
		add(rdbtnTwoPlayer);

		gameType.add(rdbtnSingleplayer);
		gameType.add(rdbtnTwoPlayer);
		rdbtnSingleplayer.setSelected(true);
	}

	private void setUpAIdifficulty(){
		
		
		
		JLabel lblAiDifficulty = new JLabel("AI Difficulty:");
		lblAiDifficulty.setBounds(217, 35, 101, 14);
		add(lblAiDifficulty);
		
		
		
		
		rdbtnEasy.setBounds(217, 57, 109, 23);
		rdbtnEasy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AiSelectedField.setText("Easy AI");
			}
		});
		add(rdbtnEasy);

		
		rdbtnHard.setBounds(217, 87, 109, 23);
		rdbtnHard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AiSelectedField.setText("Hard AI");
			}
		});
		add(rdbtnHard);

		aiDiff.add(rdbtnEasy);
		aiDiff.add(rdbtnHard);
		
		rdbtnEasy.setSelected(true);
		rdbtnEasy.setEnabled(true);
		rdbtnHard.setEnabled(true);
		
	}
	
	private void setUpSpeedMode(){
		
		chckbxSpeedMode.setBounds(48, 136, 97, 23);
		add(chckbxSpeedMode);
		
		timeLabel.setBounds(48, 177, 97, 23);
		add(timeLabel);

		
		timerTime.setBounds(48, 208, 82, 20);
		add(timerTime);
	}
	
	private void setUpAIColour(){
		
		
		
		JLabel aiStart = new JLabel("AI Colour");
		aiStart.setBounds(430, 130, 90, 20);
		add(aiStart);
		
		
		
		whiteAiPlayer.setBounds(450, 178, 20, 20);
		whiteAiPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("got here");
				AiSelectedField = txtEnterPName_1;
				txtEnterPName.setText(txtEnterPName_1.getText());
				txtEnterPName.setEditable(true);
				if(rdbtnEasy.isSelected()){
					txtEnterPName_1.setText("Easy AI");
				}else{
					txtEnterPName_1.setText("Hard AI");
				}
				txtEnterPName_1.setEditable(false);
			}
		});
		add(whiteAiPlayer);
		aiStartCol.add(whiteAiPlayer);

		
		blackAiPlayer.setBounds(450, 153, 20, 20);
		blackAiPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AiSelectedField = txtEnterPName;
				txtEnterPName_1.setText(txtEnterPName.getText());
				txtEnterPName_1.setEditable(true);
				if(rdbtnEasy.isSelected()){
					txtEnterPName.setText("Easy AI");
				}else{
					txtEnterPName.setText("Hard AI");
				}
				txtEnterPName.setEditable(false);
			}
		});
		add(blackAiPlayer);
		aiStartCol.add(blackAiPlayer);
		
		blackAiPlayer.doClick();
	}

	private void setUpPlayerTxtField(){
		
		
		black.setBounds(217, 152, 109, 22);
		add(black);
		
		white.setBounds(217, 177, 109, 23);
		add(white);
		
		
		txtEnterPName.setText("Player Black");
		txtEnterPName.setBounds(346, 153, 97, 20);
		add(txtEnterPName);
		txtEnterPName.setColumns(10);

		
		txtEnterPName_1.setText("Player White");
		txtEnterPName_1.setBounds(345, 178, 98, 20);
		add(txtEnterPName_1);
		txtEnterPName_1.setColumns(10);
	}
	
	
	public void showDialog(String message){
		JOptionPane.showMessageDialog(this, message);
	}
}
