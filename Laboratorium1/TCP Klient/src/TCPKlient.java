import java.io.*;
import java.net.*;

public class TCPKlient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		String sentence = null, usersentence = "temp";
		Socket clientSocket = null;
		BufferedReader inFromServer = null;
		clientSocket = new Socket("127.0.0.1", 6789);
		while (true) {
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream outToServer = null;
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			usersentence = inFromUser.readLine();
			outToServer.writeBytes(usersentence + "\n");
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			sentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + sentence);
			switch (sentence) {
			case "QUIT":
				clientSocket.close();
				break;
			default:
				break;
			}
			}
	}
}
