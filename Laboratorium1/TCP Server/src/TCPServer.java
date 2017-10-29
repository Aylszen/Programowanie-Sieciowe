import java.io.*;
import java.net.*;

class TCPServer {
	public static void main(String argv[]) throws Exception {
		String clientSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			switch (clientSentence) {
			case "LIST":
				File curDir = new File("C:\\Users\\student\\Documents\\Test");
			    getAllFiles(curDir,outToClient);
				break;
			case "SHUTDOWN":
				connectionSocket.close();
				break;
			default:
				outToClient.writeBytes(clientSentence + "\n");
				break;
			}

		}
	}
	private static void getAllFiles(File curDir,DataOutputStream outToClient) throws IOException {

        File[] filesList = curDir.listFiles();
        for(File f : filesList){
            if(f.isDirectory())
            	outToClient.writeBytes(f.getName() + "\n");
            if(f.isFile()){
            	outToClient.writeBytes(f.getName() + "\n ");
            }
        }
	}
}