import java.io.*;
import java.net.*;

public class TCPKlient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		String sentence = null, usersentence = null;
		String[] usersentenceArray;
		Socket clientSocket = null;
		BufferedReader inFromServer = null, inFromUser = null;
		DataOutputStream outToServer = null;

		while (true) {
			clientSocket = new Socket("127.0.0.1", 6789);

			inFromUser = new BufferedReader(new InputStreamReader(System.in));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			usersentence = inFromUser.readLine();
			usersentenceArray = usersentence.split(" ");
			outToServer.writeBytes(usersentence + "\n");
			sentence = inFromServer.readLine();
			
			checkServerSentence(sentence, clientSocket);
			checkUserSentence(usersentenceArray, sentence);
			}
		
	}

	public static void checkUserSentence(String[] usersentenceArray, String sentence)
	{
		String[] temp = null;
		switch (usersentenceArray[0].toUpperCase()) {
		case "LIST":
			temp = sentence.split("#");
			for (int i =0;i<temp.length;i++)
				System.out.println(temp[i]);
			break;
		case "SHOW":
			temp = sentence.split("#");
			for (int i =0;i<temp.length;i++)
				System.out.println(temp[i]);
			break;
		default:
			System.out.println("FROM SERVER: " + sentence);
			break;
		}
	}
	public static void checkServerSentence(String sentence, Socket clientSocket) {
		switch (sentence.toUpperCase()) {
		case "QUIT":
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "NULL":
			System.out.println("Server was closed!!");
		default:
			break;
		}
	}
	
}
