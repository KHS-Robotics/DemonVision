package org.usfirst.frc.team4342.vision.api.target;

/**
 * <p>Class to encapsulate information about a processed target</p>
 * 
 * This is how we break down the position of a target in the image.
 * The origin is the top left of the image (0.0,0.0), and the
 * bottom left is (1.0,1.0).
 * 
 * 		(0.0, 0.0)			(0.5, 0.0)			(1.0, 0.0)
 * 			-----------------------------------------
 * 			|										|
 * 			|										|
 * 			|										|
 * 			|										|
 * (0, 0.5)	|				IMAGE					| (1.0, 0.5)
 * 			|										|
 * 			|										|
 * 			|										|
 * 			|										|
 * 			-----------------------------------------
 * 		(0.0, 1.0)			(0.5, 1.0)			(1.0, 1.0)
 */
public class Target implements Comparable<Target> {
	public final int width, height, area;
	public final double x, y;
	
	/**
	 * Constructs a new <code>Target</code>. See the top of the
	 * class for a breakdown of what x and y actually is
	 * @param width the width of the target in pixels
	 * @param height the height of the target in pixels
	 * @param x the calculated x coordinate
	 * @param y the calculated y coordinate 
	 */
	public Target(int width, int height, double x, double y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.area = width*height;
	}
	
	/**
	 * Gets how many degrees to the left or right the target is
	 * @param fieldOfView the field of view of the camera, in degrees
	 * @return how far to the right or left the target is in degrees
	 */
	public double getYawOffset(double fieldOfView) {
		return fieldOfView*(0.5 - x);
	}
	
	/**
	 * <p>Represents the <code>Target</code> as a <code>String</code></p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("{%dx%d, (%.3f, %.3f)}", width, height, x, y);
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
		
		return (Math.abs(width - target.width) < 4) && (Math.abs(this.height - target.height) < 4);
	}
	
	/**
	 * Compares the targets' areas
	 * @return 0 if equal, 1 if calling object is greater,
	 * -1 if the parameter "target" is greater
	 */
	@Override
	public int compareTo(Target target) {
		return TargetComparator.compare(TargetComparator.Type.AREA, this, target);
	}
}
