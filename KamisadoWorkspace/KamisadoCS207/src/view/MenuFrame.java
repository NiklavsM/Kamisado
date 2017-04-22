package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

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

import controller.Controller;

public class MenuFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private GameOptionsPanel options;
	private Controller controller;
	private String currentlyShownPanel = "";

	public MenuFrame(Controller controller) {
		this.controller = controller;
		menuBar();
		contentPane = new JPanel(new CardLayout());
		options = new GameOptionsPanel(controller);
		addPanel(options, "New Game");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		ShowPanel("New Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 820, 700);
		setResizable(false);
		this.getContentPane().setBackground( new Color(239, 155, 0));
	}

	public void menuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFocusable(false);
		JMenu main = new JMenu("Menu");

		JMenuItem newgame = new JMenuItem("New Game");
		newgame.setMnemonic(KeyEvent.VK_N);
		newgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));

		newgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (displayConfirmExitMessage() == 0) {
					controller.killGame();
					ShowPanel("New Game");
				}
			}
		});

		JMenuItem loadgame = new JMenuItem("Load Game");
		loadgame.setMnemonic(KeyEvent.VK_L);
		loadgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));

		loadgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (displayConfirmExitMessage() == 0) {
					controller.killGame();
					ShowPanel("New Game");
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
				if (displayConfirmExitMessage() == 0) {
					controller.killGame();
					StatPanel statPanel = new StatPanel();
					addPanel(statPanel, "Stats");
					ShowPanel("Stats");
				}
			}
		});

		JMenuItem generalSettings = new JMenuItem("General Settings");
		generalSettings.setMnemonic(KeyEvent.VK_O);
		generalSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));

		generalSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (displayConfirmExitMessage() == 0) {
					controller.killGame();
					GeneralSettingsPanel generalSettingsPanel = new GeneralSettingsPanel(controller);
					addPanel(generalSettingsPanel, "General Settings");
					ShowPanel("General Settings");
				}
			}
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (displayConfirmExitMessage() == 0) {
					dispatchFrame();
				}
			}
		});

		menuBar.add(main);
		menuBar.add(new JLabel("Alt-M"));
		main.add(newgame);
		main.add(loadgame);
		main.add(stats);
		main.add(generalSettings);
		main.add(exit);
		main.setMnemonic(KeyEvent.VK_M);
		this.setJMenuBar(menuBar);

		MenuSelectionManager.defaultManager().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {

				MenuElement[] path = MenuSelectionManager.defaultManager().getSelectedPath();

				if (path.length == 0) {
					getGlassPane().setVisible(true);
				} else {
					getGlassPane().setVisible(false);
				}
			}
		});
	}

	public void dispatchFrame() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public void ShowPanel(String panelsName) {
		
		if (!panelsName.equals(currentlyShownPanel)) {
			if(currentlyShownPanel.equals("New Game")){
					System.out.println("set new game to false");
					//options.setFocusable(false);
					options.focusPlay(false);
			}
			if(panelsName.equals("New Game")){
				options.focusPlay(true);
			}
			CardLayout c1 = (CardLayout) contentPane.getLayout();
			
			c1.show(contentPane, panelsName);
			currentlyShownPanel = panelsName;
			JPanel tempGlassPane = (JPanel) this.getGlassPane();
			tempGlassPane.removeAll();
			tempGlassPane.repaint();
		}
	}

	public int displayConfirmExitMessage() {
		if (currentlyShownPanel.equals("Game View")) {
			Object[] options = { "Yes, I want to quit", "Hold on, let me finish this" };
			return JOptionPane.showOptionDialog(null, "Are you sure you want to quit the current game?",
					"Quiting so soon?!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
					options[1]);
		} else {
			return 0;
		}
	}

	public void addPanel(JPanel panel, String name) {
		contentPane.add(panel, name);
	}
}