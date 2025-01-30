package server;
import java.io.*;
import java.net.*;



public class ClientHandler {
	private Socket socket;
	private int clientNumber;
	
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		
	}
}
