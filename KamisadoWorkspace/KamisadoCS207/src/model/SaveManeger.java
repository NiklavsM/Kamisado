package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveManeger {
	String filename = "address.bin";
	public SaveManeger() {

	}
	public void save(State state){
		try {
			ObjectOutputStream stateObj = new ObjectOutputStream(new FileOutputStream(filename));
			stateObj.writeObject(state);
			stateObj.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("No file found");
		} catch (IOException s) {
			s.printStackTrace();
			System.out.println("IO problem1");
		}
		
	}
	public State load(){
		State state = null;
		try {
			ObjectInputStream sss = new ObjectInputStream(new FileInputStream(filename));

				state = (State) sss.readObject();
				
				System.out.println("testState.getPlayerBlack().:  " + state.getPlayerBlack().getPlayerTeam());
				sss.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("No file found");
		} catch (IOException s) {
			s.printStackTrace();
			System.out.println("IO problem2");
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

}
