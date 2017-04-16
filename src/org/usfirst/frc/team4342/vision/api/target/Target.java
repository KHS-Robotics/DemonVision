package org.usfirst.frc.team4342.vision.api.target;

/**
 * Class to encapsulate information about a processed target
 */
public class Target implements Comparable<Target> {
	private int width, height;
	private double x, y;
	
	/**
	 * Constructs a new <code>Target</code>
	 * @param width the width of the target
	 * @param x the calculated center x ratio
	 * @param y the calculated center y ratio
	 */
	public Target(int width, int height, double x, double y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the ratio of the target's X position (left/right) relative to
	 * the entire image. In other words, how far to the right is the
	 * center of the target relative to the left of the image?
	 * @return the center x ratio
	 */
	public double getCenterXRatio() {
		return x;
	}
	
	/**
	 * Gets the ratio of the target's Y position (up/down) relative to
	 * the entire image. In other words, how down is the
	 * center of the target relative to the top of the image?
	 * @return the center y ratio
	 */
	public double getCenterYRatio() {
		return y;
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
		return String.format("{%dx%d, (%.3f, %.3f)}", getWidth(), getHeight(), getCenterXRatio(), getCenterYRatio());
	}
	
	/**
	 * <p>Determines if two targets are equal in width and height</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		Target target = (Target) obj;
		
		return (Math.abs(this.getWidth() - target.getWidth()) < 4) && (Math.abs(this.getHeight() - target.getHeight()) < 4);
	}
	
	/**
	 * Compares the targets' areas
	 * @return 0 if equal, 1 if calling object is greater,
	 * -1 if the parameter "target" is greater
	 */
	@Override
	public int compareTo(Target target) {
		return new TargetComparator().compare(this, target);
	}
}
