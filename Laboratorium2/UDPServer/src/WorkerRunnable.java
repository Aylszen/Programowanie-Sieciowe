import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WorkerRunnable implements Runnable {

	protected Socket clientSocket = null;
	int counter = 0;
	String clientSentence;

	public WorkerRunnable(Socket clientSocket, int counter) {
		this.clientSocket = clientSocket;
		this.counter = counter;
	}

	public void run() {
		try {
			System.out.println("************************************");
			System.out.println("Client thread started\nConnection from:");
			System.out.println("IP/PORT:" + clientSocket.getRemoteSocketAddress());
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat date = new SimpleDateFormat("dd MMMMM yyyy");
			System.out.println("Date: " + date.format(cal.getTime()));
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
			System.out.println("Time: " + time.format(cal.getTime()));
			System.out.println("************************************");
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			outToClient.writeBytes(clientSentence + "\n");
			this.counter--;
		} catch (IOException e) {
			// report exception somewhere.
			e.printStackTrace();
		}
	}
}