package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;

public class InGameOptions extends JPanel{

	private static final long serialVersionUID = 1L;
	private JButton btnQuit;
	private JButton btnSave;
	private JButton btnUndo;
	private JButton btnToggle;
	private JButton btnContinue;
	private JButton btnRematch;
	private JButton btnHint;
	private boolean gridViewOn;
	/**
	 * Create the panel.
	 */
	public InGameOptions(Controller controller) {
		//this.setFocusable(false);
		setBackground(new Color(240, 240, 240));
		setLayout(new FlowLayout());
		this.setSize(400, 300);
		this.setFocusable(false);
//		this.grabFocus();
		initialiseButtons();
		setUpButtons(controller);
		
	}
	
	public void initialiseButtons(){
		btnQuit = new JButton("Quit");
		btnSave = new JButton("Save");
		btnUndo = new JButton("Undo");
		btnToggle = new JButton("Toggle");
		btnContinue = new JButton("Continue");
		btnRematch = new JButton("Rematch");
		btnHint = new JButton("Hint");
	}
	
	public void setUpButtons(Controller controller){
		btnQuit.setForeground(Color.RED);
		btnQuit.setBackground(Color.LIGHT_GRAY);
		btnQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleGridView(false);
				controller.getMenuFrame().ShowPanel("New Game");
			}
		});
		//btnQuit.setFocusable(true);
		add(btnQuit);
		btnQuit.setVisible(true);
		btnQuit.setFocusable(true);
		
		btnSave.setBackground(Color.LIGHT_GRAY);
		btnSave.setForeground(Color.BLUE);
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleGridView(false);
				controller.getGame().saveGame();
			}
		});
		//btnSave.setFocusable(false);
		add(btnSave);
		displaySave(true);
		
		btnUndo.setBackground(Color.LIGHT_GRAY);
		btnUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.getGame().undo();
			}
		});
		//btnUndo.setFocusable(false);
		add(btnUndo);
		gridViewOn = false;
		btnToggle.setBackground(Color.LIGHT_GRAY);
		btnToggle.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
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
		btnToggle.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_SPACE && !gridViewOn){
					gridViewOn = !gridViewOn;
					toggleGridView(gridViewOn);
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE && gridViewOn){
					gridViewOn = !gridViewOn;
					toggleGridView(gridViewOn);
				}
				
			}
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		//btnUndo.setFocusable(false);
		add(btnToggle);
		
		btnContinue.setBackground(Color.LIGHT_GRAY);
		btnContinue.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleGridView(false);
				if(controller.continueGame() >= 0){
					btnContinue.setVisible(false);
				}
			}
		});
		add(btnContinue);
		displayContinue(false);
		//btnContinue.setVisible(false);
		
		
		btnRematch.setBackground(Color.LIGHT_GRAY);
		btnRematch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleGridView(false);
				controller.rematch();
				btnRematch.setVisible(false);
			}
		});
		add(btnRematch);
		displayRematch(false);
		//btnRematch.setVisible(false);
		
		btnHint.setBackground(Color.LIGHT_GRAY);
		btnHint.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleGridView(false);
				controller.showHint();
			}
		});
		add(btnHint);
		displayHint(false);
		//btnHint.setVisible(false);
	}
	
	public void showUndo(boolean undoAvailable){
		btnUndo.setVisible(undoAvailable);
		btnUndo.setFocusable(undoAvailable);
	}
	
	public void addToGameLog(String message){
		((RunningGameView) this.getParent()).addToGameLog(message);
	}
	
	private void toggleGridView(boolean toggle){
		((RunningGameView) this.getParent()).toggleGridView(toggle);
	}

	public void displayContinue(boolean b) {
		btnContinue.setVisible(b);
		btnContinue.setFocusable(b);
	}

	public void displayRematch(boolean b) {
		btnRematch.setVisible(b);
		btnRematch.setFocusable(b);
	}
	
	public void displayHint(boolean b){
		btnHint.setVisible(b);
		btnHint.setFocusable(b);
	}

	public void displaySave(boolean b) {
		btnSave.setVisible(b);
		btnSave.setFocusable(b);
	}
}