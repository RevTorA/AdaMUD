package jmt.adams.edu;
import java.util.*;
import java.io.*;

public class Room {

	public static final int N = 0;
	public static final int NE = 1;
	public static final int E = 2;
	public static final int SE = 3;
	public static final int S = 4;
	public static final int SW = 5;
	public static final int W = 6;
	public static final int NW = 7;
	public static final int UP = 8;
	public static final int DOWN = 9;
	public static final int IN = 10;
	public static final int OUT = 11;
	
	private String name;
	private int id;
	private String description;
	
	private int[] exit = new int[12];
	
	private List<Character> hereList = new ArrayList<Character>();
	
	public void setName(String name) { this.name = name; }
	public void setDescription(String description) { this.description = description; }
	public void setID(int id) { this.id = id; }
	
	public void setExit(int dir, int roomID) {
		if (dir >= 0 && dir <= 11)
			exit[dir] = roomID;
	}
	
	public String getName() { return name; }
	public String getDescription() { return description; }
	public int getID() { return id; }
	
	public int getExit(int dir) {
		return exit[dir];
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
			if (c.ID() == id) {
				return c;
			}
		}
		
		return null;
	}
	
	public void leave(Character c, int dir) {
		String message = c.Name() + " leaves to the ";
		
		switch (dir) {
		case N:
			message += "north";
			break;
		case NE:
			message += "northeast";
			break;
		case E:
			message += "east";
			break;
		case SE:
			message += "southeast";
			break;
		case S:
			message += "south";
			break;
		case SW:
			message += "southwest";
			break;
		case W:
			message += "west";
			break;
		case NW:
			message += "northwest";
			break;
		default:
			return;				
		}
		
		say(message);
	}
	
	public void say(String message) {
		//Broadcast to all Players in this room
		try {
			for (Character c : hereList) {
				if (c instanceof Player) {
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
			
			//Description in regular text. TODO: Implement "writeWordWrap" in Telnet
			Telnet.writeLine(p.getSocket(), "\t" + description);
			
			//Loop through listing all characters that are here in cyan text
			for (Character c : hereList) {
				if ((Player)c != p) {
					Telnet.writeLine(p.getSocket(), "<fgcyan>" + c.Name() + " is here.<reset>");
				}
			}
			
			//Display all exits
			
			
		}
		catch (IOException e) {
			System.out.println("Client error: " + e);
		}
	}
}
