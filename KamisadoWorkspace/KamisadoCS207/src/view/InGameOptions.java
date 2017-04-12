package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Controller;

public class InGameOptions extends JPanel{

	
	private JButton btnQuit;
	private JButton btnSave;
	private JButton btnUndo;
	private JButton btnToggle;
	private JButton btnContinue;
	private boolean gridViewOn;
	/**
	 * Create the panel.
	 */
	public InGameOptions(Controller controller) {
		//this.setFocusable(false);
		setBackground(new Color(240, 240, 240));
		setLayout(new FlowLayout());
		
		
		initialiseButtons();
		setUpButtons(controller);
		
	}
	
	public void initialiseButtons(){
		btnQuit = new JButton("Quit");
		btnSave = new JButton("Save");
		btnUndo = new JButton("Undo");
		btnToggle = new JButton("Toggle");
		btnContinue = new JButton("Continue");
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
		//btnQuit.setFocusable(false);
		add(btnQuit);
		
		
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
		
		
		btnUndo.setBackground(Color.LIGHT_GRAY);
		btnUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//setWinnerLabel("");
				
				controller.getGame().undo();
			}
		});
		//btnUndo.setFocusable(false);
		add(btnUndo);
		
		gridViewOn = false;
		btnToggle.setBackground(Color.LIGHT_GRAY);
		btnToggle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gridViewOn = !gridViewOn;
				toggleGridView(gridViewOn);
			}
		});
		//btnUndo.setFocusable(false);
		add(btnToggle);
		
		btnContinue.setBackground(Color.LIGHT_GRAY);
		btnContinue.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleGridView(false);
				if(controller.getGame().nextRound() >= 0){
					btnContinue.setVisible(false);
				}
			}
		});
		add(btnContinue);
		btnContinue.setVisible(false);
	}
	
	public void showUndo(boolean undoAvailable){
		btnUndo.setVisible(undoAvailable);
	}
	
	public void addToGameLog(String message){
		((RunningGameView) this.getParent()).addToGameLog(message);
	}
	
	private void toggleGridView(boolean toggle){
		((RunningGameView) this.getParent()).toggleGridView(toggle);
	}

	public void displayContinue(boolean b) {
		btnContinue.setVisible(b);
	}
}