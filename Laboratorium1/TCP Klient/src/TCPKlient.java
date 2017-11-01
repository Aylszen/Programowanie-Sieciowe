import java.io.*;
import java.net.*;

public class TCPKlient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		String sentence = "", usersentence = null;
		String[] usersentenceArray;
		Socket clientSocket = null;
		BufferedReader inFromServer = null, inFromUser = null;
		DataOutputStream outToServer = null;
		final String IP_ADDRESS = "127.0.0.1";
		final int PORT = 6789;
		final String LOCAL_DIRETORY = "C:\\Users\\Krzysiek\\repo\\Programowanie-Sieciowe\\Laboratorium1\\ClientDocuments";
		while (true) {
			clientSocket = new Socket(IP_ADDRESS, PORT);

			inFromUser = new BufferedReader(new InputStreamReader(System.in));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			usersentence = inFromUser.readLine();
			usersentenceArray = usersentence.split(" ");
			outToServer.writeBytes(usersentence + "\n");
			// sentence = inFromServer.readLine();
			checkServerSentence(sentence, clientSocket, inFromServer);
			checkUserSentence(usersentenceArray, sentence, clientSocket, inFromServer, LOCAL_DIRETORY);
		}

	}

	public static void checkUserSentence(String[] usersentenceArray, String sentence, Socket clientSocket,
			BufferedReader inFromServer, String LOCAL_DIRETORY) throws IOException {
		String[] temp = null;
		switch (usersentenceArray[0].toUpperCase()) {
		case "LIST":
			sentence = inFromServer.readLine();
			temp = sentence.split("#");
			for (int i = 0; i < temp.length; i++)
				System.out.println(temp[i]);
			break;
		case "SHOW":
			sentence = inFromServer.readLine();
			temp = sentence.split("#");
			for (int i = 0; i < temp.length; i++)
				System.out.println(temp[i]);
			break;
		case "GET":
			byte[] mybytearray = new byte[1024];
			InputStream is = clientSocket.getInputStream();
			DataInputStream clientData = new DataInputStream(is);
			String fileName = clientData.readUTF();
			OutputStream os = new FileOutputStream(LOCAL_DIRETORY + "\\" + fileName);
			int bytesRead;
			long size = clientData.readLong();
			System.out.println("size: " + size);
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
