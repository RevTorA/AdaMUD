package jmt.adams.edu;

public class Direction {
	public static final int N = 0;
	public static final int NE = 1;
	public static final int E = 2;
	public static final int SE = 3;
	public static final int S = 4;
	public static final int SW = 5;
	public static final int W = 6;
	public static final int NW = 7;
	public static final int UP = 8;
	public static final int DOWN = 9;
	public static final int IN = 10;
	public static final int OUT = 11;
	
	public static final int[] reverse = {S, SW, W, NW, N, NE, E, SE, DOWN, UP, OUT, IN};
	public static final String[] exits = {"north", "northeast", "east", "southeast", "south", "southwest", "west", "northwest", "up", "down", "in", "out"};
}
