package org.usfirst.frc.team4342.robot.vision;

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
		
		LOG = Logger.getLogger(DemonVision.class.getName());
		
		video = new VideoCapture();
		video.open(this.usbPort);
		
		table = NetworkTable.getTable("SmartDashboard");
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
	 * 	 <ol>
	 * 	</ol>
	 * </p>
	 */
	public void start() {
		if(!connected()) {
			LOG.log(Level.WARNING, "Cannot start DemonVision because we're not connected yet!");
			return;
		}
		
		LOG.log(Level.INFO, "Starting vision pipeline...");
		
		table.putBoolean("DemonVision", true);
		
		while(true) {
			try {
				double robotYaw = table.getNumber("NavX-Yaw", 0.0);
				boolean farAngle = table.getBoolean("Shooter-Solenoid", false);
				
				image = new Mat();
				
				video.read(image);
				pipeline.process(image);
				
				ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
				
				table.putNumber("Contours-Size", contours.size());
				
				if(contours.size() > 1) {
					Rect top = Imgproc.boundingRect(contours.get(0));
					Rect bottom = Imgproc.boundingRect(contours.get(1));

					Boiler b = new Boiler(top, bottom);
					b.publishData(table);
					
					final double ADJUSTED_YAW = VisionMath.getAdjustedYaw(robotYaw, b.getTopCenterXRatio());
					final double SHOOTER_RPM = VisionMath.getIdealShooterRPM(farAngle, b.getTopCenterYRatio());
					
					table.putNumber("NavX-Target-Yaw", ADJUSTED_YAW);
					table.putNumber("Shooter-Target-RPM", SHOOTER_RPM);
				}
				
				image.release();
				pipeline.releaseOutputs();
				
				Thread.sleep(200); // temporary
			} catch(Exception ex) {
				LOG.log(Level.SEVERE, "Exception in DemonVision.start()!", ex);
				break;
			}
		}
		
		table.putBoolean("DemonVision", false);
	}
}
