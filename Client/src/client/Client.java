package client;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Client {
	private static final String IP_REGEX = 
	        "^((25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)$";
	
	private static Socket socket;
	static Scanner scanner = new Scanner(System.in);
	private static String ipAddress;
	private static int port;
	private static boolean running = false;
	static PrintWriter printWriter;
	static BufferedReader reader;
	private static String username;
	private static String password;
	private static int LOGINCOUNTER = 3;
	
	public static void main (String[] args){
		connectToServer();
	}
	
	public static void connectToServer()  {
		
		try {
			System.out.println("Entrez l'addresse IP du serveur: \n");
			
			while(true) {
				ipAddress = scanner.nextLine();
				if (Pattern.matches(IP_REGEX, ipAddress)) break;
				System.out.println("Addresse IP invalide. Réessayez: \n");
			}
			
			System.out.print("Entrez un port entre 5000 et 5050: ");
			
			while(true) {
				port = scanner.nextInt();
				scanner.nextLine();
				if (5000 <= port && port <= 5050) break;
				System.out.println("Port invalide. Réessayez: ");
			}
			

				socket = new Socket();
				socket.setReuseAddress(true);
				InetAddress serverIp = InetAddress.getByName(ipAddress);
				socket.connect(new InetSocketAddress(serverIp, port));
				System.out.format("Le serveur client roule sur l'addresse IP %s et le port %d \n", ipAddress, port);
				System.out.println("Tentative d'authentification...");
				running = true;
				
			
			new Thread(() -> 
			{
				Scanner quitScanner = new Scanner(System.in);
				while(true) {
					if (quitScanner.nextLine().equalsIgnoreCase("/quit")) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			
			
			 boolean authenticated = false;
	            while (LOGINCOUNTER > 0) {
	                authenticated = authenticate();
	                if (authenticated) {
	                    LOGINCOUNTER = 3;
	                    break;
	                }
	                LOGINCOUNTER--;
	                System.out.format("Échec de l'authentification. %d essais restant(s).\n", LOGINCOUNTER);
	            }

	            if (authenticated) {
	                new Thread(Client::receiveMessages).start();
	                sendMessages();
	            } else {
	                System.out.println("Trop de tentatives échouées. Déconnexion...");
	                socket.close();
	            }
			
		}
			
			catch (IOException e) {
			System.out.println("Erreur lors du démarrage du serveur: " + e.getMessage());
			}
		}
	
	
	
	public static boolean authenticate() {
		
		try {
			System.out.print("Nom d'utilisateur: ");
            username = scanner.nextLine();
            System.out.print("Mot de passe: ");
            password = scanner.nextLine();
            
            printWriter.println(username);
            printWriter.println(password);
            
            String response = reader.readLine();
            return response.equals("AUTH_SUCCESS");
           
		}
		catch(IOException e) {
			System.out.println("Erreur lors de l'authentification au serveur: " + e.getMessage());
			return false;
			
		}
	}
	
	public static void sendMessages() {
		
		try{
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			String userMessage;
			while (true) {
				System.out.println("> ");
				userMessage = scanner.nextLine();
				printWriter.println(userMessage);
			}
			
		}
		catch (IOException e) {
			System.out.println("Erreur lors de l'envoi du message: " + e.getMessage());
		}
		
	}
	
	public static void receiveMessages() {
		try {
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message;
			while((message = reader.readLine()) != null) {
				System.out.println("\n" + message);
				System.out.print("> ");
			}
			
		}
		catch(IOException e) {
			System.out.println("Erreur lors de la réception du message: " + e.getMessage());
			running = false;
		}
	}
	
	
	
}
