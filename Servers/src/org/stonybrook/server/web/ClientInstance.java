package org.stonybrook.server.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

class ClientInstance implements Server, Callable<String>
{
	private Socket connectionSocket = null;
	
	ClientInstance(Socket connectionSocket)
	{
		this.connectionSocket = connectionSocket;
	}
	
	@Override
	public String call() throws Exception 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		String temp = "";
		String filename = null;
		while((temp = br.readLine())!=null)
		{
			if(temp.contains("GET /"))
			{
				filename = temp.substring(temp.indexOf("/")+1,temp.indexOf("HTTP/1.1"));
				if(filename.trim().isEmpty())
					sendFile(DEFAULT_FILE);
				else if(filename.contains(".html"))
					sendFile(filename);
			}	
		}
		return null;
	}
	
	private void sendFile(String filename)
	{
		BufferedWriter bw = null;
		BufferedReader br = null;
		try 
		{
			bw = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
			br = new BufferedReader(new FileReader(new File(filename)));
			String temp = null;
			while((temp = br.readLine())!=null)
			{
				bw.write(temp + "\n");
			}
		} 
		catch (FileNotFoundException e) 
		{
			try 
			{
				bw.write(ERROR_404);
			} 
			catch (IOException e1) 
			{
				System.out.println("HTTP SEND ERROR!!!");
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		catch(IOException e)
		{
			System.out.println("HTTP SEND ERROR!!!");
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				bw.close();
				br.close();
			} 
			catch (IOException e) 
			{
				System.out.println("Error Occured While closing the html files");
				e.printStackTrace();
			}
			
		}
	}
}