package com.blurdel.udp;


public class Main {
	
	public static void main(String[] args) {
		try {
			
			int port = 9999;
			new UdpListen(port).start();
			new UdpSend(port).start();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
