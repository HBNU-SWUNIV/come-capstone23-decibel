package exserver;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	private static final int port = 1234;
	
	public static void main(String args[]) {
		try(ServerSocket listenSocket = new ServerSocket(port)) {
			Socket socket;
			while((socket = listenSocket.accept()) != null) {
				RequestHandler requestHandler = new RequestHandler(socket);
				requestHandler.start();
				System.out.println("Success");
			}
		} catch (Exception e) {
			
		}
	}
}
