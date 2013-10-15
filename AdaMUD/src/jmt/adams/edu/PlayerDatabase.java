package jmt.adams.edu;

import java.util.*;

public class PlayerDatabase {
	private List<Player> playerList = new ArrayList<Player>();
	
	public PlayerDatabase() {
		
	}
	
	
	public void add(Player p) {
		if (p != null)
			playerList.add(p);
	}
	
	public void remove(Player p) {
		if (p != null)
			playerList.remove(p);
	}
	
	public Player searchName(String name) {
		for (Player p : playerList) {
			if (p.partialMatch(name)) {
				return p;
			}
		}
		
		return null;
	}
}
