package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;
import model.Piece;
import model.Position;

public class GUIBoardView extends JPanel implements BoardView{

	private final Icon SELECTED = new ImageIcon(getClass().getResource("/images/Selected.png"));
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
		System.out.println("clearing");
		for(Position pos:selectedPositions){
			buttons[pos.getX()][pos.getY()].setIcon(DEFAULT);
		}
		System.out.println("displaying");
		selectedPositions = (ArrayList<Position>) positions.clone();
		for(Position pos:positions){
			buttons[pos.getX()][pos.getY()].setIcon(SELECTED);
			buttons[pos.getX()][pos.getY()].repaint();
		}
		this.repaint();
	}
	
	private void setupButton(int x, int y, JButton newButton){
		//newButton.add(new JLabel("      "));
		newButton.setBounds(x*50, (7-y)*50, 50, 50);
		//newButton.changeImage();
		
		newButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
						controller.buttonClicked(x, y);		
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
	//private void setUpGridConstraints(int i, int k){
//		gbcon.fill = GridBagConstraints.VERTICAL;
//		gbcon.ipady = 25;
//		gbcon.gridx = 105 * i;
//		gbcon.gridy = 105 * k;
//		gbcon.gridheight = 50;
//		gbcon.gridwidth = 50;
	//}

	@Override
	public void displayBoard() {
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x <= 7; x++) {
				buttons[x][y] = new JButton();
				buttons[x][y].setBackground(board.getBoardColours()[x][y]);
				ImageIcon image = imageChooser(board.findPieceAtLoc(x, y));
				if(image != null){
					buttons[x][y].setIcon(image);
					//System.out.println(buttons[x][y].getIcon().toString());
				}
				setupButton(x,y,buttons[x][y]);
				//setUpGridConstraints(x,y);
				this.add(buttons[x][y],gbcon);
			}
		}
	}
}
