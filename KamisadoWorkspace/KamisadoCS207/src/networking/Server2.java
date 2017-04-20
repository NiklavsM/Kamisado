package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import model.GameDriver;

public class Server2 implements Runnable, Serializable {

	private transient Socket client1;
	private transient Socket client2;
	private transient Socket clientsTurn;
	private transient ObjectOutputStream oout1;
	private transient ObjectOutputStream oout2;
	private transient ObjectInputStream ois1;
	private transient ObjectInputStream ois2;
	private int gameLength;
	private String fillOption = "none";
	private ObjectOutputStream writeToPlayerToMove;
	private ObjectInputStream readFromPlayerToMove;
	private ObjectOutputStream writeToOtherPlayer;
	private ObjectInputStream readFromOtherPlayer;

	/**
	 * Runs the application. Pairs up clients that connect.
	 */

	public Server2(int gameLength) {
		this.gameLength = gameLength;
	}

	@Override
	public void run() {
		try {
			ServerSocket listener = new ServerSocket(8905);
			client1 = listener.accept();

			System.out.println("player 1 has joined");
			client2 = listener.accept();
			clientsTurn = client2;
			
			System.out.println("player 2 has joined");
			oout1 = new ObjectOutputStream(client1.getOutputStream());
			oout2 = new ObjectOutputStream(client2.getOutputStream());
			ois1 = new ObjectInputStream(client1.getInputStream());
			ois2 = new ObjectInputStream(client2.getInputStream());
			writeToPlayerToMove = oout2;
			readFromPlayerToMove = ois2;
			writeToOtherPlayer = oout1;
			readFromOtherPlayer = ois1;
			
			Object j = ois2.readObject();
			System.out.println("Name1 recieved by server");
			if (j instanceof String) {
				oout1.writeObject(j);
				oout1.flush();
				System.out.println("Name1 sent to client");
			}
			oout1.writeObject(gameLength);

			Object p = ois1.readObject();
			System.out.println("Name2 recieved by server");
			if (p instanceof String) {
				oout2.writeObject(p);
				oout2.flush();
				System.out.println("Name2 sent to client");
			}
			oout2.writeObject(gameLength);

			while (true) {
				Object ob;
				if (clientsTurn.equals(client1)) {
					System.out.println("---Client one---");
				}else{
					System.out.println("---Client Two---");
				}
					
				ob = readFromPlayerToMove.readObject();
				System.out.println("OB: [" + ob + "]");
				if (ob instanceof Boolean) {
					flip();
				} else if (ob instanceof String) {
					if (ob.equals("gameOver")) {
						endOfGame();
					} else if (ob.equals("left")) {
						fill("left");
					} else if (ob.equals("right")) {
						fill("right");
					}
				} else if (ob instanceof Integer) {
					System.out.println("Pos been to server");
					writeToOtherPlayer.writeObject(ob);
				}
				writeToPlayerToMove.flush();
				writeToPlayerToMove.reset();
				writeToOtherPlayer.flush();
				writeToOtherPlayer.reset();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public int getFillOption() {
		if (fillOption.equals("left")) {
			return 0;
		} else if (fillOption.equals("right")) {
			return 1;
		} else {
			return -2;
		}
	}

	public void endOfGame() {
		System.out.println("ENDING GAME");
		try {
			Object p1 = ois1.readObject();
			Object p2 = ois2.readObject();

			System.out.println("Client One: " + "[" + p1 + "]");
			System.out.println("Client Two: " + "[" + p2 + "]");
			if (p1.equals(p2)) {
				System.out.println("equal?");
				oout1.writeObject(p1);
				oout2.writeObject(p1);
				oout1.flush();
				oout2.flush();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void fill(String way) {
		try {
			System.out.println("SERVER filling to " + way);
			fillOption = way;
			writeToOtherPlayer.writeObject("fill");
			int i = getFillOption();
			System.out.println("option at server - sending to client: " + i);
			writeToOtherPlayer.writeObject(i);
			fillOption = "none";
			flip();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void flip() {
		System.out.println("flipping playerToMove");
		if (clientsTurn.equals(client1)) {
			clientsTurn = client2;
			writeToPlayerToMove = oout2;
			readFromPlayerToMove = ois2;
			writeToOtherPlayer = oout1;
			readFromOtherPlayer = ois1;
		} else {
			clientsTurn = client1;
			writeToPlayerToMove = oout1;
			readFromPlayerToMove = ois1;
			writeToOtherPlayer = oout2;
			readFromOtherPlayer = ois2;
		}
	}
}