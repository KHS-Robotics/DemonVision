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
				double robotYaw = table.getNumber("NavX-Yaw", 0.0);
				
				image = new Mat();
				
				video.read(image);
				pipeline.process(image);
				
				ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
				
				table.putNumber("Contours-Size", contours.size());
				
				if(contours.size() > 1) {
					Rect top = Imgproc.boundingRect(contours.get(0));
					Rect bottom = Imgproc.boundingRect(contours.get(1));

					Boiler b = new Boiler(top, bottom);
					b.publishData();
					
					final double ADJUSTED_YAW = VisionMath.getAdjustedYaw(robotYaw, b.getTopCenterXRatio());
					final double SHOOTER_RPM = VisionMath.getIdealShooterRPM(b.getTopCenterYRatio());
					
					table.putNumber("Target-Yaw", ADJUSTED_YAW);
					table.putNumber("Shooter-Target-RPM", SHOOTER_RPM);
				}
				
				Thread.sleep(200); // temporary
			} catch(Exception ex) {
				LOG.log(Level.SEVERE, "Exception in DemonVision.start()!", ex);
				break;
			}
		}
		
		table.putBoolean("DemonVision", false);
	}
}
