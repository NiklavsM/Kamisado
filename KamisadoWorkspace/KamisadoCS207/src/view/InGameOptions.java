package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;

public class InGameOptions extends JPanel{

	/**
	 * Create the panel.
	 */
	public InGameOptions(Controller controller) {
		//this.setFocusable(false);
		setBackground(new Color(240, 240, 240));
		setLayout(new BorderLayout(0, 0));
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setForeground(Color.RED);
		btnQuit.setBackground(Color.LIGHT_GRAY);
		btnQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//controller.Quit();
			}
		});
		//btnQuit.setFocusable(false);
		add(btnQuit, BorderLayout.EAST);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBackground(Color.LIGHT_GRAY);
		btnSave.setForeground(Color.BLUE);
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.getGame().saveGame();
			}
		});
		//btnSave.setFocusable(false);
		add(btnSave, BorderLayout.WEST);
		
		JButton btnUndo = new JButton("Undo");
		btnUndo.setBackground(Color.LIGHT_GRAY);
		btnUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//controller.undo();
			}
		});
		//btnUndo.setFocusable(false);
		add(btnUndo, BorderLayout.CENTER);

	}
}