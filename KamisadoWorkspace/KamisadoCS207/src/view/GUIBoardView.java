package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import controller.Controller;
import model.Board;
import model.MyObservable;
import model.MyObserver;
import model.Piece;
import model.PieceType;
import model.Position;

public class GUIBoardView extends JPanel implements MyObservable, KeyListener {

	private final ImageIcon DEFAULT = new ImageIcon(getClass().getResource("/images/default.png"));
	private final ImageIcon GREY = new ImageIcon(getClass().getResource("/images/Grey.png"));
	private JButton[][] buttons;
	private ArrayList<Position> selectedPositions;
	private JButton previousLocation;
	private JButton selected;
	private int currentx;
	private int currenty;
	private Board board;
	ArrayList<MyObserver> observers = new ArrayList<>();
	private JPanel glassPane;

	public GUIBoardView(Controller controller) {
		board = new Board(false);
		selectedPositions = new ArrayList<>();
		buttons = new JButton[8][8];

		this.addKeyListener(this);
		this.setFocusable(true);
		this.setLayout(null);

		displayInitialBoard();

		currentx = 0;
		currenty = 0;
		selected = buttons[currentx][currenty];
		selected.setSelected(true);
		selected.setBorderPainted(true);
	}

	public BufferedImage imageChooser(Piece piece) {
		BufferedImage returnImage = null;
		BufferedImage combinedImage = null;
		BufferedImage pieceLevel = null;

		if (piece == null) {
			return null;
		}
		try {
			returnImage = ImageIO.read(getClass()
					.getResource("/images/" + piece.getTeam() + board.getColourName(piece.getColour()) + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (piece.getPieceType().equals(PieceType.Standard)) {
			return returnImage;
		}
		try {
			pieceLevel = ImageIO.read(getClass().getResource("/images/" + piece.getPieceType().toString() + ".png"));
			combinedImage = new BufferedImage(returnImage.getWidth(), returnImage.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Graphics2D g = combinedImage.createGraphics();
		g.drawImage(returnImage, 0, 0, null);
		g.drawImage(pieceLevel, 0, 0, null);
		g.dispose();
		return combinedImage;
	}

	// public void pieceMoved(Position start, Position end) {
	// if (previousLocation != null) {
	// // removeSelectable();
	// if (previousLocation != null && previousLocation.getIcon().equals(GREY))
	// {
	// previousLocation.setIcon(DEFAULT);
	// }
	// Icon pieceIcon = buttons[start.getX()][start.getY()].getIcon();
	// previousLocation = buttons[start.getX()][start.getY()];
	// buttons[end.getX()][end.getY()].setIcon(pieceIcon);
	// }
	// }

	public void removeSelectable() {
		// for (Position pos : selectedPositions) {
		// if (buttons[pos.getX()][pos.getY()].getIcon().equals(SELECTED)) {
		// buttons[pos.getX()][pos.getY()].setIcon(DEFAULT);
		// }
		// }
		glassPane.removeAll();
		glassPane.repaint();
	}

	// public void displaySelectable(ArrayList<Position> positions) {
	// removeSelectable();
	// selectedPositions = (ArrayList<Position>) positions.clone();
	// for (Position pos : positions) {
	// //buttons[pos.getX()][pos.getY()].setIcon(SELECTED);
	// JLabel label = new JLabel();
	// label.setIcon(SELECTED);
	// label.setBounds((pos.getX() * 70) + 5, ((7 - pos.getY()) * 70) + 62, 70,
	// 70);
	// glassPane.add(label);
	// glassPane.repaint();
	// }
	// }

	private void setupButton(int x, int y, JButton newButton) {
		newButton.setBounds(x * 70, (7 - y) * 70, 70, 70);
		newButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentx = x;
				currenty = y;
				changedSelected(currentx, currenty);
				// int i = 1;
				// for (MyObserver obs : getObservers()) {
				// //System.out.println(obs.getClass().toString());
				// if (obs instanceof GameDriver) {
				// // System.out.println("game " + i);
				// }
				// i++;
				// }
				tellAll(new Position(x, y));
			}

		});
	}

	public void displayInitialBoard() {
		Border thickBorder = new LineBorder(Color.WHITE, 5);

		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x <= 7; x++) {
				buttons[x][y] = new JButton();
				buttons[x][y].setBackground(board.getBoardColours()[x][y]);
				BufferedImage image = imageChooser(board.findPieceAtLoc(x, y));
				if (image != null) {
					buttons[x][y].setIcon(new ImageIcon(image));
					buttons[x][y].setDisabledIcon(new ImageIcon(image));
				} else {
					buttons[x][y].setIcon(DEFAULT);
					buttons[x][y].setDisabledIcon(DEFAULT);
				}
				buttons[x][y].setBorder(thickBorder);
				buttons[x][y].setBorderPainted(false);
				buttons[x][y].setFocusable(false);
				setupButton(x, y, buttons[x][y]);

				this.add(buttons[x][y]);
			}
		}
	}

	public void redrawBoard(Board board) {
		this.board = board;

		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x <= 7; x++) {
				buttons[x][y].setBackground(board.getBoardColours()[x][y]);
				BufferedImage image = imageChooser(board.findPieceAtLoc(x, y));
				if (image != null) {
					buttons[x][y].setIcon(new ImageIcon(image));
					buttons[x][y].setDisabledIcon(new ImageIcon(image));
				} else {
					buttons[x][y].setIcon(DEFAULT);
					buttons[x][y].setDisabledIcon(DEFAULT);
				}
				buttons[x][y].setEnabled(true);
			}
		}
		removeSelectable();
	}

	public JButton getButton(int x, int y) {
		return buttons[x][y];
	}

	public void setButtonsEnabled(boolean enabled) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				buttons[i][j].setEnabled(enabled);
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
		case KeyEvent.VK_SPACE:
			buttons[currentx][currenty].doClick();
			break;
		default:
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

	@Override
	public void tellAll(Object arg) {
		for (MyObserver obs : observers) {
			obs.update(this, arg);
		}
	}

	@Override
	public void addObserver(MyObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(MyObserver o) {
		if (observers.contains(o)) {
			observers.remove(o);
		}
	}

	@Override
	public ArrayList<MyObserver> getObservers() {
		return observers;
	}

	public void setGlassPane(Component glassPane) {
		this.glassPane = (JPanel) glassPane;
		this.glassPane.setVisible(true);
		this.glassPane.setLayout(this.getLayout());
	}

	public void setButtonsClickable(Boolean arg) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				buttons[i][j].setEnabled(arg);
			}
		}
	}

	public void showHint(Position endPos) {
		currentx = endPos.getX();
		currenty = endPos.getY();
		changedSelected(currentx, currenty);
	}
}
