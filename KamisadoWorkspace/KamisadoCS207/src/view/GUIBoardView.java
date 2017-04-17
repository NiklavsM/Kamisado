package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
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
import model.GeneralSettings;
import model.GeneralSettingsManager;
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
	private JButton previousLocation = null;
	private JButton selected;
	private int currentx;
	private int currenty;
	private Board board;
	ArrayList<MyObserver> observers = new ArrayList<>();
	private JPanel glassPane;
	private GeneralSettingsManager manager;
	private GeneralSettings settings;
	private Controller controller;

	public GUIBoardView(Controller controller) {
		this.controller = controller;
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
		if (piece == null) {
			return null;
		}
		manager = new GeneralSettingsManager();
		settings = manager.getGeneralSettings();
		BufferedImage returnImage = null;
		BufferedImage combinedImage = null;
		BufferedImage pieceLevel = null;
		BufferedImage colourImage = null;
	        
		int w = 30;
        int h = 31;
        Color pieceColour = piece.getPieceColour();
		colourImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = colourImage.createGraphics();
		if(settings.getPieceImageStyle().equals("pieceStyleOne")){
			g2d.setColor(pieceColour);
			g2d.fillRect(0, 0, w, h);
		}else{
			int R = pieceColour.getRed();
			int G = pieceColour.getGreen();
			int B = pieceColour.getBlue();
			g2d.setColor(pieceColour);
			g2d.fillRect(0, 0, w, 14);
			g2d.setColor(colourChanger(R,G,B, true));
			g2d.fillRect(0, 14, 15, 18);
			g2d.setColor(colourChanger(R,G,B, false));
			g2d.fillRect(15, 14, 15, 18);
		}
		g2d.dispose();
		
		try {
			returnImage = ImageIO.read(getClass()
					.getResource("/"+ settings.getPieceImageStyle()+"/" + piece.getTeam() + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		combinedImage = new BufferedImage(returnImage.getWidth(), returnImage.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = combinedImage.createGraphics();
		g.drawImage(colourImage, 10, 7, null);
		g.drawImage(returnImage, 0, 0, null);
		if(!piece.getPieceType().equals(PieceType.Standard)){
			try {
				pieceLevel = ImageIO.read(getClass().getResource("/images/" + piece.getTeam() + piece.getPieceType().toString() + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(pieceLevel, 0, 0, null);
		}
		g.dispose();
		return combinedImage;
	}

	private Color colourChanger(int R, int G, int B, boolean brighter){
		int increment = 30;
		if(brighter){
			increment *= -1;
		}
		if(R + increment <= 255 && R + increment >=0 ){
			R += increment;
		}else if(G + increment <= 255 && R + increment >= 0){
			G += increment;
		}else if(B + increment <= 255 && R + increment >= 0){
			B += increment;
		}else{
			increment *= -4;
			if(R + increment <= 255 && R + increment >=0 ){
				R += increment;
			}else if(G + increment <= 255 && R + increment >= 0){
				G += increment;
			}else if(B + increment <= 255 && R + increment >= 0){
				B += increment;
			}
		}
		return new Color(R,G,B);
	}
	public void removeSelectable() {
		glassPane.removeAll();
		glassPane.repaint();
	}

	private void setupButton(int x, int y, JButton newButton) {
		newButton.setBounds(x * 70, (7 - y) * 70, 70, 70);
		newButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentx = x;
				currenty = y;
				changedSelected(currentx, currenty);
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
					if(previousLocation != null && previousLocation.equals(buttons[x][y])){
						previousLocation = null;
					}
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

	public void changedSelected(int currentx, int currenty) {
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
	
	public void showPreviousLocation(Position pos){
		if(previousLocation != null){
			previousLocation.setIcon(DEFAULT);
			previousLocation.setDisabledIcon(DEFAULT);
		}
		if(controller.getGame().getCurrentState().getPieces()[pos.getX()][pos.getY()] == null){
			previousLocation = buttons[pos.getX()][pos.getY()];
			previousLocation.setIcon(GREY);
			previousLocation.setDisabledIcon(GREY);
		}
	}
}
