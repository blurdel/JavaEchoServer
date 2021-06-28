import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class UdpSend extends Thread {

	private DatagramSocket socket;
	private static int destPort;
	private volatile boolean stop;
	
	
	public static void main(String[] args) {
		try {

			if (args == null || args.length == 0) {
				System.out.println("usage: UdpSender <port>");
				return;
			}

			destPort = Short.parseShort(args[0]);
			new UdpSend().start();
			System.out.println("Sending on: " + destPort);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public UdpSend() throws SocketException {
		socket = new DatagramSocket();
	}

	public void shutDown() {
		stop = true;
	}

	public void run() {
		String msg = null;
		int count = 0;

		try {

			while (!stop) {
				
				msg = "Test Message " + count;
				socket.send(new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName("255.255.255.255"), destPort));
				System.out.println("TX: " + msg);
				Thread.sleep(500);
				
				if (++count == 100) {
					stop = true;
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (socket != null) {
				socket.close();
			}
		}
		System.out.println(getClass().getSimpleName() + " Done.");
	}

}
