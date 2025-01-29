package server;
import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.util.regex.Pattern;


public class Server {
	
	private static ServerSocket serverSocket;
	private static final String IP_REGEX = 
	        "^((25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)$";
	static Scanner scanner = new Scanner(System.in);
	static String ipAddress;
	static int port;
	private static boolean running = true;
	
	
	public static void main (String[] args){
		serverLaunch();
	}
	
	public static void serverLaunch()  {
		
	try {
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
		

			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			InetAddress serverIp = InetAddress.getByName(ipAddress);
			serverSocket.bind(new InetSocketAddress(serverIp, port));
			System.out.format("Le serveur roule sur l'addresse IP %s et le port %d", ipAddress, port);
			running = true;
		
		new Thread(() -> 
		{
			while(true) {
				if (scanner.nextLine().equalsIgnoreCase("/quit")) {
					closeServer();
					break;
				}
			}
		}).start();
		
		while(running) {
			serverSocket.accept();
;		}
	}
		
		catch (IOException e) {
		System.out.println("Erreur lors du démarrage du serveur: " + e.getMessage());
		}
	}
	
	public static void closeServer() {
		running = false;
		
		try{
			if (serverSocket != null) serverSocket.close();
			System.out.println("Serveur arrêté.");
		}
		catch (IOException e) {
			System.out.println("Erreur lors de l'arrêt du serveur: " + e.getMessage());
		}
		
	}
	
	
	
}
