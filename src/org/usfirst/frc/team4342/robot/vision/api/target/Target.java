package org.usfirst.frc.team4342.robot.vision.api.target;

/**
 * Class to encapsulate information about a processed target
 */
public class Target {
	private int width, height;
	private double centerXRatio, centerYRatio;
	
	/**
	 * Constructs a new <code>Target</code>
	 * @param width the width of the target
	 * @param centerXRatio the calculated center x ratio
	 * @param centerYRatio the calculated center y ratio
	 */
	public Target(int width, int height, double centerXRatio, double centerYRatio) {
		this.width = width;
		this.height = height;
		this.centerXRatio = centerXRatio;
		this.centerYRatio = centerYRatio;
	}
	
	/**
	 * Gets the ratio of the target's X position (left/right) relative to
	 * the entire image. In other words, how far to the right is the
	 * center of the target relative to the left of the image?
	 * @return the center x ratio
	 */
	public double getCenterXRatio() {
		return centerXRatio;
	}
	
	/**
	 * Gets the ratio of the target's Y position (up/down) relative to
	 * the entire image. In other words, how down is the
	 * center of the target relative to the top of the image?
	 * @return the center y ratio
	 */
	public double getCenterYRatio() {
		return centerYRatio;
	}
	
	/**
	 * Gets the width of the target
	 * @return the width of the target
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the height of the target
	 * @return the height of the target
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the area of the target
	 * @return the area of the target
	 */
	public int getArea() {
		return getWidth()*getHeight();
	}
	
	/**
	 * Gets how many degrees to the left or right the target is
	 * @param fieldOfView the field of view of the camera, in degrees
	 * @return how far to the right or left the target is in degrees
	 */
	public double getYawOffset(double fieldOfView) {
		return fieldOfView*(0.5 - getCenterXRatio());
	}
	
	/**
	 * <p>Represents the <code>Target</code> as a <code>String</code></p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("{%dx%d, X=%.3f, Y=%.3f}", width, height, getCenterXRatio(), getCenterYRatio());
	}
	
	/**
	 * <p>Determines if two targets are equal</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		Target target = (Target) obj;
		
		final double TOLERANCE = 0.02;
		final double X_DIFF = Math.abs(this.getCenterXRatio() - target.getCenterXRatio());
		final double Y_DIFF = Math.abs(this.getCenterYRatio() - target.getCenterYRatio());
		
		return (this.getArea() == target.getArea()) && (X_DIFF < TOLERANCE) && (Y_DIFF < TOLERANCE);
	}
}
