import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
			try {
				clientSocket = this.serverSocket.accept();

			} catch (IOException e) {
				if (isStopped()) {
					System.out.println("Server Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			new Thread(new WorkerRunnable(clientSocket, counter)).start();

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
