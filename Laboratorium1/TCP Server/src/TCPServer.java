import java.io.*;
import java.net.*;

class TCPServer {
	@SuppressWarnings("resource")
	public static void main(String argv[]) {
		String clientSentence;
		ServerSocket welcomeSocket = null;
		final int PORT = 6789;
		try {
			welcomeSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("PORT ALREADY TAKEN: " + PORT + "\n" + e.getMessage());
		}
		String[] clientSentenceArray;
		final String LOCAL_DIRETORY = "C:\\Users\\ekrzkit\\repo\\Programowanie-Sieciowe\\Laboratorium1\\ServerDocuments";
		File curDir = new File(LOCAL_DIRETORY);

		while (true) {
			try {
				Socket connectionSocket = welcomeSocket.accept();
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));

				clientSentence = inFromClient.readLine();
				System.out.println("Received: " + clientSentence);
				clientSentenceArray = clientSentence.split(" ");
				outToClient.writeBytes(clientSentence + "\n");
				checkClientSentence(clientSentence, clientSentenceArray, LOCAL_DIRETORY, curDir, connectionSocket,
						outToClient);

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

	private static void checkClientSentence(String clientSentence, String[] clientSentenceArray,
			final String LOCAL_DIRETORY, File curDir, Socket connectionSocket, DataOutputStream outToClient)
			throws IOException, FileNotFoundException {
		switch (clientSentenceArray[0].toUpperCase()) {
		case "LIST":
			outToClient.writeBytes(getAllFiles(curDir) + "\n");
			break;
		case "SHUTDOWN":
			outToClient.writeBytes(clientSentence + "\n");
			connectionSocket.close();
			System.exit(1);
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
			break;
		case "GET":

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
			bis.close();
			break;

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