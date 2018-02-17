import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerThread implements Runnable {

	protected boolean isStopped = false;
	String clientSentence;
	ServerSocket welcomeSocket = null;
	Socket connectionSocket = null;
	int PORT = 0;
	Random generator = new Random();
	InetAddress ipAddress;
	int counter = 0;

	@Override
	public void run() {
		System.out.println("Server started!");
		synchronized (this) {
			Thread.currentThread();
		}
		openServerSocket();
		while (!isStopped()) {
			try {
				connectionSocket = welcomeSocket.accept();
				counter++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DataOutputStream outToClient;
			if (counter <= 1) {
				try {
					outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					outToClient.writeBytes("Server ready" + "\n");
					counter--;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				new Thread(new ConnectionThread(connectionSocket)).start();
			} else {
				try {
					outToClient = new DataOutputStream(connectionSocket.getOutputStream());
					outToClient.writeBytes("Server busy" + "\n");
					counter--;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.connectionSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			PORT = generator.nextInt(65000);
			welcomeSocket = new ServerSocket(PORT);
			new Thread(new UDPListener(welcomeSocket)).start();
		} catch (IOException e) {
			System.out.println("PORT ALREADY TAKEN: " + PORT + "\n" + e.getMessage());
		}
	}
}
