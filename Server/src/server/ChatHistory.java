package server;
import java.io.*;
import java.util.*;

public class ChatHistory {
	private static final String MESSAGE_FILE = "messages.csv";
	private static int maxMessages = 15;
	public static List<String> messages;
	private static BufferedReader bufferReader;
	
	
	ChatHistory(){
		messages = new ArrayList<>();
		loadMessages();
	}
	
	
	public void loadMessages() {
		
		try {
			bufferReader = new BufferedReader(new FileReader(MESSAGE_FILE));
			String line;
			messages.clear();
			while((line = bufferReader.readLine()) != null) {
				messages.add(line);
			}
		}
		catch(IOException e) {
			System.err.println("Erreur lors de la recherche des messsages: " + e.getMessage());
			
		}
		
	}
	
	public static void printMessages() {
		
		for (String message: messages) 
			System.err.println(message + "\n");
		
	}
	
	public static void getAllMessages() {
		try {
			messages.clear();
			bufferReader = new BufferedReader(new FileReader(MESSAGE_FILE));
			String line;
			while((line = bufferReader.readLine()) != null) {
				messages.add(line);
			}
		}
		catch(IOException e) {
			System.err.println("Erreur lors de la recherche des messsages: " + e.getMessage());
			
		}
	}
	
	public void addMessage(String message) {
		try (BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(MESSAGE_FILE, true))) {
	        bufferWriter.write(message);
	        bufferWriter.newLine();
	    } catch (IOException e) {
	        System.err.println("Erreur lors de l'ajout d'un message: " + e.getMessage());
	    }
	}
	
	public List<String> getMessages() {
	    return messages;
	}
	

}
