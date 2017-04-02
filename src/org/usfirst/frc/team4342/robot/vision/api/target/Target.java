package org.usfirst.frc.team4342.robot.vision.api.target;

/**
 * Class to encapsulate information about a processed target
 */
public class Target {
	private double centerXRatio;
	private double centerYRatio;
	
	/**
	 * Constructs a new <code>Target</code>
	 * @param centerXRatio the calculated center x ratio
	 * @param centerYRatio the calculated center y ratio
	 */
	public Target(double centerXRatio, double centerYRatio) {
		this.centerXRatio = centerXRatio;
		this.centerYRatio = centerYRatio;
	}
	
	/**
	 * Gets the ratio of the target's X position (left/right) relative to
	 * the entire image. In other words, how far to the right or left is the
	 * target from the center of the image's x-axis?
	 * @return the center x ratio
	 */
	public double getCenterXRatio() {
		return centerXRatio;
	}
	
	/**
	 * Gets the ratio of the target's Y position (up/down) relative to
	 * the entire image. In other words, how far up or down is the
	 * target from the center of the image's y-axis?
	 * @return the center y ratio
	 */
	public double getCenterYRatio() {
		return centerYRatio;
	}
	
	/**
	 * Gets how many degrees to the left or right the target is
	 * @param fieldOfView the field of view of the camera, in degrees
	 * @return how far to the right or left the target is in degrees
	 */
	public double getYawOffset(double fieldOfView) {
		return fieldOfView*(0.5 - getCenterXRatio());
	}
}
