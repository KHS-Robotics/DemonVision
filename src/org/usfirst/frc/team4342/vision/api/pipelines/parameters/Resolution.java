package org.usfirst.frc.team4342.vision.api.pipelines.parameters;

/**
 * Class to encapsulate Resolution of images or cameras
 */
public class Resolution {
	private int x, y;
	
	/**
	 * Constructs a new <code>Resolution</code>
	 * @param x the x resolution
	 * @param y the y resolution
	 */
	public Resolution(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the x resolution
	 * @return the x resolution
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y resolution
	 * @return the y resolution
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the resolution
	 * @return the resolution, AKA the product of getX() and getY()
	 */
	public int get() {
		return getX()*getY();
	}
}
