package server;
import java.util.*;
import java.io.*;
import java.net.*;

//Ceci va etre la classe qu'on va utiliser pour gérer les usernames et passwords des clients, 
//incluant la verification de l'utilisateur ainsi que la transmission dans un fichier qui peut log les changements

public class LoginManager {
	
	private static final String USER_FILE = "usagers.csv";
	private static Map<String, String> users;
	private static BufferedWriter bufferWriter;
	private static BufferedReader bufferReader;
	
	public LoginManager() {
		users = new HashMap<>();
		loadUsers();
	}
	
	public static void loadUsers() {
		
		try {
			
			bufferReader = new BufferedReader(new FileReader(USER_FILE));
			String line;
			while((line = bufferReader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2) users.put(parts[0], parts[1]);
			}
			
		}catch (IOException e){
			System.err.println("Erreur lors du loading des usagers: " + e.getMessage());
		}
		
	}
	
	public static boolean register(String username, String password) {
		if (users.containsKey(username)) return false; //precaution
		
		users.put(username, password);
		appendUser(username, password);
		
		return true;
	}
	
	public static void appendUser(String username, String password) {
		
		try {
			bufferWriter = new BufferedWriter(new FileWriter(USER_FILE, true));
			bufferWriter.write(username + "," + password);
			bufferWriter.newLine();
			
		} catch(IOException e){
			System.err.println("Erreur lors de la sauvegarde des usagers: " + e.getMessage());
		}
		
	}
	
	public static boolean authenticate(String username, String password) {
		if (!users.containsKey(username)) 
			return register(username, password);
		return users.get(username) == password;
	}
	
	
	

}
