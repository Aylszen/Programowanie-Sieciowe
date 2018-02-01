import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client {
	static int PORT = 0;
	static String IP_ADDRESS = "";

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		String sentence = "";
		BufferedReader inFromServer = null, inFromUser = null;
		DataOutputStream outToServer = null;
		int value;
		Thread t;
		Random generator = new Random();
		t = new Thread(new UDPSender());
		t.start();
		try {
			t.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(IP_ADDRESS + " " + PORT);
		Socket clientSocket = new Socket(IP_ADDRESS, PORT);
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		sentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + sentence);
		System.out.println("Wybierz czestotliwosc wysylania do serwera(10 - 10 000ms)");
		value = Integer.parseInt(inFromUser.readLine());
		if (sentence.matches("Server ready")) {
			while (true) {
				try {
					outToServer = new DataOutputStream(clientSocket.getOutputStream());
					Thread.sleep(value);
					outToServer.writeBytes(generator.nextInt(101) + "\n");
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
		if(sentence.matches("Server busy"))
		{
			clientSocket.close();
		}
	}

}
