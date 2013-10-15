package jmt.adams.edu;
import java.net.*;
import java.io.*;

public class ClientHandler extends Thread {

	private Socket cs;
	private Server s;
	
	private Player player;
	
	public ClientHandler(Socket cs, Server s) {
		this.cs = cs;
		this.s = s;
	}
	
	public void run() {
		try {
			Telnet.writeLine(cs, "<fggreen> >>> Welcome to AdaMUD <<< <reset>");
			Telnet.flushInput(cs);
			
			handleLogin();
			
			while (true) {
				String message = Telnet.readLine(cs).trim();
				
				if(message.equals("bye")) {
					Telnet.writeLine(cs, "Goodbye!");
					break;
				}
				
				parseCommand(message);
			}
			
			cs.close();
		}
		catch (IOException e) {
			System.out.println("Client error: " + e);
		}
		finally {
			s.getPlayerDB().remove(player);
			s.remove(cs);
		}
	}
	
	private void handleLogin() throws IOException {
		Telnet.writeLine(cs, "What is your name? ");
		String name = Telnet.readLine(cs).trim();
		
		player = new Player(name, s.numClients, cs);
		s.getPlayerDB().add(player);
		
		Telnet.writeLine(cs,  "Hello " + name + "!");
	}
	
	private void parseCommand(String message) throws IOException {
		if (message.length() == 0)
			return;
		
		//Get the command (first word)
		String comm;
		String arguments = null;
		
		if(message.indexOf(' ') != -1) {
			comm = message.substring(0, message.indexOf(' '));
			arguments = message.substring(message.indexOf(' ') + 1);
		}
		else {
			comm = message;
		}
		
		switch (comm.toLowerCase()) {
		
		case "say":
			say(arguments);
			break;
			
		case "tell":
			tell(arguments);
			break;
			
		default:
			Telnet.writeLine(cs, "Your meaning is unclear");
		}
	}
	
	private void say(String message) throws IOException {
		s.broadcast("<bright><fgcyan>" + player.Name() + " says \"" + message + "\"<reset>");
	}
	
	private void tell(String message) throws IOException {
		String recipient;
		
		if (message.indexOf(' ') != -1) {
			recipient = message.substring(0, message.indexOf(' '));
			message = message.substring(message.indexOf(' ') + 1);
		}
		else {
			Telnet.writeLine(cs, "<fgred>Tell what to whom?<reset>");
			return;
		}
		
		Player p;
		
		if ((p = s.getPlayerDB().searchName(recipient)) != null) {
			Telnet.writeLine(p.getSocket(), "<bright><fgyellow>" + player.Name() + " tells you: " + message + "<reset>");
		}
		else
		{
			Telnet.writeLine(cs, "<fgred>No player " + recipient + " found<reset>");
			return;
		}
	}
}
