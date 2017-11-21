
public class TCPServer {
	public static void main(String argv[]) {
		
		final int PORT = 7;
		
		MultiThreadedServer server = new MultiThreadedServer(PORT);
		new Thread(server).start();

		try {
			Thread.sleep(20 * 10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();
	}
}
