package org.usfirst.frc.team4342.robot.vision;

/**
 * Main class
 */
public class Program  {
	private static final int USB_PORT = 0;
	
	/**
	 * The main entry point
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		DemonVisionPipeline pipeline = new DemonVisionPipeline();
		DemonVision vision = new DemonVision(USB_PORT, pipeline);
		
		try {
			vision.start();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println("Somehow exited an infinite loop... Ending program");
		System.exit(1);
	}
}
