package jmt.adams.edu;

import java.net.*;
import java.util.*;
import java.io.*;

public class Server {
	
	private static final int PORT = 23;
	
	public int numClients = 0;
	private ArrayList<Socket> clientList = new ArrayList<Socket>();
	
	private PlayerDatabase dbPlayers = new PlayerDatabase();
	private RoomDatabase dbRooms = new RoomDatabase();
	
	public static void main (String[] args) {
		try {
			Server s = new Server();
			s.initRooms();
			s.listen(PORT);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void listen(int port) throws IOException {
		ServerSocket listener = new ServerSocket(port);
		try {
			System.out.println(">>> AdaMUD Server now running <<<");
			System.out.println("   Telnet to " + InetAddress.getLocalHost().getHostName() + " on port " + port);
			System.out.println(" Ctrl+C to terminate server");
			
			while (true) {
				
				Socket client = listener.accept();
				
				add(client);
				new ClientHandler(client, this).start();
				System.out.println("New client number " + numClients + " from " + client.getInetAddress().getHostAddress());
				
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
		finally {
			listener.close();
		}
	}
	
	public void broadcast(String message) throws IOException {
		for (Socket s : clientList) {
			Telnet.writeLine(s, message);
		}
	}
	
	public synchronized void add (Socket s) {
		clientList.add(s);
		numClients++;
	}
	
	public synchronized void remove (Socket s) {
		clientList.remove(s);
		numClients--;
	}
	
	public PlayerDatabase getPlayerDB() { return dbPlayers; }
	public RoomDatabase getRoomDB() { return dbRooms; }
	
	//Temporarily sets up dummy rooms
	private void initRooms () {
		try {
			dbRooms.loadDatabaseFromFile("rooms.xml");
		}
		catch (Exception e) {
			System.out.println("Server error: " + e);
		}
	}
}
