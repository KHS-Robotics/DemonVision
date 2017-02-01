package org.usfirst.frc.team4342.robot.vision;

/**
 * Main class
 */
public class Program  {
	private static final String DEFAULT_IP = "10.43.42.16";
	
	/**
	 * The main entry point
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		final String IP = args.length != 0 ? args[0] : DEFAULT_IP;
		
		DemonVisionPipeline pipeline = new DemonVisionPipeline();
		DemonVision vision = new DemonVision(IP, pipeline);
		
		while(!vision.connected()) {
			// Wait until we are connected
		}
		
		vision.start();
		
		System.out.println("Somehow exited an infinite loop... Ending program");
		System.exit(1);
	}
}
