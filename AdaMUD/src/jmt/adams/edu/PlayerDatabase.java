package jmt.adams.edu;

import java.util.*;
import java.io.IOException;
import java.net.*;
import java.sql.*;

public class PlayerDatabase {
	private List<Player> playerList = new ArrayList<Player>();
	
	public PlayerDatabase() {
		
	}
	
	public Player login(Socket cs, Server s) throws IOException {
		Telnet.writeLine(cs, "What is your name? ");
		String name = Telnet.readLine(cs).trim();
		Player p = null;
		
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "tionajm", "hereigns");
			Statement stmt = c.createStatement();
			ResultSet r = stmt.executeQuery("SELECT * FROM adamud.players WHERE name = \"" + name + "\"");
			
			if(r.next()) {
				Telnet.writeLine(cs, "Password: ");
				String password = Telnet.readLine(cs).trim();
				System.out.println(r.getString("password"));
				if(password.equals(r.getString("password"))) {
					p = new Player(r.getString("name"), r.getInt("id"), s.getRoomDB().getByID(r.getInt("location")), cs);
					playerList.add(p);
					System.out.println("Player logged in");
					
					Telnet.writeLine(cs,  "Hello " + name + "!\n");
					p.location.look(p);
				}
				else {
					Telnet.writeLine(cs, "Incorrect password!");
					return null;
				}
			}
			else {
				System.out.println("Not Found!");
				Telnet.writeLine(cs, "New character, please enter a password: ");
				String password = Telnet.readLine(cs).trim();
				Telnet.writeLine(cs, "Confirm password: ");
				if(password.equals(Telnet.readLine(cs).trim())) {
					r = stmt.executeQuery("SELECT MAX(id) FROM adamud.players");
					r.next();
					int id = r.getInt("id") + 1;
					stmt.executeQuery("INERT INTO adamud.players VALUES (" + id + "," + name + ", " + password + ", 0)");
				}
				else {
					Telnet.writeLine(cs, "Passwords do not match!");
				}
			}
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		
		return p;
	}
	
	public void add(Player p) {
		if (p != null)
			playerList.add(p);
	}
	
	public void remove(Player p) {
		if (p != null)
			p.location.remove(p);
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
