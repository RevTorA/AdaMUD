package jmt.adams.edu;

import java.util.*;

public class PlayerDatabase {

	private List<Player> playerList = new ArrayList<Player>();
	private int numPlayers = 0;
	
	public PlayerDatabase() {
		
	}
	
	public void add(Player p) {
		playerList.add(p);
		numPlayers++;
	}
	
	public void remove(Player p) {
		playerList.remove(p);
		numPlayers--;
	}
	
	public Player findPartial(String name) {
		return null;
	}
	
	public Player findExact(String name) {
		for (Player p : playerList) {
			if(p.fullMatch(name))
				return p;
		}
		
		return null;
	}
}
