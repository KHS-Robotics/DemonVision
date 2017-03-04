package org.usfirst.frc.team4342.robot.vision;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
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
	
	private static final int CONNECTION_TIMEOUT_SECONDS = 120;
	private static final long TIMEOUT = 30 * 1000;
	private int numLoops;
	
	private static NetworkTable table;
	private final VideoCapture video;
	
	private long startTime;
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
		
		startTime = System.currentTimeMillis();
		
		while(true) {
			try {
				double robotYaw = table.getNumber("NavX-Yaw", 0.0);
				boolean farAngle = table.getBoolean("Shooter-Solenoid", false);
				
				image = new Mat();
				
				video.read(image);
				pipeline.process(image);
				
				//ArrayList<MatOfPoint> contours = pipeline.findContoursOutput();
				ArrayList<MatOfPoint> filteredContours = pipeline.filterContoursOutput();
				
				if(timeHasPassed(startTime, System.currentTimeMillis(), TIMEOUT)) {
					Imgcodecs.imwrite("resizedOutput.png", pipeline.resizeImageOutput());
					Imgcodecs.imwrite("blurOutput.png", pipeline.blurOutput());
					Imgcodecs.imwrite("hslThresholdOutput.png", pipeline.hslThresholdOutput());
					
//					if(contours.size() > 1) {
//						Imgcodecs.imwrite("/home/pi/testthing/contoursOutput[0].png", contours.get(0));
//						Imgcodecs.imwrite("/home/pi/testthing/contoursContoursOutput[1].png", contours.get(1));
//					}
//
//					if(filteredContours.size() > 1) {
//						Imgcodecs.imwrite("/home/pi/testthing/filteredContoursOutput[0].png", filteredContours.get(0));
//						Imgcodecs.imwrite("/home/pi/testthing/filteredContoursOutput[1].png", filteredContours.get(1));
//					}
					
					startTime = System.currentTimeMillis();
				}
				
				table.putNumber("Contours-Size", filteredContours.size());
				
				if(filteredContours.size() > 1) {
					Rect top = Imgproc.boundingRect(filteredContours.get(0));
					Rect bottom = Imgproc.boundingRect(filteredContours.get(1));

					Boiler b = new Boiler(top, bottom);
					b.publishData(table);
					
					final double ADJUSTED_YAW = VisionMath.getAdjustedYaw(robotYaw, b);
					final double SHOOTER_RPM = VisionMath.getIdealShooterRPM(farAngle, b);
					
					table.putNumber("NavX-Target-Yaw", ADJUSTED_YAW);
					table.putNumber("Shooter-Target-RPM", SHOOTER_RPM);
				}
				
				image.release();
				pipeline.releaseOutputs();
			} catch(Exception ex) {
				LOG.log(Level.SEVERE, "DemonVision has crashed!", ex);
				throw ex;
			} finally {
				table.putBoolean("DemonVision", false);
				image.release();
				pipeline.releaseOutputs();
			}
		}
	}
	
	/**
	 * Helper method to determine if a certain amount of time is passed
	 * @param start the start time, in milliseconds
	 * @param current the current time to compare with start, in milliseconds
	 * @param desiredPassedTime the desired past time, in milliseconds
	 * @return true if the time has passed, false otherwise
	 * @throws IllegalArgumentException if start time is greater than current time
	 */
	private static boolean timeHasPassed(long start, long current, long desiredPassedTime) {
		if(start > current)
			throw new IllegalArgumentException("start time cannot be greater than current time!");
		
		final long ACTUAL_TIME_PASSED = current - start;
		
		return ACTUAL_TIME_PASSED >= desiredPassedTime;
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
