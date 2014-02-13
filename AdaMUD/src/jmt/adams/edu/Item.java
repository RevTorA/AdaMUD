package jmt.adams.edu;

public abstract class Item extends Entity {

	String description;
	Room location;
	Character owner;
	
	public Item(String name, int id, String description) {
		super(name, id);
		this.description = description;
	}
	
	public void move(Character dest) {
		
	}
	
	public void move(Room location) {
		
	}
	
	public String lookAt() {
		return description;
	}
}
