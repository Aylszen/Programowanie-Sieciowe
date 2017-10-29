import java.io.*;
import java.net.*;

public class TCPKlient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		String sentence = null, usersentence = null;
		Socket clientSocket = null;
		BufferedReader inFromServer = null, inFromUser = null;
		DataOutputStream outToServer = null;
				
		while (true){
		clientSocket = new Socket("127.0.0.1", 6789);
			
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		usersentence = inFromUser.readLine();
		outToServer.writeBytes(usersentence + "\n");

		sentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + sentence);
		
		switch (sentence) {
		case "QUIT":
			clientSocket.close();
			break;
		case "null":
			System.out.println("Server was closed!!");
		default:
			break;
			}
		}
	}
}
