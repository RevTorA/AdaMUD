package jmt.adams.edu;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Server {
	
	private static final int PORT = 23;
	
	public int num = 0;
	private List<Socket> clientList = new ArrayList<Socket>();
	
	public static void main (String[] args) {
		Server s = new Server();
		s.listen(PORT);
	}
	
	public void listen(int port) {
		try {
			ServerSocket listener = new ServerSocket(port);
			
			System.out.println(">>> AdaMUD Server now running <<<");
			System.out.println("   Telnet to " + InetAddress.getLocalHost().getHostName() + " on port " + port);
			System.out.println(" Ctrl+C to terminate server");
			
			while (true) {
				
				Socket client = listener.accept();
				
				add(client);
				new ClientHandler(client, this).start();
				System.out.println("New client number " + num + " from " + client.getInetAddress().getHostAddress());
				
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public synchronized void add (Socket s) {
		
	}
	
	public synchronized void remove (Socket s) {
		
	}
}
