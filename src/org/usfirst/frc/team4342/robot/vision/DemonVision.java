package org.usfirst.frc.team4342.robot.vision;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.videoio.VideoCapture;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class DemonVision {
	static {
		NetworkTable.setClientMode();
		NetworkTable.setNetworkIdentity("coprocessor");
		NetworkTable.setIPAddress("roborio-4342.local");
	}
	
	private Logger LOG;
	
	private static NetworkTable table;
	private final VideoCapture video;
	
	private Mat image = new Mat();
	
	private int usbPort;
	private String ip;
	private DemonVisionPipeline pipeline;
	
	public DemonVision(int usbPort, DemonVisionPipeline pipeline) {
		this.usbPort = usbPort;
		this.pipeline = pipeline;
		
		LOG = Logger.getLogger(DemonVision.class.getName());
		
		video = new VideoCapture();
		video.open(this.usbPort);
	}
	
	public DemonVision(String ip, DemonVisionPipeline pipeline) {
		this.ip = ip;
		this.pipeline = pipeline;
		
		LOG = Logger.getLogger(DemonVision.class.getName());
		
		table = NetworkTable.getTable("SmartDashboard");
		
		video = new VideoCapture();
		video.open(this.ip);
	}
	
	public boolean connected() {
		return video.isOpened() && table.isConnected();
	}
	
	public void start() {
		if(!connected()) {
			LOG.log(Level.WARNING, "Cannot start DemonVision because we're not connected yet!");
			return;
		}
		
		LOG.log(Level.INFO, "Starting vision pipeline...");
		
		while(true) {
			try {
				video.read(image);
				pipeline.process(image);
				
				MatOfKeyPoint blob = pipeline.findBlobsOutput();
				
				// We will do more with the blob but for now
				// at least it's putting something on the table
				table.putNumber("DV-Blob-Width", blob.width());
				table.putNumber("DV-Blob-Height", blob.height());
				
				Thread.sleep(20);
			} catch(Exception ex) {
				LOG.log(Level.SEVERE, "Exception in DemonVision.start()!", ex);
				break;
			}
		}
	}
}
