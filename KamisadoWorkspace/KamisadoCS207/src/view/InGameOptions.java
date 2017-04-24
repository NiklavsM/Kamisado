package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;

public class InGameOptions extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton btnQuit;
	private JButton btnSave;
	private JButton btnUndo;
	private JButton btnToggle;
	private JButton btnContinue;
	private JButton btnRematch;
	private JButton btnHint;
	private boolean gridViewOn;
	private String fontStyle = "Sitka Text";
	private Color buttonColor;

	public InGameOptions(Controller controller) {
		setLayout(new FlowLayout());
		this.setSize(400, 300);
		this.setFocusable(false);
		buttonColor = new Color(255, 237, 183);

		setUpButtons(controller);
	}
	
	@Override
	  protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	    ImageIcon woodImage = new ImageIcon(getClass().getResource("/images/backgroundwood.png"));
        g.drawImage(woodImage.getImage(), 0, 0, null);
	}

	public void setUpButtons(Controller controller) {
		btnQuit = new JButton("Quit");
		btnQuit.setForeground(Color.RED);
		btnQuit.setBackground(buttonColor);
		btnQuit.setFont(new Font(fontStyle, Font.BOLD, 20));
		btnQuit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.killGame();
				controller.disconnect();
			}
		});

		add(btnQuit);
		btnQuit.setVisible(true);
		btnQuit.setFocusable(true);

		btnSave = new JButton("Save");
		btnSave.setBackground(buttonColor);
		btnSave.setForeground(Color.BLUE);
		btnSave.setFont(new Font(fontStyle, Font.BOLD, 20));
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.getGame().saveGame();
			}
		});

		add(btnSave);
		displaySave(true);

		btnUndo = new JButton("Undo");
		btnUndo.setBackground(buttonColor);
		btnUndo.setFont(new Font(fontStyle, Font.BOLD, 20));
		btnUndo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.getGame().undo();
			}
		});

		add(btnUndo);
		gridViewOn = false;
		btnToggle = new JButton("Toggle");
		btnToggle.setBackground(buttonColor);
		btnToggle.setFont(new Font(fontStyle, Font.BOLD, 20));
		btnToggle.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				gridViewOn = !gridViewOn;
				toggleGridView(gridViewOn);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				gridViewOn = !gridViewOn;
				toggleGridView(gridViewOn);
			}
		});
		btnToggle.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE && !gridViewOn) {
					gridViewOn = !gridViewOn;
					toggleGridView(gridViewOn);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE && gridViewOn) {
					gridViewOn = !gridViewOn;
					toggleGridView(gridViewOn);
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		add(btnToggle);

		btnContinue = new JButton("Continue");
		btnContinue.setBackground(buttonColor);
		btnContinue.setFont(new Font(fontStyle, Font.BOLD, 20));
		btnContinue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (controller.continueGame() >= 0) {
					btnContinue.setVisible(false);
				}
			}
		});
		add(btnContinue);
		displayContinue(false);

		btnRematch = new JButton("Rematch");
		btnRematch.setBackground(buttonColor);
		btnRematch.setFont(new Font(fontStyle, Font.BOLD, 20));
		btnRematch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.rematch();
				btnRematch.setVisible(false);
			}
		});
		add(btnRematch);
		displayRematch(false);

		btnHint = new JButton("Hint");
		btnHint.setBackground(buttonColor);
		btnHint.setFont(new Font(fontStyle, Font.BOLD, 20));
		btnHint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showHint();
			}
		});
		add(btnHint);
		displayHint(false);
	}

	public void showUndo(boolean undoAvailable) {
		btnUndo.setFocusable(undoAvailable);
		btnUndo.setVisible(undoAvailable);
	}

	public void addToGameLog(String message) {
		((RunningGameView) this.getParent()).addToGameLog(message);
	}

	private void toggleGridView(boolean toggle) {
		((RunningGameView) this.getParent()).toggleGridView(toggle);
	}

	public void displayContinue(boolean b) {
		btnContinue.setFocusable(b);
		btnContinue.setVisible(b);
	}

	public void displayRematch(boolean b) {
		btnRematch.setFocusable(b);
		btnRematch.setVisible(b);
	}

	public void displayHint(boolean b) {
		btnHint.setFocusable(b);
		btnHint.setVisible(b);
	}

	public void displaySave(boolean b) {
		btnSave.setFocusable(b);
		btnSave.setVisible(b);
	}
}