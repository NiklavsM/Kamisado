package view;

import java.awt.BorderLayout;
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
import model.Move;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import player.Player;

public class RunningGameView extends JFrame implements MyObserver{

	private JPanel contentPane;
	private JButton selected;
	private GUIBoardView gameBoard;
	private Controller controller;
        private int currentx;
        private int currenty;


	
	public RunningGameView(Controller newController) {
		controller = newController;
                gameBoard = new GUIBoardView(newController);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 522, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(gameBoard);
                currentx = 0;
                currenty = 0;
                selected = gameBoard.getButton(currentx,currenty);
                selected.setSelected(true);
                selected.setBorderPainted(true);
		gameBoard.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
                            switch (e.getKeyCode()) {
                                case KeyEvent.VK_UP:
                                    if(currenty < 7){
                                        selected.setBorderPainted(false);
                                        selected = gameBoard.getButton(currentx, currenty++);
                                        selected.setSelected(true);
                                        selected.setBorderPainted(true);
                                    }
                                    break;
                                case KeyEvent.VK_DOWN:
                                    if(currenty > 0){
                                        selected.setBorderPainted(false);
                                        selected = gameBoard.getButton(currentx, currenty--);
                                        selected.setSelected(true);
                                        selected.setBorderPainted(true);
                                    }
                                    break;
                                case KeyEvent.VK_RIGHT:
                                    if(currentx < 7){
                                        selected.setBorderPainted(false);
                                        selected = gameBoard.getButton(currentx++, currenty); 
                                        selected.setSelected(true);
                                        selected.setBorderPainted(true);
                                    }
                                    break;
                                case KeyEvent.VK_LEFT:
                                    if(currentx > 0){
                                        selected.setBorderPainted(false);
                                        selected = gameBoard.getButton(currentx--, currenty);
                                        selected.setSelected(true);
                                        selected.setBorderPainted(true);
                                    }
                                    break;
                                case KeyEvent.VK_ENTER:
                                    selected.doClick();
                                    break;
                                default:
                                    System.out.println("Not arrow");
                                    break;
                            }
			}
		});
		setContentPane(contentPane);
	}
	
	public GUIBoardView getGameBoard(){
		return gameBoard;
	}

        @Override
	public void update(MyObservable o, Object arg) {
		//System.out.println("got here1");
		if(arg instanceof ArrayList<?>){
			//System.out.println("have been updated");
			gameBoard.displaySelectable((ArrayList<Position>) arg);
		}else if(arg instanceof Move){
			System.out.println("got Move");
			gameBoard.removeSelectable();
			//System.out.println("endx: " + ((Move) arg).getEndPos().getX() + " endy: "+ ((Move) arg).getEndPos().getY());
			gameBoard.pieceMoved(((Move) arg).getStartPos(), ((Move) arg).getEndPos());			
		}else if(arg instanceof String){
                    switch ((String)arg) {
                        case "White":
                            System.out.println("White Wins!");
                            break;
                        case "Black":
                            System.out.println("Black Wins!");
                            break;
                        case "Draw":
                            System.out.println("Draw!");
                    }
                        gameBoard.disableButtons();
                }
	}

    public void displaycSelectable(ArrayList<Position> validMoves) {
        gameBoard.removeSelectable();
        gameBoard.displaySelectable(validMoves);
    }

//    public Position waitForClick() {
//        System.out.println("waiting for click from gameBoard");
//        return gameBoard.respondToClick();
//    }

    public void addObserver(Player player) {
        gameBoard.addObserver((MyObserver)player);
    }
}
