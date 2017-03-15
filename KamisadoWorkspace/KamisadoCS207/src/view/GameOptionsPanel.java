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

		white = new JLabel("Player White");
		white.setBounds(217, 152, 109, 22);
		add(white);
		black = new JLabel("Player Black");
		black.setBounds(217, 177, 109, 23);
		add(black);
		JLabel lblAiDifficulty = new JLabel("AI Difficulty:");
		lblAiDifficulty.setBounds(217, 35, 101, 14);
		add(lblAiDifficulty);

		rdbtnEasy = new JRadioButton("Easy");
		rdbtnEasy.setBounds(217, 57, 109, 23);
		add(rdbtnEasy);

		rdbtnHard = new JRadioButton("Hard");
		rdbtnHard.setBounds(217, 87, 109, 23);
		add(rdbtnHard);

		aiDiff.add(rdbtnEasy);
		aiDiff.add(rdbtnHard);

		JLabel BestOf = new JLabel("Best of:");
		BestOf.setBounds(48, 177, 97, 23);
		add(BestOf);

		JComboBox<String> comboBox = new JComboBox<>(new String[] { "1", "3", "5", "7" });
		comboBox.setBounds(48, 208, 82, 20);
		add(comboBox);

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
		txtEnterPName.setText("P1");
		txtEnterPName.setBounds(346, 153, 97, 20);
		add(txtEnterPName);
		txtEnterPName.setColumns(10);

		txtEnterPName_1 = new JTextField();
		txtEnterPName_1.setText("P2");
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
				if (rdbtnSingleplayer.isSelected()) {
					if (rdbtnEasy.isSelected()) {
						if(chckbxSpeedMode.isSelected()){
							thisController.playSinglePlayer(true,true,true, txtEnterPName.getText(), txtEnterPName_1.getText());
						}
						else{
							thisController.playSinglePlayer(true,false,true, txtEnterPName.getText(), txtEnterPName_1.getText());
						}
					} else {
						if(chckbxSpeedMode.isSelected()){
							thisController.playSinglePlayer(true, true, false, txtEnterPName.getText(), txtEnterPName_1.getText());
						}
						else{
							thisController.playSinglePlayer(true, false, false, txtEnterPName.getText(), txtEnterPName_1.getText());
						}
					}
				} else if (rdbtnTwoPlayer.isSelected()) {
					if(chckbxSpeedMode.isSelected()){
						thisController.playTwoPlayer(true, txtEnterPName.getText(), txtEnterPName_1.getText());
					}else{
						thisController.playTwoPlayer(false, txtEnterPName.getText(), txtEnterPName_1.getText());
					}
				}
			}
		});
		add(btnPlay);
	}
}
