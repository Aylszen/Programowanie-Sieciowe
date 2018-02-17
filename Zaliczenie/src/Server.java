
public class Server {

	public static void main(String[] args) {
		ServerThread server = new ServerThread();
		new Thread(server).start();

		try {
			Thread.sleep(20 * 10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stopping Server");
	}
}
