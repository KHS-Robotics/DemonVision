package org.usfirst.frc.team4342.vision.api.cameras;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * USB Camera
 */
public class USBCamera extends Camera {
	private VideoCapture vc;
	
	private int number;
	
	/**
	 * Constructs a new <code>USBCamera</code>
	 * @param number the port number for the camera
	 * @param fov the field of view of the camera in degrees
	 */
	public USBCamera(int number, double fov) {
		super(fov);
		
		this.number = number;
		
		vc = new VideoCapture();
		vc.open(this.number);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void close() {
		vc.release();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConnected() {
		return vc.isOpened();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mat getFrame() {
		Mat img = new Mat();
		vc.read(img);
		return img;
	}
	
	/**
	 * Gets the port number for the camera
	 * @return the port the camera is connected to
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
