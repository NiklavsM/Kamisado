package networking;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import model.Position;

public class Client {
	
	static int sentence;
	  static int modifiedSentence;
	  static BufferedReader inFromUser;
	  static Socket clientSocket;
	  static DataOutputStream outToServer;
	  static BufferedReader inFromServer;
	
	public Client(){
		inFromUser = null;
		clientSocket = null;
		inFromServer = null;
		outToServer = null;
	}
	
	public static void main(String argv[]) throws Exception{	
		if(setupConnection()){
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromUser = new BufferedReader( new InputStreamReader(System.in));
			Thread inputFromServer = new Thread(new Runnable(){public void run(){readFromServer();}});
		}else{
			System.out.println("try again");
		}
	 }
	
	public static boolean setupConnection(){
		try {
			clientSocket = new Socket("localhost", 6789);
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void closeConnection(){
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readFromServer(){
//		try {
//			modifiedSentence = inFromServer.readLine();
//			System.out.println("FROM SERVER: " + modifiedSentence);
//			modifiedSentence.charAt(0);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}  
	}
	
	public static void sendToServer(String pos){
		try {
//			sentence = inFromUser.readLine();
			outToServer.writeBytes(pos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
