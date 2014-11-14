package toolBox.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceThread extends Thread{
	
	ServerSocket server = null;
	Socket socket = null;
	ServiceParameters parameters = null;
	
	@Override
	public void run()
	{
		openService();
	}
	
	public ServiceThread(ServiceParameters para)
	{
		parameters = para;
	}
	
	public ServiceThread()
	{
		parameters = new ServiceParameters();
	}
	
	
	public void openService()
	{
		BufferedReader inStream = null;
		PrintWriter oStream = null;
		try {
			server = new ServerSocket(parameters.getPort());
			socket = server.accept();
			
			String task;
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			task = inStream.readLine();
			
			System.out.println("task");
			
			ServiceJob serviceJob = ServiceParser.parse(task);
			
			String result = serviceJob.executeJob();			
			oStream = new PrintWriter(socket.getOutputStream());
			oStream.println(result);
			oStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				inStream.close();
				oStream.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

	public void shutdownService()
	{
		try {
			socket.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new ServiceThread().start();
	}
}
