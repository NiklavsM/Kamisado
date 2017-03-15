package view;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
import model.State;
import player.GUIPlayer;
import player.Player;

public class MenuFrame extends JFrame {

	private JPanel homePanel;
	private JPanel contentPane;
	private GameOptionsPanel options;
	private StatPanel statPanel;
	private LoadGamePanel loadPanel;
	private RunningGameView gameView;

	private JMenuBar menuBar;
	private JMenu main;
	private JMenuItem home;
	private JMenuItem newgame;
	private JMenuItem loadgame;
	private JMenuItem stats;
	private JMenuItem exit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuFrame frame = new MenuFrame(new Controller());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MenuFrame(Controller controller) {
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
		c1.show(contentPane, "Home");
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 500, 500);
	}

	public void menuBar() {
		menuBar = new JMenuBar();
		menuBar.setFocusable(false);
		main = new JMenu("Menu");

		home = new JMenuItem("Home");
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

		newgame = new JMenuItem("New Game");
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

		loadgame = new JMenuItem("Load Game");
		loadgame.setMnemonic(KeyEvent.VK_L);
		loadgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));

		loadgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CardLayout c1 = (CardLayout) contentPane.getLayout();
				c1.show(contentPane, "Load Game");
				loadPanel.requestFocus();
			}
		});

		stats = new JMenuItem("Stats");
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

		exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
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

	public void addPanel(RunningGameView panel) {
		contentPane.remove(gameView);
		gameView = panel;
		// gameView.requestFocus();
		CardLayout c1 = (CardLayout) contentPane.getLayout();
		contentPane.add(gameView, "Game View");
		c1.show(contentPane, "Game View");
		gameView.requestFocus();
	}

	public RunningGameView getRunningGameView() {
		return gameView;
	}
}