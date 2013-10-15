package jmt.adams.edu;

public class Player {
	public String name;
	
	public Player (String name) {
		this.name = name;
	}
	
	public boolean fullMatch (String search) {
		if (name.equals(search))
			return true;
		else
			return false;
	}
	
	public boolean partialMatch (String search) {
		return true;
	}
}
