package view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import controller.Controller;

public class MenuFrame extends JFrame {

	private JPanel homePanel;
	private JPanel contentPane;
	private GameOptionsPanel options;
	private StatPanel statPanel;
	private RunningGameView gameView;
	private GeneralSettingsPanel generalSettingsPanel;
	private Controller controller;
	private String currentlyShownPanel;

	public MenuFrame(Controller controller, RunningGameView gameView) {
		this.controller = controller;
		menuBar();
		this.gameView = gameView;
		contentPane = new JPanel(new CardLayout());
		options = new GameOptionsPanel(controller);
		homePanel = new JPanel();

		JLabel homeLabel = new JLabel();
		ImageIcon homeImage = new ImageIcon(getClass().getResource("/images/logo.png"));
		homeLabel.setIcon(homeImage);
		homePanel.add(homeLabel);
		homePanel.setFocusable(false);

		contentPane.add(homePanel, "Home");
		contentPane.add(options, "New Game");
		contentPane.add(this.gameView, "Game View");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		CardLayout c1 = (CardLayout) contentPane.getLayout();
		c1.show(contentPane, "New Game");
		currentlyShownPanel = "New Game";
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 700, 700);
		setResizable(false);
	}

	public void menuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFocusable(false);
		JMenu main = new JMenu("Menu");

		JMenuItem home = new JMenuItem("Home");
		home.setMnemonic(KeyEvent.VK_H);
		home.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));

		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(displayConfirmExitMessage() == 0){
					CardLayout c1 = (CardLayout) contentPane.getLayout();
					c1.show(contentPane, "Home");
					currentlyShownPanel = "Home";
					homePanel.requestFocus();
					JPanel tempGlassPane = (JPanel) getGlassPane();
					tempGlassPane.removeAll();
					tempGlassPane.repaint();
				}
			}
		});

		JMenuItem newgame = new JMenuItem("New Game");
		newgame.setMnemonic(KeyEvent.VK_N);
		newgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));

		newgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(displayConfirmExitMessage() == 0){
					controller.killGame();
					gameView.setUpTimer();
					CardLayout c1 = (CardLayout) contentPane.getLayout();
					c1.show(contentPane, "New Game");
					currentlyShownPanel = "New Game";
					options.requestFocus();
					JPanel tempGlassPane = (JPanel) getGlassPane();
					tempGlassPane.removeAll();
					tempGlassPane.repaint();
				}
			}
		});

		JMenuItem loadgame = new JMenuItem("Load Game");
		loadgame.setMnemonic(KeyEvent.VK_L);
		loadgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));

		loadgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(displayConfirmExitMessage() == 0){
					controller.killGame();
					CardLayout c1 = (CardLayout) contentPane.getLayout();
					c1.show(contentPane, "New Game");
					currentlyShownPanel = "New Game";
					gameView.setWinnerLabel("");
					gameView.setUpTimer();
					JPanel tempGlassPane = (JPanel) getGlassPane();
					tempGlassPane.removeAll();
					tempGlassPane.repaint();
					controller.loadGame();
				}
			}
		});

		JMenuItem stats = new JMenuItem("Stats");
		stats.setMnemonic(KeyEvent.VK_S);
		stats.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));

		stats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(displayConfirmExitMessage() == 0){
					statPanel = new StatPanel();
					contentPane.add(statPanel, "Stats");
					CardLayout c1 = (CardLayout) contentPane.getLayout();
					c1.show(contentPane, "Stats");
					currentlyShownPanel = "Stats";
					statPanel.requestFocus();
					JPanel tempGlassPane = (JPanel) getGlassPane();
					tempGlassPane.removeAll();
					tempGlassPane.repaint();
				}
			}
		});
		
		JMenuItem generalSettings = new JMenuItem("General Settings");
		generalSettings.setMnemonic(KeyEvent.VK_O);
		generalSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));

		generalSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(displayConfirmExitMessage() == 0){
					generalSettingsPanel = new GeneralSettingsPanel();
					contentPane.add(generalSettingsPanel, "General Settings");
					CardLayout c1 = (CardLayout) contentPane.getLayout();
					c1.show(contentPane, "General Settings");
					currentlyShownPanel = "General Settings";
					generalSettingsPanel.requestFocus();
					JPanel tempGlassPane = (JPanel) getGlassPane();
					tempGlassPane.removeAll();
					tempGlassPane.repaint();
				}
			}
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(displayConfirmExitMessage() == 0){
					dispatchFrame();
				}
			}
		});
		
		    
		
		menuBar.add(main);
		menuBar.add(new JLabel("Alt-M"));
		main.add(home);
		main.add(newgame);
		main.add(loadgame);
		main.add(stats);
		main.add(generalSettings);
		main.add(exit);
		main.setMnemonic(KeyEvent.VK_M);
		this.setJMenuBar(menuBar);
		
		MenuSelectionManager.defaultManager().addChangeListener(
		        new ChangeListener() {
		            public void stateChanged(ChangeEvent evt) {

		              MenuElement[] path = MenuSelectionManager.defaultManager()
		                  .getSelectedPath();

		              if (path.length == 0) {
		                getGlassPane().setVisible(true);
		              }else{
		            	  getGlassPane().setVisible(false);
		              }
		            }
		          });
	}

	public void dispatchFrame() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public void ShowGameViewPanel() {
		CardLayout c1 = (CardLayout) contentPane.getLayout();
		c1.show(contentPane, "Game View");
		currentlyShownPanel = "Game View";
		gameView.requestFocus();
		JPanel tempGlassPane = (JPanel) this.getGlassPane();
		tempGlassPane.removeAll();
		tempGlassPane.repaint();
	}
	
	public int displayConfirmExitMessage(){
		if(currentlyShownPanel.equals("Game View")){
			Object[] options = { "Yes, I want to quit", "Hold on, let me finish this" };
			return JOptionPane.showOptionDialog(null, "Are you sure you want to quit the current game?",
					"Quiting so soon?!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
					options[1]);
		}else{
			return 0;
		}
		
	}
}