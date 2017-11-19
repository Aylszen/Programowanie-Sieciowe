import java.io.*;
import java.net.*;

public class TCPKlient {

	public static void main(String[] args) {
		String sentence = "", userSentence = null;
		String[] userSentenceArray;
		Socket clientSocket = null;
		BufferedReader inFromServer = null, inFromUser = null;
		DataOutputStream outToServer = null;
		final String IP_ADDRESS = "127.0.0.1";
		final int PORT = 7;
		final String LOCAL_DIRETORY = "C:\\Users\\ekrzkit\\repo\\Programowanie-Sieciowe\\Laboratorium1\\ClientDocuments";
		while (true) {

			try {
				clientSocket = new Socket(IP_ADDRESS, PORT);
				inFromUser = new BufferedReader(new InputStreamReader(System.in));
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				userSentence = inFromUser.readLine();
				userSentenceArray = userSentence.split(" ");
				outToServer.writeBytes(userSentence + "\n");
				//checkServerSentence(sentence, clientSocket, inFromServer);
				//checkClientSentence(userSentenceArray, sentence, clientSocket, inFromServer, LOCAL_DIRETORY);
				sentence = inFromServer.readLine();
				System.out.println("FROM SERVER: " + sentence);
			} catch (UnknownHostException e) {
				System.out.println("UNKNOWN HOST:" + e.getMessage());
			} catch (IOException e) {
				System.out.println("IO ERROR: \n" + e.getMessage());
			}

		}

	}

	public static void checkClientSentence(String[] usersentenceArray, String sentence, Socket clientSocket,
			BufferedReader inFromServer, String LOCAL_DIRETORY) throws IOException {
		String[] sentenceArray = null;
		switch (usersentenceArray[0].toUpperCase()) {
		case "LIST":
			sentence = inFromServer.readLine();
			sentenceArray = sentence.split("#");
			for (int i = 0; i < sentenceArray.length; i++)
				System.out.println(sentenceArray[i]);
			break;
		case "SHOW":
			sentence = inFromServer.readLine();
			sentenceArray = sentence.split("#");
			for (int i = 0; i < sentenceArray.length; i++)
				System.out.println(sentenceArray[i]);
			break;
		case "GET":
			byte[] mybytearray = new byte[1024];
			InputStream is = clientSocket.getInputStream();
			DataInputStream clientData = new DataInputStream(is);
			String fileName = clientData.readUTF();
			OutputStream os = new FileOutputStream(LOCAL_DIRETORY + "\\" + fileName);
			int bytesRead;
			long size = clientData.readLong();
			while (size > 0
					&& (bytesRead = clientData.read(mybytearray, 0, (int) Math.min(mybytearray.length, size))) != -1) {
				os.write(mybytearray, 0, bytesRead);
				size -= bytesRead;
			}
			os.close();
			os.flush();
			is.close();
			break;
		default:
			sentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + sentence);
			break;
		}
	}

	public static void checkServerSentence(String sentence, Socket clientSocket, BufferedReader inFromServer)
			throws IOException {
		sentence = inFromServer.readLine();
		switch (sentence.toUpperCase()) {
		case "QUIT":
			clientSocket.close();
			System.exit(1);
			break;
		case "SHUTDOWN":
			System.out.println("Server was closed!!");
			break;
		default:
			break;
		}
	}

}
