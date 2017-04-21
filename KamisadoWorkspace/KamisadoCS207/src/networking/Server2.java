package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import model.GameDriver;
import model.Position;
import player.GUIPlayer;
import player.Player;

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
	private ServerSocket listener;
	private GameDriver game;
	private boolean gameEnded = false;

	/**
	 * Runs the application. Pairs up clients that connect.
	 */

	public Server2(int gameLength) {
		this.gameLength = gameLength;
	}

	@Override
	public void run() {
		try {
			listener = new ServerSocket(8905);
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
			System.out.println("Name1 recieved by server " + j);
			if (j instanceof String) {
				oout1.writeObject(j);
				oout1.flush();
				System.out.println("Name1 sent to client");
			}
			oout1.writeObject(gameLength);
			GUIPlayer playerWhite = new GUIPlayer("TeamWhite", (String) j, true, null, true);
			
			
			Object p = ois1.readObject();
			System.out.println("Name2 recieved by server "  + p);
			if (p instanceof String) {
				oout2.writeObject(p);
				oout2.flush();
				System.out.println("Name2 sent to client");
			}
			oout2.writeObject(gameLength);
		
			GUIPlayer playerBlack = new GUIPlayer("TeamBlack",(String) p, false, null, true);
			game = new GameDriver(playerWhite, playerBlack, playerWhite, gameLength, false);
			game.playGame();
			while (true) {
				Object ob;
				
				System.out.println("game Over? : " + gameEnded);
				System.out.println("current State game over? = " + game.getCurrentState().isGameOver());
				if(game.getCurrentState().isGameOver() && !gameEnded){
					gameEnded = true;
					endOfGame();
				}else{
					
					ob = readFromPlayerToMove.readObject();
					System.out.println("OB: [" + ob + "]");
						if (ob instanceof String) {
							System.out.println("SERVER HAS : " + ob);
							if (ob.equals("left")) {
								fill("left");
							} else if (ob.equals("right")) {
								fill("right");
						}
					} else if (ob instanceof Integer) {
						System.out.println("Pos been to server");
						writeToOtherPlayer.writeObject(ob);
						Object obj = readFromPlayerToMove.readObject();
						if(obj instanceof Integer){
							writeToOtherPlayer.writeObject(obj);
							int x = (Integer) ob;
							int y = (Integer) obj;
							System.out.println(x);
							System.out.println(y);
							
							Position previousPos = new Position(x, y);
							
							game.update(null, previousPos);
						}
					}
					if(game.getCurrentState().getPlayerToMove().getPlayerTeam().equals("TeamBlack")){
						System.out.println("---Client one---");
						makeClientsTurn(1);
					}else {
						System.out.println("---Client Two---");
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
			System.out.println("Client One: " + "[" + p1 + "]");
			Object p2 = ois2.readObject();
			System.out.println("Client Two: " + "[" + p2 + "]");
			if (p1.equals(p2)) {
				
				System.out.println("equal?");
				oout1.writeObject(p1);
				oout2.writeObject(p1);
				oout1.flush();
				oout2.flush();
				if(p1.equals("rematch")){
					rematch();
				}
				if(game.getCurrentState().getPlayerToMove().getPlayerTeam().equals("TeamBlack")){
					//System.out.println("---Client one---");
					makeClientsTurn(1);
				}else {
					//System.out.println("---Client Two---");
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
			System.out.println("SERVER filling to " + way);
			fillOption = way;
			writeToOtherPlayer.writeObject("fill");
			int i = getFillOption();
			System.out.println("option at server - sending to client: " + i);
			//System.out.println("sending pos to gui : " + i);
			//game.getCurrentState().getPlayerToMove().update(null, i);
			game.getCurrentState().getPlayerBlack().update(null,i);
			game.getCurrentState().getPlayerWhite().update(null,i);
//			System.out.println("black fill home row : " + game.getCurrentState().getPlayerBlack().fillHomeRow());
//			System.out.println("white fill home row : " + game.getCurrentState().getPlayerWhite().fillHomeRow());
			writeToOtherPlayer.writeObject(i);
			game.nextRound();
			gameEnded = false;
			
			fillOption = "none";
			if(game.getCurrentState().getPlayerToMove().getPlayerTeam().equals("TeamBlack")){
				//System.out.println("---Client one---");
				makeClientsTurn(1);
			}else {
				//System.out.println("---Client Two---");
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
		//System.out.println("flipping playerToMove");
		if (i == 2) {
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
	
	public void rematch(){
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
	
	private void disconnect(){
		try {
			ois1.close();
			ois2.close();
			oout1.close();
			oout2.close();
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}