package networking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import model.Position;

public class Server {
	
	private static ServerSocket OpenSocket;
	private static String sentFromUser;
	static Socket clientConnection;
	static BufferedReader inFromClient;
    static DataOutputStream outToClient;
	
	public Server(){
		OpenSocket = null;
		inFromClient = null;
        outToClient = null;
	}
	
	public static void main(String argv[]) throws Exception{ 
       while(true)
       {
    	   OpenSocket = new ServerSocket(6789);
    	   openConnections();
		
		inFromClient = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
		outToClient = new DataOutputStream(clientConnection.getOutputStream());
		Thread sendToServer = new Thread(new Runnable(){public void run(){sendToClient();}});
		System.out.println("Received: " + sentFromUser);
		
       }
    }
	
	public static void openConnections(){
		try {
			clientConnection = OpenSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void closeConnections(){
		try {
			clientConnection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readFromClient(){
		try {
			sentFromUser = inFromClient.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendToClient(){
		try {
			outToClient.writeBytes(sentFromUser);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
