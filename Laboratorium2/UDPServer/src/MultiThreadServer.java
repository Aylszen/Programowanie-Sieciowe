import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {
	public static void main(String argv[]) {
		String clientSentence;
		ServerSocket welcomeSocket = null;
		final int PORT = 7;
		try {
			welcomeSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("PORT ALREADY TAKEN: " + PORT + "\n" + e.getMessage());
		}
		while (true) {
			try {
				Socket connectionSocket = welcomeSocket.accept();
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));

				clientSentence = inFromClient.readLine();
				System.out.println("Received: " + clientSentence);
				outToClient.writeBytes(clientSentence + "\n");
				
			} catch (FileNotFoundException e) {
				System.out.println("FILE NOT FOUND: /n" + e.getMessage());

			} catch (IOException e) {
				System.out.println("IO ERROR: /n" + e.getMessage());
			} catch (NullPointerException e) {
				System.out.println("NULL POINTER: /n" + e.getMessage());
				return;
			}
		}
	}
}
