package jmt.adams.edu;

import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;

public class RoomDatabase extends DefaultHandler {
	
	private List<Room> roomList = new ArrayList<Room>();
	
	//XML helper members
	private Room tempRoom;
	private String tempString;
	
	public void add(Room r) {
		roomList.add(r);
	}
	
	public void remove(Room r) {
		roomList.remove(r);
	}
	
	public Room getByID(int id) {
		for (Room r : roomList) {
			if (r.getID() == id) {
				return r;
			}
		}
		
		return null;
	}
	
	public void loadDatabaseFromFile(String filename) throws SAXException, ParserConfigurationException, IOException {
		//Create a "parser factory" for creating SAX parsers
		SAXParserFactory spfac = SAXParserFactory.newInstance();
		
		//Use the factory to create a parser
		SAXParser sp = spfac.newSAXParser();
		
		//Parse the db
		sp.parse(filename, this);
	}
	
	//Called whenever a new element is encountered
	public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
		
		if(qName.equalsIgnoreCase("room")) {
			tempRoom = new Room(this);
			tempRoom.setID(Integer.parseInt(attr.getValue("id")));
		}
		
		if(qName.equalsIgnoreCase("name")) {
			tempString = "";
		}
		
		if(qName.equalsIgnoreCase("description")) {
			tempString = "";
		}
		
		if(qName.equalsIgnoreCase("exit")) {
			int dir = Direction.map.get(attr.getValue("dir").toLowerCase());
			int id = Integer.parseInt(attr.getValue("id"));
			
			System.out.println("Room id: " + tempRoom.getID() + " Dir: " + Direction.exits[dir] + " ID: " + id);
			tempRoom.setExit(dir, id);
		}
	}
	
	//Called whenever the end of an element in encountered
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase("room")) {
			add(tempRoom);
		}
		if(qName.equalsIgnoreCase("name")) {
			tempRoom.setName(tempString);
		}
		if(qName.equalsIgnoreCase("description")) {
			tempRoom.setDescription(tempString);
		}
	}
	
	//Called whenever plain text is encountered
	public void characters(char[] buffer, int start, int length) {
		tempString = new String(buffer, start, length);
	}
}
