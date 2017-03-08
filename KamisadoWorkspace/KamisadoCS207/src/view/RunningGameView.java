package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Position;

public class RunningGameView extends JFrame implements Observer{

	private JPanel contentPane;
	private JButton selected;
	private GUIBoardView gameBoard;
	private Controller controller;


	/**
	 * Create the frame.
	 */
	public RunningGameView(Controller newController) {
		controller = newController;
		gameBoard = new GUIBoardView(newController);	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 522, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(gameBoard);
		contentPane.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP){
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		setContentPane(contentPane);
	}
	
	public GUIBoardView getGameBoard(){
		return gameBoard;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("have been updated");
		gameBoard.displaySelectable((ArrayList<Position>) arg);
	}

}
