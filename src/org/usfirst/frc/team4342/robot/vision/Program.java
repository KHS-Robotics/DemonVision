package org.usfirst.frc.team4342.robot.vision;

/**
 * Main class
 */
public class Program  {
	private static final int CONNECTION_TIMEOUT_SECONDS = 60;
	private static final String DEFAULT_IP = "10.43.42.16";
	
	/**
	 * The main entry point
	 * @param args command-line arguments
	 */
	public static void main(String[] args) throws Exception {
		final String IP = args.length != 0 ? args[0] : DEFAULT_IP;
		
		DemonVisionPipeline pipeline = new DemonVisionPipeline();
		DemonVision vision = new DemonVision(IP, pipeline);
		
		System.out.print("Connecting...");
		
		int numLoops = 0;
		while(!vision.connected()) {
			if(numLoops >= CONNECTION_TIMEOUT_SECONDS)
				throw new java.net.ConnectException("Failed to connect Pi to camera/SmartDashboard!");
			
			System.out.print(".");
			numLoops++;
			
			Thread.sleep(1000);
		}
		
		vision.start();
		
		System.out.println("Somehow exited an infinite loop... Ending program");
		System.exit(1);
	}
}
