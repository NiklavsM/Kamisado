package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import controller.Controller;
import model.GameDriver;
import model.MyObservable;
import model.MyObserver;
import model.Position;
import model.State;
import player.Player;

public class Client2 extends Player implements Runnable, MyObserver, Serializable {

	private static int PORT = 8905;
	private transient Socket socket;
	private transient ObjectInputStream ois;
	private transient ObjectOutputStream oout;
	private boolean myTurn;
	private Position previousPos = null;
	private boolean connected = false;
	private Controller controller;
	private boolean firstMove = true;
	private int option = -2;
	private int gameLength;
	private String sendOption = "none";

	public Client2(String team, String playerName, String opponentName, boolean goingFirst, boolean hosting,
			Controller controller) {
		super(team, playerName, goingFirst, true);
		this.myTurn = goingFirst;
		this.controller = controller;
		try {
			socket = new Socket("localhost", PORT);
			ois = new ObjectInputStream(socket.getInputStream());
			oout = new ObjectOutputStream(socket.getOutputStream());

			oout.writeObject(opponentName);
			oout.flush();
			connected = true;

			Object ob = ois.readObject();
			System.out.println("Name read");
			if (ob instanceof String) {
				System.out.println("Name Set: " + ob);
				this.setName((String) ob);
				ob = ois.readObject();
				if (ob instanceof Integer) {
					gameLength = (Integer) ob;
				}
			}
		} catch (Throwable e) {
			disconnected();
			e.printStackTrace();
		}
	}

	public int getGameLengthFromServer() {
		return gameLength;
	}

	@Override
	public void run() {

		try {
			System.out.println("started client thread");
			while (true) {
				if (!sendOption.equals("none")) {
					System.out.println("sending options! :" + sendOption);
					oout.writeObject(sendOption);
					oout.flush();
					sendOption = "none";
				}
				System.out.println("waiting for input at client");
				Object obj = ois.readObject();
				System.out.println("got object: [" + obj + "]");
				System.out.println(obj.getClass());
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
					}
				} else if (obj instanceof String) {
					System.out.println("obj: [" + obj + "]");
					if (obj.equals("continue")) {
						askingThisClientToContinue();
					} else if (obj.equals("rematch")) {
						System.out.println("or here?");
						askingThisClientToRematch();
					} else if (obj.equals("fill")) {
						System.out.println("got fill");
						option = (int) ois.readObject();
					}
				}
			}
		} catch (Throwable e) {
			disconnected();
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
//				if (!firstMove && (pos.getY() == 0 || pos.getY() == 7)) {
//					System.out.println("Y: " + pos.getY());
//					oout.writeObject("gameOver");
//					oout.flush();
//				}
			} catch (Throwable e) {
				disconnected();
				e.printStackTrace();
			}
		}
	}

	@Override
	public void TurnEnded(Position pos) {
		//if(pos.getY() > 0 || pos.getY() < 7){
			try {
				System.out.println("sending boolean");
				oout.writeObject(new Boolean(true));
				firstMove = false;
				oout.flush();
			} catch (Throwable e) {
				disconnected();
				e.printStackTrace();
			}
		//}
	}
	
	@Override
	public void gameOver(){
//		System.out.println("was called");
			try {
				oout.writeObject(new String("gameOver"));
				oout.flush();
			} catch (Throwable e) {
				disconnected();
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
		firstMove = true;
		System.out.println("returning before : " + option);
		try {
			String s = (String) ois.readObject();
			option = (int) ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			disconnected();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("returning after : " + option);
		return option;
	}

	@Override
	public void otherPersonOption(int option) {
		firstMove = true;
		sendOption = "none";
		if (option == 1) {
			sendOption = "right";
		} else if (option == 0) {
			sendOption = "left";
		}
		System.out.println("setting option to : " + sendOption);
	}

	@Override
	public void setToFirstMove(boolean isGoingFirst) {
		firstMove = true;
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
			System.out.println("sending continue");
			oout.writeObject("continue");
			oout.flush();

		} catch (IOException e) {
			disconnected();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void askToRematch() {
		try {
			System.out.println("sending rematch");
			oout.writeObject("rematch");
			oout.flush();
		} catch (IOException e) {
			disconnected();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnected(){
//		try {
////			ois.close();
////			oout.close();
////			socket.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		JOptionPane.showMessageDialog(null, "Connection Lost");
		controller.getMenuFrame().ShowPanel("New Game");
	}
}
