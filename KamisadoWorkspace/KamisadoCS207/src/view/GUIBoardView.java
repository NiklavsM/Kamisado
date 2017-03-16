package view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import controller.Controller;
import model.Board;
import model.MyObservable;
import model.Piece;
import model.Position;

public class GUIBoardView extends JPanel implements MyObservable, KeyListener {

	private final Icon SELECTED = new ImageIcon(getClass().getResource("/images/Selected.png"));
	private final ImageIcon DEFAULT = new ImageIcon(getClass().getResource("/images/default.png"));
	private final ImageIcon GREY = new ImageIcon(getClass().getResource("/images/Grey.png"));
	private JButton[][] buttons;
	private ArrayList<Position> selectedPositions;
	private Controller controller;
	private JButton previousLocation;
	private JButton selected;
	private int currentx;
	private int currenty;
	private Board board;

	public GUIBoardView(Controller controller) {
		board = new Board();
		this.addKeyListener(this);
		this.setFocusable(true);
		this.controller = controller;
		selectedPositions = new ArrayList<>();
		buttons = new JButton[8][8];
		this.setLayout(new GridBagLayout());
		displayBoard();
		setLayout(null);
		currentx = 0;
		currenty = 0;
		selected = buttons[currentx][currenty];
		selected.setSelected(true);
		selected.setBorderPainted(true);
	}

	public ImageIcon imageChooser(Piece piece) {
		ImageIcon returnImage = null;
		for (Piece p : Piece.values()) {
			if (p != null && p.equals(piece)) {
				returnImage = new ImageIcon(getClass().getResource("/images/" + p.toString() + ".png"));
			}
		}
		return returnImage;
	}

	public void pieceMoved(Position start, Position end) {
		// removeSelectable();
		if (previousLocation != null && previousLocation.getIcon().equals(GREY)) {
			previousLocation.setIcon(DEFAULT);
		}
		Icon pieceIcon = buttons[start.getX()][start.getY()].getIcon();
		previousLocation = buttons[start.getX()][start.getY()];
		System.out.println("endx: " + end.getX() + " endy: " + end.getY());
		int endx = end.getX();
		int endy = end.getY();
		System.out.println("endx: " + endx + " endy: " + endy);
		buttons[endx][endy].setIcon(pieceIcon);
		System.out.println(buttons[endx][endy].getIcon());
		previousLocation.setIcon(GREY);
		// buttons[start.getX()][start.getY()].setIcon(pieceIcon);
	}

	public void removeSelectable() {
		System.out.println("removing selected");
		for (Position pos : selectedPositions) {
			if (buttons[pos.getX()][pos.getY()].getIcon().equals(SELECTED)) {
				buttons[pos.getX()][pos.getY()].setIcon(DEFAULT);
			}
		}
	}

	public void displaySelectable(ArrayList<Position> positions) {
		removeSelectable();
		selectedPositions = (ArrayList<Position>) positions.clone();
		for (Position pos : positions) {
			buttons[pos.getX()][pos.getY()].setIcon(SELECTED);
		}
	}

	private void setupButton(int x, int y, JButton newButton) {
		newButton.setBounds(x * 50, (7 - y) * 50, 50, 50);
		newButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				currentx = x;
				currenty = y;
				changedSelected(currentx, currenty);
				tellAll(new Position(x, y));
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

	// private void setUpGridConstraints(int i, int k){
	// gbcon.fill = GridBagConstraints.VERTICAL;
	// gbcon.ipady = 25;
	// gbcon.gridx = 105 * i;
	// gbcon.gridy = 105 * k;
	// gbcon.gridheight = 50;
	// gbcon.gridwidth = 50;
	// }
	public void displayBoard() {
		Border thickBorder = new LineBorder(Color.WHITE, 5);
		System.out.println("displayBoard");

		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x <= 7; x++) {
				buttons[x][y] = new JButton();
				buttons[x][y].setBackground(board.getBoardColours()[x][y]);
				ImageIcon image = imageChooser(board.findPieceAtLoc(x, y));
				if (image != null) {
					buttons[x][y].setIcon(image);
					// System.out.println(buttons[x][y].getIcon().toString());
				} else {
					buttons[x][y].setIcon(DEFAULT);
				}
				buttons[x][y].setBorder(thickBorder);
				buttons[x][y].setBorderPainted(false);
				buttons[x][y].setFocusable(false);
				setupButton(x, y, buttons[x][y]);
				// setUpGridConstraints(x,y);
				this.add(buttons[x][y]);
			}
		}
	}
	
	public void redrawBoard(Board board) {
		this.board = board;
		Border thickBorder = new LineBorder(Color.WHITE, 5);
		System.out.println("displayBoard");

		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x <= 7; x++) {
				ImageIcon image = imageChooser(board.findPieceAtLoc(x, y));
				if (image != null) {
					buttons[x][y].setIcon(image);
					// System.out.println(buttons[x][y].getIcon().toString());
				} else {
					buttons[x][y].setIcon(DEFAULT);
				}
				buttons[x][y].setBorder(thickBorder);
				buttons[x][y].setBorderPainted(false);
				buttons[x][y].setFocusable(false);
				//setupButton(x, y, buttons[x][y]);
				// setUpGridConstraints(x,y);
				//this.add(buttons[x][y], gbcon);
			}
		}
	}

	public JButton getButton(int x, int y) {
		return buttons[x][y];
	}

	public void disableButtons() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				buttons[i][j].setEnabled(false);
			}
		}
	}
	
	private void changedSelected(int currentx, int currenty) {
		selected.setBorderPainted(false);
		selected = buttons[currentx][currenty];
		selected.setSelected(true);
		selected.setBorderPainted(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("got here");
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (currenty < 7) {
				changedSelected(currentx, ++currenty);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (currenty > 0) {
				changedSelected(currentx, --currenty);
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (currentx < 7) {
				changedSelected(++currentx, currenty);
			}
			break;
		case KeyEvent.VK_LEFT:
			if (currentx > 0) {
				changedSelected(--currentx, currenty);
			}
			break;
		case KeyEvent.VK_ENTER:
			tellAll(new Position(currentx, currenty));
			break;
		default:
			System.out.println("Not arrow");
			break;
		}
	}

	

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	// public Position respondToClick() {
	// if(selectedPosition == null){
	// return new Position(0,0);
	// }
	// return selectedPosition;
	// }
}
