import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static void main(String argv[]) {
		
		final int PORT = 7;
		
		MultiThreadedServer server = new MultiThreadedServer(PORT);
		new Thread(server).start();

		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();
	}
}
