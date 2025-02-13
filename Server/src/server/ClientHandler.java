package server;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;



public class ClientHandler implements Runnable {
	private Server server;
	private Socket socket;
	private static ChatHistory chat;
	private PrintWriter writer;
    private BufferedReader reader;
	
	public ClientHandler(Socket socket, ChatHistory chatHistory, Server server) {
		this.socket = socket;
		ClientHandler.chat = chatHistory;
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
         ClientHandler.chat.addMessage(message);
     }
     
//     public static void sendLastMessages() {
//    	 writer.println("Voici les 15 derniers messages: "){
//    		 
//    	 }
//     }
     
     

}
