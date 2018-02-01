import java.awt.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class UDPReceiver implements Runnable {

	List list;
	String msgRec;
	MulticastSocket mcSocket;
	DatagramPacket packet;
	public UDPReceiver(List list, MulticastSocket mcSocket) {
		this.list = list;
		this.mcSocket = mcSocket;
		this.packet = new DatagramPacket(new byte[1024], 1024);
	}

	@Override
	public void run() {
		while (true) {
			//System.out.println(System.currentTimeMillis());
			try {
				mcSocket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msgRec = new String(packet.getData(), packet.getOffset(), packet.getLength());
			System.out.println("Received: " + msgRec);
			if (msgRec != "") {
				list.add(msgRec);
			}
		}

	}

}
