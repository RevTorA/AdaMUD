package jmt.adams.edu;
import java.net.*;
import java.io.*;

public class ClientHandler extends Thread {

	private Socket cs;
	private Server s;
	
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
				
				Telnet.writeLine(cs, "You said: " + message);
			}
			
			cs.close();
		}
		catch (IOException e) {
			System.out.println("Client error: " + e);
		}
		finally {
			s.remove(cs);
		}
	}
	
	private void handleLogin() throws IOException {
		Telnet.writeLine(cs, "What is your name? ");
		String name = Telnet.readLine(cs).trim();
		
		if(s.playerDatabase.findExact(name) != null) {
			Telnet.writeLine(cs, "Welcome back " + name + "!");
		}
		else {
			Telnet.writeLine(cs, "New around here huh? Well I'll remember you.");
			s.playerDatabase.add(new Player(name));
		}
	}
}
