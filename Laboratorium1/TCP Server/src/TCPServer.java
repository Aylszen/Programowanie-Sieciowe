import java.io.*;
import java.net.*;

class TCPServer {
	public static void main(String argv[]) throws Exception {
		String clientSentence, listSentence = null;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		String[] clientSentenceArray;
		File curDir = new File("C:\\Users\\Krzysiek\\repo\\Programowanie-Sieciowe\\Laboratorium1\\Documents");
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			clientSentenceArray = clientSentence.split(" ");
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
				//allLines.replaceAll("\n", "#");
				outToClient.writeBytes(allLines +"\n");
				file.close();
				break;
			default:
				outToClient.writeBytes(clientSentence + "\n");
				break;

			}
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