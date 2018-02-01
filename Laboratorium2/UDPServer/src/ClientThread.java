import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.server.ServerCloneException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ClientThread implements Runnable {

	protected Socket clientSocket = null;
	int counter = 0;
	String clientSentence;

	public ClientThread(Socket clientSocket, int counter) {
		this.clientSocket = clientSocket;
		this.counter = counter;
	}

	public void run() {
		try {
			while (true){
				DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				clientSentence = inFromClient.readLine();
				System.out.println("Received: " + clientSentence);
				outToClient.writeBytes(clientSentence + "\n");
			}
		} catch (IOException e) {
			// report exception somewhere.
			e.printStackTrace();
		}
	}
}