import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.SocketException;

public class UDPListener implements Runnable{

	ServerSocket welcomeSocket;
	public UDPListener(ServerSocket welcomeSocket)
	{
		this.welcomeSocket = welcomeSocket;
	}
	
	public void begin()
	{
		System.out.println("NASLUCHIWANIE");
		System.out.println("IP: " + welcomeSocket.getInetAddress().getHostAddress());
		System.out.println("PORT: " + welcomeSocket.getLocalPort());
	}
	
	private DatagramSocket udpSocket;
	@Override
	public void run() {
		begin();
		int mcPort = 7;
	    String mcIPStr = "230.1.1.1";
	    MulticastSocket mcSocket = null;
	    InetAddress mcIPAddress = null;
	    String msgRec ="";
	    DatagramPacket packet;
	    while (true)
	    {
	    	try {
				mcIPAddress = InetAddress.getByName(mcIPStr);
				mcSocket = new MulticastSocket(mcPort);
			    System.out.println("Multicast Receiver running at:"
			        + mcSocket.getLocalSocketAddress());
			    mcSocket.joinGroup(mcIPAddress);

			    packet = new DatagramPacket(new byte[1024], 1024);

			    System.out.println("Waiting for a  multicast message...");
			    mcSocket.receive(packet);
			    msgRec = new String(packet.getData(), packet.getOffset(),
			        packet.getLength());
			    System.out.println("[Multicast  Receiver] Received:" + msgRec);
			    if (msgRec.matches("DISCOVERY"))
			    {
			    	System.out.println("Weszlo");
			    	try {
						udpSocket = new DatagramSocket();
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			    	String temp = "OFFER ADDRESS PORT " + welcomeSocket.getInetAddress().getHostAddress() + " " + welcomeSocket.getLocalPort();
					byte[] msgSent = temp.getBytes();
					packet = new DatagramPacket(msgSent, msgSent.length);
					System.out.println("[Multicast  Receiver] Sent");
					packet.setAddress(mcIPAddress);
					packet.setPort(mcPort);
					try {
						Thread.sleep(300);
						udpSocket.send(packet);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }

			   // mcSocket.leaveGroup(mcIPAddress);
			  //  mcSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    
	}

}
