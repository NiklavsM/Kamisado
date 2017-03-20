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

	/**
	 * Create the panel.
	 */
	public InGameOptions(Controller controller) {
		//this.setFocusable(false);
		setBackground(new Color(240, 240, 240));
		setLayout(new FlowLayout());
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setForeground(Color.RED);
		btnQuit.setBackground(Color.LIGHT_GRAY);
		btnQuit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.Quit();
			}
		});
		//btnQuit.setFocusable(false);
		add(btnQuit);
		
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
		add(btnSave);
		
		JButton btnUndo = new JButton("Undo");
		btnUndo.setBackground(Color.LIGHT_GRAY);
		btnUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.getGame().undo();
			}
		});
		//btnUndo.setFocusable(false);
		add(btnUndo);
		JButton btnReset = new JButton("Reset");
		btnReset.setBackground(Color.LIGHT_GRAY);
		btnReset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.getGame().reset();
			}
		});
		//btnUndo.setFocusable(false);
		add(btnReset);

	}
}