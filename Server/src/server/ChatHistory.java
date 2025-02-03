package server;
import java.io.*;
import java.util.*;


//Ceci va etre la classe a utiliser pour faire la gestion des messages des clients et aller les read et write dans 
//un fichier standard.
public class ChatHistory {
	private static final String MESSAGE_FILE = "messages.csv";
	private static int maxMessages = 15;
	public static List<String> messages;
	private static BufferedWriter bufferWriter;
	private static BufferedReader bufferReader;
	
	
	ChatHistory(){
		messages = new ArrayList<>();
		loadMessages();
	}
	
	
	private static void loadMessages() {
		
		try {
			bufferReader = new BufferedReader(new FileReader(MESSAGE_FILE));
			String line;
			while((line = bufferReader.readLine()) != null && maxMessages > 0) {
				messages.add(line);
				maxMessages--;
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
	
	public static void getAllMessages() { //méthode pour aller chercher tous les messages.
		try {
			messages.clear();
			maxMessages = 15;
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
	

}
