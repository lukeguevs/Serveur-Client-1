package client;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Client {
	private static final String IP_REGEX = 
	        "^((25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|1?[0-9][0-9]?)$";
	
	private static Socket socket;
	private static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	private static String ipAddress;
	private static int port;
	static PrintWriter printWriter;
	static BufferedReader reader;
	private static String username;
	private static String password;
	
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
	
	public Client() throws IOException {
		connectToServer();
		quit();
	}
	
	
	
	public  void connectToServer() throws IOException  {
		
		try {
			System.out.println("Entrez l'addresse IP du serveur: ");
			
				ipAndPortConnect();
		
				socket = new Socket();
				socket.setReuseAddress(true);
				InetAddress serverIp = InetAddress.getByName(ipAddress);
				socket.connect(new InetSocketAddress(serverIp, port));
				System.out.format("Le serveur client roule sur l'addresse IP %s et le port %d \n", ipAddress, port);
				System.out.println("Tentative d'authentification...");
				printWriter = new PrintWriter(socket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				
				while(true) {
					boolean authenticated = authenticate();
				
		            if (authenticated) {
		            	System.out.print("Vous êtes connecté.e au serveur.\n");
		            	new Thread(this.receiveMessages()).start();
		            	sendMessages();
		                break;
		            } else {
		                System.out.println("Mauvais mot de passe, déconnexion...");
		                socket.close();
		                break;
		            }
				}	
		 }
			
			catch (IOException e) {
			System.out.println("Erreur lors du démarrage du serveur: " + e.getMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
				socket.close();
		}
	}
	
	
	
	
	public  boolean authenticate() throws InterruptedException {
		
		int attempts = 0;
	    try {
	        while (attempts < 3) {
	            System.out.print("Nom d'utilisateur: ");
	            username = inputReader.readLine();
	            System.out.print("Mot de passe: ");
	            password = inputReader.readLine();
	            printWriter.println(username + "," + password);
	            
	            String response = reader.readLine();
	            if (response.equals("AUTH_SUCCESS")) {
	                return true;
	            } else if (response.equals("AUTH_MAX_ATTEMPTS")) {
	                System.out.println("Échec de l'authentification après 3 tentatives.");
	                return false;
	            } else if (response.startsWith("AUTH_FAILED")) {
	                attempts++;
	                String[] parts = response.split(",");
	                int remainingAttempts = parts.length > 1 ? Integer.parseInt(parts[1]) : 3 - attempts;
	                System.out.println("Mauvais mot de passe, tentatives restantes: " + remainingAttempts);
	            }
	        }
	        return false; 
	    } catch (IOException e) {
	        System.out.println("Erreur lors de l'authentification au serveur: " + e.getMessage());
	        return false;
	    }
	}
	
	public void sendMessages() {
	    if (printWriter == null) {
	        System.out.println("Erreur : connexion non établie.");
	        return;
	    }

	    try {
	        String userMessage;
	        while (true) {
	            System.out.print(">");
	            userMessage = inputReader.readLine();
	            if (userMessage.equals("/quit")) {
	            	socket.close();
	            	System.out.print("Déconnexion...");
	            	break;
	            }
	            String timestamp = LocalDateTime.now().format(formatter);
	            printWriter.format("[%s - %s:%d - %s]: %s\n",username, ipAddress, port, timestamp, userMessage);
	            printWriter.flush();

	            if (printWriter.checkError()) {
	                System.out.println("Erreur d'écriture. Connexion fermée.");
	                break;
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Erreur lors de l'envoi du message: " + e.getMessage());
	    }
	}
	
	public Runnable receiveMessages() {
	    return () -> {
	        try {
	            String message;
	            while ((message = reader.readLine()) != null) {
	                System.out.println("\n" + message + "\n>");
	            }
	        } catch (IOException e) {
	            System.out.println("Erreur lors de la réception du message: " + e.getMessage());
	        }
	    };
	}

	
	public void quit() {
		new Thread(() -> 
		{
			try (Scanner quitScanner = new Scanner(System.in)) {
			while(true) {
				if (quitScanner.nextLine().equalsIgnoreCase("/quit")) {
						socket.close();
						System.out.println("Serveur Fermé.");
						break;
					}
				Thread.sleep(100);
				}
			}
		catch (IOException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}).start();
		
	}
	
	
	public void ipAndPortConnect(){
		
		try{
			while(true) {
				ipAddress = inputReader.readLine();
				if (Pattern.matches(IP_REGEX, ipAddress)) break;
				System.out.println("Addresse IP invalide. Réessayez: \n");
			}
			
			System.out.print("Entrez un port entre 5000 et 5050: ");
			
			while(true) {
				port = Integer.parseInt(inputReader.readLine());
				if (5000 <= port && port <= 5050) break;
				System.out.println("Port invalide. Réessayez: ");
			}
		}
		catch(IOException e) {
			System.out.print(e.getStackTrace());
		}
	}
	
}
