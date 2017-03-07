package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;
import model.Piece;
import model.Position;

public class GUIBoardView extends JPanel implements BoardView, Observer{

	private final ImageIcon SELECTED = new ImageIcon(getClass().getResource("/images/Selected.png"));
	private final ImageIcon DEFAULT = new ImageIcon(getClass().getResource("/images/default.png"));
	private JButton[][] buttons;
	private GridBagConstraints gbcon;
	private ArrayList<Position> selectedPositions;
	private Controller controller;
	/**
	 * Create the panel.
	 */
	public GUIBoardView(Controller controller) {
		this.controller = controller;
		selectedPositions = new ArrayList<>();
		buttons = new JButton[8][8];
		gbcon = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		
		setLayout(null);
		displayBoard();
	}
	
	public ImageIcon imageChooser(Piece piece){
		ImageIcon returnImage = null;
		for(Piece p : Piece.values()){
			if(p != null && p.equals(piece)){
				//System.out.println(getClass().getResource("/images/"+ p.toString() + ".png"));
				returnImage = new ImageIcon(getClass().getResource("/images/"+ p.toString() + ".png")); 
				
			}
		}
		return returnImage;
	}
	
	public void displaySelectable(ArrayList<Position> positions){
		for(Position pos:selectedPositions){
			buttons[pos.getX()][pos.getY()].setIcon(DEFAULT);
		}
		ImageIcon returnImage = null;
		selectedPositions = positions;
		for(Position pos:positions){
			System.out.println("selected");
			returnImage = new ImageIcon(getClass().getResource("/images/TeamBlack-Cyan.png")); 
			buttons[pos.getX()][pos.getY()].setIcon(returnImage);
			System.out.println(buttons[pos.getX()][pos.getY()].getIcon().toString());
			buttons[pos.getX()][pos.getY()].repaint();
			buttons[pos.getX()][pos.getY()].revalidate();
		}
		this.repaint();
		this.revalidate();
	}
	
	private void setupButton(int x, int y, JButton newButton){
		//newButton.add(new JLabel("      "));
		newButton.setBounds(x*50, y*50, 50, 50);
		//newButton.changeImage();
		
		newButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
						controller.buttonClicked(x, 7-y);		
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	private void setUpGridConstraints(int i, int k){
		gbcon.fill = GridBagConstraints.VERTICAL;
		gbcon.ipady = 25;
		gbcon.gridx = 105 * i;
		gbcon.gridy = 105 * k;
		gbcon.gridheight = 50;
		gbcon.gridwidth = 50;
	}

	@Override
	public void displayBoard() {
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				buttons[i][k] = new JButton();
				buttons[i][k].setBackground(board.getBoardColours()[7-i][k]);
				ImageIcon image = imageChooser(board.findPieceAtLoc(i, k));
				if(image != null){
					buttons[i][k].setIcon(image);
					System.out.println(buttons[i][k].getIcon().toString());
				}
				setupButton(i,k,buttons[i][k]);
				setUpGridConstraints(i,k);
				this.add(buttons[i][k],gbcon);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("have been updated");
		displaySelectable((ArrayList<Position>) arg);
	}

}
