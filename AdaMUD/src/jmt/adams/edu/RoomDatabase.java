package jmt.adams.edu;

import java.util.*;

public class RoomDatabase {
	List<Room> roomList = new ArrayList<Room>();
	
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
}
