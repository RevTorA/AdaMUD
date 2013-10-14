package jmt.adams.edu;

import java.net.*;
import java.io.*;

public class Telnet {
	public static final String RESET 		=	(char)27 + "[0m";
	public static final String BRIGHT 		=	(char)27 + "[1m";
	public static final String DIM 			= 	(char)27 + "[2m";
	public static final String UNDERLINE 	= 	(char)27 + "[4m";
	public static final String BLINKING 	= 	(char)27 + "[5m";
	public static final String SWAP_FG_BG 	= 	(char)27 + "[7m";
	public static final String HIDDEN 		= 	(char)27 + "[8m";
	
	public static final String FG_BLACK 	= 	(char)27 + "[30m";
	public static final String FG_RED 		= 	(char)27 + "[31m";
	public static final String FG_GREEN 	= 	(char)27 + "[32m";
	public static final String FG_YELLOW 	= 	(char)27 + "[33m";
	public static final String FG_BLUE 		= 	(char)27 + "[34m";
	public static final String FG_MAGENTA 	= 	(char)27 + "[35m";
	public static final String FG_CYAN 		= 	(char)27 + "[36m";
	public static final String FG_WHITE 	= 	(char)27 + "[37m";
	
	public static final String BG_BLACK 	= 	(char)27 + "[40m";
	public static final String BG_RED 		= 	(char)27 + "[41m";
	public static final String BG_GREEN 	= 	(char)27 + "[42m";
	public static final String BG_YELLOW 	= 	(char)27 + "[43m";
	public static final String BG_BLUE 		= 	(char)27 + "[44m";
	public static final String BG_MAGENTA 	= 	(char)27 + "[45m";
	public static final String BG_CYAN 		= 	(char)27 + "[46m";
	public static final String BG_WHITE 	= 	(char)27 + "[47m";
	
	public static String readLine(Socket s) throws IOException {
		//Reads data into buffer until newline is found, ignoring special characters and using backspaces properly
		String buffer = "";
		InputStreamReader in = new InputStreamReader(s.getInputStream());
		char inChar;
		
		while (true) {
			inChar = (char)in.read();
			
			if(inChar == '\n') {
				//Add newline and return to buffer, then break
				buffer += "\r\n";
				break;
			}
			
			if(inChar == '\b') {
				//Remove last character (if applicable) from buffer
				if(buffer.length() >= 1) {
					buffer = buffer.substring(0, buffer.length() - 2);
				}
			}
			
			if(inChar >= 32 && inChar <= 126) {
				buffer += inChar;
			}
		}
		
		return buffer;
	}
	
	public static void writeLine(Socket s, String message) throws IOException{
		//Writes message to Telnet socket, replacing <> tags with appropriate VT100 codes
		
		//First we replace all <> tags
		message.replace("<fgblack>", 	FG_BLACK);
		message.replace("<fgred>", 		FG_RED);
		message.replace("<fggreen>", 	FG_GREEN);
		message.replace("<fgyellow>", 	FG_YELLOW);
		message.replace("<fgblue>", 	FG_BLUE);
		message.replace("<fgmagenta>", 	FG_MAGENTA);
		message.replace("<fgcyan>", 	FG_CYAN);
		message.replace("<fgwhite>", 	FG_WHITE);
		
		message.replace("<bgblack>", 	BG_BLACK);
		message.replace("<bgred>", 		BG_RED);
		message.replace("<bggreen>", 	BG_GREEN);
		message.replace("<bgyellow>", 	BG_YELLOW);
		message.replace("<bgblue>", 	BG_BLUE);
		message.replace("<bgmagenta>", 	BG_MAGENTA);
		message.replace("<bgcyan>", 	BG_CYAN);
		message.replace("<bgwhite>", 	BG_WHITE);
		
		message.replace("<reset>", 		RESET);
		message.replace("<bright>", 	BRIGHT);
		message.replace("<dim>", 		DIM);
		message.replace("<underline>", 	UNDERLINE);
		message.replace("<blinking>", 	BLINKING);
		message.replace("<swap_fg_bg>", SWAP_FG_BG);
		message.replace("<hidden>", 	HIDDEN);
		
		//Then write the message to the socket
		PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
		
		out.println(message);
	}
}
