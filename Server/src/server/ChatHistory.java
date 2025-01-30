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
	
	
	
	private static void loadMessages() {
		
		try {
			bufferReader = new BufferedReader(new FileReader(MESSAGE_FILE));
			String line;
			
			
		}
		catch(IOException e) {
			System.out.println("Erreur lors de la recherche des messsages: " + e.getMessage());
			
		}
		
	}
	
	private static void printMessages() {
		
		
		
		
	}
	
	public static void getAllMessages() { //méthode pour aller chercher tous les messages.
		
	}
	

}
