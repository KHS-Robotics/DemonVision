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
	
	private static final double CAMERA_VIEW_ANGLE = 68.5;
	private static final int RES_X = 360;
	private static final int RES_Y = 240;
	
	private Logger LOG;
	
	private static NetworkTable table;
	private final VideoCapture video;
	
	private Mat image;
	private double robotYaw;
	
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
				robotYaw = table.getNumber("NaxX-Yaw", 0.0);
				
				image = new Mat();
				
				video.read(image);
				pipeline.process(image);
				
				ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
				
				table.putNumber("Contours-Size", contours.size());
				
				if(contours.size() > 1) {
					Rect top = Imgproc.boundingRect(contours.get(0));
					Rect bottom = Imgproc.boundingRect(contours.get(1));
					
					// Turn width and height into ratios
					double topWidthRatio = top.width / RES_X;
					double topHeightRatio = top.height / RES_Y;
					double bottomWidthRatio = bottom.width / RES_X;
					double bottomHeightRatio = bottom.height / RES_Y;
					table.putNumber("Top-Width", topWidthRatio);
					table.putNumber("Top-Height", topHeightRatio);
					table.putNumber("Bottom-Width", bottomWidthRatio);
					table.putNumber("Bottom-Height", bottomHeightRatio);

					// Calculate center coordinates for top
					double topCenterXRatio = (top.x + (top.width / 2)) / RES_X;
					double topCenterYRatio = (top.y + (top.height / 2)) / RES_Y;
					table.putNumber("Top-Center-X", topCenterXRatio);
					table.putNumber("Top-Center-Y",topCenterYRatio);
					
					// Calculate center coordinates for bottom
					double bottomCenterXRatio = (bottom.x + (bottom.height / 2)) / RES_X;
					double bottomCenterYRatio = (bottom.y + (bottom.height / 2)) / RES_Y;
					table.putNumber("Bottom-Center-X", bottomCenterXRatio);
					table.putNumber("Bottom-Center-Y", bottomCenterYRatio);
					
					final double ADJUSTED_YAW = robotYaw + CAMERA_VIEW_ANGLE*(topCenterXRatio - 0.5);
					table.putNumber("Target-Yaw", ADJUSTED_YAW);
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
