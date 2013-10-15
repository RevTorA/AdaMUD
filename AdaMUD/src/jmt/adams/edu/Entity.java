package jmt.adams.edu;

public class Entity {
	private String name;
	private int id;
	
	public Entity(String name, int id) {
		this.name = name;
		this.id = id;
	}
		
	public void Name(String name) {
		this.name = name;
	}
	
	public String Name() {
		return name;
	}
	
	public void ID(int id) { this.id = id; }
	public int ID() { return id; }
	
	public boolean partialMatch(String search) {
		int idx = 0;
		
		if (search.length() == 0) 
			return true;
		
		while(true) {
			if ((idx = name.toLowerCase().indexOf(search.toLowerCase(), idx)) != -1) {
				if (idx == 0 || name.charAt(idx - 1) == ' ') {
					return true;
				}
				else {
					idx++;
					continue;
				}
			}
			else {
				break;
			}
		}
		
		return false;
	}
	
	public boolean fullMatch(String search) {
		if (name.toLowerCase().equals(search.toLowerCase())) {
			return true;
		}
		else {
			return false;
		}
	}
}
