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

	/**
	 * Create the panel.
	 */
	public GameOptionsPanel(Controller controller) {
		setLayout(null);
		Controller thisController = controller;
		ButtonGroup gameType = new ButtonGroup();
		ButtonGroup aiDiff = new ButtonGroup();

		chckbxSpeedMode = new JCheckBox("Speed Mode");
		chckbxSpeedMode.setBounds(48, 136, 97, 23);
		add(chckbxSpeedMode);

		JSeparator separator = new JSeparator();
		separator.setBounds(32, 108, 168, 22);
		add(separator);

		black = new JLabel("Player Black");
		black.setBounds(217, 152, 109, 22);
		add(black);
		white = new JLabel("Player White");
		white.setBounds(217, 177, 109, 23);
		add(white);
		JLabel lblAiDifficulty = new JLabel("AI Difficulty:");
		lblAiDifficulty.setBounds(217, 35, 101, 14);
		add(lblAiDifficulty);

		whiteAiPlayer = new JRadioButton();
		whiteAiPlayer.setBounds(450, 153, 20, 20);
		add(whiteAiPlayer);

		blackAiPlayer = new JRadioButton();
		blackAiPlayer.setBounds(450, 178, 20, 20);
		add(blackAiPlayer);
		
		rdbtnEasy = new JRadioButton("Easy");
		rdbtnEasy.setBounds(217, 57, 109, 23);
		add(rdbtnEasy);

		rdbtnHard = new JRadioButton("Hard");
		rdbtnHard.setBounds(217, 87, 109, 23);
		add(rdbtnHard);

		aiDiff.add(rdbtnEasy);
		aiDiff.add(rdbtnHard);

		
		timeLabel = new JLabel("Seconds:");
		timeLabel.setBounds(48, 177, 97, 23);
		add(timeLabel);

		timerTime = new JTextField("20");
		timerTime.setBounds(48, 208, 82, 20);
		add(timerTime);
		

		rdbtnSingleplayer = new JRadioButton("Single Player");
		rdbtnSingleplayer.setBounds(48, 52, 109, 23);
		rdbtnSingleplayer.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				rdbtnEasy.setEnabled(true);
				rdbtnHard.setEnabled(true);
			}
		});
		add(rdbtnSingleplayer);

		rdbtnTwoPlayer = new JRadioButton("Two Player");
		rdbtnTwoPlayer.setBounds(48, 78, 109, 23);
		rdbtnTwoPlayer.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				rdbtnEasy.setEnabled(false);
				rdbtnHard.setEnabled(false);
			}
		});
		add(rdbtnTwoPlayer);

		gameType.add(rdbtnSingleplayer);
		gameType.add(rdbtnTwoPlayer);
		rdbtnSingleplayer.setSelected(true);
		rdbtnEasy.setSelected(true);
		rdbtnEasy.setEnabled(true);
		rdbtnHard.setEnabled(true);
		
		txtEnterPName = new JTextField();
		txtEnterPName.setText("Player Black");
		txtEnterPName.setBounds(346, 153, 97, 20);
		add(txtEnterPName);
		txtEnterPName.setColumns(10);

		txtEnterPName_1 = new JTextField();
		txtEnterPName_1.setText("Player White");
		txtEnterPName_1.setBounds(345, 178, 98, 20);
		add(txtEnterPName_1);
		txtEnterPName_1.setColumns(10);

		JLabel lblGameType = new JLabel("Game Type");
		lblGameType.setBounds(48, 23, 81, 14);
		add(lblGameType);

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
							thisController.playSinglePlayer(true, chckbxSpeedMode.isSelected(), rdbtnEasy.isSelected(),
									txtEnterPName_1.getText(), txtEnterPName.getText(), time);
						} else if (rdbtnTwoPlayer.isSelected()) {
							thisController.playTwoPlayer(chckbxSpeedMode.isSelected(), txtEnterPName_1.getText(),
									txtEnterPName.getText(), time);
						}
						
					}else{
						if (rdbtnSingleplayer.isSelected()) {
							thisController.playSinglePlayer(true,chckbxSpeedMode.isSelected(),rdbtnEasy.isSelected(), txtEnterPName_1.getText(), txtEnterPName.getText(),0);
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
	
	public void showDialog(String message){
		JOptionPane.showMessageDialog(this, message);
	}
}
