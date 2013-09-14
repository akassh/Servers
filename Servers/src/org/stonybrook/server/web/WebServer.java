package org.stonybrook.server.web;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class WebServer implements Server 
{
	public static void main(String[] args) 
	{
		System.out.println("!!!!!SERVER STARTED!!!!!");
		ServerSocket svr = null;
		try 
		{
			svr = new ServerSocket(SERVER_PORT);
			ExecutorService clients = Executors.newFixedThreadPool(NUMBER_OF_CLIENTS);
			while(true)
			{
				Socket connectionSocket = svr.accept();
				clients.submit(new ClientInstance(connectionSocket));
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				svr.close();
			} 
			catch (IOException e) 
			{
				System.out.println("Server Socket Closing Error!!!");
				e.printStackTrace();
			}
		}
	}	
}