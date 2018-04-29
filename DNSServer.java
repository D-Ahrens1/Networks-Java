/**
 * 
 * Author:  
 * Date: 2/9/17
 * Description: UDP server that checks hosts.txt for url and ip address, if it can't find any
 * then it references to InetAddress to send a result back to the client
 */
//package UDPApplication;

import java.io.*; 
import java.net.*;
import java.nio.file.*;
import java.net.InetAddress; 

public class DNSServer {

	// port number to listen to
	//private static final int DEST_PORT = 5911;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try
		{
			//String port = args[0];
         String port = "3333";
			System.out.println("Starting server on port: " + port);
			int DEST_PORT = Integer.parseInt(port);
			
			//Create local port and bind to DEST_PORT
			DatagramSocket serverSocket = new DatagramSocket(DEST_PORT); 	  
			
			//Server always listens for incoming connections
			while(true) 
	        {
				byte[] receiveData = new byte[1024]; 
				
				//Create UDP packet object to receive packet from client
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
				System.out.println("Waiting for Client");
				
				// Blocking receive
				serverSocket.receive(receivePacket); 
				
				//Client packet contains source IP address and port number.
				//Get these values to reply back to client
				InetAddress clientIP = receivePacket.getAddress();
				int clientPort = receivePacket.getPort();
				
				//Get text from client and convert to upper case
				String recvString = new String(receivePacket.getData(), 0, receivePacket.getLength());    //String recvString = new String(receivePacket.getData());
				//recvString = recvString.toUpperCase();
				
				if(false==recvString.endsWith(".com") && false==recvString.endsWith(".edu")){
					recvString = recvString + ".wiu.edu";
				}
				if(false==recvString.startsWith("www.")&&false==recvString.endsWith(".wiu.edu")){
					recvString = "www." + recvString;
				}
				//System.out.println(recvString);
				Path path = FileSystems.getDefault().getPath("hosts.txt");
				InputStream in = Files.newInputStream(path);
				BufferedReader fileReader = new BufferedReader(new InputStreamReader(in));
				
			    String output;
			    String[] outArray;
				boolean found = false;
				while((output = fileReader.readLine()) != null){
					//output = fileReader.readLine();
					outArray = output.split(" ");
					if(0 == outArray[0].compareTo(recvString)){

						found = true;
						recvString = output;
					}
				
				}
				
				if(found == true){
				//Create reply packet to send back to client
				DatagramPacket sendPacket = new DatagramPacket(recvString.getBytes(), recvString.length(), clientIP, clientPort);
				
				//Send packet to client
				serverSocket.send(sendPacket);
				System.out.println("Finished processing");
				}
				if(found == false){
					//InetAddress address = InetAddress.getByName(recvString);
					try{
					InetAddress address = InetAddress.getByName(recvString);
					String temp = address.toString();
					String[] ip = temp.split("/");
					recvString = ip[0] + " " + ip[1];
					recvString = "Could not resolve name";
					DatagramPacket sendPacket = new DatagramPacket(recvString.getBytes(), recvString.length(), clientIP, clientPort);
					//Send packet to client
					serverSocket.send(sendPacket);
					System.out.println("Finished processing");
					found = true;
					}
					catch(UnknownHostException ex){
						recvString = "Could not resolve name";
						DatagramPacket sendPacket = new DatagramPacket(recvString.getBytes(), recvString.length(), clientIP, clientPort);
						//Send packet to client
						serverSocket.send(sendPacket);
						System.out.println("Finished processing");
						continue;
					}
					
				}
				
	        }              
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
