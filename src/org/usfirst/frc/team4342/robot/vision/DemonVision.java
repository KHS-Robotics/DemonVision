package org.usfirst.frc.team4342.robot.vision;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class DemonVision {
	static {
		NetworkTable.setClientMode();
		NetworkTable.setNetworkIdentity("raspberry-pi-3");
		NetworkTable.setIPAddress("roborio-4342-frc.local");
	}
	
	private Logger LOG;
	
	private static final int CONNECTION_TIMEOUT_SECONDS = 120 * 4;
	private int numLoops;
	
	private static NetworkTable table;
	private final VideoCapture video;

	private Mat image;
	
	private int usbPort;
	private DemonVisionPipeline pipeline;
	
	/**
	 * Creates a new <code>DemonVision</code> object that calculates
	 * proper output values for the robot
	 * @param usbPort the USB port of the camera
	 * @param pipeline the pipeline that processes the image
	 */
	public DemonVision(int usbPort, DemonVisionPipeline pipeline) {
		this.usbPort = usbPort;
		this.pipeline = pipeline;
		
		video = new VideoCapture();
		video.open(this.usbPort);
		
		table = NetworkTable.getTable("SmartDashboard");
		
		try {
			LOG = Logger.getLogger(DemonVision.class.getName());
			LOG.addHandler(new java.util.logging.FileHandler("demon_vision.log"));
		} catch (SecurityException | IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Gets if Demon Vision is connected to the camera and Smart Dashboard
	 * @return true if Demon Vision is connected, falseo otherwise
	 */
	public boolean connected() {
		return cameraConnected() && smartDashboardConnected();
	}
	
	/**
	 * Gets if Demon Vision is connected to the camera
	 * @return true if conencted, false otherwise
	 */
	public boolean cameraConnected() {
		return video.isOpened();
	}
	
	/**
	 * Gets if Demon Vision is connected to the Smart Dashboard
	 * @return true if conencted, false otherwise
	 */
	public boolean smartDashboardConnected() {
		return table.isConnected();
	}
	
	/**
	 * Starts Demon Vision. The method iterates indefinitely with the following sequence:
	 * <p>
	 * 	<ol>
	 * 	 <li>Get robotYaw and farAngle from the Smart Dashboard</li>
	 * 	 <li>Read the image from the camera</li>
	 * 	 <li>Process the image</li>
	 * 	 <li>Put the number of contours found from the processed image on the Smart Dashboard</li>
	 * 	 <li>If the number of contours is greater than one:</li>
	 * 	 <ol>
	 * 	  <li>Get the top and bottom contours as bounding rectangles</li>
	 * 	  <li>Calculate ratios of the image of the boiler based on the rectangles</li>
	 * 	  <li>Publish calculated ratios to the Smart Dashboard</li>
	 * 	  <li>Calculate the desired robot yaw and shooter RPM based on top center X and Y ratios</li>
	 * 	  <li>Publish the desired yaw and shooter RPM to the Smart Dashboard</li>
	 * 	 </ol>
	 * 	</ol>
	 * </p>
	 * @throws Exception if any error occurrs while starting/running
	 */
	public void start() throws Exception {
		LOG.info("Connecting to camera...");
		while(!cameraConnected()) {
			if(numLoops >= CONNECTION_TIMEOUT_SECONDS) {
				java.io.IOException ex = new java.io.IOException();
				LOG.log(Level.SEVERE, ex.getMessage(), ex);
				throw ex;
			}
			
			numLoops++;
			Thread.sleep(250);
		}
		
		LOG.info("Connecting to SmartDashboard...");
		while(!smartDashboardConnected()) {
			if(numLoops >= CONNECTION_TIMEOUT_SECONDS) {
				java.net.ConnectException ex = new java.net.ConnectException("Failed to connect to SmartDashboard!");
				LOG.log(Level.SEVERE, ex.getMessage(), ex);
				throw ex;
			}
			
			numLoops++;
			Thread.sleep(250);
		}
		
		table.putBoolean("DemonVision", true);
		
		LOG.info("Started vision pipeline.");
		
		try {
			while(true) {
				double robotYaw = table.getNumber("NavX-Yaw", 0.0);
				
				image = new Mat();
				
				video.read(image);
				pipeline.process(image);
				
				ArrayList<MatOfPoint> filteredContours = pipeline.filterContoursOutput();
				
				table.putNumber("Contours-Size", filteredContours.size());
				
				if(filteredContours.size() > 1) {
					Rect top = Imgproc.boundingRect(filteredContours.get(0));
					Rect bottom = Imgproc.boundingRect(filteredContours.get(1));

					Boiler b = new Boiler(top, bottom);
					
					final double ADJUSTED_YAW = VisionMath.getAdjustedYaw(robotYaw, b);
					
					table.putNumber("Boiler-Yaw", ADJUSTED_YAW);
				}
				
				image.release();
				pipeline.releaseOutputs();
			}
		} catch(Exception ex) {
			LOG.log(Level.SEVERE, "DemonVision has crashed!", ex);
			throw ex;
		} finally {
			image.release();
			pipeline.releaseOutputs();
			table.putBoolean("DemonVision", false);
		}
	}
	
	/**
	 * Frees all processed images
	 */
	@Override
	protected void finalize() {
		image.release();
		pipeline.releaseOutputs();
	}
}
