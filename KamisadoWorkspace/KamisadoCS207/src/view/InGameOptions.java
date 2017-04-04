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
	private JButton btnReset;
	private JButton btnContinue;
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
		btnContinue = new JButton("Continue");
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
		
		btnContinue.setBackground(Color.LIGHT_GRAY);
		btnContinue.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//setWinnerLabel("");
				
				Object[] options = {"Fill from the left", "Fill from the right"};
				int n = JOptionPane.showOptionDialog(null,"Winner, Please select an option!","Ready for next round!",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[1]);
				if(n == -1){
					
				}else{
					controller.getGame().nextRound(n);
					btnContinue.setVisible(false);
				}
			}
		});
		//btnUndo.setFocusable(false);
		add(btnContinue);
		btnContinue.setVisible(false);
	}
	
	public void showUndo(boolean undoAvailable){
		btnUndo.setVisible(undoAvailable);
	}
	
	public void setWinnerLabel(String message){
		((RunningGameView) this.getParent()).setWinnerLabel(message);
	}

	public void displayContinue() {
		btnContinue.setVisible(true);
	}
}