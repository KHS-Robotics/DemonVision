package org.usfirst.frc.team4342.robot.vision;

import java.util.logging.Logger;

import org.usfirst.frc.team4342.robot.vision.api.cameras.Camera;
import org.usfirst.frc.team4342.robot.vision.api.cameras.USBCamera;
import org.usfirst.frc.team4342.robot.vision.api.listeners.Listener;
import org.usfirst.frc.team4342.robot.vision.api.listeners.SteamworksListener;
import org.usfirst.frc.team4342.robot.vision.api.pipelines.parameters.Blur;
import org.usfirst.frc.team4342.robot.vision.api.pipelines.parameters.PiplelineParameters;
import org.usfirst.frc.team4342.robot.vision.api.pipelines.parameters.RGBBounds;
import org.usfirst.frc.team4342.robot.vision.api.pipelines.parameters.Resolution;

/**
 * Main class
 */
public class Main  {
	// Microsoft LifeCam HD 3000
	private static final int USB_PORT = 0;
	private static final double FIELD_OF_VIEW = 68.5;
	
	// 360x240, Gaussian blur, and keep green targets
	private static final Resolution RESOLUTION = new Resolution(360, 240);
	private static final Blur BLUR = new Blur(Blur.Type.GAUSSIAN, 1.88);
	private static final RGBBounds RGB = new RGBBounds(0, 70, 84, 255, 0, 45);
	
	/**
	 * The main entry point
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		// Logger
		Logger logger = Logger.getLogger(DemonVision.class.getName());
		try {
			logger.addHandler(new java.util.logging.FileHandler("demon_vision.log"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		logger.info("Initalizing DemonVision...");
		
		// Camera
		Camera camera = new USBCamera(USB_PORT, FIELD_OF_VIEW);
		
		// Pipeline Parameters
		PiplelineParameters parameters = new PiplelineParameters(RESOLUTION, BLUR, RGB);
		
		// Listener
		Listener listener = new SteamworksListener(camera);
		
		// DemonVision
		DemonVision dv = new DemonVision(camera, parameters, listener);
		
		logger.info("Starting DemonVision...");
		
		// Let's do this
		dv.runForever();
	}
}
