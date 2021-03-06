package jmt.adams.edu;
import java.util.*;
import java.io.*;

public class Room {

	private String name;
	private int id;
	private String description;
	
	private int[] exit = new int[12];
	
	private ArrayList<Character> hereList = new ArrayList<Character>();
	private ArrayList<Item> itemList = new ArrayList<Item>();
	
	private RoomDatabase db;
	
	public Room(RoomDatabase db) {
		this.db = db;
	}
	
	public void setName(String name) { this.name = name; }
	public void setDescription(String description) { this.description = description; }
	public void setID(int id) { this.id = id; }
	
	public void setExit(int dir, int id) {
		if (dir >= 0 && dir <= 11)
			exit[dir] = id;
	}
	
	public String getName() { return name; }
	public String getDescription() { return description; }
	public int getID() { return id; }
	
	public Room getExit(int dir) {
		if (dir >= 0 && dir <= 11) {
			return db.getByID(exit[dir]);
		}
		
		return null;
	}
	
	public Character findByName(String search) {
		for (Character c : hereList) {
			if (c.partialMatch(search)) {
				return c;
			}
		}
		
		return null;
	}
	
	public Character findByID(int id) {
		for (Character c : hereList) {
			if (c.getID() == id) {
				return c;
			}
		}
		
		return null;
	}
	
	public Item findItemByName(String search) {
		for (Item i : itemList) {
			if (i.partialMatch(search)) {
				return i;
			}
		}
		
		return null;
	}
	
	public Item findItemByID(int id) {
		for (Item i : itemList) {
			if (i.getID() == id) {
				return i;
			}
		}
		
		return null;
	}
	
	public void add(Character c) {
		if(c != null)
			hereList.add(c);
	}
	
	public void remove(Character c) {
		if(c != null)
			hereList.remove(c);
	}
	
	public void leave(Character c, int dir) {
		if (dir >= 0 && dir <= 11) {
			String message = c.Name() + " leaves to the " + Direction.exits[dir] + ".\n";
			say(message, (Player)c);
		}
		remove(c);
	}
	
	public void arrive(Character c, int dir) {
		if (dir >= 0 && dir <= 11) {
			String message = c.Name() + " arrives from the " + Direction.exits[dir] + ".\n";
			say(message, (Player)c);
		}
		add(c);
	}
	
	public void say(String message) {
		say(message, null);
	}
	
	public void say(String message, Player ignorePlayer) {
		//Broadcast to all Players in this room
		try {
			for (Character c : hereList) {
				if (c instanceof Player) {
					if (c != ignorePlayer)
						Telnet.writeLine(((Player)c).getSocket(), message);
				}
			}
		}
		catch (IOException e) {
			System.out.println("Client error: " + e);
		}
	}
	
	public void look(Player p) {
		try {
			//Name in green text
			Telnet.writeLine(p.getSocket(), "<fggreen>" + name + "<reset>");
			
			//Description in regular text.
			Telnet.writeLine(p.getSocket(), description);
			
			//Loop through listing all characters that are here in cyan text
			String playerList = "<fgcyan>";
			for (Character c : hereList) {
				if ((Player)c != p) {
					playerList += c.Name() + " is here. ";
				}
			}
			playerList += "<reset>";
			Telnet.writeLine(p.getSocket(), playerList);
			
			//Display all exits
			String exitList = "<dim><fgyellow>There are exits to the";
			for (int i = 0; i < 12; i++) {
				if (exit[i] != 0) {
					exitList += " " + Direction.exits[i] + ",";
				}
			}
			exitList = exitList.substring(0, exitList.length() - 1) + ".<reset>\n";
			Telnet.writeLine(p.getSocket(), exitList);
			
		}
		catch (IOException e) {
			System.out.println("Client error: " + e);
		}
	}
}
