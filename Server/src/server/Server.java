package server;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

//0.0.0.0


public class Server {
	
	private static ServerSocket serverSocket;
	private static final String IP_REGEX = 
	        "^((25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)$";
	static Scanner scanner = new Scanner(System.in);
	static String ipAddress;
	static int port;
	private static boolean running = true;
	private static LoginManager loginManager;
	private static final List<ClientHandler> clients = new ArrayList<>();
	private static ChatHistory chatHistory = new ChatHistory();
	
	
	public static void main (String[] args){
		loginManager = new LoginManager();
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
			System.out.format("Le serveur roule sur l'addresse IP %s et le port %d\n", ipAddress, port);
			running = true;
			
		
		handleClientLogin();
		quittingOperation();
		
	}
		
		catch (IOException e) {
			System.out.println("Erreur lors du démarrage du serveur: " + e.getMessage());
			closeServer();
		
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
	
	public static void quittingOperation() {
		new Thread(() -> 
		{
			while(true) {
				if (scanner.nextLine().equalsIgnoreCase("/quit")) {
					closeServer();
					break;
				}
			}
		}).start();
	}
	
	public static void handleClientLogin() {
	    try {
	        while (running) {
	            Socket clientSocket = serverSocket.accept();
	          if (clientSocket != null) {
	            new Thread(() -> {
	                try (
	                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
	                ) {
	                    System.out.println("Nouveau client connecté, réception de l'authentification...");
	                    String[] loginCredentials = reader.readLine().split(",");
	                    
	                    if (loginCredentials.length < 2) {
	                        writer.println("AUTH_FAILED");
	                        System.out.println("Échec de l'authentification: Données invalides.");
	                        clientSocket.close();
	                        return;
	                    }

	                    String username = loginCredentials[0];
	                    String password = loginCredentials[1];

	                    boolean authenticated = loginManager.authenticate(username, password);
	                    if (authenticated) {
	                        System.out.println("Utilisateur " + username + " connecté.");
	                        writer.println("AUTH_SUCCESS");

	                        ClientHandler clientHandler = new ClientHandler(clientSocket, chatHistory);
	                        new Thread(clientHandler).start();

	                    } else {
	                        System.out.println("Échec de l'authentification pour " + username);
	                        writer.println("AUTH_FAILED");
	                        clientSocket.close();
	                    }
	                } catch (IOException e) {
	                    System.out.println("Erreur lors du traitement de l'authentification: " + e.getMessage());
	                }
	            }).start();
	          }
	        }
	    } catch (IOException e) {
	        System.out.println("Erreur lors de l'attente des connexions: " + e.getMessage());
	    }
	}


	public static void broadcastMessage(String message) {
		for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
		
		
		
		
	}
	
}
