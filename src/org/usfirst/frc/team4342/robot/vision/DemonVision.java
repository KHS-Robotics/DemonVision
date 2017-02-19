package org.usfirst.frc.team4342.robot.vision;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
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
	
	public DemonVision(int usbPort, DemonVisionPipeline pipeline) {
		this.usbPort = usbPort;
		this.pipeline = pipeline;
		
		LOG = Logger.getLogger(DemonVision.class.getName());
		
		video = new VideoCapture();
		video.open(this.usbPort);
		
		table = NetworkTable.getTable("SmartDashboard");
	}
	
	public boolean connected() {
		return cameraConnected() && smartDashboardConnected();
	}
	
	public boolean cameraConnected() {
		return video.isOpened();
	}
	
	public boolean smartDashboardConnected() {
		return table.isConnected();
	}
	
	public void start() {
		if(!connected()) {
			LOG.log(Level.WARNING, "Cannot start DemonVision because we're not connected yet!");
			return;
		}
		
		LOG.log(Level.INFO, "Starting vision pipeline...");
		
		table.putBoolean("DemonVision", true);
		
		while(true) {
			try {
				image = new Mat();
				
				video.read(image);
				pipeline.process(image);
				
				ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
				
				// TODO: Calculate essential information/ratios with
				// the filtered contour output and put data up to
				// the SmartDashboard
				
				Thread.sleep(200);
			} catch(Exception ex) {
				LOG.log(Level.SEVERE, "Exception in DemonVision.start()!", ex);
				break;
			}
		}
		
		table.putBoolean("DemonVision", false);
	}
}
