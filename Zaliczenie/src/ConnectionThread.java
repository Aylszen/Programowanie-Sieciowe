import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionThread implements Runnable {

	Socket connectionSocket;

	public ConnectionThread(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	@Override
	public void run() {
		while (true) {
			try {
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));
				String clientSentence = inFromClient.readLine();
				System.out.println("Received[" + connectionSocket.getRemoteSocketAddress() + "]" + clientSentence);
				outToClient.writeBytes(clientSentence + "\n");

			} catch (FileNotFoundException e) {
				System.out.println("FILE NOT FOUND: " + e.getMessage());
				return;
			} catch (IOException e) {
				System.out.println("IO ERROR: " + e.getMessage());
				try {
					connectionSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			} catch (NullPointerException e) {
				System.out.println("NULL POINTER: " + e.getMessage());
				return;
			}
		}
	}

}
