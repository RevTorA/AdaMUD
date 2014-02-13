package jmt.adams.edu;

import java.util.*;

public class ItemDatabase {

	private ArrayList<Item> itemList = new ArrayList<Item>();
	
	public void add(Item i) {
		itemList.add(i);
	}
	
	public void remove(Item i, boolean removeFromDB) {
		//Removes i from the local list, and then deletes it from the MySQL db if removeFromDB == true
		itemList.remove(i);
	}
	
	public void remove(Item i) {
		remove(i, false);
	}
	
	public Item getByID(int id) {
		for (Item i : itemList) {
			if(i.getID() == id) {
				return i;
			}
		}
		
		return null;
	}
}
