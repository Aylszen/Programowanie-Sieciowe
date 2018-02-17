import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import javax.naming.CommunicationException;

public class Client {
	static int PORT = 0;
	static int LAST_PORT = 0;
	static String IP_ADDRESS = "";
	static String LAST_IP_ADDRESS = "";
	static boolean flag2 = true;
	static boolean flag = true;
	static int value = 1000;

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		String sentence = "";
		BufferedReader inFromServer = null, inFromUser = null;
		DataOutputStream outToServer = null;
		
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
		while (true)
		{
			

		if (sentence.matches("Server ready")) {
			flag2 = true;
			System.out.println("Last connection to: " + LAST_IP_ADDRESS + " " + LAST_PORT);
			while (flag2) {
				flag2 = communation(inFromUser, inFromServer, outToServer, clientSocket, generator, sentence);
			}
			LAST_IP_ADDRESS = IP_ADDRESS;
			LAST_PORT = PORT;
		}
		flag = true;
		if (sentence.matches("Server busy") || flag2 == false) {
			System.out.println("Last connection to: " + LAST_IP_ADDRESS + " " + LAST_PORT);
			t = new Thread(new UDPSender());
			t.start();
			flag2 = true;
			try {
				t.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(IP_ADDRESS + " " + PORT);
			clientSocket = new Socket(IP_ADDRESS, PORT);
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			if (inFromServer.readLine().contains("Server busy"))
					continue;
			while (flag2) {
				communation(inFromUser, inFromServer, outToServer, clientSocket, generator, sentence);
			}
			LAST_IP_ADDRESS = IP_ADDRESS;
			LAST_PORT = PORT;
			flag = true;
		}
		}
	}

	public static boolean communation(BufferedReader inFromUser, BufferedReader inFromServer,
			DataOutputStream outToServer, Socket clientSocket, Random generator, String sentence)
			throws InterruptedException {

		try {

			if (flag) {
				System.out.println("Wybierz czestotliwosc wysylania do serwera(10 - 10 000ms)");
				value = Integer.parseInt(inFromUser.readLine());
				flag = false;
			}
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			Thread.sleep(value);
			outToServer.writeBytes(generator.nextInt(101) + "\n");
			sentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + sentence);
		} catch (UnknownHostException e) {
			System.out.println("UNKNOWN HOST:" + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("IO ERROR: \n" + e.getMessage());
			return false;
		}
		return true;

	}

}
