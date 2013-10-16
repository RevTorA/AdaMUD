package jmt.adams.edu;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Server {
	
	private static final int PORT = 23;
	
	public int numClients = 0;
	private List<Socket> clientList = new ArrayList<Socket>();
	
	private PlayerDatabase dbPlayers = new PlayerDatabase();
	private RoomDatabase dbRooms = new RoomDatabase();
	
	public static void main (String[] args) {
		Server s = new Server();
		s.initRooms();
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
				System.out.println("New client number " + numClients + " from " + client.getInetAddress().getHostAddress());
				
			}
		}
		catch (IOException e) {
			System.out.println(e);
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
		Room r1 = new Room(dbRooms);
		Room r2 = new Room(dbRooms);
		
		r1.setName("CSCI Classroom");
		r1.setDescription("A classroom at Adams");
		r1.setID(1);
		r1.setExit(Direction.E, 2);
		
		r2.setName("Utility Closet");
		r2.setDescription("A small closet home to spare computer parts, some cleaning supplies, and programming manuals");
		r2.setID(2);
		r2.setExit(Direction.W, 1);
		
		dbRooms.add(r1);
		dbRooms.add(r2);
	}
}
