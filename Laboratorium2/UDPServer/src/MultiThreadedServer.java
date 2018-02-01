import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.IOException;

public class MultiThreadedServer implements Runnable {

	protected int serverPort = 0;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	protected int counter = 0;

	public MultiThreadedServer(int port) {
		this.serverPort = port;
	}

	public void run() {
		System.out.println("Server started!");
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		System.out.println("IP: " + serverSocket.getInetAddress());
		System.out.println("PORT: " + serverSocket.getLocalPort());
		while (!isStopped()) {
			Socket clientSocket = null;
			counter++;
			if (Thread.activeCount() == 6) {
				System.out.println("Exceeded number of maximal connections!!");
				System.out.println("DISCONNECTING");
				
				stop();
			} else {
				try {
					clientSocket = this.serverSocket.accept();

				} catch (IOException e) {
					if (isStopped()) {
						System.out.println("Server Stopped.");
						return;
					}
					throw new RuntimeException("Error accepting client connection", e);
				}
				System.out.println("************************************");
				System.out.println("Client thread started\nConnection from:");
				System.out.println("IP/PORT:" + clientSocket.getRemoteSocketAddress());
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat date = new SimpleDateFormat("dd MMMMM yyyy");
				System.out.println("Date: " + date.format(cal.getTime()));
				SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
				System.out.println("Time: " + time.format(cal.getTime()));
				System.out.println("************************************");
				new Thread(new ClientThread(clientSocket, counter)).start();
			}
		}

		System.out.println("Server Stopped.");
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			InetAddress ipAddress = InetAddress.getByName("127.0.0.1");
			this.serverSocket = new ServerSocket(serverPort, 3, ipAddress);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port 7", e);
		}
	}

}
