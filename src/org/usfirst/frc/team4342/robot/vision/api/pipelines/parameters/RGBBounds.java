package org.usfirst.frc.team4342.robot.vision.api.pipelines.parameters;

/**
 * Bounds for RGB Threshold
 */
public class RGBBounds {
	private final int minRed;
	private final int maxRed;
	private final int minGreen;
	private final int maxGreen;
	private final int minBlue;
	private final int maxBlue;
	
	/**
	 * Constructs a new <code>RGBBounds</code>
	 * @param minRed the minimum red
	 * @param maxRed the maximum red
	 * @param minGreen the minimum green
	 * @param maxGreen the maximum green
	 * @param minBlue the minimum blue
	 * @param maxBlue the maximum blue
	 */
	public RGBBounds(int minRed, int maxRed, int minGreen, int maxGreen, int minBlue, int maxBlue) {
		if(maxRed - minRed < 0)
			throw new IllegalArgumentException("lowerRed cannot be greater than upperRed");
		if(maxGreen - minGreen < 0)
			throw new IllegalArgumentException("lowerGreen cannot be greater than upperGreen");
		if(maxBlue - minBlue < 0)
			throw new IllegalArgumentException("lowerBlue cannot be greater than upperBlue");
		
		this.minRed = minRed;
		this.maxRed = maxRed;
		this.minGreen = minGreen;
		this.maxGreen = maxGreen;
		this.minBlue = minBlue;
		this.maxBlue = maxBlue;
	}
	
	/**
	 * Gets the minimum red
	 * @return the minimum red
	 */
	public int getMinRed() {
		return minRed;
	}
	
	/**
	 * Gets the maximum red
	 * @return the maximum red
	 */
	public int getMaxRed() {
		return maxRed;
	}
	
	/**
	 * Gets the minimum green
	 * @return the minimum green
	 */
	public int getMinGreen() {
		return minGreen;
	}
	
	/**
	 * Gets the maximum green
	 * @return the maximum green
	 */
	public int getMaxGreen() {
		return maxGreen;
	}
	
	/**
	 * Gets the minimum blue
	 * @return the minimum blue
	 */
	public int getMinBlue() {
		return minBlue;
	}
	
	/**
	 * Gets the maximum blue
	 * @return the maximum blue
	 */
	public int getMaxBlue() {
		return maxBlue;
	}
}
