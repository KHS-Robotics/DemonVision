package org.usfirst.frc.team4342.vision.api.cameras;

import org.opencv.core.Mat;

/**
 * Basic methods for a camera
 */
public abstract class Camera {
	private double fov;
	
	/**
	 * Constructs a new <code>Camera</code>
	 * @param fov the field of view of the camera, in degrees
	 */
	public Camera(double fov) {
		this.fov = fov;
	}
	
	/**
	 * Gets the field of view of the camera in degrees
	 * @return the field of view of the camera in degrees
	 */
	public double getFOV() {
		return fov;
	}
	
	/**
	 * Gets if the camera is connected
	 * @return true if connected, false otherwise
	 */
	public abstract boolean isConnected();
	
	/**
	 * Gets an image from the camera
	 * @return an image from the camera
	 */
	public abstract Mat getFrame();
	
	/**
	 * Frees resources
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
