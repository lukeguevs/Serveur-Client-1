package server;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.List;



public class ClientHandler implements Runnable {
	private Server server;
	private Socket socket;
	private ChatHistory chat;
	private PrintWriter writer;
    private BufferedReader reader;
	
	public ClientHandler(Socket socket, ChatHistory chatHistory, Server server) {
		this.socket = socket;
		this.chat = chatHistory;
		this.server = server;
		
	}
	
	 @Override
     public void run() {
         try {
        	 reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             writer = new PrintWriter(socket.getOutputStream(), true);

                 String message;
                 while ((message = reader.readLine()) != null) {
                     System.out.println(message);
                     server.broadcastMessage(message);
                 }
                 
         }
         catch(IOException e) {
        	 System.out.print("Error at handlingClient: " + e.getMessage() + "\n");
        	 try {
				this.socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	 
         }
         
	 }
     public void sendMessage(String message) {
         writer.println(message);
         writer.flush();
         //this.chat.addMessage(message);
     }
     
     public void sendLastMessages() {
    	 if (writer != null) {
    	        List<String> allMessages = chat.getMessages(); 

    	        if (allMessages.size() > 0) {
    	            writer.println("Voici les 15 derniers messages:");
    	            for (int i = Math.max(0, allMessages.size() - 15); i < allMessages.size(); i++) {
    	                writer.println(allMessages.get(i));
    	            }
    	            writer.println("Fin des 15 derniers messages.");
    	            writer.flush();
    	        }
    	    } else {
    	        System.err.println("Erreur : PrintWriter non initialisé.");
    	    }
     }
     
     

}
