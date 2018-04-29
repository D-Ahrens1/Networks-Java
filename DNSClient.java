/**
 * Author:
 * Date: 2/9/17
 * Description: Asks user for url and passes it via UDP to a server.
 * The server sends a response back.
 * 
 */

//package UDPApplication;


import java.io.*;
import java.net.*;

public class DNSClient {

	// port number to connect

	//private static final int DEST_PORT = 5911;
	//private static final String SERVER_NAME = "localhost"; //toolman.wiu.edu
	/**
	 * @param args
	 */
	public static void main(String[] args) { 
		String inputString;

		try
		{
			String[] hostAndPort = args[0].split(":");
			System.out.println("Host: " + hostAndPort[0] + "Port: " + hostAndPort[1]);
			String SERVER_NAME = hostAndPort[0];
			int DEST_PORT = Integer.parseInt(hostAndPort[1]);
			
			BufferedReader kbdReader = new BufferedReader(new InputStreamReader(System.in));
			
			while(true){
			System.out.println("Enter a URL:");
			
			inputString = kbdReader.readLine();
			if(inputString.equals("exit")){
				break;
			}
			
			byte[] sendBytes = inputString.getBytes();
	
			//Create IP address object from IP address of destination 
			
			InetAddress dstIP;
			dstIP = InetAddress.getByName(SERVER_NAME);
			
			// Create  UDP socket and UDP packet. Since there is no
			// connection between the devices, the destination address
			// and port number must be specified in the packet.
			DatagramPacket sendPkt = new DatagramPacket(sendBytes, inputString.length(), dstIP, DEST_PORT);
			DatagramSocket clientSocket = new DatagramSocket();
			
			//Send packet to server 
			clientSocket.send(sendPkt);
			
			byte[] receiveData = new byte[1024];
			
			//DatagramPacket object must be created for receiving data from server
			DatagramPacket recvPacket = new DatagramPacket(receiveData, receiveData.length);
			
			//Receive reply from server
			clientSocket.receive(recvPacket);
			
			String recvString = new String(recvPacket.getData(), 0, recvPacket.getLength());
			if( 0 == recvString.compareTo("Could not resolve name")){
				System.out.println(recvString);
				
			}
			else{
			String[] recvHostRecvIP = recvString.split(" ");
			System.out.println("Received URL: " + recvHostRecvIP[0] + "\nReceived IP Address: " + recvHostRecvIP[1]);
			//System.out.println("Received URL and IP " + recvString + " from Server");
			}
			clientSocket.close();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
