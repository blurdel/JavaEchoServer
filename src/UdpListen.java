import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.Inet4Address;
import java.net.SocketTimeoutException;


public class UdpListen extends Thread {

	private DatagramSocket socket;
	private DatagramPacket datagram;
	private byte buffer[];
	private volatile boolean stop;
	
	
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

	public UdpListen(int port) throws SocketException {
		buffer = new byte[1024];
		datagram = new DatagramPacket(buffer, buffer.length);
		socket = new DatagramSocket(port);
		socket.setReceiveBufferSize(1024 * 1024);
		socket.setSoTimeout(100);
	}

	public void shutDown() {
		stop = true;
	}

	public void run() {
		String pktData = null;

		try {

			while (!stop) {

				try {

					socket.receive(datagram);
					pktData = new String(buffer, 0, datagram.getLength());

					System.out.println("RX: " + pktData +
								" [len=" + datagram.getLength() +
								"] from: " + ((Inet4Address) datagram.getAddress()).getHostAddress());

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
			if (socket != null) {
				socket.close();
			}
		}
		System.out.println(getClass().getSimpleName() + " Done.");
	}

}
