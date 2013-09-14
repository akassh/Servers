package org.stonybrook.server.web;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class WebServer implements Server 
{
	public static void main(String[] args) throws IOException 
	{
		System.out.println("!!!!!SERVER STARTED!!!!!");
		ServerSocket svr = new ServerSocket(SERVER_PORT);
		ExecutorService clients = Executors.newFixedThreadPool(NUMBER_OF_CLIENTS);
		while(true)
		{
			Socket connectionSocket = svr.accept();
			clients.submit(new ClientInstance(connectionSocket));
		}
	}	
}