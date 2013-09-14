package org.stonybrook.server.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
	
	private void sendFile(String filename) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
		if(!(new File(filename).exists()))
		{
			bw.write(ERROR_404 + "\n");
			bw.close();
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String temp = null;
		while((temp = br.readLine())!=null)
		{
			bw.write(temp + "\n");
		} 
		bw.close();
		br.close();
	}
}