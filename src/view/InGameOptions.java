package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;

public class InGameOptions extends JPanel{

	
	private JButton btnQuit;
	private JButton btnSave;
	private JButton btnUndo;
	private JButton btnReset;
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
		btnReset = new JButton("Reset");
	}
	
	public void setUpButtons(Controller controller){
		btnQuit.setForeground(Color.RED);
		btnQuit.setBackground(Color.LIGHT_GRAY);
		btnQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//controller.Quit();
			}
		});
		//btnQuit.setFocusable(false);
		add(btnQuit);
		
		
		btnSave.setBackground(Color.LIGHT_GRAY);
		btnSave.setForeground(Color.BLUE);
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.getGame().saveGame();
			}
		});
		//btnSave.setFocusable(false);
		add(btnSave);
		
		
		btnUndo.setBackground(Color.LIGHT_GRAY);
		btnUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setWinnerLabel("");
				controller.getGame().undo();
			}
		});
		//btnUndo.setFocusable(false);
		add(btnUndo);
		
		btnReset.setBackground(Color.LIGHT_GRAY);
		btnReset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//setWinnerLabel("");
				//controller.getGame().reset();
			}
		});
		//btnUndo.setFocusable(false);
		add(btnReset);
	}
	
	public void showUndo(boolean undoAvailable){
		btnUndo.setVisible(undoAvailable);
	}
	
	public void setWinnerLabel(String message){
		((RunningGameView) this.getParent()).setWinnerLabel(message);
	}
}