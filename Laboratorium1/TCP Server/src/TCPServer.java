import java.io.*;
import java.net.*;

class TCPServer {
	public static void main(String argv[]) throws Exception {
		String clientSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		String[] clientSentenceArray;
		final String LOCAL_DIRETORY = "C:\\Users\\Krzysiek\\repo\\Programowanie-Sieciowe\\Laboratorium1\\ServerDocuments";
		File curDir = new File(LOCAL_DIRETORY);
		
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			clientSentenceArray = clientSentence.split(" ");

			checkClientSentence(clientSentence, clientSentenceArray, LOCAL_DIRETORY, curDir, connectionSocket,
					outToClient);
		}
	}

	private static void checkClientSentence(String clientSentence, String[] clientSentenceArray,
			final String LOCAL_DIRETORY, File curDir, Socket connectionSocket, DataOutputStream outToClient)
			throws IOException, FileNotFoundException {
		switch (clientSentenceArray[0].toUpperCase()) {
		case "LIST":
			outToClient.writeBytes(getAllFiles(curDir) + "\n");
			break;
		case "SHUTDOWN":
			connectionSocket.close();
			break;
		case "SHOW":
			FileReader file = new FileReader(
					curDir.getAbsolutePath().replaceAll("\\\\", "/") + "/" + clientSentenceArray[1]);
			String line, allLines = "";
			BufferedReader br = new BufferedReader(file);
			while ((line = br.readLine()) != null) {
				allLines += line;
				allLines += "#";
			}
			outToClient.writeBytes(allLines + "\n");
			file.close();
		case "GET":
			if (clientSentenceArray[1]!="")
			{
				File myFile = new File(LOCAL_DIRETORY + "\\" + clientSentenceArray[1]);

				byte[] mybytearray = new byte[(int) myFile.length()];
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
				bis.read(mybytearray, 0, mybytearray.length);
				OutputStream os = connectionSocket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				dos.writeUTF(myFile.getName());
				dos.writeLong(mybytearray.length);
				os.write(mybytearray, 0, mybytearray.length);
				os.flush();
				os.close();
				dos.close();
				break;
			}
			
		default:
			outToClient.writeBytes(clientSentence + "\n");
			break;

		}
	}

	private static String getAllFiles(File curDir) throws IOException {
		String listSentence = "Lista plikow:#";
		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			if (f.isFile())
				listSentence += (f.getName() + "#");
		}
		return listSentence;
	}
}