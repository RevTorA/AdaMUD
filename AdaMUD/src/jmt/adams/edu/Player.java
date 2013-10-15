package jmt.adams.edu;

import java.net.*;

public class Player extends Character {

	private Socket s;
	
	public Player(String name, int id, Socket s) {
		super(name, id);
		
		this.s = s;
	}
	
	public void setSocket (Socket s) {
		this.s = s;
	}
	
	public Socket getSocket() {
		return s;
	}
}
