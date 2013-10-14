package jmt.adams.edu;
import java.net.*;
import java.util.*;
import java.io.*;

public class ClientHandler extends Thread {

	private Socket cs;
	private Server s;
	
	public ClientHandler(Socket cs, Server s) {
		this.cs = cs;
		this.s = s;
	}
	
	public void run() {
		
	}
}
