package org.usfirst.frc.team4342.robot.vision;

/**
 * Main class
 */
public class Program  {
	private static final int CONNECTION_TIMEOUT_SECONDS = 60;
	private static final int USB_PORT = 0;
	
	/**
	 * The main entry point
	 * @param args command-line arguments
	 */
	public static void main(String[] args) throws Exception {
		DemonVisionPipeline pipeline = new DemonVisionPipeline();
		DemonVision vision = new DemonVision(USB_PORT, pipeline);
		
		int numLoops = 0;
		
		System.out.print("Connecting to camera...");
		while(!vision.cameraConnected()) {
			if(numLoops >= CONNECTION_TIMEOUT_SECONDS)
				throw new java.io.IOException("Failed to connect to camera on USB port " + USB_PORT + "!");
			
			numLoops++;
			Thread.sleep(1000);
		}
		
		System.out.println("Connecting to SmartDashboard...");
		while(!vision.smartDashboardConnected()) {
			if(numLoops >= CONNECTION_TIMEOUT_SECONDS)
				throw new java.net.ConnectException("Failed to connect to SmartDashboard!");
			
			numLoops++;
			Thread.sleep(1000);
		}
		
		vision.start();
		
		System.out.println("Somehow exited an infinite loop... Ending program");
		System.exit(1);
	}
}
