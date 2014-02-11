package jmt.adams.edu;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.sql.*;
import java.security.*;

public class PlayerDatabase {
	private List<Player> playerList = new ArrayList<Player>();
	
	public PlayerDatabase() {
		
	}
	
	public Player login(Socket cs, Server s) throws IOException {
		//User MySQL database to login user or create a new user if necessary.
		
		Player p = null;
		String name;
		
		Connection c;
		
		//Get username
		Telnet.write(cs, "What is your name? ");
		name = Telnet.readLine(cs).trim();
		
		try {
			//Create MySQL connection to use
			c = DriverManager.getConnection(MySQLDatabase.url, MySQLDatabase.login, MySQLDatabase.password);
			
			//Check database to see if username already exists
			if(exists(name, c)) {
				//Log the person in
				return login(cs, s, name, c);
			}
			else {
				//Offer to create a new player
				return createNewPlayer(cs, s, name, c);
			}
			
		}
		catch (Exception e) {
			
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
	
	private boolean exists(String name, Connection c) throws SQLException{
		//Checks player database for player with name 'name'
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery("SELECT * FROM adamud.players WHERE username = \"" + name + "\"");
		
		//If nothing was found return false, otherwise return true
		if(r.isBeforeFirst()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private Player login(Socket cs, Server server, String name, Connection c) throws SQLException, IOException, NoSuchAlgorithmException, UnsupportedEncodingException {
		//It's assumed at this point that the player has been verified as existing in the db
		String password;
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery("SELECT * FROM adamud.players WHERE username = \"" + name + "\"");
		r.next();
		
		Telnet.write(cs, "Password: ");
		//Ask them for the password until they get it right or until they give no password
		while(!(password = Telnet.readLine(cs).trim()).equals("")) {
			if(checkHash(password, r.getString("password"))) {
				break;
			}
			else {
				Telnet.writeLine(cs, "Wrong password!");
				Telnet.write(cs, "Password: ");
			}
		}
		
		if(password.equals(""))
			return null;
		
		Room location = server.getRoomDB().getByID(r.getInt("location"));
		int id = r.getInt("id");
		
		Player p = new Player(name, id, location, cs);
		return p;
	}
	
	private Player createNewPlayer(Socket cs, Server server, String name, Connection c) throws SQLException, IOException, NoSuchAlgorithmException {
		//Offer to create a new player
		Telnet.writeLine(cs, "This is your first time logging in, do you want to create a new character? ");
		String input = Telnet.readLine(cs);
		
		while(!(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"))) {
			Telnet.writeLine(cs, "Yes or No please");
			input = Telnet.readLine(cs);
		}
		
		if(input.equalsIgnoreCase("no")) {
			return null;
		}
		
		String password, confirm;
		
		while(true) {
			Telnet.write(cs, "Password: ");
			password = Telnet.readLine(cs);
			Telnet.write(cs, "Confirm: ");
			confirm = Telnet.readLine(cs);
			
			if(!password.equals(confirm)) {
				Telnet.writeLine(cs, "Passwords don't match!");
				continue;
			}
			else {
				break;
			}
		}
		
		//Hash password, give id, set location to 1, and insert into db
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashByte = digest.digest(password.getBytes("UTF-8"));
		String hash = hashToString(hashByte);
		
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery("SELECT MAX(id) FROM adamud.players");
		r.next();
		int id = r.getInt("id") + 1;
		
		r = s.executeQuery("INSERT INTO adamud.players VALUES (" + id + ", \"" + name + "\", \"" + hash + "\", 1)");
		
		//Create the player, and return it
		Player p = new Player(name, id, server.getRoomDB().getByID(1), cs);
		return p;
	}
	private String hashToString(byte[] hash) {
		StringBuffer hashBuffer = new StringBuffer();
		
		for(byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if(hex.length() == 1)
				hashBuffer.append('0');
			hashBuffer.append(hex);
		}
		
		return hashBuffer.toString();
	}
	
	private boolean checkHash(String pass, String hash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] byteHash = digest.digest(pass.getBytes("UTF-8"));
		
		if(hashToString(byteHash).equals(hash)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
}
