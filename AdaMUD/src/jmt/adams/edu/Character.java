package jmt.adams.edu;

public class Character extends Entity {
	protected Room location;
	
	public Character(String name, int id, Room location) {
		super(name, id);
		
		this.location = location;
	}
	
	public void move (int dir) {
		if(location.getExit(dir) != null) {
			location.leave(this, dir);
			location = location.getExit(dir);
			location.arrive(this, Direction.reverse[dir]);
		}
	}

	public void say(String message) {
		location.say("<bright><fgcyan>" + name + " says \"" + message + "\"<reset>");
	}
}
