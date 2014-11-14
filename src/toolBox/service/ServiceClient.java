package toolBox.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServiceClient {
	public static void main(String[] args)
	{
			try {
				Socket socket = new Socket("192.168.1.13",2047);
				
				PrintWriter oStream=new PrintWriter(socket.getOutputStream());
				String command = "just for hadoop tool box";
				oStream.println(command);
				oStream.flush();
				
				BufferedReader inStream=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String result = inStream.readLine();
				
				System.out.println(result);
				
				inStream.close();
				oStream.close();
				
				socket.close();
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
