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
		ResultSet r = s.executeQuery("SELECT * FROM adamud.players WHERE name = \"" + name + "\"");
		
		//If nothing was found return false, otherwise return true
		if(r.isBeforeFirst()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private Player login(Socket cs, String name, Connection c) throws SQLException, IOException, NoSuchAlgorithmException, UnsupportedEncodingException {
		//It's assumed at this point that the player has been verified as existing in the db
		String password;
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery("SELECT * FROM adamud.players WHERE name = \"" + name + "\"");
		
		Telnet.write(cs, "Password: ");
		//Ask them for the password until they get it right or until they give no password
		while(!(password = Telnet.readLine(cs)).equals("")) {
			if(checkHash(password, r.getString("hash"))) {
				
			}
			else {
				Telnet.writeLine(cs, "Wrong password!");
				Telnet.write(cs, "Password: ");
			}
		}
		
		return null;
	}
	
	private boolean checkHash(String pass, String hash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] byteHash = digest.digest(pass.getBytes("UTF-8"));
		StringBuffer hashBuffer = new StringBuffer();
		
		for(byte b : byteHash) {
			String hex = Integer.toHexString(0xff & b);
			if(hex.length() == 1)
				hashBuffer.append('0');
			hashBuffer.append(hex);
		}
		
		if(hashBuffer.toString().equals(hash)) {
			return true;
		}
		else {
			return false;
		}
	}
}
