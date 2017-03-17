package view;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import controller.Controller;

public class MenuFrame extends JFrame {

	private JPanel homePanel;
	private JPanel contentPane;
	private GameOptionsPanel options;
	private StatPanel statPanel;
	private LoadGamePanel loadPanel;
	private RunningGameView gameView;
	private Controller controller;


	public MenuFrame(Controller controller) {
		this.controller = controller;
		menuBar();
		gameView = new RunningGameView("Test1", "Test2", controller);
		contentPane = new JPanel(new CardLayout());
		statPanel = new StatPanel();
		loadPanel = new LoadGamePanel();
		options = new GameOptionsPanel(controller);
		homePanel = new JPanel();
		
		
		JLabel homeLabel = new JLabel();
		ImageIcon homeImage = new ImageIcon(getClass().getResource("/images/logo.png"));
		homeLabel.setIcon(homeImage);
		homePanel.add(homeLabel);
		homePanel.setFocusable(false);

		contentPane.add(homePanel, "Home");
		contentPane.add(options, "New Game");
		contentPane.add(loadPanel, "Load Game");
		contentPane.add(statPanel, "Stats");
		contentPane.add(gameView, "Game View");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		CardLayout c1 = (CardLayout) contentPane.getLayout();
		c1.show(contentPane, "New Game");
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 530, 530);
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
				CardLayout c1 = (CardLayout) contentPane.getLayout();
				c1.show(contentPane, "Home");
				homePanel.requestFocus();
			}
		});

		JMenuItem newgame = new JMenuItem("New Game");
		newgame.setMnemonic(KeyEvent.VK_N);
		newgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));

		newgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CardLayout c1 = (CardLayout) contentPane.getLayout();
				c1.show(contentPane, "New Game");
				options.requestFocus();
			}
		});

		JMenuItem loadgame = new JMenuItem("Load Game");
		loadgame.setMnemonic(KeyEvent.VK_L);
		loadgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));

		loadgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.getGame().loadGame();
//				CardLayout c1 = (CardLayout) contentPane.getLayout();
//				c1.show(contentPane, "Load Game");
//				loadPanel.requestFocus();
			}
		});

		JMenuItem stats = new JMenuItem("Stats");
		stats.setMnemonic(KeyEvent.VK_S);
		stats.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));

		stats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CardLayout c1 = (CardLayout) contentPane.getLayout();
				c1.show(contentPane, "Stats");
				statPanel.requestFocus();
			}
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispatchFrame();
			}
		});

		main.setMnemonic(KeyEvent.VK_M);
		menuBar.add(main);
		main.add(home);
		main.add(newgame);
		main.add(loadgame);
		main.add(stats);
		main.add(exit);
		this.setJMenuBar(menuBar);
	}
	
	public void dispatchFrame(){
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public void addPanel(RunningGameView panel) {
		contentPane.remove(gameView);
		gameView = null;
		gameView = panel;
		CardLayout c1 = (CardLayout) contentPane.getLayout();
		contentPane.add(gameView, "Game View");
		c1.show(contentPane, "Game View");
		gameView.requestFocus();
	}

	public RunningGameView getRunningGameView() {
		return gameView;
	}
}