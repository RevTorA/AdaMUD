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
}
