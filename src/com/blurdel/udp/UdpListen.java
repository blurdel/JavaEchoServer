package com.blurdel.udp;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.Inet4Address;
import java.net.SocketTimeoutException;


public class UdpListen extends Thread {

	private DatagramSocket mSocket;
	private DatagramPacket datagram;
	private byte buffer[];
	private volatile boolean mStop;
	
	
	public static void main(String[] args) {
		try {

			if (args == null || args.length == 0) {
				System.out.println("usage: UdpListen <port>");
				return;
			}
			
			int port = Short.parseShort(args[0]);
			new UdpListen(port).start();
			System.out.println("Listening on: " + port);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UdpListen(int pPort) throws SocketException {
		buffer = new byte[1024];
		datagram = new DatagramPacket(buffer, buffer.length);
		mSocket = new DatagramSocket(pPort);
		mSocket.setReceiveBufferSize(1024 * 1024);
		mSocket.setSoTimeout(100);
	}

	public void shutDown() {
		mStop = true;
	}

	public void run() {
		String pktData = null;

		try {

			while (!mStop) {

				try {

					mSocket.receive(datagram);
					pktData = new String(buffer, 0, datagram.getLength());

					System.out.println("RX: " + pktData +
								" [len=" + datagram.getLength() +
								"] from: " + ((Inet4Address) datagram.getAddress()).getHostAddress());

					if ("STOP".equals(pktData)) {
						mStop = true;
					}
				}
				catch (SocketTimeoutException e) {
					// swallow timeouts...
				}
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
