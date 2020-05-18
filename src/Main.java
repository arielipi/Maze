import controller.Controller;


public class Main {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread controllerThread = new Thread(new Controller());
		controllerThread.run();
	}
}
