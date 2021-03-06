import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TCPKlient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		String sentence = "", userSentence = null;
		Socket clientSocket = null;
		BufferedReader inFromServer = null, inFromUser = null;
		DataOutputStream outToServer = null;
		final String IP_ADDRESS = "127.0.0.1";
		final int PORT = 7;
		clientSocket = new Socket(IP_ADDRESS, PORT);
		while (true) {

			try {
							
				inFromUser = new BufferedReader(new InputStreamReader(System.in));
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				userSentence = inFromUser.readLine();
				outToServer.writeBytes(userSentence + "\n");
				sentence = inFromServer.readLine();
				System.out.println("FROM SERVER: " + sentence);
				
			} catch (UnknownHostException e) {
				System.out.println("UNKNOWN HOST:" + e.getMessage());
				System.exit(1);
			} catch (IOException e) {
				System.out.println("IO ERROR: \n" + e.getMessage());
				System.exit(1);
			}
			
		}
	}

}
