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
	private GameDriver game;

	/**
	 * Runs the application. Pairs up clients that connect.
	 */

	public Server2() {
	}

	@Override
	public void run() {
		try {
			ServerSocket listener = new ServerSocket(8905);
			client1 = listener.accept();
			clientsTurn = client1;
			System.out.println("player 1 has joined");
			client2 = listener.accept();
			System.out.println("player 2 has joined");
			oout1 = new ObjectOutputStream(client1.getOutputStream());
			oout2 = new ObjectOutputStream(client2.getOutputStream());
			ois1 = new ObjectInputStream(client1.getInputStream());
			ois2 = new ObjectInputStream(client2.getInputStream());

			Object j = ois2.readObject();
			System.out.println("Name1 recieved by server");
			if (j instanceof String) {
				oout1.writeObject(j);
				oout1.flush();
				System.out.println("Name1 sent to client");
			}
			Object p = ois1.readObject();
			System.out.println("Name2 recieved by server");
			if (p instanceof String) {
				oout2.writeObject(p);
				oout2.flush();
				System.out.println("Name2 sent to client");
			}
			// Object i = ois1.readObject();
			// System.out.println("Game recieved by server");
			// if(i instanceof GameDriver){
			// game = (GameDriver)i;
			// oout2.writeObject(game);
			// System.out.println("Game sent to client");
			// oout2.flush();
			// }

			// Object j = ois2.readObject();
			//
			// oout1.writeObject(j);
			// oout2.writeObject(i);
			// oout1.flush();
			// oout2.flush();

			// oout1.writeObject(true);
			// oout2.writeObject(true);
			// oout1.flush();
			// oout2.flush();
			while (true) {
				Object ob;
				if (clientsTurn.equals(client2)) {
					ob = ois1.readObject();
					if (ob instanceof Boolean) {
						clientsTurn = client1;
						System.out.println("flipping turn");
					} else {
						oout2.writeObject(ob);
						oout2.flush();
					}
					System.out.println("updating game from c1");
				} else {
					ob = ois2.readObject();
					if (ob instanceof Boolean) {
						clientsTurn = client2;
						System.out.println("flipping turn");
					} else {
						oout1.writeObject(ob);
						oout1.flush();
						System.out.println("updating game from c2");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}