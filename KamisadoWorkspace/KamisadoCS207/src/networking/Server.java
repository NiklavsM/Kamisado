package networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import model.GameDriver;
import model.Position;
import player.GUIPlayer;
import player.Player;

public class Server implements Runnable {

	private transient Socket client1;
	private transient Socket client2;
	private transient ObjectOutputStream oout1;
	private transient ObjectOutputStream oout2;
	private transient ObjectInputStream ois1;
	private transient ObjectInputStream ois2;
	private int gameLength;
	private String fillOption = "none";
	private ObjectOutputStream writeToPlayerToMove;
	private ObjectInputStream readFromPlayerToMove;
	private ObjectOutputStream writeToOtherPlayer;
	private ServerSocket listener;
	private GameDriver game;
	private boolean gameEnded = false;

	public Server(int gameLength) {
		this.gameLength = gameLength;

	}

	@Override
	public void run() {
		try {
			listener = new ServerSocket(8905);
			client1 = listener.accept();

			oout1 = new ObjectOutputStream(client1.getOutputStream());
			ois1 = new ObjectInputStream(client1.getInputStream());
			listener.setSoTimeout(10000);
			client2 = listener.accept();

			listener.setSoTimeout(0);
			oout2 = new ObjectOutputStream(client2.getOutputStream());
			ois2 = new ObjectInputStream(client2.getInputStream());
			writeToPlayerToMove = oout2;
			readFromPlayerToMove = ois2;
			writeToOtherPlayer = oout1;

			Object j = ois2.readObject();
			if (j instanceof String) {
				oout1.writeObject(j);
				oout1.flush();
			}
			oout1.writeObject(gameLength);
			GUIPlayer playerWhite = new GUIPlayer("TeamWhite", (String) j, true, null, true);

			Object p = ois1.readObject();
			if (p instanceof String) {
				oout2.writeObject(p);
				oout2.flush();
			}
			oout2.writeObject(gameLength);

			GUIPlayer playerBlack = new GUIPlayer("TeamBlack", (String) p, false, null, true);
			game = new GameDriver(playerWhite, playerBlack, playerWhite, gameLength, false);
			game.playGame();
			while (true) {
				Object ob;
				if (game.getCurrentState().isGameOver() && !gameEnded) {
					gameEnded = true;
					endOfGame();
				} else {
					ob = readFromPlayerToMove.readObject();
					if (ob instanceof String) {
						if (ob.equals("left")) {
							fill("left");
						} else if (ob.equals("right")) {
							fill("right");
						}
					} else if (ob instanceof Integer) {
						writeToOtherPlayer.writeObject(ob);
						Object obj = readFromPlayerToMove.readObject();
						if (obj instanceof Integer) {
							writeToOtherPlayer.writeObject(obj);
							int x = (Integer) ob;
							int y = (Integer) obj;
							Position previousPos = new Position(x, y);

							game.update(null, previousPos);
						}
					}
					if (game.getCurrentState().getPlayerToMove().getPlayerTeam().equals("TeamBlack")) {
						makeClientsTurn(1);
					} else {
						makeClientsTurn(2);
					}
				}

				writeToPlayerToMove.flush();
				writeToPlayerToMove.reset();
				writeToOtherPlayer.flush();
				writeToOtherPlayer.reset();
			}
		} catch (Throwable e) {
			disconnect();
			return;
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
		try {
			Object p1 = ois1.readObject();
			Object p2 = ois2.readObject();
			if (p1.equals(p2)) {

				oout1.writeObject(p1);
				oout2.writeObject(p1);
				oout1.flush();
				oout2.flush();
				if (p1.equals("rematch")) {
					rematch();
				}
				if (game.getCurrentState().getPlayerToMove().getPlayerTeam().equals("TeamBlack")) {
					makeClientsTurn(1);
				} else {
					makeClientsTurn(2);
				}
			}
		} catch (Throwable e) {
			disconnect();
			e.printStackTrace();
		}
	}

	private void fill(String way) {
		try {
			fillOption = way;
			writeToOtherPlayer.writeObject("fill");
			int i = getFillOption();
			game.getCurrentState().getPlayerBlack().update(null, i);
			game.getCurrentState().getPlayerWhite().update(null, i);
			writeToOtherPlayer.writeObject(i);
			game.nextRound();
			gameEnded = false;

			fillOption = "none";
			if (game.getCurrentState().getPlayerToMove().getPlayerTeam().equals("TeamBlack")) {
				makeClientsTurn(1);
			} else {
				makeClientsTurn(2);
			}
			((GUIPlayer) game.getCurrentState().getPlayerBlack()).setOption(-2);
			((GUIPlayer) game.getCurrentState().getPlayerWhite()).setOption(-2);
		} catch (Throwable e) {
			disconnect();
			e.printStackTrace();
		}
	}

	private void makeClientsTurn(int i) {
		if (i == 2) {
			writeToPlayerToMove = oout2;
			readFromPlayerToMove = ois2;
			writeToOtherPlayer = oout1;
		} else {
			writeToPlayerToMove = oout1;
			readFromPlayerToMove = ois1;
			writeToOtherPlayer = oout2;
		}
	}

	public void rematch() {
		Player PWhite = game.getCurrentState().getPlayerWhite();
		Player PBlack = game.getCurrentState().getPlayerBlack();
		PBlack.setScore(0);
		PWhite.setScore(0);
		game = new GameDriver(PWhite, PBlack, PWhite, gameLength, false);
		PWhite.setToFirstMove(true);
		PBlack.setToFirstMove(false);
		game.playGame();
		gameEnded = false;
	}

	public void disconnect() {
		try {
			game = null;
			if (ois1 != null) {
				ois1.close();
			}
			if(oout1 !=null){
				oout1.close();
			}
			if (ois2 != null) {
				ois2.close();
			}
			if(oout1 !=null){
				oout1.close();
			}
			if (listener != null) {
				listener.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}