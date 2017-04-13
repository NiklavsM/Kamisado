package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
import javax.swing.UIManager;

import controller.Controller;

public class GameOptionsPanel extends JPanel {
	private JTextField txtBlackName;
	private JTextField txtWhiteName;
	JCheckBox chckbxSpeedMode;
	JCheckBox chckbxRandomBoard;
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
	JComboBox<Integer> gameLength;

	/**
	 * Create the panel.
	 */
	public GameOptionsPanel(Controller controller) {
		UIManager.put("Button.focus", Color.red);
		UIManager.put("RadioButton.focus", Color.red);
		UIManager.put("CheckBox.focus", Color.red);
		setLayout(null);
		Controller thisController = controller;
		initialiseComponents();
		setUpAIdifficulty();
		setUpGameTypeSelect();
		setUpRandomBoardMode();
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
				String playerBlack = txtBlackName.getText().trim();
				String playerWhite = txtWhiteName.getText().trim();
				Boolean randomBoard = chckbxRandomBoard.isSelected();
				txtBlackName.setText(playerBlack);
				txtWhiteName.setText(playerWhite);
				if(playerWhite.length() <= 12 && playerBlack.length() <=12){
					if(playerWhite.length() != 0 && playerBlack.length() !=0){
						if(!playerWhite.equals(playerBlack)){
							if(whiteAiPlayer.isSelected()){
								if(playerBlack.equals("Easy AI") || playerBlack.equals("Hard AI")){
									JOptionPane.showMessageDialog(null, "Please rename player Black, this name is protected!");
									return;
								}
							}else if(blackAiPlayer.isSelected()){
								if(playerWhite.equals("Easy AI") || playerWhite.equals("Hard AI")){
									JOptionPane.showMessageDialog(null, "Please rename player White, this name is protected!");
									return;
								}
							}
							if(chckbxSpeedMode.isSelected()){
								int time;
								try{
									time = Integer.parseInt(timerTime.getText());
									System.out.println(time);
								}catch(NumberFormatException exception){
									JOptionPane.showMessageDialog(null, "Please Enter a Number From 5 To 20!");
									return;
								}
								if (time < 5 || time > 20) {
									JOptionPane.showMessageDialog(null, "Please Enter a Number From 5 To 20!");
								} else{
									initialiseGame(thisController, playerWhite, playerBlack, time, randomBoard);
								}
							}else{
								initialiseGame(thisController, playerWhite, playerBlack, 0, randomBoard);
							}
						}else{
							JOptionPane.showMessageDialog(null, "Player Names can't be the same, You can't play yourself!");
						}
					}else{
						JOptionPane.showMessageDialog(null, "Player Name can't be empty, Please Enter at least one character!");
					}
				}else{
					JOptionPane.showMessageDialog(null, "Player Names are too long, Please limit to 12 characters!");
				}
			}
		});
		add(btnPlay);
	}
	
	private void initialiseGame(Controller thisController, String playerWhite, String playerBlack, int timerTime, boolean randomBoard ){
		if (rdbtnSingleplayer.isSelected()) {
			thisController.playSinglePlayer(blackAiPlayer.isSelected(),chckbxSpeedMode.isSelected(),rdbtnEasy.isSelected(), playerWhite, playerBlack,timerTime, (int)gameLength.getSelectedItem(), randomBoard);
		} else if (rdbtnTwoPlayer.isSelected()) {
			thisController.playTwoPlayer(chckbxSpeedMode.isSelected(), playerWhite, playerBlack, timerTime, (int) gameLength.getSelectedItem(), randomBoard);
		}
	}
	
	private void initialiseComponents(){
		rdbtnSingleplayer = new JRadioButton("Single Player");
		rdbtnTwoPlayer = new JRadioButton("Two Player");
		rdbtnEasy = new JRadioButton("Easy");
		rdbtnHard = new JRadioButton("Hard");
		chckbxRandomBoard = new JCheckBox("Random Board");
		chckbxSpeedMode = new JCheckBox("Speed Mode");
		timerTime = new JTextField("20");
		timeLabel = new JLabel("Seconds:");
		whiteAiPlayer = new JRadioButton("...");
		blackAiPlayer = new JRadioButton("...");
		black = new JLabel("Player Black");
		white = new JLabel("Player White");
		txtBlackName = new JTextField();
		txtWhiteName = new JTextField();
		AiSelectedField = txtBlackName;
		gameLength = new JComboBox<Integer>(new Integer[]{1,3,7,15});
		gameLength.setSelectedIndex(0);
	}
	
	private void setUpGameTypeSelect(){
		JLabel lblGameType = new JLabel("Game Type");
		lblGameType.setBounds(48, 23, 81, 14);
		add(lblGameType);
		
		rdbtnSingleplayer.setBounds(48, 52, 109, 23);
		rdbtnSingleplayer.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED){
					rdbtnEasy.setEnabled(true);
					rdbtnHard.setEnabled(true);
					rdbtnEasy.setSelected(true);
					whiteAiPlayer.setEnabled(true);
					
					blackAiPlayer.setEnabled(true);
					blackAiPlayer.doClick();
					//blackAiPlayer.setSelected(true);
					AiSelectedField = txtBlackName;
					AiSelectedField.setFocusable(false);
					AiSelectedField.setEditable(false);
				}else if(arg0.getStateChange() == ItemEvent.DESELECTED){
					rdbtnEasy.setEnabled(false);
					rdbtnHard.setEnabled(false);
					aiDiff.clearSelection();
					whiteAiPlayer.setEnabled(false);
					blackAiPlayer.setEnabled(false);
					aiStartCol.clearSelection();
					AiSelectedField.setEditable(true);
					AiSelectedField.setFocusable(true);
					AiSelectedField.setText("New user");
				}
			}
		});
		add(rdbtnSingleplayer);

		rdbtnTwoPlayer.setBounds(48, 78, 109, 23);
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
		rdbtnEasy.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					AiSelectedField.setText("Easy AI");
				}else if(e.getStateChange() == ItemEvent.DESELECTED){
					AiSelectedField.setText("Hard AI");
				}
			}
		});
		add(rdbtnEasy);

		rdbtnHard.setBounds(217, 87, 109, 23);
		add(rdbtnHard);

		aiDiff.add(rdbtnEasy);
		aiDiff.add(rdbtnHard);
		
		rdbtnEasy.setSelected(true);
		rdbtnEasy.setEnabled(true);
		rdbtnHard.setEnabled(true);
		rdbtnEasy.doClick();
	}
	private void setUpRandomBoardMode(){
		chckbxRandomBoard.setBounds(48, 136, 130, 23);
		add(chckbxRandomBoard);
	}
	
	private void setUpSpeedMode(){
		
		chckbxSpeedMode.setBounds(48, 160, 97, 23);
		chckbxSpeedMode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					timerTime.setEditable(true);
					timerTime.setFocusable(true);
				}else if(e.getStateChange() == ItemEvent.DESELECTED){
					timerTime.setEditable(false);
					timerTime.setFocusable(false);
				}
			}
		});
		add(chckbxSpeedMode);
		
		timeLabel.setBounds(48, 182, 97, 23);
		add(timeLabel);

		timerTime.setBounds(48, 208, 82, 20);
		add(timerTime);
		chckbxSpeedMode.setSelected(true);
		chckbxSpeedMode.setSelected(false);
	}
	
	private void setUpAIColour(){
		JLabel aiStart = new JLabel("AI Colour");
		aiStart.setBounds(430, 130, 90, 20);
		add(aiStart);
		
		whiteAiPlayer.setBounds(450, 178, 60, 20);
		whiteAiPlayer.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					AiSelectedField = txtWhiteName;
					txtBlackName.setText(txtWhiteName.getText());
					txtBlackName.setEditable(true);
					txtBlackName.setFocusable(true);
					if(rdbtnEasy.isSelected()){
						txtWhiteName.setText("Easy AI");
					}else{
						txtWhiteName.setText("Hard AI");
					}
					txtWhiteName.setEditable(false);
					txtWhiteName.setFocusable(false);
				}else if(e.getStateChange() == ItemEvent.DESELECTED){
					AiSelectedField = txtBlackName;
					txtWhiteName.setText(txtBlackName.getText());
					txtWhiteName.setEditable(true);
					txtWhiteName.setFocusable(true);
					if(rdbtnEasy.isSelected()){
						txtBlackName.setText("Easy AI");
					}else{
						txtBlackName.setText("Hard AI");
					}
					txtBlackName.setEditable(false);
					txtBlackName.setFocusable(false);
				}
			}
		});
		add(whiteAiPlayer);
		aiStartCol.add(whiteAiPlayer);

		blackAiPlayer.setBounds(450, 153, 60, 20);
		add(blackAiPlayer);
		aiStartCol.add(blackAiPlayer);
		whiteAiPlayer.doClick();
		blackAiPlayer.doClick();
		
		gameLength.setBounds(48, 250, 82, 20);
		
		add(gameLength);
	}

	private void setUpPlayerTxtField(){
		black.setBounds(217, 152, 109, 22);
		add(black);
		
		white.setBounds(217, 177, 109, 23);
		add(white);

		txtBlackName.setText("Player Black");
		txtBlackName.setBounds(346, 153, 97, 20);
		add(txtBlackName);
		txtBlackName.setColumns(10);

		txtWhiteName.setText("Player White");
		txtWhiteName.setBounds(345, 178, 98, 20);
		add(txtWhiteName);
		txtWhiteName.setColumns(10);
	}
}
