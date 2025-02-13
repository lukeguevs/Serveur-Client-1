package server;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;



public class ClientHandler implements Runnable {
	private Socket socket;
	private static ChatHistory chat;
	private static PrintWriter writer;
    private static BufferedReader reader;
	
	public ClientHandler(Socket socket, ChatHistory chatHistory) {
		this.socket = socket;
		ClientHandler.chat = chatHistory;
	}
	
	 @Override
     public void run() {
         try {
        	 reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             writer = new PrintWriter(socket.getOutputStream(), true);

                 String message;
                 while ((message = reader.readLine()) != null) {
                     System.out.println(message);
                     Server.broadcastMessage(message);
                 }
                 
         }
         catch(IOException e) {
        	 System.out.print("Error at handlingClient: " + e.getMessage() + "\n");
         }
         
	 }
     public void sendMessage(String message) {
         writer.println(message);
         ClientHandler.chat.addMessage(message);
     }
     
//     public static void sendLastMessages() {
//    	 writer.println("Voici les 15 derniers messages: "){
//    		 
//    	 }
//     }
     
     

}
