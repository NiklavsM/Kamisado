package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import controller.Controller;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import model.State;
import player.Player;

public class Client extends Player implements Runnable, MyObserver {

	private static final long serialVersionUID = 1L;
	private static int PORT = 8905;
	private transient Socket socket;
	private transient ObjectInputStream ois;
	private transient ObjectOutputStream oout;
	private boolean myTurn;
	private Position previousPos = null;
	private Controller controller;
	private int option = -2;
	private int gameLength;
	private String sendOption = "none";
	private String opponentName;
	private String ip;

	public Client(String team, String playerName, String opponentName, boolean goingFirst, boolean hosting,
			Controller controller, String ip) {
		super(team, playerName, goingFirst, true);
		this.myTurn = goingFirst;
		this.controller = controller;
		this.opponentName = opponentName;
		this.ip = ip;
	}

	public boolean tryConnect() {
		try {
			socket = new Socket(ip, PORT);
			ois = new ObjectInputStream(socket.getInputStream());
			oout = new ObjectOutputStream(socket.getOutputStream());

			oout.writeObject(opponentName);
			oout.flush();

			Object ob = ois.readObject();
			if (ob instanceof String) {
				this.setName((String) ob);
				ob = ois.readObject();
				if (ob instanceof Integer) {
					gameLength = (Integer) ob;
				}
			}
			return true;
		} catch (Throwable e) {
			disconnected();
			//e.printStackTrace();
			return false;
		}
	}

	public int getGameLengthFromServer() {
		return gameLength;
	}

	@Override
	public void run() {

		try {
			while (true) {
				if (!sendOption.equals("none")) {
					oout.writeObject(sendOption);
					oout.flush();
					sendOption = "none";
				}

				Object obj = ois.readObject();
				if (obj instanceof Integer) {
					Object obj2 = ois.readObject();
					if (obj2 instanceof Integer) {
						int x = (Integer) obj;
						int y = (Integer) obj2;
						previousPos = new Position(x, y);
						this.tellAll(previousPos);
					}
				} else if (obj instanceof String) {

					if (obj.equals("continue")) {
						askingThisClientToContinue();
					} else if (obj.equals("rematch")) {
						askingThisClientToRematch();
					} else if (obj.equals("fill")) {
						option = (int) ois.readObject();
					}
				}
			}
		} catch (Throwable e) {
			disconnected();
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if (arg instanceof Position) {
			Position pos = (Position) arg;

			try {
				oout.writeObject(pos.getX());
				oout.writeObject(pos.getY());
				oout.flush();
			} catch (Throwable e) {
				disconnected();
				e.printStackTrace();
			}
		}
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}

	@Override
	public void getMove(State state) {
	}

	@Override
	public int fillHomeRow() {
		try {
			String s = (String) ois.readObject();
			option = (int) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			disconnected();
			e.printStackTrace();
		}
		return option;
	}

	@Override
	public void otherPersonOption(int option) {
		sendOption = "none";
		if (option == 1) {
			sendOption = "right";
		} else if (option == 0) {
			sendOption = "left";
		}
	}

	@Override
	public void setToFirstMove(boolean isGoingFirst) {
		this.setGoingFirst(isGoingFirst);
	}

	private void askingThisClientToContinue() {
		controller.networkContinue();
	}

	private void askingThisClientToRematch() {
		controller.networkRematch();
	}

	public void askToContinue() {
		try {
			oout.writeObject("continue");
			oout.flush();

		} catch (IOException e) {
			disconnected();
			e.printStackTrace();
		}
	}

	public void askToRematch() {
		try {
			oout.writeObject("rematch");
			oout.flush();
		} catch (IOException e) {
			disconnected();
			e.printStackTrace();
		}
	}

	public void disconnected() {
		try {
			if (ois != null) {
				ois.close();
			}
			if (oout != null) {
				oout.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		controller.getMenuFrame().ShowPanel("New Game");
		JOptionPane.showMessageDialog(null, "Connection Lost");
	}
}
