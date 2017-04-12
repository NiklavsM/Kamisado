package model;

import java.io.Serializable;

public class Position implements Serializable {

	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(Position position) {
		this.x = position.x;
		this.y = position.y;
	}

	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	@Override
	public String toString(){
		return new String(x + ":" + y);
	}
	
}
