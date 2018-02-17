import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPSender implements Runnable {

	private DatagramSocket udpSocket;

	List list = new List();
	String choice = "";

	@Override
	public void run() {
		int mcPort = 7;
		String mcIPStr = "230.1.1.1";
		MulticastSocket mcSocket = null;

		String msgRec = "";
		try {
			udpSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InetAddress mcIPAddress = null;
		try {
			mcIPAddress = InetAddress.getByName(mcIPStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatagramPacket packet = null;
		packet = sendDiscovery(packet, mcIPStr, mcIPAddress, mcPort);
		try {
			udpSocket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Sent a  multicast message.");
		try {
			mcIPAddress = InetAddress.getByName(mcIPStr);
			mcSocket = new MulticastSocket(mcPort);
			mcSocket.joinGroup(mcIPAddress);

			packet = new DatagramPacket(new byte[1024], 1024);

			System.out.println("Waiting for a  multicast message...");
			boolean flag = true;
			int k = 999;
			int sleepTime = 3000;
			while (flag) {
				Thread udpReceiver;
				udpReceiver = new Thread(new UDPReceiver(list, mcSocket));
				udpReceiver.start();
				udpReceiver.sleep(sleepTime);

				for (int j = 0; j < list.getItemCount(); j++) {
					System.out.println("Received[" + j + "]: " + list.getItem(j));
				}
				System.out.println("Pick a server [0-" + (list.getItemCount() - 1) + "] ");
				BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
				k = Integer.parseInt(inFromUser.readLine());
				flag = false;
				udpReceiver.stop();
				if (k == 999 || list.getItemCount() == 0) {
					sleepTime = 15000;
					flag = true;
					packet = sendDiscovery(packet, mcIPStr, mcIPAddress, mcPort);
					try {
						udpSocket.send(packet);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Sent a  multicast message.");
				}
			}
			String[] tempTab;

			tempTab = list.getItem(k).split(" ");
			System.out.println(tempTab[0]);
			System.out.println(tempTab[1]);
			System.out.println(tempTab[2]);
			Client.IP_ADDRESS = tempTab[3];
			Client.PORT = Integer.parseInt(tempTab[4]);

			mcSocket.leaveGroup(mcIPAddress);
			mcSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		udpSocket.close();
	}

	public DatagramPacket sendDiscovery(DatagramPacket packet, String mcIPStr, InetAddress mcIPAddress, int mcPort) {
		byte[] msg = "DISCOVERY".getBytes();
		packet = new DatagramPacket(msg, msg.length);
		packet.setAddress(mcIPAddress);
		packet.setPort(mcPort);
		return packet;
	}
}
