package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.GameDriver;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import model.State;
import player.GUIPlayer;
import player.Player;
import view.MenuFrame;

public class Client2 extends Player implements Runnable, MyObserver, Serializable {

	private static int PORT = 8905;
	private transient Socket socket;
	private transient ObjectInputStream ois;
	private transient ObjectOutputStream oout;
	private boolean myTurn;
	private Position previousPos = null;
	private boolean connected = false;

	public Client2(String team, String playerName, String opponentName, boolean goingFirst, boolean hosting) {
		super(team, playerName, goingFirst, true);
		this.myTurn = goingFirst;

		try {
			socket = new Socket("localhost", PORT);
			ois = new ObjectInputStream(socket.getInputStream());
			oout = new ObjectOutputStream(socket.getOutputStream());

			oout.writeObject(opponentName);
			oout.flush();
			connected = true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot connect!");
		}
	}

	public void sendGameToServer(GameDriver game) {
		try {

			oout.writeObject(game);
			System.out.println("Game sent to server");
			oout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getNameFromServer() {
		if(connected == true){
			while (true) {
				Object ob;
				try {
					ob = ois.readObject();
					System.out.println("Name read");
					if (ob instanceof String) {
						System.out.println("Name Set: " + ob);
						this.setName((String) ob);
						break;
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {
		System.out.println("started client thread");
		try {
			while (true) {
				Object obj = ois.readObject();
				if (obj instanceof Integer) {
					System.out.println("read in posX");
					Object obj2 = ois.readObject();
					if (obj2 instanceof Integer) {
						System.out.println("read in posY");
						int x = (Integer) obj;
						int y = (Integer) obj2;
						System.out.println(x);
						System.out.println(y);
						previousPos = new Position(x, y);
						this.tellAll(previousPos);
						return;
					}
				} else if (obj instanceof Boolean) {

				}
				System.out.println(obj.getClass());
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(MyObservable o, Object arg) {
		if (arg instanceof Position) {
			Position pos = (Position) arg;
			System.out.println("sending POSITION to server");
			try {
				oout.writeObject(pos.getX());
				oout.writeObject(pos.getY());
				oout.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void wasValidMove() {
		try {
			System.out.println("sending boolean");
			oout.writeObject(new Boolean(true));
			oout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return 0;
	}

	@Override
	public void setToFirstMove(boolean isGoingFirst) {

	}

}
