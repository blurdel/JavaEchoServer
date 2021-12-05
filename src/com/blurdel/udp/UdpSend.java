package com.blurdel.udp;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class UdpSend extends Thread {

	private DatagramSocket mSocket;
	private int mPort;
	private volatile boolean mStop;
	
	
	public static void main(String[] args) {
		try {

			if (args == null || args.length == 0) {
				System.out.println("usage: UdpSend <port>");
				return;
			}

			int port = Short.parseShort(args[0]);			
			new UdpSend(port).start();
			System.out.println("Sending on: " + port);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public UdpSend(int pPort) throws SocketException {
		mSocket = new DatagramSocket();
		mPort = pPort;
	}

	public void shutDown() {
		mStop = true;
	}

	public void run() {
		String msg = null;
		int count = 0;

		try {

			while (!mStop) {
				
				msg = "Test Message " + count;
				
				if (++count == 100) {
					msg = "STOP";
					mStop = true;
				}				
				
				mSocket.send(new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName("255.255.255.255"), mPort));
				System.out.println("TX: " + msg);
				Thread.sleep(100);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (mSocket != null) {
				mSocket.close();
			}
		}
		System.out.println(getClass().getSimpleName() + " Done.");
	}

}
