package server;
import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.util.regex.Pattern;


public class Server {
	
	private static ServerSocket Listener;
	private static final String IP_REGEX = 
	        "^((25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)$";
	static Scanner scanner = new Scanner(System.in);
	static String ipAddress;
	static int port;
	
	public static void main (String[] args){
		serverLaunch();
	}
	
	public static void serverLaunch()  {
		System.out.print("Entrez l'addresse IP du serveur: \n");
		
		while(true) {
			ipAddress = scanner.nextLine();
			if (Pattern.matches(IP_REGEX, ipAddress)) break;
			System.out.print("Addresse IP invalide. Réessayez: \n");
		}
		
		System.out.print("Entrez un port entre 5000 et 5050: ");
		
		while(true) {
			port = scanner.nextInt();
			if (5000 <= port && port <= 5050) break;
			System.out.print("Port invalide. Réessayez: ");
		}
		
		try {
			Listener = new ServerSocket();
			Listener.setReuseAddress(true);
			InetAddress serverIp = InetAddress.getByName(ipAddress);
			Listener.bind(new InetSocketAddress(serverIp, port));
			System.out.format("Le serveur roule sur l'addresse IP %s et le port %d", ipAddress, port);
	}
		catch (IOException e) {
			 System.out.println("Erreur lors du démarrage du serveur.");
		}
	
	}
	
}
