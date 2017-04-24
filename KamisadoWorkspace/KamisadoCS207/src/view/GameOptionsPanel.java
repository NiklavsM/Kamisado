package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;

import controller.Controller;

public class GameOptionsPanel extends JPanel {

	private JTextField txtBlackName;
	private JTextField txtWhiteName;
	private JCheckBox chckbxSpeedMode;
	private JCheckBox chckbxRandomBoard;
	private JLabel white;
	private JLabel black;
	private JRadioButton rdbtnEasy;
	private JRadioButton rdbtnHard;
	private JRadioButton rdbtnSingleplayer;
	private JRadioButton rdbtnTwoPlayer;
	private JRadioButton rdbtnNetworkPlay;
	private JRadioButton whiteAiPlayer;
	private JRadioButton blackAiPlayer;
	private JRadioButton hostNetworkGame;
	private JRadioButton joinNetworkGame;
	private JTextField timerTime;
	private JLabel timeLabel;
	private JButton btnPlay;
	private JTextField AiSelectedField;
	private ButtonGroup gameType = new ButtonGroup();
	private ButtonGroup aiStartCol = new ButtonGroup();
	private ButtonGroup aiDiff = new ButtonGroup();
	private ButtonGroup networkOption = new ButtonGroup();
	private JComboBox<Integer> gameLength;
	private JLabel gameRoundLabel;
	private String fontStyle = "Sitka Text";
	Controller controller;

	public GameOptionsPanel(Controller controller) {
		UIManager.put("Button.focus", Color.red);
		UIManager.put("RadioButton.focus", Color.red);
		UIManager.put("CheckBox.focus", Color.red);
		setLayout(null);
		this.controller = controller;
		JLabel title = new JLabel("Game options");
		title.setBounds(300, 100, 200, 40);
		title.setFont(new Font(fontStyle, Font.BOLD, 28));
		add(title);
		initialiseComponents();
		setUpAIdifficulty();
		setUpGameTypeSelect();
		setUpRandomBoardMode();
		setUpSpeedMode();
		setUpPlayerTxtField();
		setUpAIColour();
		setUpNetworkOptions();
		setUpRounds();
		setUpPlayButton();
		setUpGraphics();
	}

	private void initialiseGame(Controller thisController, String playerWhite, String playerBlack, int timerTime,
			boolean randomBoard) {
		if (rdbtnSingleplayer.isSelected()) {
			thisController.playSinglePlayer(blackAiPlayer.isSelected(), chckbxSpeedMode.isSelected(),
					rdbtnEasy.isSelected(), playerWhite, playerBlack, timerTime, (int) gameLength.getSelectedItem(),
					randomBoard);
		} else if (rdbtnTwoPlayer.isSelected()) {
			thisController.playTwoPlayer(chckbxSpeedMode.isSelected(), playerWhite, playerBlack, timerTime,
					(int) gameLength.getSelectedItem(), randomBoard);
		} else if (rdbtnNetworkPlay.isSelected()) {
			thisController.playNetwork(hostNetworkGame.isSelected(), chckbxSpeedMode.isSelected(), playerWhite,
					playerBlack, timerTime, (int) gameLength.getSelectedItem());
		}
	}

	private void initialiseComponents() {
		rdbtnSingleplayer = new JRadioButton("Single Player");
		rdbtnTwoPlayer = new JRadioButton("Two Player");
		rdbtnNetworkPlay = new JRadioButton("Network Play");
		hostNetworkGame = new JRadioButton("Host Game");
		joinNetworkGame = new JRadioButton("Join Game");
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
		gameRoundLabel = new JLabel("Rounds");
		gameLength = new JComboBox<Integer>(new Integer[] { 1, 3, 7, 15 });
		gameLength.setSelectedIndex(0);
		btnPlay = new JButton("Play");
	}

	private void setUpNetworkOptions() {
		JLabel lblGameType = new JLabel("Host or Join");
		lblGameType.setBounds(500, 200, 120, 20);
		add(lblGameType);

		hostNetworkGame.setBounds(500, 230, 120, 20);
		hostNetworkGame.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					AiSelectedField = txtWhiteName;
					txtBlackName.setText(txtWhiteName.getText());
					txtBlackName.setEditable(true);
					txtBlackName.setFocusable(true);
					txtWhiteName.setText("Opponent");
					txtWhiteName.setEditable(false);
					txtWhiteName.setFocusable(false);
				} else if (arg0.getStateChange() == ItemEvent.DESELECTED) {
					txtWhiteName.setEditable(true);
					txtWhiteName.setFocusable(true);
				}
			}
		});
		add(hostNetworkGame);

		joinNetworkGame.setBounds(500, 250, 120, 20);
		joinNetworkGame.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					AiSelectedField = txtBlackName;
					txtWhiteName.setText(txtBlackName.getText());
					txtWhiteName.setEditable(true);
					txtWhiteName.setFocusable(true);
					txtBlackName.setText("Opponent");
					txtBlackName.setEditable(false);
					txtBlackName.setFocusable(false);
					userIsHosting(false);
				} else if (arg0.getStateChange() == ItemEvent.DESELECTED) {
					txtBlackName.setEditable(false);
					txtBlackName.setFocusable(false);
					userIsHosting(true);
				}
			}
		});
		add(joinNetworkGame);

		networkOption.add(hostNetworkGame);
		networkOption.add(joinNetworkGame);
	}

	private void setUpGameTypeSelect() {
		JLabel lblGameType = new JLabel("Game Type");
		lblGameType.setBounds(200, 200, 120, 20);
		add(lblGameType);

		rdbtnSingleplayer.setBounds(200, 230, 109, 25);
		rdbtnSingleplayer.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					AiSelectedField = txtBlackName;
					AiSelectedField.setFocusable(false);
					AiSelectedField.setEditable(false);

					showAIOptions(true);
					blackAiPlayer.doClick();

				} else if (arg0.getStateChange() == ItemEvent.DESELECTED) {
					AiSelectedField.setFocusable(true);
					AiSelectedField.setEditable(true);

					showAIOptions(false);
				}
			}
		});
		add(rdbtnSingleplayer);

		rdbtnTwoPlayer.setBounds(200, 255, 109, 25);
		rdbtnTwoPlayer.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					AiSelectedField.setEditable(true);
					AiSelectedField.setFocusable(true);
					AiSelectedField.setText("New user");
				}
			}
		});
		add(rdbtnTwoPlayer);

		rdbtnNetworkPlay.setBounds(200, 280, 109, 25);
		rdbtnNetworkPlay.setContentAreaFilled(false);
		rdbtnNetworkPlay.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					chckbxSpeedMode.setSelected(false);
					hostNetworkGame.setSelected(true);
					showNetworkOptions(true);
				} else if (arg0.getStateChange() == ItemEvent.DESELECTED) {
					showNetworkOptions(false);
				}
			}
		});
		add(rdbtnNetworkPlay);
		showNetworkOptions(false);

		gameType.add(rdbtnNetworkPlay);
		gameType.add(rdbtnSingleplayer);
		gameType.add(rdbtnTwoPlayer);
		rdbtnSingleplayer.setSelected(true);
	}

	private void setUpAIdifficulty() {
		JLabel lblAiDifficulty = new JLabel("AI Difficulty:");
		lblAiDifficulty.setBounds(367, 200, 120, 20);
		add(lblAiDifficulty);

		rdbtnEasy.setBounds(367, 230, 109, 23);
		rdbtnEasy.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					AiSelectedField.setText("Easy AI");
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					AiSelectedField.setText("Hard AI");
				}
			}
		});
		add(rdbtnEasy);

		rdbtnHard.setBounds(367, 250, 109, 23);
		add(rdbtnHard);

		aiDiff.add(rdbtnEasy);
		aiDiff.add(rdbtnHard);

		rdbtnEasy.setSelected(true);
		rdbtnEasy.setEnabled(true);
		rdbtnHard.setEnabled(true);
		rdbtnEasy.doClick();
	}

	private void setUpRandomBoardMode() {
		chckbxRandomBoard.setBounds(200, 336, 130, 23);
		add(chckbxRandomBoard);
	}

	private void setUpSpeedMode() {

		chckbxSpeedMode.setBounds(200, 360, 97, 23);
		chckbxSpeedMode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					timerTime.setEditable(true);
					timerTime.setFocusable(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					timerTime.setEditable(false);
					timerTime.setFocusable(false);
				}
			}
		});
		add(chckbxSpeedMode);

		timeLabel.setBounds(200, 382, 97, 23);
		add(timeLabel);

		timerTime.setBounds(200, 408, 82, 20);
		add(timerTime);
		chckbxSpeedMode.setSelected(false);
		timerTime.setEditable(false);
	}

	private void setUpAIColour() {
		JLabel aiStart = new JLabel("AI");
		aiStart.setBounds(605, 300, 30, 20);
		add(aiStart);

		whiteAiPlayer.setBounds(600, 360, 60, 20);
		whiteAiPlayer.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					AiSelectedField = txtWhiteName;
					txtBlackName.setText(txtWhiteName.getText());
					txtBlackName.setEditable(true);
					txtBlackName.setFocusable(true);
					if (rdbtnEasy.isSelected()) {
						txtWhiteName.setText("Easy AI");
					} else {
						txtWhiteName.setText("Hard AI");
					}
					txtWhiteName.setEditable(false);
					txtWhiteName.setFocusable(false);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					AiSelectedField = txtBlackName;
					txtWhiteName.setText(txtBlackName.getText());
					txtWhiteName.setEditable(true);
					txtWhiteName.setFocusable(true);
					if (rdbtnEasy.isSelected()) {
						txtBlackName.setText("Easy AI");
					} else {
						txtBlackName.setText("Hard AI");
					}
					txtBlackName.setEditable(false);
					txtBlackName.setFocusable(false);
				}
			}
		});
		add(whiteAiPlayer);
		aiStartCol.add(whiteAiPlayer);

		blackAiPlayer.setBounds(600, 330, 60, 20);
		add(blackAiPlayer);
		aiStartCol.add(blackAiPlayer);
		whiteAiPlayer.doClick();
		blackAiPlayer.doClick();

	}

	private void setUpPlayerTxtField() {
		black.setBounds(390, 330, 109, 20);
		add(black);

		white.setBounds(390, 360, 109, 20);
		add(white);

		txtBlackName.setText("Player Black");
		txtBlackName.setBounds(500, 330, 90, 20);
		add(txtBlackName);
		txtBlackName.setColumns(10);

		txtWhiteName.setText("Player White");
		txtWhiteName.setBounds(500, 360, 90, 20);
		add(txtWhiteName);
		txtWhiteName.setColumns(10);
	}

	private void setUpRounds() {
		gameRoundLabel.setBounds(200, 430, 80, 20);
		add(gameRoundLabel);
		gameLength.setBounds(200, 452, 82, 20);
		add(gameLength);
	}

	private void setUpPlayButton() {

		btnPlay = new JButton("Play");
		btnPlay.setBounds(510, 500, 120, 40);
		btnPlay.setBackground(new Color(239, 155, 0));
		btnPlay.setFont(new Font(fontStyle, Font.BOLD, 24));
		btnPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String playerBlack = txtBlackName.getText().trim();
				String playerWhite = txtWhiteName.getText().trim();
				Boolean randomBoard = chckbxRandomBoard.isSelected();
				txtBlackName.setText(playerBlack);
				txtWhiteName.setText(playerWhite);
				if (playerWhite.length() <= 12 && playerBlack.length() <= 12) {
					if (playerWhite.length() != 0 && playerBlack.length() != 0) {
						if (!playerWhite.equals(playerBlack)) {
							if (whiteAiPlayer.isSelected()) {
								if (playerBlack.equals("Easy AI") || playerBlack.equals("Hard AI")) {
									JOptionPane.showMessageDialog(null,
											"Please rename player Black, this name is protected!");
									return;
								}
							} else if (blackAiPlayer.isSelected()) {
								if (playerWhite.equals("Easy AI") || playerWhite.equals("Hard AI")) {
									JOptionPane.showMessageDialog(null,
											"Please rename player White, this name is protected!");
									return;
								}
							}
							if (chckbxSpeedMode.isSelected()) {
								int time;
								try {
									time = Integer.parseInt(timerTime.getText());
								} catch (NumberFormatException exception) {
									JOptionPane.showMessageDialog(null, "Please Enter a Number From 5 To 60!");
									return;
								}
								if (time < 5 || time > 60) {
									JOptionPane.showMessageDialog(null, "Please Enter a Number From 5 To 60!");
								} else {
									focusPlay(false);
									initialiseGame(controller, playerWhite, playerBlack, time, randomBoard);
								}
							} else {
								focusPlay(false);
								initialiseGame(controller, playerWhite, playerBlack, 0, randomBoard);
							}
						} else {
							JOptionPane.showMessageDialog(null,
									"Player Names can't be the same, You can't play yourself!");
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"Player Name can't be empty, Please Enter at least one character!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Player Names are too long, Please limit to 12 characters!");
				}
			}
		});
		add(btnPlay);
	}

	private void setUpGraphics() {

		JLabel logo = new JLabel();
		logo.setBounds(260, 0, 500, 100);
		ImageIcon tempImage = new ImageIcon(getClass().getResource("/images/logo.png"));
		logo.setIcon(tempImage);
		add(logo);

		JLabel dragonLeft = new JLabel();
		dragonLeft.setBounds(-10, 0, 300, 650);
		ImageIcon dragonLeftImage = new ImageIcon(getClass().getResource("/images/dragonleft.png"));
		dragonLeft.setIcon(dragonLeftImage);
		add(dragonLeft);

		JLabel dragonRight = new JLabel();
		dragonRight.setBounds(555, 0, 300, 650);
		ImageIcon dragonRightImage = new ImageIcon(getClass().getResource("/images/dragonright.png"));
		dragonRight.setIcon(dragonRightImage);
		add(dragonRight);

	}

	public void focusPlay(boolean b) {
		this.setFocusable(false);
		btnPlay.setFocusable(b);
		btnPlay.transferFocus();
	}

	private void userIsHosting(boolean b) {
		gameLength.setEnabled(b);
		gameLength.setFocusable(b);
	}

	private void showNetworkOptions(boolean b) {
		if (!b) {
			networkOption.clearSelection();
		}
		hostNetworkGame.setEnabled(b);
		hostNetworkGame.setFocusable(b);
		joinNetworkGame.setEnabled(b);
		joinNetworkGame.setFocusable(b);
		hostNetworkGame.setSelected(b);
		chckbxSpeedMode.setEnabled(!b);
		chckbxSpeedMode.setFocusable(!b);
		chckbxRandomBoard.setEnabled(!b);
		chckbxRandomBoard.setFocusable(!b);
	}

	private void showAIOptions(boolean b) {
		if (!b) {
			aiDiff.clearSelection();
			aiStartCol.clearSelection();
		}
		rdbtnEasy.setEnabled(b);
		rdbtnEasy.setFocusable(b);
		rdbtnHard.setEnabled(b);
		rdbtnHard.setFocusable(b);
		rdbtnEasy.setSelected(b);
		whiteAiPlayer.setEnabled(b);
		whiteAiPlayer.setFocusable(b);
		blackAiPlayer.setEnabled(b);
		blackAiPlayer.setFocusable(b);
	}
}
